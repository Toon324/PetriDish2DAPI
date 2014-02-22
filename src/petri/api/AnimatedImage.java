package petri.api;

import java.awt.Polygon;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * A GameImage that divide up the given image into frames. Each frame has its
 * own generated outline, stored in an ArrayList of Polygons.
 * 
 * @author Cody Swendrowski
 */
public class AnimatedImage extends GameImage {

	private static final long serialVersionUID = -1217005359769960098L;
	private ArrayList<Polygon> outlines = new ArrayList<Polygon>();

	/**
	 * Creates a new AnimatedImage. Divides image into frame using the given
	 * user data.
	 * 
	 * @param s
	 *            Name of Image to read in
	 * @param numHor
	 *            Number of frames in image, horizontally
	 * @param numVert
	 *            Number of frames in image, vertically
	 * @throws IOException
	 *             If given Image is not found, an IOException is thrown
	 */
	public AnimatedImage(String s, int numHor, int numVert) throws Exception {

		numAcross = numHor;
		numDown = numVert;

		image = ImageIO.read(getClass().getResource(path + s));
		width = image.getWidth() / numAcross;
		height = image.getHeight() / numDown;
		try {
			int n = numDown * numAcross;
			for (int o = 0; o < n; o++) {
				outlines.add(new Polygon());
			}

			for (int y = 0; y < numDown; y++) {
				for (int x = 0; x < numAcross; x++) {
					generate(outlines.get(y * numAcross + x), width, height);
				}
			}
		} catch (Exception e) {
			GameEngine.log("AnimatedImage error: Can not generate outline for " + s + ". Reason: " + e.getMessage());
		}
	}

	/**
	 * Creates a new AnimatedImage. Divides image into frame using the given
	 * user data.
	 * 
	 * @param p
	 *            Path that the Image is located at
	 * @param s
	 *            Name of Image to read in
	 * @param numHor
	 *            Number of frames in image, horizontally
	 * @param numVert
	 *            Number of frames in image, vertically
	 * @throws IOException
	 *             If given Image is not found, an IOException is thrown
	 */
	public AnimatedImage(String p, String s, int numHor, int numVert)
			throws IOException {

		numAcross = numHor;
		numDown = numVert;
		path = p;
		image = ImageIO.read(getClass().getResource(path + s));
		width = image.getWidth() / numAcross;
		height = image.getHeight() / numDown;
		try {
			int n = numDown * numAcross;
			for (int o = 0; o < n; o++) {
				outlines.add(new Polygon());
			}

			for (int y = 0; y < numDown; y++) {
				for (int x = 0; x < numAcross; x++) {
					generate(outlines.get(y * numAcross + x), width, height);
				}
			}
		} catch (Exception e) {
			GameEngine.log("AnimatedImage error: Can not generate outline for " + s + ". Reason: " + e.getMessage());
		}
	}

	/**
	 * Helper method. Generates an outline that is stored in given Polygon. See
	 * GameImage.generate for more information on how this is done.
	 * 
	 * @param outline
	 *            Polygon to store outline in
	 * @param w
	 *            Width of frame
	 * @param h
	 *            Height of frame
	 */
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
