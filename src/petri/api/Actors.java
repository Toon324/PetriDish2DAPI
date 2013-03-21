package petri.api;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.*;

/**
 * Container for all Actors. Handles updating and drawing of all contained
 * Actors.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class Actors {

	private final int MAX_ACTORS = 1000;
	private ArrayList<Actor> actors = new ArrayList<Actor>();
	private ArrayList<Actor> toAdd = new ArrayList<Actor>();
	private GameEngine engine;
	private ExecutorService threadPool = Executors.newCachedThreadPool();

	/**
	 * Creates a new container of Actor.
	 * 
	 * @param debug
	 * @param gameEngine
	 */
	public Actors(GameEngine gameEngine) {
		engine = gameEngine;
	}

	/**
	 * Adds a created Actor to actors.
	 * 
	 * @param a
	 *            Actor to be added
	 */
	public void add(Actor a) {
		if (actors.size() >= MAX_ACTORS) {
			return;
		}
		toAdd.add(a);
	}

	private int evade;
	private boolean canFire;

	/**
	 * Moves and checks for death all actors which are alive. Removes all dead
	 * actors.
	 * 
	 * All arraylist iterations are first converted to array to avoid concurrent
	 * modification.
	 */
	public void handleActors(int ms) {
		ArrayList<Actor> toRemove = new ArrayList<Actor>(); // Dead objects to
															// be removed
		ArrayList<Point2D.Float> particles = new ArrayList<Point2D.Float>(); // Particles to add

		// Adds Actors toAdd
		for (Actor a : toAdd.toArray(new Actor[0])) {
			actors.add(a);
		}
		toAdd.clear();

		// Collects dead actors in an array and moves live ones, checking for
		// collisions
		for (Actor a : actors.toArray(new Actor[0])) {
			if (a.isDead()) {
				toRemove.add(a);
				if (!(a instanceof Bullet))
					particles.add(a.getCenter());
			} else {
				a.move(ms);
				if (a instanceof Triangle)
					((Triangle) a).setEvade(evade);
				if (a instanceof FightingActor && canFire)
					((FightingActor) a).fire();

				// Check for collisions. Uses a threadPool to maximize
				// utilization of system resources and speed up processing.
				threadPool.execute(new CollisionThread(a, actors.toArray(new Actor[0])));
			}

		}

		// Removes dead actors from the arrayList
		for (Actor a : toRemove.toArray(new Actor[0])) {

			// If dead actor is a Triangle or a Square, we need to let another
			// class know that those actors are dead
			if (a instanceof Triangle) {
				Triangle f = (Triangle) (a);
				engine.mainGame.setTrianglePositionToFalse(f.destination,
						(int) f.center.y);
			} else if (a instanceof Square) {
				Square f = (Square) (a);
				engine.mainGame.setSquarePositionToFalse(f.destination,
						(int) f.center.y);
			}

			if (!actors.remove(a))
				GameEngine.log("Error in removing actor " + a.toString());
		}

		// Spawns particle explosions
		for (Point2D.Float p : particles) {
			engine.particleEngine.spawnRandomExplosion(p);
		}

		canFire = false;
	}

	/**
	 * Calls the draw method of all Actors.
	 * 
	 * @param g
	 *            Graphics to be drawn with
	 */
	public void drawActors(Graphics g) {
		for (Actor a : actors.toArray(new Actor[0])) {
			a.draw(g);
		}
	}

	/**
	 * Returns current ArrayList of actors
	 * 
	 * @return actors
	 */
	public ArrayList<Actor> getArrayList() {
		return actors;
	}

	/**
	 * Adds a Triangle to Actors.
	 * 
	 * @param destination
	 *            Where the Triangle should move to horizontally
	 * @param x
	 *            X position to spawn at
	 * @param y
	 *            Y position to spawn at
	 */
	public void addTriangle(int destination, int x, int y) {
		Triangle c = new Triangle(engine, destination);
		c.setCenter(x, y);
		add(c);
	}

	/**
	 * Adds a Square to Actors.
	 * 
	 * @param destination
	 *            Where the Square should move to horizontally
	 * @param x
	 *            X position to spawn at
	 * @param y
	 *            Y position to spawn at
	 */
	public void addSquare(int destination, int x, int y) {
		Square c = new Square(engine, destination);
		c.setCenter(x, y);
		add(c);
	}

	/**
	 * Adds a Particle to Actors.
	 * 
	 * @param center
	 *            The point to spawn at
	 * @param vectorSpeed
	 *            The speed of the Particle both horizontally and vertically
	 * @param c
	 *            The color of the Particle
	 */
	public void addParticle(Point2D.Float center, Point2D.Float vectorSpeed,
			Color c) {
		Particle p = new Particle(engine, vectorSpeed, c);
		p.setCenter(center);
		add(p);
	}

	/**
	 * Sets the GameEngine to utilize.
	 * 
	 * @param e
	 *            engine to set to
	 */
	public void setEngine(GameEngine e) {
		engine = e;
	}

	/**
	 * Sets the evade chance to update Triangles to.
	 * 
	 * @param e
	 *            evade chance, on the scale of 0 to 100, with 100 being always
	 *            evade and 0 being never evade.
	 */
	public void setEvade(int e) {
		evade = e;
	}

	/**
	 * Adds a Bullet to actors.
	 * 
	 * @param center
	 *            Location to spawn at
	 * @param drawClr
	 *            Color to draw with
	 * @param velocity
	 *            Speed of Bullet
	 */
	public void fireBullet(Point2D.Float center, Color drawClr,
			Point2D.Float velocity) {
		Bullet b = new Bullet(engine, velocity, drawClr);
		b.setCenter(center.x, center.y);
		add(b);
	}

	/**
	 * Signals that FightingActors can fire at each other.
	 */
	public void fire() {
		canFire = true;
	}

	/**
	 * Clears the arrayList of all current actors. Called when the player starts
	 * a new set of questions.
	 */
	public void clear() {
		actors.clear();
	}

}
