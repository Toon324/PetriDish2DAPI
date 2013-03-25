/**
 * 
 */
package petri.api.example;

import java.awt.geom.Point2D;

import petri.api.AnimatedImageActor;
import petri.api.GameEngine;
import petri.api.GameImage;

/**
 * @author Cody
 *
 */
public class ExampleActor extends AnimatedImageActor {
	
	private final int MAX_CNT = 10;
	private int cnt;

	/**
	 * @param e
	 * @param i
	 */
	public ExampleActor(GameEngine e, GameImage i) {
		super(e, i);
		vectVel.x = 1;
		cnt = 0;
		size.x = 50;
		size.y = 50;
		xTiles = i.getNumAcross();
		yTiles = i.getNumDown();
		tileSpeed = 50;
	}

	@Override
	public void move(int ms) {
		super.move(ms);
		cnt++;
		if (cnt > MAX_CNT)
			vectVel.x = -vectVel.x;
	}

	public Point2D.Float getSize() {
		return size;
	}

}
