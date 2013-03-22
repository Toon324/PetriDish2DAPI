package petri.api;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

/**
 * Generic class for all objects in the game that both move and paint.
 * 
 * @author Cody Swendrowski
 */
public abstract class PolygonActor extends Actor{

	protected Color drawClr;
	protected Polygon drawPoly;

	/**
	 * Creates a new Actor.
	 * 
	 * @param e
	 *            GameEngine to utilize
	 */

	public PolygonActor(GameEngine e) {
		super(e);
		basePoly = new Polygon();
		vectVel = new Point2D.Float(0, 0);
		center = new Point2D.Float(0, 0);
		drawClr = Color.cyan;
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
		drawPoly = new Polygon(poly.xpoints, poly.ypoints, poly.npoints);
	}

	/**
	 * Returns Polygon used for drawing
	 * 
	 * @return drawnPoly
	 */
	public Polygon getDrawnPoly() {
		return drawPoly;
	}

	/**
	 * Draws the Actor.
	 * 
	 * @param p
	 *            Corner of the screen to draw in reference to
	 * @param g
	 *            Graphics to be drawn with
	 */
	public void draw(Graphics g) {
		g.setColor(drawClr);
		if (drawPoly == null)
			drawPoly = basePoly;

		drawPoly(g, drawPoly, new Point((int) center.x, (int) center.y), true);
	}

	/**
	 * Draws the Polygon with given graphics and center.
	 * 
	 * @param g
	 *            Graphics to draw with
	 * @param p
	 *            Polygon to draw
	 * @param thisCenter
	 *            The center of the Polygon
	 * @param centerLines
	 *            If true, draws lines from all points to center
	 */
	private void drawPoly(Graphics g, Polygon p, Point thisCenter,
			boolean centerLines) {
		
		//If there is no Polygon, return
		if (p == null)
			return;

		int x = 0, y = 0, firstX = 0, firstY = 0;
		int res;
		float array[] = new float[6];
		PathIterator iter = p.getPathIterator(new AffineTransform());
		
		//Iterate through the Path of the Polygon, drawing lines according to data provided
		while (!iter.isDone()) {
			res = iter.currentSegment(array);
			switch (res) {
			case PathIterator.SEG_CLOSE:
				array[0] = firstX;
				array[1] = firstY;
			case PathIterator.SEG_LINETO:
				g.drawLine(x, y, (int) array[0], (int) array[1]);

				//Draws lines to center if boolean is true
				if (centerLines)
					g.drawLine(x, y, (int) thisCenter.x, (int) thisCenter.y);

				x = (int) array[0];
				y = (int) array[1];
				break;
			case PathIterator.SEG_MOVETO:
				x = (int) array[0];
				y = (int) array[1];
				firstX = x;
				firstY = y;
				break;
			}
			iter.next();
		}
	}
	
	/**
	 * Sets the center of the Actor by first transforming the basePoly (and
	 * drawPoly if not null) to (0,0), then transforms to new center.
	 * 
	 * @param x
	 *            X co-ordinate of center
	 * @param y
	 *            Y co-ordinate of center
	 */
	public void setCenter(float x, float y) {
		//Translates to (0,0)
		if (drawPoly != null)
			drawPoly.translate((int) -center.x, (int) -center.y);
		basePoly.translate((int) -center.x, (int) -center.y);
		
		//Translates to new center
		center = new Point2D.Float(x, y);
		if (drawPoly != null)
			drawPoly.translate((int) center.x, (int) center.y);
		basePoly.translate((int) center.x, (int) center.y);
	}
	
	/**
	 * Checks to see if Actor is colliding with another Actor.
	 * 
	 * @param other
	 *            Actor to check collision against
	 */
	public void checkCollision(PolygonActor other) {
		if (other.equals(this))
			return;
		int distance = 1;
		if (other.basePoly != null && basePoly != null)
			distance = Math.abs((other.basePoly.getBounds().x - basePoly.getBounds().x)
				/ (other.basePoly.getBounds().y - basePoly.getBounds().y));
		if (distance > 5
				|| drawClr.toString()
						.equalsIgnoreCase(other.drawClr.toString())
				||  other instanceof Particle)
			return;

		Polygon otherPoly = other.basePoly;
		for (int i = 0; i < otherPoly.npoints; i++) {
			if (basePoly.contains(new Point(otherPoly.xpoints[i],
					otherPoly.ypoints[i]))) {
				setDeath(true);
				other.setDeath(true);
			}
		}
	}

}
