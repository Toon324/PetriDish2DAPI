package petri.api;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;

public abstract class Actor {
	
	protected boolean death;
	protected Point2D.Float center;
	protected Polygon basePoly;
	protected GameEngine engine;
	protected Point2D.Float vectVel; // speed in pixels/s
	protected int health, dir, maxHealth;
	
	public Actor(GameEngine e) {
		death = false;
		engine = e;
		basePoly = new Polygon();
		health = 0;
		dir = 0;
		maxHealth = 0;
	}
	
	/**
	 * Returns center of Actor in a Point2D.Float format.
	 * 
	 * @return center
	 */
	public Point2D.Float getCenter() {
		return center;
	}

	/**
	 * Returns velocity of Actor in a Point2D.Float format.
	 * 
	 * @return vectVel
	 */
	public Point2D.Float getVelocity() {
		return vectVel;
	}
	
	public abstract void draw(Graphics g);
	
	/**
	 * Moves the Actor.
	 * 
	 * @param w
	 *            Width of window to draw in
	 * @param h
	 *            Height of window to draw in
	 */
	public void move(int ms) {
		setCenter(center.x + (ms / 1000F) * vectVel.x, center.y + (ms / 1000F)
				* vectVel.y);
	}

	/**
	 * Returns true if Actor is dead; else, returns false.
	 * 
	 * @return death
	 */
	public boolean isDead() {
		return death;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void dealDamage(int damage) {
		health -= damage;
		if (health <= 0)
			death = true;
	}
	
	public void heal(int healAmt) {
		health += healAmt;
		if (health >= maxHealth)
			health = maxHealth;
	}

	/**
	 * Checks to see if Actor is colliding with another Actor.
	 * 
	 * @param a
	 *            Actor to check collision against
	 */
	public void checkCollision(Actor a) {
		if (a.equals(this))
			return;
		int distance = 1;
		if (a.basePoly != null && basePoly != null)
			distance = Math.abs((a.basePoly.getBounds().x - basePoly.getBounds().x)
				/ (a.basePoly.getBounds().y - basePoly.getBounds().y));
		if (distance > 5
				||  a instanceof Particle)
			return;

		Polygon otherPoly = a.basePoly;
		for (int i = 0; i < otherPoly.npoints; i++) {
			if (basePoly.contains(new Point(otherPoly.xpoints[i],
					otherPoly.ypoints[i]))) {
				setDeath(true);
				a.setDeath(true);
			}
		}
	}

	/**
	 * Sets the center of the Actor.
	 * 
	 * @param x
	 *            X co-ordinate of center
	 * @param y
	 *            Y co-ordinate of center
	 */
	public void setCenter(float x, float y) {
		center = new Point2D.Float(x,y);
	}

	/**
	 * Sets the death boolean of Actor.
	 * 
	 * @param d
	 *            boolean to set to
	 */
	public void setDeath(boolean d) {
		death = d;
	}

	/**
	 * Allows System to print name of object. Returns the name of the Actor
	 * 
	 * @return "Actor"
	 */
	public String toString() {
		return "Actor";
	}
	
}
