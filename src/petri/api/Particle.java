package petri.api;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

/**
 * A non-colliding Actor that is used for particle effects. Kills itself if it
 * is outside of the visible area.
 * 
 * @author Cody Swendrowski
 */
public class Particle extends PolygonActor {

	protected int alpha;

	/**
	 * Creates a new Particle.
	 * 
	 * @param e
	 *            GameEngine to utilize
	 * @param vectorSpeed
	 *            Speed of Actor in pixels per second
	 * @param c
	 *            Color of Particle
	 */
	public Particle(GameEngine e, Point2D.Float vectorSpeed, Color c) {
		super(e);
		alpha = 255;
		vectVel = vectorSpeed;
		drawColor = c;

		// Size of particle
		basePoly.addPoint(0, 0);
		basePoly.addPoint(4, 0);
		basePoly.addPoint(4, 4);
		basePoly.addPoint(0, 4);
	}

	@Override
	public void draw(Graphics g) {
		// Draws a fading tail behind the Particle
		for (int a = 0; a <= 10; a += 1) {
			drawColor = new Color(drawColor.getRed(), drawColor.getGreen(),
					drawColor.getBlue(), alpha / (a + 1));
			g.setColor(drawColor);
			g.fillRect((int) (center.x - vectVel.x / 6 * a),
					(int) (center.y - vectVel.y / 6 * a), 4, 4);
		}
	}

	@Override
	public void move(int ms) {
		setCenter(center.x + (vectVel.x * (ms / 100f)), center.y
				+ (vectVel.y * (ms / 100f)));
	}

	@Override
	public void setCenter(float x, float y) {
		if (x < 0 || y < 0 || x > engine.getEnvironmentSize().x
				|| y > engine.getEnvironmentSize().y) {
			death = true;
			return;
		}
		super.setCenter(x, y);
	}

	@Override
	public void checkCollision(Actor other) {
		return; // Particle does not collide
	}

	@Override
	public String toString() {
		return "Particle";
	}
}
