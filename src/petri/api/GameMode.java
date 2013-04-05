package petri.api;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Base class that contains all of the necessary methods for GameModes. This
 * class should be overridden by the user to enable
 * 
 * @author Cody Swendrowski
 */

public class GameMode {

	protected ArrayList<Button> buttons;
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
	 * Runs the logic of the GameMode. By default, this does nothing. Call
	 * engine.actors.handleActors(ms) to make Actors move.
	 * 
	 * @param ms
	 *            The amount of time passed since last call, in Milliseconds
	 */
	public void run(int ms) {
	}

	/**
	 * Paints the necessary components in GameMode. Call
	 * engine.actors.drawActors(g) to draw Actors.
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

	/**
	 * Automatically sets the toString() to the class's simple name. Used to
	 * switch GameModes in GameEngine .setCurrentGameMode(String name).
	 */
	public String toString() {
		return this.getClass().getSimpleName();
	}

	/**
	 * Accepts mouse input. Checks click against all buttons in GameMode.
	 * 
	 * @param e
	 *            MouseEvent
	 */
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
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Accepts mouse input.
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mouseMoved(MouseEvent e) {
	}

	/**
	 * Accepts mouse input.
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mouseDragged(MouseEvent e) {
	}

	/**
	 * Accepts mouse input.
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * Accepts mouse input.
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Accepts mouse input.
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Accepts mouse input.
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Accepts mouse input.
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Accepts mouse input.
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void keyPressed(KeyEvent e) {
	}
}
