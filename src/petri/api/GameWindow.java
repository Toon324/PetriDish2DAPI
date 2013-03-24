package petri.api;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * Starts the game and displays the Applet.
 * 
 * @author Cody Swendrowski
 */
public final class GameWindow {
	Game game;
	JFrame frame;
	
	public GameWindow(String name) {
		// Sets up game
		game = new Game();
		frame = new JFrame(name);

		// Initializes game
		game.init();

		// Add applet to frame
		frame.add(game, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setResizable(true);
		frame.setVisible(true);

		// Runs game
		game.run();
	}


	public GameEngine getEngine() {
		return game.getEngine();
	}
	
	public JFrame getFrame() {
		return frame;
	}
}
