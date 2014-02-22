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
 * The class that takes Applet input and distributes it to GameEngine and
 * current GameMode.
 * 
 * @author Cody Swendrowski
 */
public final class GameApplet extends Applet implements Runnable, MouseListener,
		MouseMotionListener, KeyListener {

	private static final long serialVersionUID = 42l;

	private Thread th; // Game thread
	private Thread close; // Used for closing the game
	private GameEngine engine;
	private boolean debugMode;

	/**
	 * Creates a new game applet.
	 * 
	 * @param debug
	 *            If True, game prints out debug information.
	 */
	public GameApplet(boolean debug) {
		debugMode = debug;
		close = new Thread(new CloseHook(this));
		th = new Thread(this);
		Runtime.getRuntime().addShutdownHook(close);
		GameEngine.log("Game has been initialized.");
		GameEngine.log("Found " + Runtime.getRuntime().availableProcessors()
				+ " processors to use");
		canRun = false;
	}

	/**
	 * Called when game is first initialized. Allows this class to listen to
	 * Mouse and Keyboard input.
	 */
	public void init() {
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		engine = new GameEngine(debugMode);
		engine.setWindowSize(getWidth(), getHeight());

	}

	/**
	 * A thread used to close the game correctly.
	 * 
	 * @author Cody Swendrowski
	 */
	public class CloseHook implements Runnable {
		GameApplet g;

		public CloseHook(GameApplet game) {
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

	private boolean canRun;

	/**
	 * Called to run the game. Will continue running until game is closed. All
	 * game logic calls stem from here.
	 */
	public synchronized void run() {
		// run until stopped
		while (true) {
			// GameEngine.log("Running");
			while (canRun) {

				// controls game flow
				engine.run();

				// repaint applet
				repaint();

				try {
					wait(); // wait for applet to draw
				} catch (Exception ex) {
					GameEngine.log("GameApplet error: " + ex.getMessage());
				}
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
		engine.getCurrentGameMode().keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		engine.getCurrentGameMode().keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		engine.getCurrentGameMode().keyTyped(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		engine.getCurrentGameMode().clickedAt(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		engine.getCurrentGameMode().mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		engine.getCurrentGameMode().mouseExited(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		engine.getCurrentGameMode().mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		engine.getCurrentGameMode().mouseReleased(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		engine.getCurrentGameMode().mouseDragged(e);
	}

	/**
	 * Called when game exits. Properly closes resources.
	 */
	public void onClose() {
		engine.onClose();
	}

	/**
	 * Called to allow game logic to run.
	 */
	public void startGame() {
		canRun = true;
	}

	/**
	 * Called to stop game logic from running. Does not exit game.
	 */
	public void stopGame() {
		canRun = false;
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

		engine.getCurrentGameMode().mouseMoved(e);
	}

	/**
	 * Returns the GameEngine that the GameApplet is using.
	 * 
	 * @return engine
	 */
	public GameEngine getEngine() {
		return engine;
	}

	/**
	 * Sets the GameEngine that the GameApplet should use. This method is called
	 * to replace the default GameEngine with an overridden varient.
	 * 
	 * @param engine
	 *            The GameEngine to set
	 */
	public void setEngine(GameEngine engine) {
		this.engine = engine;
	}
}
