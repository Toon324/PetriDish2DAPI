package petri.api;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

/**
 * Fundamentally the same as a Bullet, but doesn't collide with other actors due
 * to class name.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class Particle extends PolygonActor {

	private int alpha;
	
	/**
	 * Creates a new Particle.
	 * 
	 * @param e
	 *            GameEngine to utilize
	 * @param vectorSpeed
	 *            Speed of Actor
	 * @param c
	 *            Color to draw with
	 */
	public Particle (GameEngine e, Point2D.Float vectorSpeed, Color c) {
		super(e);
		alpha = 255;
		vectVel = vectorSpeed;
		drawClr = c;
		
		//Size of particle
		basePoly.addPoint(0, 0);
		basePoly.addPoint(4, 0);
		basePoly.addPoint(4, 4);
		basePoly.addPoint(0, 4);
	}

	public void draw(Graphics g) {
		// Draws a fading tail behind the Particle
		for (int a = 0; a <= 10; a += 1) {
			drawClr = new Color(drawClr.getRed(), drawClr.getGreen(),
					drawClr.getBlue(), alpha / (a + 1));
			g.setColor(drawClr);
			g.fillRect((int) (center.x - vectVel.x/6 * a),
					(int) (center.y - vectVel.y/6 * a), 4, 4);
			//engine.log("Drawn at " + (center.x - vectVel.x*a) + ", " + (center.y - vectVel.y*a));
		}
	}

	public void move(int ms) {
		setCenter(center.x + (vectVel.x * (ms / 100f)), center.y
				+ (vectVel.y * (ms / 100f)));
	}

	@Override
	public void setCenter(float x, float y) {
		if (x < 0 || y < 0 || x > engine.getEnvironmentSize().x || y > engine.getEnvironmentSize().y) {
			death = true;
			return;
		}
		super.setCenter(x, y);
	}
	
	public void checkCollision(PolygonActor other) {
		return; //Particle does not collide
	}
	
	public String toString() {
		return "Particle";
	}

	public void setCenter(Point2D.Float center) {
		this.center = center;
	}
}
