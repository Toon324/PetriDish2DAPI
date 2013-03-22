package petri.api;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Holds both a High and Low res Image for use in drawing and collision detection, respectively.
 * Creates the outline of an Image for use in collision detection from the Low-Res.
 * Provides High-Res Image for drawing.
 * @author Cody Swendrowski
 */
@SuppressWarnings("serial")
public class DualResolutionGameImage extends GameImage
{
	private BufferedImage image1, image2;
	
	/**
	 * Creates a new container of a High and Low res Image of name s, and generates an outline.
	 * @param s Name of Image to read in
	 * @throws IOException if file is not found
	 */
	public DualResolutionGameImage(String high, String low) throws IOException
	{
		image1 = ImageIO.read(new File(path + low)); //Low res used for outline
		image2 = ImageIO.read(new File(path + high)); //High res used for drawing
		generate();
	}
	
	public DualResolutionGameImage(String high, String low, String p) throws IOException {
		path = p;
		image1 = ImageIO.read(new File(path + low)); //Low res used for outline
		image2 = ImageIO.read(new File(path + high)); //High res used for drawing
		generate();
	}
	
	/**
	 * Generates an outline from the Low-res Image by determining pixel color and comparing to surrounding pixels to determine an edge.
	 */
	private void generate()
	{
		int w = image1.getWidth();
	    int h = image1.getHeight();
	    
	    for (int x=0; x<w; x++)
	    {
	    	Boolean last = null;
	    	for (int y=0; y<h; y++)
	    	{	
	    		Boolean solid = false; //Transparent
	    		int pixel = image1.getRGB(x, y);
	    		int alpha = (pixel >> 24) & 0xff;
	    	    if (alpha != 255) //Color
	    	    {
	    	    	solid = true;
	    	    }
	    	    if (last == null)
	    	    {
	    	    	last = solid;
	    	    }
	    	    else if (last != solid)
	    	    {
	    	    	outline.addPoint(x,y);
	    	    }
	    	    last = solid;
	    	}
	    }
	}
	
	/**
	 * Returns the High-Res Image to draw with.
	 * @return BufferedImage image
	 */
	public BufferedImage getImage()
	{
		return image2;
	}
}
