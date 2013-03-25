package petri.api;

import java.awt.Component;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	 * @param s Name of Image to read in
	 * @throws IOException if file is not found
	 */
	public GameImage(String s) throws IOException
	{
		this();
		image = ImageIO.read(new File(path + s));
		generate();
	}
	
	public GameImage(String s, String p) throws IOException {
		this();
		path = p;
		image = ImageIO.read(new File(path + s));
		generate();
	}
	
	/**
	 * Generates an outline from the Low-res Image by determining pixel color and comparing to surrounding pixels to determine an edge.
	 */
	private void generate()
	{
		width = image.getWidth();
	    height = image.getHeight();
	    
	    for (int x=0; x<width; x++)
	    {
	    	Boolean last = null;
	    	for (int y=0; y<height; y++)
	    	{	
	    		Boolean solid = false; //Transparent
	    		int pixel = image.getRGB(x, y);
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
	 * Returns an ArrayList containing Point objects representing the outline.
	 * @return outline
	 */
	public Polygon getOutline()
	{
		return outline;
	}
	
	/**
	 * Returns the High-Res Image to draw with.
	 * @return BufferedImage image
	 */
	public BufferedImage getImage()
	{
		return image;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public int getNumAcross() {
		return numAcross;
	}
	
	public int getNumDown() {
		return numDown;
	}
}
