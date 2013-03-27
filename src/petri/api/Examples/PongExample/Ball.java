package petri.api.Examples.PongExample;

import petri.api.Actor;
import petri.api.GameEngine;
import petri.api.GameImage;
import petri.api.ImageActor;

/**
 * 
 * @author Cody Swendrowski
 */
public class Ball extends ImageActor {

	/**
	 * @param e
	 */
	public Ball(GameEngine e, GameImage i) {
		super(e, i);
		size.x = 20;
		size.y = 20;
		vectVel.x = 100;

	}

	/* (non-Javadoc)
	 * @see petri.api.Actor#checkCollision(petri.api.Actor)
	 */
	@Override
	public void checkCollision(Actor a) {
		super.checkCollision(a);
		if (isColliding())
			vectVel.x = -vectVel.x; //Turn around
	}

	@Override
	public void setCenter(float x, float y) {
		if (center.y < 0 || center.y + size.y >= engine.getEnvironmentSize().y)
			vectVel.y = -vectVel.y;
		else if (center.x + size.x < 0 || center.x > engine.getEnvironmentSize().x)
			setDeath(true);
		else
			super.setCenter(x, y);
	}
	
	@Override
	public String toString() {
		return "Ball";
	}
}
