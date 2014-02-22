package petri.api;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

/**
 * Handles all the game logic and painting based on the current game mode.
 * 
 * @author Cody Swendrowski
 */
public class GameEngine {

	// Resources to use
	public final Font large = new Font("Serif", Font.BOLD, 30);
	public final Font medium = new Font("Serif", Font.PLAIN, 20);
	public final Font small = new Font("Serif", Font.PLAIN, 12);
	
	private GameMode currentMode;
	private long millis; // Used to calculate time between each frame

	// GameModes
	protected ArrayList<GameMode> gameModes = new ArrayList<GameMode>();

	private ParticleEngine particleEngine;
	private SoundPlayer soundPlayer;
	private NetworkAdapter networkAdapter;

	protected ArrayList<Integer> scores = new ArrayList<Integer>();
	protected static String path = "";

	private Point environmentSize = new Point(0, 0);
	public static PrintWriter debugWriter;
	private static boolean debugMode;

	protected ArrayList<Long> stepTimes;
	private Actors actors;
	protected double FPS;

	/**
	 * @return the currentMode
	 */
	public GameMode getCurrentMode() {
		return currentMode;
	}

	/**
	 * @param currentMode the currentMode to set
	 */
	public void setCurrentMode(GameMode currentMode) {
		this.currentMode = currentMode;
	}

	/**
	 * @return the particleEngine
	 */
	public ParticleEngine getParticleEngine() {
		return particleEngine;
	}

	/**
	 * @param particleEngine the particleEngine to set
	 */
	public void setParticleEngine(ParticleEngine particleEngine) {
		this.particleEngine = particleEngine;
	}

	/**
	 * @return the soundPlayer
	 */
	public SoundPlayer getSoundPlayer() {
		return soundPlayer;
	}

	/**
	 * @param soundPlayer the soundPlayer to set
	 */
	public void setSoundPlayer(SoundPlayer soundPlayer) {
		this.soundPlayer = soundPlayer;
	}

	/**
	 * @return the networkAdapter
	 */
	public NetworkAdapter getNetworkAdapter() {
		return networkAdapter;
	}

	/**
	 * @param networkAdapter the networkAdapter to set
	 */
	public void setNetworkAdapter(NetworkAdapter networkAdapter) {
		this.networkAdapter = networkAdapter;
	}

	/**
	 * @return the scores
	 */
	public ArrayList<Integer> getScores() {
		return scores;
	}

	/**
	 * @param scores the scores to set
	 */
	public void setScores(ArrayList<Integer> scores) {
		this.scores = scores;
	}

	/**
	 * @return the actors
	 */
	public Actors getActors() {
		return actors;
	}

	/**
	 * @param actors the actors to set
	 */
	public void setActors(Actors actors) {
		this.actors = actors;
	}

	/**
	 * @return the fPS
	 */
	public double getFPS() {
		return FPS;
	}

	/**
	 * @param gameModes the gameModes to set
	 */
	public void setGameModes(ArrayList<GameMode> gameModes) {
		this.gameModes = gameModes;
	}

	/**
	 * @param environmentSize the environmentSize to set
	 */
	public void setEnvironmentSize(Point environmentSize) {
		this.environmentSize = environmentSize;
	}

	static {
		File file = new File(path + "debug.txt");
		try {
			if (!file.exists())
				file.createNewFile();
			debugWriter = new PrintWriter(
					new FileWriter(file.getAbsoluteFile()));
		} catch (IOException e) {
			System.out.println("Error creating debug debug output stream\n"
					+ System.getProperty("user.dir"));
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new GameEngine.
	 * 
	 * @param debug
	 *            If true, prints out debug messages.
	 */
	public GameEngine(boolean debug) {
		actors = new Actors(this);
		debugMode = debug;

		particleEngine = new ParticleEngine(this);
		networkAdapter = new NetworkAdapter();
		soundPlayer = new SoundPlayer();

		currentMode = new GameMode(this);

		environmentSize.x = 800;
		environmentSize.y = 600;

		millis = System.currentTimeMillis();

		stepTimes = new ArrayList<Long>();
		stepTimes.add(millis);
	}

	/**
	 * Runs the game logic based on the current game mode. Calls
	 * GameMode.run(int ms) on current GameMode.
	 */
	public void run() {
		long lastMillis = millis;
		millis = System.currentTimeMillis();
		stepTimes.add(millis - lastMillis);
		if (stepTimes.size() > 10)
			stepTimes.remove(0);

		currentMode.run((int) (millis - lastMillis));
		FPS = 1000 / average(stepTimes); // Calculates average FPS
	}

	/**
	 * Helper method. Finds the average in a given list of doubles. Used for FPS
	 * calculations.
	 * 
	 * @param list
	 *            List of numbers to use
	 * @return Average of list
	 */
	private double average(ArrayList<Long> list) {
		double avg = 0;
		int num = 0;
		for (long l : list) {
			avg += l;
			num++;
		}
		return (avg / num);
	}

	/**
	 * Paints the game based on the current game mode. Calls
	 * GameMode.paint(Graphics g) on current GameMode.
	 * 
	 * @param g
	 *            Graphics to paint with.
	 */
	public void paint(Graphics g) {
		currentMode.paint(g);
		g.setFont(large);
		g.setColor(Color.blue);
		if (debugMode) {
			g.drawString(String.format("%6.2f", FPS), 10, 30);
		}
	}

	/**
	 * Given a String, draws it centered between two bounds. Calculates this
	 * center using the current boundaries of the FontMetrics of the Graphics
	 * provided.
	 * 
	 * @param g
	 *            Graphics to draw with
	 * @param s
	 *            String to draw centered
	 * @param leftBound
	 *            Left boundary of area to center
	 * @param rightBound
	 *            Right boundary of area to center
	 * @param y
	 *            Location to draw the text vertically
	 */
	public void centerTextHorizontally(Graphics g, String s, int leftBound,
			int rightBound, int y) {
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D bounds = fm.getStringBounds(s, g);
		int width = (int) bounds.getWidth();

		g.drawString(s, ((rightBound - leftBound) / 2) - (width / 2), y);
	}

	/**
	 * Sets the current game mode.
	 * 
	 * @param newMode
	 */
	public void setMode(GameMode newMode) {
		currentMode = newMode;
	}

	/**
	 * Sets the current size of the window to draw in.
	 * 
	 * @param width
	 *            Width of window
	 * @param height
	 *            Height of window
	 */
	public void setWindowSize(int width, int height) {
		environmentSize.x = width;
		environmentSize.y = height;
	}

	/**
	 * Called when the engine is closed. Properly closes resources.
	 */
	public void onClose() {
		log("Closing writer");
		debugWriter.close();
	}

	/**
	 * Debug tool. Used to print a String if Debug mode is enabled.
	 * 
	 * @param s
	 *            String to print.
	 */
	public static void log(String s) {
		if (debugMode) {
			System.out.println(s);
		}
		debugWriter.println(s);
	}

	/**
	 * Checks the current mode to see if mouse is hovering over a Button.
	 * 
	 * @param e
	 *            MouseEvent to check
	 * @return True if mouse is over a Button
	 */
	public boolean isOver(MouseEvent e) {
		return currentMode.isOver(e.getX(), e.getY());
	}

	/**
	 * Returns the current score for the engine.
	 * @param i 
	 * 
	 * @return score
	 */
	public int getScore(int i) {
		while (scores.size() <= i)
			scores.add(new Integer(0));
		
		return scores.get(i).intValue();
	}

	/**
	 * Sets the current score for the engine.
	 * 
	 * @param newScore
	 *            New score to set
	 */
	public void setScore(int scoreIndex, int newScore) {
		while (scores.size() <= scoreIndex)
			scores.add(new Integer(0));
		
		scores.set(scoreIndex, Integer.valueOf(newScore));
	}

	/**
	 * Adds a GameMode to the list of available GameModes.
	 * 
	 * @param newMode
	 *            GameMode to add.
	 */
	public void addGameMode(GameMode newMode) {
		gameModes.add(newMode);
	}

	/**
	 * Given the name of a GameMode, sets the current GameMode to the given
	 * GameMode. Operates using GameMode.toString() and comparing nameOfMode to
	 * the .toString().
	 * 
	 * @param nameOfMode
	 *            String name of mode to switch to
	 */
	public void setCurrentGameMode(String nameOfMode) {
		for (GameMode mode : gameModes)
			if (mode.toString().equals(nameOfMode))
				currentMode = mode;
	}

	/**
	 * Given the index of a GameMode in the current gameModes ArrayList,
	 * switches to that GameMode.
	 * 
	 * @param indexOfMode
	 *            Index of GameMode to switch to.
	 */
	public void setCurrentGameMode(int indexOfMode) {
		currentMode = gameModes.get(indexOfMode);
	}

	/**
	 * Returns the current GameMode.
	 * 
	 * @return currentMode
	 */
	public GameMode getCurrentGameMode() {
		return currentMode;
	}

	/**
	 * Returns the ArrayList of currently available GameModes.
	 * 
	 * @return gameModes
	 */
	public ArrayList<GameMode> getGameModes() {
		return gameModes;
	}

	/**
	 * Returns the current environment size, which is pulled from the current
	 * window size.
	 * 
	 * @return environmentSize
	 */
	public Point getEnvironmentSize() {
		return environmentSize;
	}

	/**
	 * Gets a GameMode based on it's name.
	 * 
	 * @param name
	 *            Name of GameMode
	 * @return GameMode
	 */
	public GameMode getGameMode(String name) {
		for (GameMode mode : gameModes)
			if (mode.toString().equals(name))
				return mode;
		return null;
	}
	
	/**
	 * Sets the path used by the debug writer.
	 * @param dpath
	 */
	public static void setDebugPath(String dpath) {
		path = dpath;
	}

	/**
	 * Gets the current UTC time of the System.
	 * 
	 * @return date.getTime()
	 */
	public long getCurrentUTCTime() {
		Date date = new Date();
		return date.getTime();
	}
}
