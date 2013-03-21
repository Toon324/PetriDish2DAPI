package petri.api;

import java.awt.Color;
import java.awt.geom.Point2D.Float;

/**
 * Fundamentally the same as a Bullet, but doesn't collide with other actors due
 * to class name.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class Particle extends Bullet {

	/**
	 * Creates a new Bullet.
	 * 
	 * @param e
	 *            GameEngine to utilize
	 * @param vectorSpeed
	 *            Speed of Actor
	 * @param c
	 *            Color to draw with
	 */
	public Particle(GameEngine e, Float vectorSpeed, Color c) {
		super(e, vectorSpeed, c);
	}

}
