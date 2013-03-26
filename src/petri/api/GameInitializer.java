package petri.api;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * Starts the game and displays the Applet. Offers methods to fetch the
 * GameEngine, GameApplet, and Frame for manipulation.
 * 
 * @author Cody Swendrowski
 */
public final class GameInitializer {
	private GameApplet game;
	private JFrame frame;

	/**
	 * Creates a new Game with the given name.
	 * 
	 * @param name
	 *            Name to display on frame
	 * @param debug
	 *            IFF true, GameEngine prints out messages to console.
	 */
	public GameInitializer(String name, boolean debug) {
		// Sets up game
		game = new GameApplet(debug);
		frame = new JFrame(name);

		// Initializes game
		game.init();

		// Add applet to frame
		frame.add(game, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setResizable(true);
		frame.setVisible(true);
	}

	/**
	 * Returns the GameApplet. Called to replace the GameApplet's default
	 * GameEngine.
	 * 
	 * @return the GameApplet
	 */
	public GameApplet getGameApplet() {
		return game;
	}

	/**
	 * Returns the current GameEngine.
	 * 
	 * @return the GameEngine
	 */
	public GameEngine getEngine() {
		return game.getEngine();
	}

	/**
	 * Returns the JFrame that the game is being drawn in.
	 * 
	 * @return the JFrame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Starts the game.
	 */
	public void startGame() {
		game.startGame();
		// Runs game
		game.run();
	}

	/**
	 * Pauses the flow of game logic. Called before closing the game.
	 */
	public void stopGame() {
		game.stopGame();
	}
}
