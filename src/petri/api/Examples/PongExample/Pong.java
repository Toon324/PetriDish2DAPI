package petri.api.Examples.PongExample;

import petri.api.*;

/**
 * 
 * @author Cody Swendrowski
 */
public class Pong {
	private static GameInitializer gi = new GameInitializer("Pong", true);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		GameEngine engine = gi.getEngine();

		MainMenu mainMenu = new MainMenu(engine);
		LocalGame localGame = new LocalGame(engine);
		InternetGame internetGame = new InternetGame(engine);

		engine.addGameMode(mainMenu);
		engine.addGameMode(localGame);
		engine.addGameMode(internetGame);

		engine.setCurrentGameMode(mainMenu.toString()); // Can also be called as
													// .setCurrentGameMode("Main Menu")
													// or .setCurrentGameMode(0)
		
		gi.startGame();
	}
	
	public static void stopGame() {
		gi.stopGame();
		System.exit(0);
	}
}
