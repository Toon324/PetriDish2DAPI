package petri.api;

import java.awt.Graphics;

/**
 * An Actor that will change frames from a AnimatedImage while moving to display
 * an animation.
 * 
 * @author Cody Swendrowski
 */
public abstract class AnimatedImageActor extends ImageActor {

	protected int xTile, yTile, tileTimer, tileSpeed, xTiles, yTiles;

	/**
	 * Creates a new AnimatedImageActor.
	 * 
	 * @param e
	 *            GameEngine to utilize
	 * @param i
	 *            AnimatedImage to draw from
	 */
	public AnimatedImageActor(GameEngine e, GameImage i) {
		super(e, i);
		xTile = 0;
		yTile = 0;
		tileTimer = 0;
		tileSpeed = 0;
		xTiles = 0;
		yTiles = 0;
	}

	/**
	 * Moves the Actor. Every call updates a timer. When this timer exceeds
	 * tileSpeed, advances the frame to the next horizontal frame. When it
	 * reaches the end of horizontal frames, advances 1 frame vertically and
	 * starts from left horizontally again.
	 * 
	 * @param ms
	 *            Time since last call
	 */
	public void move(int ms) {
		tileTimer++;
		if (tileTimer > tileSpeed) {
			xTile++;
			tileTimer = 0;
			if (xTile >= xTiles) {
				xTile = 0;
				yTile++;
				if (yTile >= yTiles)
					yTile = 0;
			}
		}
		super.move(ms);
	}

	@Override
	public void draw(Graphics g) {
		try {
			g.drawImage(image.getImage(), (int) (center.x), (int) (center.y),
					(int) (center.x) + (int) (size.x), (int) (center.y)
							+ (int) (size.y), xTile * image.getWidth(), yTile
							* image.getHeight(), (xTile + 1) * image.getWidth(),
					(yTile + 1) * image.getHeight(), null);
		} catch (Exception e) {
			System.out.println("Could not draw animatedImage " + toString() + " due to a null image.");
		}
	}
}
