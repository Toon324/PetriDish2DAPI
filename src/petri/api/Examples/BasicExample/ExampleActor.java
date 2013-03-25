/**
 * 
 */
package petri.api.Examples.BasicExample;

import java.awt.geom.Point2D;

import petri.api.AnimatedImageActor;
import petri.api.GameEngine;
import petri.api.GameImage;

/**
 * @author Cody
 *
 */
public class ExampleActor extends AnimatedImageActor {
	
	private final int MAX_CNT = 500;
	private int cnt;

	/**
	 * @param e
	 * @param i
	 */
	public ExampleActor(GameEngine e, GameImage i) {
		super(e, i);
		vectVel.x = 10;
		cnt = 0;
		size.x = 50;
		size.y = 50;
		xTiles = i.getNumAcross();
		yTiles = i.getNumDown();
		tileSpeed = 50;
	}

	@Override
	public void move(int ms) {
		cnt++;
		if (cnt > MAX_CNT) {
			vectVel.x = -vectVel.x;
			cnt = 0;
		}
		super.move(ms);
	}

	public Point2D.Float getSize() {
		return size;
	}

}
