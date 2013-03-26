package petri.api.Examples.Pong;

import petri.api.*;

/**
 * 
 * @author Cody Swendrowski
 */
public class Pong {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameInitializer gi = new GameInitializer("Pong", true);
		GameEngine engine = gi.getEngine();

		MainMenu mainMenu = new MainMenu(engine);

		engine.addGameMode(mainMenu);

		engine.setCurrentGameMode(mainMenu.toString()); // Can also be called as
													// .setCurrentGameMode("Main Menu")
													// or .setCurrentGameMode(0)
	}

}
