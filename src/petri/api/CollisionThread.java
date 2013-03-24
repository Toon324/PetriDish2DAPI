package petri.api;

/**
 * Calculates the collisions of one actor in relation to the group of actors.
 * 
 * @author Cody Swendrowski
 */
public class CollisionThread implements Runnable {

	private Actor actor;
	private Actor[] actors;

	/**
	 * Creates a new CollisionThread.
	 * 
	 * @param a
	 *            Actor to check collisions with
	 * @param actors2
	 *            Collection of Actors to check collisions against
	 */
	public CollisionThread(Actor a, Actor[] actors2) {
		actor = a;
		actors = actors2;
	}

	@Override
	public void run() {
		for (Actor a : actors) {
			if (!actor.equals(a))
				actor.checkCollision(a);
		}
	}

}
