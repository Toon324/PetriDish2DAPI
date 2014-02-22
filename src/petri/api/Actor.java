package petri.api;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;

/**
 * The base class for all things that both move and paint. Offers variables for
 * position, size, velocity, health, direction facing, and max health. Offers
 * methods for healing, damage, drawing, moving, collision checking, and death.
 * 
 * @author Cody Swendrowski
 * 
 */
public abstract class Actor {

	protected boolean death;
	protected Point2D.Float center, size, velocity, acceleration, maxVelocity;
	protected Polygon basePoly;
	protected GameEngine engine;
	protected int health, dir, maxHealth;

	/**
	 * Creates a new Actor.
	 * 
	 * @param e
	 *            GameEngine to utilize
	 */
	public Actor(GameEngine e) {
		velocity = new Point2D.Float(0, 0); // speed in pixels/s
		center = new Point2D.Float(0, 0);
		size = new Point2D.Float(0, 0);
		acceleration = new Point2D.Float(0, 0);
		maxVelocity = new Point2D.Float(Integer.MAX_VALUE, Integer.MAX_VALUE);
		death = false;
		engine = e;
		basePoly = new Polygon();
		health = 0;
		dir = 0;
		maxHealth = 0;
		_checkInvariant();
	}

	/**
	 * 
	 */
	private boolean _checkInvariant() {
		if (engine == null)
			return _report("No GameEngine found.");
		
		if (health == 0) {
			_warning("Actor health is 0.");
		}
		
		if (maxHealth == 0)
			_warning("Max health is 0.");
		
		if (size.x == 0 || size.y == 0)
			_warning("The size of the Actor is 0 in one of the dimensions.");
			
		if (basePoly == null)
			return _report("The polygon is null, so Actor can't be drawn.");
		
		return true;
	}
	
	/**
	 * @param string
	 * @return
	 */
	private void _warning(String s) {
		System.out.println("Invariant warning: " + s);
	}

	private boolean _report(String s) {
		System.out.println("Invariant error: " + s);
		return false;
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
		return velocity;
	}

	public Point2D.Float getMaxVelocity() {
		return maxVelocity;
	}

	public Point2D.Float getAcceleration() {
		return acceleration;
	}

	/**
	 * Returns the size of the Actor in Point2D.Float format.
	 * 
	 * @return size
	 */
	public Point2D.Float getSize() {
		return size;
	}

	/**
	 * Returns the upper-left most corner of the bounding rectangle for the
	 * Actor.
	 * 
	 * @return corner
	 */
	public Point getCorner() {
		return basePoly.getBounds().getLocation();
	}

	/**
	 * Draws the Actor.
	 * 
	 * @param g
	 *            Graphics to draw with
	 */
	public abstract void draw(Graphics g);

	/**
	 * Moves the Actor. Increases velocity by acceleration up until maxVelocity
	 * is hit (positive or negative), which is the max value of Integer by
	 * default. Then proceeds to update position by
	 * 
	 * @param ms
	 *            Time since last call in Milliseconds
	 */
	public void move(int ms) {
		if (velocity.x + (acceleration.x * (ms / 100F)) <= maxVelocity.x
				&& velocity.x + (acceleration.x * (ms / 100F)) >= -maxVelocity.x)
			velocity.x += acceleration.x * (ms / 100F);

		if (velocity.y + (acceleration.y * (ms / 100F)) <= maxVelocity.y
				&& velocity.y + (acceleration.y * (ms / 100F)) >= -maxVelocity.y)
			velocity.y += acceleration.y * (ms / 100F);

		setCenter(center.x + (ms / 100F) * velocity.x, center.y + (ms / 100F)
				* velocity.y);
	}

	/**
	 * Called by handleActors to check to see if this Actor should be removed.
	 * By default, returns true if Actor is dead.
	 * 
	 * @return death
	 */
	public boolean shouldRemove() {
		return death;
	}

	/**
	 * Returns the amount of health an Actor has, in int form
	 * 
	 * @return health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Deals damage to the Actor. If Actor health is less than or equal to 0,
	 * sets Actor to dead.
	 * 
	 * @param damage
	 *            Amount of damage to deal to Actor.
	 */
	public void dealDamage(int damage) {
		health -= damage;
		if (health <= 0)
			death = true;
		_checkInvariant();
	}

	/**
	 * Heals the Actor up to max health amount.
	 * 
	 * @param healAmt
	 *            Max amount to heal by
	 */
	public void heal(int healAmt) {
		health += healAmt;
		if (health >= maxHealth)
			health = maxHealth;
		_checkInvariant();
	}

	/**
	 * Sets the basePoly, which is the default shape of the Actor, to a new
	 * Polygon.
	 * 
	 * @param poly
	 *            Polygon to set to
	 */
	public void setBasePoly(Polygon poly) {
		basePoly = poly;
		_checkInvariant();
	}

	/**
	 * Checks to see if Actor is colliding with another Actor. This method
	 * checks to see if either of the Polygons that contain collision points are
	 * intersecting, and if they are, calls onCollision on both Actors.
	 * 
	 * @param a
	 *            Actor to check collision against
	 */
	public void checkCollision(Actor a) {
		if (a.equals(this))
			return;

		Polygon otherPoly = a.basePoly;
		int distance = 1;

		if (otherPoly != null && basePoly != null) {
			// Calculate distance using the formula x^2 + y^2 = z^2
			int x = otherPoly.getBounds().x - basePoly.getBounds().x;
			int y = otherPoly.getBounds().y - basePoly.getBounds().y;
			distance = (int) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		}
		if (distance > 150 || a instanceof Particle)
			return;

		for (int i = 0; i < basePoly.npoints; i++) {
			if (otherPoly.contains(new Point(basePoly.xpoints[i],
					basePoly.ypoints[i]))) {
				onCollision(a);
				a.onCollision(a);
			}
		}
	}

	/**
	 * Called when this Actor collides with another Actor. By default, does
	 * nothing.
	 */
	protected void onCollision(Actor other) {

	}

	/**
	 * Sets the center of the Actor. This method translates the base Polygon to
	 * 0,0 then back to the new co-ordinates. This maintains the correct Polygon
	 * shape.
	 * 
	 * @param x
	 *            X co-ordinate of center
	 * @param y
	 *            Y co-ordinate of center
	 */
	public void setCenter(float x, float y) {
		basePoly.translate((int) -center.x, (int) -center.y);
		center = new Point2D.Float(x, y);
		basePoly.translate((int) center.x, (int) center.y);
	}

	/**
	 * Sets the death boolean of Actor. If an Actor's death is set to true, it
	 * will be removed on the next call to handleActors
	 * 
	 * @param d
	 *            boolean to set to
	 */
	public void setDeath(boolean d) {
		death = d;
	}

	/**
	 * @param velX
	 * @param velY
	 */
	public void setVelocity(float velX, float velY) {
		velocity.x = velX;
		velocity.y = velY;
	}

	public void setAcceleration(float acelX, float acelY) {
		acceleration.x = acelX;
		acceleration.y = acelY;
	}

	public void setMaxVelocity(float x, float y) {
		maxVelocity.x = x;
		maxVelocity.y = y;
	}

	public void setSize(float x, float y) {
		size.x = x;
		size.y = y;
		_checkInvariant();
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
