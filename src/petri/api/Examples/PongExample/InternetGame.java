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
public class InternetGame extends GameMode {

	/**
	 * @param eng
	 */
	public InternetGame(GameEngine eng) {
		super(eng);
	}

	@Override
	public String toString() {
		return "InternetGame";
	}
}
