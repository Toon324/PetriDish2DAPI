/**
 * 
 */
package petri.api;

import java.awt.Graphics;

/**
 * @author Cody
 *
 */
public class ImageActor extends Actor {

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
		// TODO Auto-generated method stub

	}

}
