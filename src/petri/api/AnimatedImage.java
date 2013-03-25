package petri.api;

import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * @author Cody Swendrowski
 * 
 */
public class AnimatedImage extends GameImage {

	private static final long serialVersionUID = -1217005359769960098L;
	private ArrayList<Polygon> outlines = new ArrayList<Polygon>();

	/**
	 * Creates a new container of a High and Low res Image of name s, and
	 * generates an outline.
	 * 
	 * @param s
	 *            Name of Image to read in
	 */
	public AnimatedImage(String s, int numHor, int numVert) {

		numAcross = numHor;
		numDown = numVert;
		
		try {
			image = ImageIO.read(getClass().getResource(path + s));
		} catch (Exception e) {
			GameEngine.log("Error in Image retrevial for " + s);
		}
		width = image.getWidth()/numAcross;
		height = image.getHeight()/numDown;
		try {
			int n = numDown * numAcross;
			for (int o = 0; o < n; o++) {
				outlines.add(new Polygon());
			}

			for (int y = 0; y < numDown; y++) {
				for (int x = 0; x < numAcross; x++) {
					generate(outlines.get(y * numAcross + x),
							width, height);
				}
			}
		} catch (Exception e) {
			GameEngine.log("Error in Image generation for " + s);
		}
	}

	/**
	 * @param s
	 * @param p
	 * @throws IOException
	 */
	public AnimatedImage(String p, String s, int numHor, int numVert) throws IOException {
		
		numAcross = numHor;
		numDown = numVert;
		path = p;
		try {
			image = ImageIO.read(getClass().getResource(path + s));
		} catch (Exception e) {
			GameEngine.log("Error in Image retrevial for " + s);
		}
		width = image.getWidth()/numAcross;
		height = image.getHeight()/numDown;
		try {
			int n = numDown * numAcross;
			for (int o = 0; o < n; o++) {
				outlines.add(new Polygon());
			}

			for (int y = 0; y < numDown; y++) {
				for (int x = 0; x < numAcross; x++) {
					generate(outlines.get(y * numAcross + x),
							width, height);
				}
			}
		} catch (Exception e) {
			GameEngine.log("Error in Image generation for " + s);
		}
	}

	private void generate(Polygon outline, int w, int h) {

		for (int x = w; x < w + width; x++) {

			Boolean last = null;
			for (int y = h; y < h + height; y++) {
				Boolean solid = false; // Transparent
				int pixel = image.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;
				if (alpha != 255) // Color
				{
					solid = true;
				}
				if (last == null) {
					last = solid;
				} else if (last != solid) {
					outline.addPoint(x, y);
				}

				if (((y == height) || (x == height)) && solid) {
					outline.addPoint(x, y);
				}

				last = solid;
			}
		}
	}

}
