package petri.api;

import java.awt.Graphics;

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
		basePoly = i.getOutline();
	}

	@Override
	public void move(int ms) {
		super.move(ms);
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(image.getImage(), (int) center.x, (int) center.y, null);
	}

}
