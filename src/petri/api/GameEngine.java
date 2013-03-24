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

	protected int score;

	public static Point environmentSIze = new Point(0, 0);
	public static PrintWriter debugWriter;
	public static boolean debugMode;

	private int windowWidth, windowHeight, frames;
	boolean ENTER;
	ArrayList<Long> stepTimes;
	Actors actors;
	double FPS;

	static {
		File file = new File("petri/api/resources/debug.txt");
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
		
		windowWidth = 800;
		windowHeight = 600;
		
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
	 * Called when a click occurs, sends the click to the current gameMode.
	 * 
	 * @param x
	 * @param y
	 */
	public void clickedAt(MouseEvent e) {
		currentMode.clicked(e.getX(), e.getY());
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
		windowWidth = width;
		windowHeight = height;
	}

	/**
	 * Uses AudioSystem to get a clip of name s and play it.
	 * 
	 * @param s
	 *            Name of sound clip to play
	 * @param loop
	 *            If true, loops sound forever
	 */
	public void playSound(String s, boolean loop) {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(Actors.class
							.getResourceAsStream("Resources\\" + s));
			clip.open(inputStream);
			if (loop) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				clip.start();
			}
			log("Now playing " + s);
		} catch (Exception e) {
			log("Could not load sound clip " + s + " Error: " + e.toString());
		}
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
	 * Accepts keyEvents when a key is typed. Exits on ESC.
	 * 
	 * @param e
	 *            KeyEvent to handle
	 */
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ESCAPE)
			System.exit(0);
		else
			currentMode.keyTyped(e);
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
	
	public ArrayList<GameMode> getGameModes() {
		return gameModes;
	}

	public Point getEnvironmentSize() {
		return environmentSIze;
	}
}
