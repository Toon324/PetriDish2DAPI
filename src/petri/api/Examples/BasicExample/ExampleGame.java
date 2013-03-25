/**
 * 
 */
package petri.api.Examples.BasicExample;

import petri.api.*;

/**
 * @author Cody
 *
 */
public class ExampleGame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameInitializer init = new GameInitializer("Example Game", true);
		
		GameEngine engine = init.getEngine();
		
		ExampleMode ea = new ExampleMode(engine);
		
		engine.addGameMode(ea);
		
		engine.setCurrentGameMode(ea.toString());
		
		init.startGame();
	}

}
