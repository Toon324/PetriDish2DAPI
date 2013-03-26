package petri.api;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.*;

/**
 * Container for all Actors. Handles updating and drawing of all contained
 * Actors.
 * 
 * @author Cody Swendrowski
 */
public final class Actors {

	private final int MAX_ACTORS = 10000;
	private ArrayList<Actor> actors = new ArrayList<Actor>();
	private ArrayList<Actor> toAdd = new ArrayList<Actor>();
	private GameEngine engine;
	private ExecutorService threadPool = Executors.newCachedThreadPool();

	/**
	 * Creates a new container of Actor.
	 * 
	 * @param gameEngine
	 *            Engine to utilize
	 */
	public Actors(GameEngine gameEngine) {
		engine = gameEngine;
	}

	/**
	 * Adds a created Actor to current ArrayList. Does not add Actor if
	 * ArrayList is currently meeting or exceeding MAX_ACTORS.
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

	/**
	 * Moves and checks for death all actors which are alive. Removes all dead
	 * actors.
	 * 
	 * All ArrayList iterations are first converted to array to avoid concurrent
	 * modification.
	 * 
	 * @param ms
	 *            Time since last call in Milliseconds
	 */
	public void handleActors(int ms) {
		ArrayList<Actor> toRemove = new ArrayList<Actor>(); // Dead objects to
															// be removed
		ArrayList<Point2D.Float> particles = new ArrayList<Point2D.Float>(); // Particles
																				// to
																				// add

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
			} else {
				a.move(ms);

				// Check for collisions. Uses a threadPool to maximize
				// utilization of system resources and speed up processing.
				threadPool.execute(new CollisionThread(a, actors
						.toArray(new Actor[0])));
			}

		}

		// Removes dead actors from the arrayList
		for (Actor a : toRemove.toArray(new Actor[0])) {
			if (!actors.remove(a))
				GameEngine.log("Error in removing actor " + a.toString());
		}

		// Spawns particle explosions
		for (Point2D.Float p : particles) {
			engine.particleEngine.spawnRandomExplosion(p);
		}
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
		p.setCenter(center.x,center.y);
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
	 * Clears the arrayList of all current actors.
	 */
	public void clear() {
		actors.clear();
	}

}
