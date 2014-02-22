package petri.api;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Holds both a High and Low res Image for use in drawing and collision
 * detection, respectively. Creates the outline of an Image for use in collision
 * detection from the Low-Res. Provides High-Res Image for drawing.
 * 
 * Notes for use: The high resolution image can be of any size. It is
 * automatically scaled down to the size of the Actor. The low resolution image,
 * however, should be manually sized down to the size of the Actor using a
 * third-party program. This will ensure that the Actor uses a pixel-perfect
 * collision outline while still looking nice.
 * 
 * @author Cody Swendrowski
 */

public class DualResolutionGameImage extends GameImage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8990635310168498546L;

	private BufferedImage image2;

	/**
	 * Creates a new container of a High and Low res Image of name s, and
	 * generates an outline.
	 * 
	 * @param high
	 *            Name of high resolution image
	 * @param low
	 *            Name of low resolution image
	 * @throws IOException
	 *             IFF file is not found, throws an IOException
	 */
	public DualResolutionGameImage(String high, String low) throws Exception {
		super(low);
		image2 = ImageIO.read(getClass().getResource(high)); // High res used for
														// drawing
	}

	/**
	 * Creates a new container of a High and a Low res Image of name s at path
	 * p, and generates an outline.
	 * 
	 * @param p
	 *            Path to images, from this class
	 * @param high
	 *            Name of high resolution image
	 * @param low
	 *            Name of low resolution image
	 * @throws IOException
	 *             IFF file is not found, throws an IOException
	 */
	public DualResolutionGameImage(String p, String high, String low)
			throws Exception {
		super(p,low);
		
		image2 = ImageIO.read(getClass().getResource(path + high)); // High res used for
														// drawing
	}

	/**
	 * Returns the High-Res Image to draw with.
	 * 
	 * @return BufferedImage image
	 */
	public BufferedImage getImage() {
		if (image2 == null)
			GameEngine.log("DualResolutionImage error: The BufferedImage is null.");
		return image2;
	}
}
