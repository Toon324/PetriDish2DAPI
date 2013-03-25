package petri.api;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * Starts the game and displays the Applet.
 * 
 * @author Cody Swendrowski
 */
public final class GameInitializer {
	GameApplet game;
	JFrame frame;
	
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


	public GameEngine getEngine() {
		return game.getEngine();
	}
	
	public JFrame getFrame() {
		return frame;
	}


	public void startGame() {
		game.startGame();
		// Runs game
		game.run();
	}
	
	public void stopGame() {
		game.stopGame();
	}
}
