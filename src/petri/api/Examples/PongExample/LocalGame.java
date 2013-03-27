/**
 * 
 */
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
 * @author Cody
 *
 */
public class LocalGame extends GameMode {

	private boolean w,s,up,down,esc;
	private Paddle left, right;
	private int leftScore, rightScore;
	private Font scoreFont = new Font("Monospaced", Font.PLAIN, 30);
	private DualResolutionGameImage ballImage = null;
	private Ball ball;
	
	/**
	 * @param eng
	 */
	public LocalGame(GameEngine eng) {
		super(eng);
		
		clearKeyInputs();
		try {
			ballImage = new DualResolutionGameImage("Examples/PongExample/Resources/", "ball_high.png", "ball_low.png");
		} catch (IOException e) {
			GameEngine.log(this.toString() + ": " + e.getMessage());
		}
		ball = new Ball(engine, ballImage);
		ball.setCenter((engine.getEnvironmentSize().x/2) - (ball.getSize().x/2), (engine.getEnvironmentSize().y/2) - (ball.getSize().y/2));
		
		left = new Paddle(engine);
		right = new Paddle(engine);
		
		left.setCenter(10, (engine.getEnvironmentSize().y/2) - 15);
		right.setCenter(engine.getEnvironmentSize().x - right.getSize().x - 15 , (engine.getEnvironmentSize().y/2) - 15);
		
		engine.actors.add(ball);
		engine.actors.add(left);
		engine.actors.add(right);
		
		leftScore = 0;
		rightScore = 0;
	}

	private void clearKeyInputs() {
		w = false;
		s = false;
		up = false;
		down = false;
		esc = false;
	}

	@Override
	public void run(int ms) {
		engine.actors.handleActors(ms);
		
		if (w)
			left.setCenter(left.getCenter().x, left.getCenter().y-(left.getVelocity().y*(ms/100f)));
		if (s)
			left.setCenter(left.getCenter().x, left.getCenter().y+(left.getVelocity().y*(ms/100f)));
		
		if (up)
			right.setCenter(right.getCenter().x, right.getCenter().y-(right.getVelocity().y*(ms/100f)));
		if (down)
			right.setCenter(right.getCenter().x, right.getCenter().y+(right.getVelocity().y*(ms/100f)));
		
		super.run(ms);
		
		if (ball.isDead()) {
			GameEngine.log("Reset");
			ball = new Ball(engine, ballImage);
			ball.setCenter((engine.getEnvironmentSize().x/2) - (ball.getSize().x/2), (engine.getEnvironmentSize().y/2) - (ball.getSize().y/2));
			engine.actors.add(ball);
		}
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, engine.getEnvironmentSize().x, engine.getEnvironmentSize().y);
		engine.actors.drawActors(g);
		g.setColor(Color.green);
		g.setFont(scoreFont);
		engine.centerTextHorizontally(g, leftScore + "     :     " + rightScore, 0, engine.getEnvironmentSize().x, 30);
		super.paint(g);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
			w = false;
		else if (e.getKeyCode() == KeyEvent.VK_S)
			s = false;
		
		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = false;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			down = false;
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			esc = false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
			w = true;
		else if (e.getKeyCode() == KeyEvent.VK_S)
			s = true;
		
		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = true;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			down = true;
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			esc = true;
	}
	
	public void scorePoint(String side) {
		if (side.equalsIgnoreCase("left"))
			leftScore++;
		else if (side.equalsIgnoreCase("right"))
			rightScore ++;
	}

	@Override
	public String toString() {
		return "LocalGame";
	}
}
