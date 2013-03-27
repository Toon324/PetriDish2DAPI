package petri.api.Examples.PongExample;

import java.util.Random;

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
		size.x = 25;
		size.y = 25;
		Random gen = new Random();
		vectVel.x = gen.nextInt(50)-25;
	}
	
	public void move(int ms) {
		if (vectVel.x < 0)
			vectVel.x -= .01f;
		else
			vectVel.x += .01f;
		
		super.move(ms);
	}

	/* (non-Javadoc)
	 * @see petri.api.Actor#checkCollision(petri.api.Actor)
	 */
	@Override
	public void checkCollision(Actor a) {
		super.checkCollision(a);
		if (isColliding()) {
			vectVel.x = -vectVel.x; //Turn around
			Random gen = new Random();
			vectVel.y = gen.nextInt(20)-10;
			clearCollision();
		}
	}

	@Override
	public void setCenter(float x, float y) {
		if (center.y < 0 || center.y + size.y >= engine.getEnvironmentSize().y) {
			vectVel.y = -vectVel.y;
			super.setCenter(x, y+(vectVel.y));
		}
		else if (center.x + size.x < 0) {
			setDeath(true);
			LocalGame lg = (LocalGame) engine.getCurrentGameMode();
			lg.scorePoint("left");
		}
		else if (center.x > engine.getEnvironmentSize().x) {
			setDeath(true);
			LocalGame lg = (LocalGame) engine.getCurrentGameMode();
			lg.scorePoint("right");
		}
		else
			super.setCenter(x, y);
	}
	
	@Override
	public String toString() {
		return "Ball";
	}
}
