package petri.api;

/**
 * Calculates the collisions of one actor in relation to the group of actors.
 * 
 * @author Cody Swendrowski
 */
public class CollisionThread implements Runnable {

	private PolygonActor actor;
	private PolygonActor[] actors;

	/**
	 * Creates a new CollisionThread.
	 * 
	 * @param a
	 *            Actor to check collisions with
	 * @param objects
	 *            Collection of Actors to check collisions against
	 */
	public CollisionThread(PolygonActor a, PolygonActor[] objects) {
		actor = a;
		actors = objects;
	}

	@Override
	public void run() {
		for (PolygonActor a : actors) {
			if (!actor.equals(a))
				actor.checkCollision(a);
		}
	}

}
