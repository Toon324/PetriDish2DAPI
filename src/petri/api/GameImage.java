package petri.api;

import java.awt.Component;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The base class for all Images used in the game. Generates a pixel-perfect
 * outline of the Image by determining boundaries as a change between an alpha
 * (transparant) pixel and a non-alpha (color) pixel. As such, this method is
 * only accurate in determining the actual image contained in a .png. User must
 * override to support other edge-detection methods if they want pixel-perfect
 * collisions for other image formats, or .pngs with non-transparent
 * backgrounds.
 * 
 * @author Cody Swendrowski
 */
public class GameImage extends Component {
	private static final long serialVersionUID = -5278646585066522954L;

	protected BufferedImage image;
	protected String path = "";
	protected Polygon outline;
	protected int width, height, numDown, numAcross;

	public GameImage() {
		width = 0;
		height = 0;
		numDown = 1;
		numAcross = 1;
		outline = new Polygon();
	}

	/**
	 * Loads a new Image, and generates an outline.
	 * 
	 * @param s
	 *            Name of Image to read in
	 * @throws IOException
	 *             if file is not found
	 */
	public GameImage(String s) throws IOException {
		this();
		image = ImageIO.read(getClass().getResource(s));
		generate();
	}

	/**
	 * Loads a new Image at path p, and generates an outline.
	 * 
	 * @param p
	 *            Path to Image, from this class
	 * @param s
	 *            Name of Image
	 * @throws IOException
	 *             if file is not found
	 */
	public GameImage(String p, String s) throws IOException {
		this();
		path = p;
		image = ImageIO.read(getClass().getResource(path + s));
		generate();
	}

	/**
	 * Generates an outline from the Image by determining pixel color and
	 * comparing to surrounding pixels to determine an edge.
	 */
	protected void generate() {
		width = image.getWidth();
		height = image.getHeight();

		for (int x = 0; x < width; x++) {
			Boolean lastPixelWasSolid = null;
			for (int y = 0; y < height; y++) {
				Boolean thisPixelIsSolid = false;

				int pixel = image.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;

				if (alpha == 255) // Color
					thisPixelIsSolid = true;

				if (lastPixelWasSolid == null)
					lastPixelWasSolid = thisPixelIsSolid;

				else if (!lastPixelWasSolid && thisPixelIsSolid)
					outline.addPoint(x, y);

				else if (!thisPixelIsSolid && lastPixelWasSolid)
					outline.addPoint(x, y - 1);

				lastPixelWasSolid = thisPixelIsSolid;
			}
		}

		//outline = traceEdges(outline);
		outline = removeExtraPoints(outline, 0);
	}

	private Polygon traceEdges(Polygon p) {
		Polygon toReturn = new Polygon();
		
		Point start = new Point(p.xpoints[0], p.ypoints[0]);
		
		
		return toReturn;
	}
	
	private boolean polyContains(Polygon poly, int x, int y) {
		for (int i=0; i<poly.npoints; i++)
			if (x == poly.xpoints[i])
				if (y == poly.ypoints[i])
					return true;
		
		return false;
	}

	/**
	 * Calculates the angle differential between consecutive line segments of a
	 * Polygon. If the difference is less than radianVariance, it does not add
	 * the point between those two line segments into the new Polygon. Every
	 * other point is added, and the newly built Polygon is returned. This leads
	 * to faster collision checking.
	 * 
	 * Written by Dan Miller
	 * 
	 * @param poly
	 *            Polygon to remove extra points from
	 * @param radianVariance
	 *            Amount one point has to be apart from the other to save itself
	 *            from being removed, in radians
	 * @return The simplified Polygon
	 */
	protected Polygon removeExtraPoints(Polygon poly, float radianVariance) {
		Polygon resultant = new Polygon();
		int[] xPoints = poly.xpoints, yPoints = poly.ypoints;
		double curAngle, lastAngle = 0;
		for (int i = poly.npoints - 1; i > 0; i--) {
			curAngle = (Math.atan2((yPoints[i] - yPoints[i - 1]),
					(xPoints[i] - xPoints[i - 1])) + 2 * Math.PI)
					% (2 * Math.PI);
			if (Math.abs(curAngle - lastAngle) >= radianVariance) {
				resultant.addPoint(xPoints[i], yPoints[i]);
			}
			lastAngle = curAngle;
		}
		curAngle = Math.atan2((yPoints[0] - yPoints[poly.npoints - 1]),
				(xPoints[0] - xPoints[poly.npoints - 1]));
		if (Math.abs(curAngle - lastAngle) >= radianVariance) {
			resultant.addPoint(xPoints[0], yPoints[0]);
		}
		return resultant;
	}

	/**
	 * Returns an ArrayList containing Point objects representing the outline.
	 * 
	 * @return outline
	 */
	public Polygon getOutline() {
		if (outline == null)
			GameEngine.log("Null outline in " + this.toString());
		return outline;
	}

	/**
	 * Returns the High-Res Image to draw with.
	 * 
	 * @return BufferedImage image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Returns the width of the image.
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the image.
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns how many frames there are in the Image, horizontally.
	 * 
	 * @return numAcross
	 */
	public int getNumAcross() {
		return numAcross;
	}

	/**
	 * Returns how many frames there are in the Image, vertically.
	 * 
	 * @return numDown
	 */
	public int getNumDown() {
		return numDown;
	}
}
