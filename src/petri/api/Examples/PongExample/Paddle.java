package petri.api.Examples.PongExample;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;

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
		Polygon base = new Polygon();
		base.addPoint(0, 0);
		base.addPoint(PADDLE_WIDTH, 0);
		base.addPoint(PADDLE_WIDTH, PADDLE_HEIGHT);
		base.addPoint(0, PADDLE_HEIGHT);
		setBasePoly(base);
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
