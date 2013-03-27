package petri.api.Examples.PongExample;

import java.awt.Color;
import java.awt.Point;

import petri.api.GameEngine;
import petri.api.PolygonActor;

/**
 * 
 * @author Cody
 */
public class Paddle extends PolygonActor {

	private final int PADDLE_HEIGHT = 100, PADDLE_WIDTH = 10;
	public Paddle(GameEngine e) {
		super(e);
		basePoly.addPoint(0, 0);
		basePoly.addPoint(PADDLE_WIDTH, 0);
		basePoly.addPoint(PADDLE_WIDTH, PADDLE_HEIGHT);
		basePoly.addPoint(0, PADDLE_HEIGHT);
		centerLines = true; //Nifty effect
		drawColor = Color.green;
		vectVel.y = 3;
	}
	
	@Override
	public void move(int ms) {
		//Do nothing. Only move on Player input.
	}

	@Override
	public boolean isDead() {
		return false; //Paddles can't die
	}

	@Override
	public String toString() {
		return "Paddle";
	}
	
	public Point getSize() {
		return new Point(PADDLE_WIDTH, PADDLE_HEIGHT);
	}
	
}
