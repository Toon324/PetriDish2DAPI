package petri.api.Examples.PongExample;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import petri.api.DualResolutionGameImage;
import petri.api.GameEngine;
import petri.api.GameMode;

/**
 * 
 * @author Cody Swendrowski
 */
public class InternetGame extends GameMode {

	private boolean up, down, otherUp, otherDown, isHost;
	private Paddle left, right;
	private int leftScore, rightScore;
	private Font scoreFont = new Font("Monospaced", Font.PLAIN, 30);
	private DualResolutionGameImage ballImage = null;
	private Ball ball;
	private DataInputStream input;
	private DataOutputStream output;

	/**
	 * @param eng
	 */
	public InternetGame(GameEngine eng) {
		super(eng);
		down = false;
		up = false;
		otherDown = false;
		otherUp = false;
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

		engine.actors.add(ball);
		engine.actors.add(left);
		engine.actors.add(right);

		leftScore = 0;
		rightScore = 0;
	}

	@Override
	public void run(int ms) {
		if (up) {
			if (isHost)
				left.setCenter(left.getCenter().x,
						left.getCenter().y
								- (left.getVelocity().y * (ms / 100f)));
			else
				right.setCenter(right.getCenter().x, right.getCenter().y
						- (right.getVelocity().y * (ms / 100f)));
			
			try {
				output.writeInt(0); // 0 == paddle movement

				if (isHost)
					output.writeInt(0); // 00 == paddleLeft movement
				else
					output.writeInt(1); // 01 == paddleRight movement

				output.writeInt(0); // 0X0 == up

				output.writeInt(ms); // Time for this call
				output.writeLong(engine.getCurrentUTCTime()); // When it was
																// sent
				output.flush();

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
						- (right.getVelocity().y * (ms / 100f)));
			
			try {
				output.writeInt(0); // 0 == paddle movement

				if (isHost)
					output.writeInt(0); // 00 == paddleLeft movement
				else
					output.writeInt(1); // 01 == paddleRight movement

				output.writeInt(1); // 0X1 == down

				output.writeInt(ms); // Time for this call
				output.writeLong(engine.getCurrentUTCTime()); // When it was
																// sent
				output.flush();

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
		while (engine.networkAdapter.isDataAvailable()) {
			try {
				if (input.readInt() == 0) {
					int side = input.readInt();
					int dir = input.readInt();
					int ms = input.readInt();
					int time = (int) ((engine.getCurrentUTCTime() - input
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
				leftScore + "     :     " + rightScore, 0,
				engine.getEnvironmentSize().x, 30);
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

	public void scorePoint(String side) {
		if (side.equalsIgnoreCase("left"))
			leftScore++;
		else if (side.equalsIgnoreCase("right"))
			rightScore++;
	}

	public void setHost(boolean host) {
		isHost = host;
	}

	/**
	 * @param outputStream
	 */
	public void setOutput(DataOutputStream outputStream) {
		output = outputStream;
	}

	public void setInput(DataInputStream inputStream) {
		input = inputStream;
	}
}
