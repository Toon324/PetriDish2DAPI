package petri.api;

import java.applet.Applet;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * The class that controls and owns all necessary objects.
 * 
 * @author Cody Swendrowski
 */
public class Game extends Applet implements Runnable, MouseListener,
		MouseMotionListener, KeyListener {
	
	private static final long serialVersionUID = 42l;
	
	private Thread th; // Game thread
	private Thread close; // Used for closing the game
	private GameEngine engine;

	/**
	 * Creates a new game object.
	 * 
	 * @param debug
	 *            If True, game prints out debug information.
	 */
	public Game() {
		close = new Thread(new CloseHook(this));
		th = new Thread(this);
		Runtime.getRuntime().addShutdownHook(close);
		GameEngine.log("Game has been initialized.");
		GameEngine.log("Found " + Runtime.getRuntime().availableProcessors()
				+ " processors to use");
	}

	/**
	 * Called when game is first initialized. Sets all values and objects to
	 * default state, and allows this class to listen to Mouse and Keyboard
	 * input.
	 */
	public void init() {
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		engine = new GameEngine(false);
		engine.setWindowSize(getWidth(), getHeight());

	}

	/**
	 * A thread used to close the game correctly.
	 * 
	 * @author Cody Swendrowski
	 */
	public class CloseHook implements Runnable {
		Game g;

		public CloseHook(Game game) {
			g = game;
		}

		@Override
		public void run() {
			g.onClose(); // Closes the game from a new thread to avoid errors
			try {
				this.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Starts the game thread.
	 */
	public void start() {
		th.start();
	}

	/**
	 * Called to run the game. Will continue running until game is closed. All
	 * game logic is called from here.
	 */
	public synchronized void run() {
		// run until stopped
		while (true) {
			// controls game flow
			engine.run();

			// repaint applet
			repaint();

			try {
				wait(); // wait for applet to draw
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		}
	}

	/**
	 * Updates the graphics of the game using a double buffer system to avoid
	 * screen flicker.
	 */
	public void update(Graphics g) {
		// Start buffer
		Image dbImage = createImage(getWidth(), getHeight());
		Graphics dbg = dbImage.getGraphics();

		// Clear screen in background
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, getWidth(), getHeight());

		// Draw game in background
		dbg.setColor(getForeground());
		paint(dbg);

		// Draw Image on screen
		g.drawImage(dbImage, 0, 0, this);
	}

	/**
	 * Only called from update(Graphics g). Paints all objects and menus in the
	 * game.
	 */
	public void paint(Graphics g) {
		engine.setWindowSize(getWidth(), getHeight());
		engine.paint(g);
		super.paint(g);

		synchronized (this) {
			notifyAll(); // Lets the run() method know that painting is
							// completed
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		engine.keyTyped(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		engine.clickedAt(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	/**
	 * Called when game exits. Properly closes resources.
	 */
	public void onClose() {
		engine.onClose();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (engine.isOver(e))
			setCursor(new Cursor(Cursor.HAND_CURSOR)); // If mouse is over a
														// Button, change to
														// hand cursor
		else
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // If it isn't, change
															// back to default
	}
}
