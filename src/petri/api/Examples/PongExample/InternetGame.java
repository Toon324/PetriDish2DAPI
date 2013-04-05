package petri.api.Examples.PongExample;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;

import petri.api.DualResolutionGameImage;
import petri.api.GameEngine;
import petri.api.GameMode;

/**
 * 
 * @author Cody Swendrowski
 */
public class InternetGame extends GameMode {

	private boolean up, down, isHost;
	private Paddle left, right;
	private Font scoreFont = new Font("Monospaced", Font.PLAIN, 30);
	private DualResolutionGameImage ballImage = null;
	private Ball ball;
	private boolean initialized;

	/**
	 * @param eng
	 */
	public InternetGame(GameEngine eng) {
		super(eng);
		down = false;
		up = false;
		isHost = false;

		try {
			ballImage = new DualResolutionGameImage(
					"Examples/PongExample/Resources/", "ball_high.png",
					"ball_low.png");
		} catch (IOException e) {
			GameEngine.log(this.toString() + ": " + e.getMessage());
		}
		ball = new Ball(engine, ballImage);
		ball.setCenter((engine.getEnvironmentSize().x / 2)
				- (ball.getSize().x / 2), (engine.getEnvironmentSize().y / 2)
				- (ball.getSize().y / 2));

		left = new Paddle(engine);
		right = new Paddle(engine);

		left.setCenter(10, (engine.getEnvironmentSize().y / 2) - 15);
		right.setCenter(engine.getEnvironmentSize().x - right.getSize().x - 15,
				(engine.getEnvironmentSize().y / 2) - 15);

		initialized = false;
	}

	@Override
	public void run(int ms) {

		if (!initialized) {
			engine.actors.clear();
			engine.actors.add(ball);
			engine.actors.add(left);
			engine.actors.add(right);
		}
		engine.actors.handleActors(ms);
		
		//Writes ball movement
		if (isHost) {
			try {
				engine.networkAdapter.getOutputStream().writeInt(1); //Ball movement
				engine.networkAdapter.getOutputStream().writeFloat(ball.getCenter().x);
				engine.networkAdapter.getOutputStream().writeFloat(ball.getCenter().y);
				engine.networkAdapter.getOutputStream().writeFloat(ball.getVelocity().x);
				engine.networkAdapter.getOutputStream().writeFloat(ball.getVelocity().y);
			} catch (IOException e) {
				GameEngine.log(e.getMessage());
			} 
		}
		
		if (up) {
			if (isHost)
				left.setCenter(left.getCenter().x,
						left.getCenter().y
								- (left.getVelocity().y * (ms / 100f)));
			else
				right.setCenter(right.getCenter().x, right.getCenter().y
						- (right.getVelocity().y * (ms / 100f)));
			
			try {
				engine.networkAdapter.getOutputStream().writeInt(0); // 0 == paddle movement

				if (isHost)
					engine.networkAdapter.getOutputStream().writeInt(0); // 00 == paddleLeft movement
				else
					engine.networkAdapter.getOutputStream().writeInt(1); // 01 == paddleRight movement

				engine.networkAdapter.getOutputStream().writeInt(0); // 0X0 == up

				engine.networkAdapter.getOutputStream().writeInt(ms); // Time for this call
				engine.networkAdapter.getOutputStream().writeLong(engine.getCurrentUTCTime()); // When it was
																// sent
				engine.networkAdapter.getOutputStream().flush();

			} catch (IOException e) {
				GameEngine.log(e.getMessage());
			}
		} else if (down) {
			if (isHost)
				left.setCenter(left.getCenter().x,
						left.getCenter().y
								+ (left.getVelocity().y * (ms / 100f)));
			else
				right.setCenter(right.getCenter().x, right.getCenter().y
						+ (right.getVelocity().y * (ms / 100f)));
			
			try {
				engine.networkAdapter.getOutputStream().writeInt(0); // 0 == paddle movement

				if (isHost)
					engine.networkAdapter.getOutputStream().writeInt(0); // 00 == paddleLeft movement
				else
					engine.networkAdapter.getOutputStream().writeInt(1); // 01 == paddleRight movement

				engine.networkAdapter.getOutputStream().writeInt(1); // 0X1 == down

				engine.networkAdapter.getOutputStream().writeInt(ms); // Time for this call
				engine.networkAdapter.getOutputStream().writeLong(engine.getCurrentUTCTime()); // When it was
																// sent
				engine.networkAdapter.getOutputStream().flush();

			} catch (IOException e) {
				GameEngine.log(e.getMessage());
			}
		}

		pollNetwork();

		super.run(ms);
	}

	/**
	 * 
	 */
	private void pollNetwork() {
		if (engine.networkAdapter.isDataAvailable()) {
			try {
				if (engine.networkAdapter.getInputStream().readInt() == 0) {
					GameEngine.log("paddle input");
					int side = engine.networkAdapter.getInputStream().readInt();
					int dir = engine.networkAdapter.getInputStream().readInt();
					int ms = engine.networkAdapter.getInputStream().readInt();
					int time = (int) ((engine.getCurrentUTCTime() - engine.networkAdapter.getInputStream()
							.readInt()) + ms); // Paddle will move backwards if
												// message was sent from the
												// future. It happens you know.
					if (side == 0)
						if (dir == 0)
							left.setCenter(
									left.getCenter().x,
									left.getCenter().y
											- (left.getVelocity().y * (time / 100f)));
						else if (dir == 1)
							left.setCenter(
									left.getCenter().x,
									left.getCenter().y
											+ (left.getVelocity().y * (time / 100f)));

						else if (side == 1)
							if (dir == 0)
								right.setCenter(
										right.getCenter().x,
										right.getCenter().y
												- (right.getVelocity().y * (time / 100f)));
							else if (dir == 1)
								right.setCenter(
										right.getCenter().x,
										right.getCenter().y
												+ (right.getVelocity().y * (time / 100f)));
				}
				else {
					GameEngine.log("Ball input");
					try {
						if (isHost)
							return;
						
						float centerX = engine.networkAdapter.getInputStream().readFloat();
						float centerY = engine.networkAdapter.getInputStream().readFloat();
						float velX = engine.networkAdapter.getInputStream().readFloat();
						float velY = engine.networkAdapter.getInputStream().readFloat();
						GameEngine.log("Center: " + centerX + "," + centerY + "  VeL: " + velX + "," + velY);
						ball.setCenter(centerX, centerY);
						ball.setVelocity(velX, velY);
					}
					catch (Exception e) {
						GameEngine.log(e.getMessage());
					}
				}
				engine.networkAdapter.clearDataAvailable();
			} catch (IOException e) {
				GameEngine.log(e.toString());
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, engine.getEnvironmentSize().x,
				engine.getEnvironmentSize().y);
		engine.actors.drawActors(g);
		g.setColor(Color.green);
		g.setFont(scoreFont);
		engine.centerTextHorizontally(g,
				engine.getScore(0) + "     :     " + engine.getScore(1), 0,
				engine.getEnvironmentSize().x, 30);
		engine.actors.drawActors(g);
		super.paint(g);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = false;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			down = false;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = true;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			down = true;

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			engine.setCurrentGameMode("MainMenu");
	}

	public void setHost(boolean host) {
		isHost = host;
	}

}
