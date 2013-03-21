package petri.api;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * Starts the game and displays the Applet.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class Window {
	public static void main(String[] args) {
		// Sets up game
		Trivia game = new Trivia();
		JFrame frame = new JFrame("Trivia");

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
}
