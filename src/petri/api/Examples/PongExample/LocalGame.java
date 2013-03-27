/**
 * 
 */
package petri.api.Examples.PongExample;

import java.awt.Color;
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
	/**
	 * @param eng
	 */
	public LocalGame(GameEngine eng) {
		super(eng);
		
		clearKeyInputs();
		DualResolutionGameImage ballImage = null;
		try {
			ballImage = new DualResolutionGameImage("Examples/PongExample/Resources/", "ball_high.png", "ball_low.png");
		} catch (IOException e) {
			GameEngine.log(this.toString() + ": " + e.getMessage());
		}
		Ball ball = new Ball(engine, ballImage);
		ball.setCenter((engine.getEnvironmentSize().x/2) - (ball.getSize().x/2), (engine.getEnvironmentSize().y/2) - (ball.getSize().y/2));
		
		left = new Paddle(engine);
		right = new Paddle(engine);
		
		left.setCenter(10, (engine.getEnvironmentSize().y/2) - 15);
		right.setCenter(engine.getEnvironmentSize().x - right.getSize().x - 15 , (engine.getEnvironmentSize().y/2) - 15);
		
		engine.actors.add(ball);
		engine.actors.add(left);
		engine.actors.add(right);
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
			left.setCenter(left.getCenter().x, left.getCenter().y-left.getVelocity().y);
		if (s)
			left.setCenter(left.getCenter().x, left.getCenter().y+left.getVelocity().y);
		
		if (up)
			right.setCenter(right.getCenter().x, right.getCenter().y-right.getVelocity().y);
		if (down)
			right.setCenter(right.getCenter().x, right.getCenter().y+right.getVelocity().y);
		
		super.run(ms);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, engine.getEnvironmentSize().x, engine.getEnvironmentSize().y);
		engine.actors.drawActors(g);
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

	@Override
	public String toString() {
		return "LocalGame";
	}
}
