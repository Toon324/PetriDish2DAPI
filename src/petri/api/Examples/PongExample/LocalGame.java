/**
 * 
 */
package petri.api.Examples.PongExample;

import petri.api.GameEngine;
import petri.api.GameMode;

/**
 * @author Cody
 *
 */
public class LocalGame extends GameMode {

	/**
	 * @param eng
	 */
	public LocalGame(GameEngine eng) {
		super(eng);
	}

	@Override
	public String toString() {
		return "LocalGame";
	}
}
