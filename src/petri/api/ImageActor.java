/**
 * 
 */
package petri.api;

import java.awt.Graphics;

/**
 * @author Cody
 *
 */
public abstract class ImageActor extends Actor {

	protected GameImage image;
	
	/**
	 * @param e
	 */
	public ImageActor(GameEngine e, GameImage i) {
		super(e);
		image = i;
		basePoly = i.getOutline();
	}

	/* (non-Javadoc)
	 * @see petri.api.Actor#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(image.getImage(), (int) center.x, (int) center.y, null);
	}

}
