package petri.api;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Handles all the game logic and painting based on the current game mode.
 * 
 * @author Cody Swendrowski
 */
public class GameEngine {

	// Resources to use
	public final Font large = new Font("Serif", Font.BOLD, 30);
	public final Font small = new Font("Serif", Font.PLAIN, 12);

	private GameMode currentMode;
	private long millis; // Used to calculate time between each frame

	// GameModes
	protected ArrayList<GameMode> gameModes = new ArrayList<GameMode>();
	
	public ParticleEngine particleEngine;
	public SoundPlayer soundPlayer;

	protected int score;

	public static Point environmentSize = new Point(0, 0);
	public static PrintWriter debugWriter;
	public static boolean debugMode;

	protected ArrayList<Long> stepTimes;
	public Actors actors;
	protected double FPS;

	static {
		File file = new File("bin/petri/api/resources/debug.txt");
		try {
			if (!file.exists())
				file.createNewFile();
			debugWriter = new PrintWriter(
					new FileWriter(file.getAbsoluteFile()));
		} catch (IOException e) {
			System.out.println("Error creating debug output stream\n"
					+ System.getProperty("user.dir"));
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new GameEngine.
	 * 
	 * @param actors
	 *            Array of actors to pass logic to.
	 * @param debug
	 *            If true, prints out debug messages.
	 */
	public GameEngine(boolean debug) {
		actors = new Actors(this);
		debugMode = debug;
		
		particleEngine = new ParticleEngine(this);
		
		currentMode = new GameMode(this);
		
		environmentSize.x = 800;
		environmentSize.y = 600;
		
		millis = System.currentTimeMillis();
		
		stepTimes = new ArrayList<Long>();
		stepTimes.add(millis);
		
		score = 0;
	}

	/**
	 * Runs the game logic based on the current game mode.
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
	 * Paints the game based on the current game mode.
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
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int newScore) {
		score = newScore;
	}
	
	public void addGameMode(GameMode newMode) {
		gameModes.add(newMode);
	}
	
	public void setCurrentGameMode(String nameOfMode) {
		for (GameMode mode : gameModes)
			if (mode.toString().equals(nameOfMode))
				currentMode = mode;
	}
	
	public void setCurrentGameMode(int indexOfMode) {
		currentMode = gameModes.get(indexOfMode);
	}
	
	public GameMode getCurrentGameMode() {
		return currentMode;
	}
	
	public ArrayList<GameMode> getGameModes() {
		return gameModes;
	}

	public Point getEnvironmentSize() {
		return environmentSize;
	}
}
