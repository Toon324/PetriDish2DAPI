/**
 * 
 */
package petri.api;

/**
 * @author Cody
 *
 */
public abstract class AnimatedImageActor extends ImageActor {

	protected int xTile, yTile, tileTimer, tileSpeed, xTiles, yTiles;
	/**
	 * @param e
	 * @param i
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
	 * Moves the Actor.
	 * @param ms Time since last call
	 */
	public void move(int ms)
	{	
		tileTimer++;
		if (tileTimer > tileSpeed)
		{
			xTile++;
			tileTimer =0;
			if (xTile > xTiles)
			{
				xTile = 0;
				yTile++;
				if (yTile > yTiles)
					yTile = 0;
			}
		}
		
		super.move(ms);
	}
}
