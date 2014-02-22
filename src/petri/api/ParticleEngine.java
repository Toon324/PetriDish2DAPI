package petri.api;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Controls the logic behind the creation of differing Particle explosions. This
 * class should be overridden to generate custom explosions.
 * 
 * @author Cody Swendrowski
 */
public class ParticleEngine {

	protected GameEngine engine;

	/**
	 * Creates a new ParticleEngine.
	 * 
	 * @param e
	 *            GameEngine to use
	 */
	public ParticleEngine(GameEngine e) {
		engine = e;
	}

	/**
	 * Spawns an explosion of particles, all sharing a random color, vector, and
	 * decay rate. Particles are spawned in an equal division around the center
	 * point.
	 * 
	 * @param center
	 *            Center point to spawn around
	 */
	public void spawnRandomExplosion(Point2D.Float center) {
		Random gen = new Random();
		int num = gen.nextInt(11) + 4; // Number of particles
		double angleInc = (2 * Math.PI) / num; // Angle between particles
		int speed = gen.nextInt(25) + 15; // Speed base of particle
		Color c = new Color(gen.nextFloat(), gen.nextFloat(), gen.nextFloat(),
				1.0f); // Color of particle
		c = c.brighter(); // Takes the generated color and makes it brighter for
							// visiblity

		// Spawns particles
		for (int x = 0; x < num; x++) {
			// Calculates particle vector speeds
			Point2D.Float vector = new Point2D.Float();
			vector.x = (float) (speed * Math.cos(angleInc * x));
			vector.y = (float) (speed * Math.sin(angleInc * x));

			engine.getActors().addParticle(center, vector, c);
		}
	}

	/**
	 * Spawns an explosion of particles, all sharing the given color, speed, and
	 * decay rate. Particles are spawned in an equal division around the center
	 * point.
	 * 
	 * @param center
	 *            Center point to spawn around
	 * @param amount
	 *            Number of Particles to spawn
	 * @param speed
	 *            Speed Particles should move it
	 * @param color
	 *            Color Particles should be
	 */
	public void spawnColorExplosion(Point2D.Float center, int amount,
			int speed, Color color) {
		double angleInc = (2 * Math.PI) / amount; // Angle between particles

		for (int x = 0; x < amount; x++) {
			// Calculates particle vector speeds
			Point2D.Float vector = new Point2D.Float();
			vector.x = (float) (speed * Math.cos(angleInc * x));
			vector.y = (float) (speed * Math.sin(angleInc * x));

			engine.getActors().addParticle(center, vector, color);
		}
	}
}
