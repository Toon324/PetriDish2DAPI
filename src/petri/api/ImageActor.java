package petri.api;

import java.awt.Graphics;
import java.awt.Polygon;

/**
 * An Actor that uses an Image when drawing itself.
 * 
 * @author Cody Swendrowski
 */
public abstract class ImageActor extends Actor {

	protected GameImage image;

	/**
	 * Creates a new ImageActor.
	 * 
	 * @param e
	 *            GameEngine to utilize
	 * @param i
	 *            GameImage to draw from
	 */
	public ImageActor(GameEngine e, GameImage i) {
		super(e);
		image = i;
		if (image == null)
			GameEngine.log("Null Image in Actor " + this.toString());
		setBasePoly(new Polygon(image.getOutline().xpoints,image.getOutline().ypoints, image.getOutline().npoints));
	}

	@Override
	public void move(int ms) {
		super.move(ms);
	}

	@Override
	public void draw(Graphics g) {
		try {
			g.drawImage(image.getImage(), getCorner().x, getCorner().y ,(int) size.x,(int) size.y, null);
		} catch (Exception e) {
			System.out.println("Could not draw " + toString() + " due to a null image.");
		}
	}
	
	@Override
	public String toString() {
		return "ImageActor";
	}

}
