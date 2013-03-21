package petri.api;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Abstract class that contains all of the necessary methods for GameModes.
 * 
 * @author Cody Swendrowski, Dan Miller
 */

public abstract class GameMode {

	public ArrayList<Button> buttons;
	public GameEngine engine;
	protected Image background;

	/**
	 * Creates a new GameMode.
	 * 
	 * @param eng
	 *            GameEngine to pass data back to.
	 */
	public GameMode(GameEngine eng) {
		engine = eng;
		buttons = new ArrayList<Button>();
		try {
			background = ImageIO.read(MainMenu.class
					.getResourceAsStream("Resources\\space_background.jpg"));
		} catch (IOException e) {
			GameEngine.log("Can not load background image.");
		}
	}

	/**
	 * Runs the logic of the GameMode.
	 */
	public void run(int ms) {
	}

	/**
	 * Recieves MouseClick data.
	 * 
	 * @param x
	 *            MouseClick X value
	 * @param y
	 *            MouseClick Y value
	 */
	public void clicked(int x, int y) {
		try {
			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).checkClick(x, y);
			}
		} catch (java.lang.NullPointerException e) {
			GameEngine.log("No defined buttons in " + this.toString());
		}
	}

	/**
	 * Paints the necessary components in GameMode.
	 * 
	 * @param g
	 *            Graphics to paint with
	 */
	public void paint(Graphics g) {
		try {
			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).draw(g);
			}
		} catch (java.lang.NullPointerException e) {
			GameEngine.log("No defined buttons in " + this.toString());
		}
	}

	/**
	 * Checks every button to see if mouse is hovering over a Button.
	 * 
	 * @param x
	 *            X position of mouse
	 * @param y
	 *            Y position of mouse
	 * @return True is mouse is hovering over a Button in the current mode
	 */
	public boolean isOver(int x, int y) {
		try {
			for (int i = 0; i < buttons.size(); i++) {
				if (buttons.get(i).checkOver(x, y))
					return true;
			}
		} catch (java.lang.NullPointerException e) {
			GameEngine.log("No defined buttons in " + this.toString());
		}
		return false;
	}

}
