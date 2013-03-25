package petri.api;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Abstract class that contains all of the necessary methods for GameModes.
 * 
 * @author Cody Swendrowski, Dan Miller
 */

public class GameMode {

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
	}

	/**
	 * Runs the logic of the GameMode.
	 */
	public void run(int ms) {
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

	public String toString() {
		return "GameMode";
	}
	
	public void clickedAt(MouseEvent e) {
		try {
			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).checkClick(e.getX(), e.getY());
			}
		} catch (java.lang.NullPointerException ex) {
			GameEngine.log("No defined buttons in " + this.toString());
		}
	}

	/**
	 * Accepts keyboard input.
	 * 
	 * @param e
	 *            KeyEvent
	 */
	public void keyTyped(KeyEvent e) {}
	
	public void mouseMoved(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void keyReleased(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {}
}
