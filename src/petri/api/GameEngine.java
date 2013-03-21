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
 * @author Cody Swendrowski, Dan Miller
 */
public class GameEngine {

	// Resources to use
	final Font large = new Font("Serif", Font.BOLD, 30);
	final Font small = new Font("Serif", Font.PLAIN, 12);
	final Color transGray = new Color(Color.gray.getRed(),
			Color.gray.getGreen(), Color.gray.getBlue(), 200);

	private GameMode mode;
	private long millis; // Used to calculate time between each frame

	// GameModes
	MainMenu mainMenu;
	MainGame mainGame;
	Instructions instructions;
	EndGame endGame;
	Sandbox sandbox;
	ParticleEngine particleEngine;

	protected int score;

	public static Point envSize = new Point(0, 0);
	public static PrintWriter debugWriter;
	public static boolean debugMode;

	int windowWidth, windowHeight, frames;
	boolean ENTER;
	ArrayList<Long> stepTimes;
	Actors actors;
	double FPS;

	static {
		File file = new File("src\\trivia\\Resources\\log.txt");
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
		mainMenu = new MainMenu(this);
		endGame = new EndGame(this);
		sandbox = new Sandbox(this);
		particleEngine = new ParticleEngine(this);
		instructions = new Instructions(this);
		mode = mainMenu;
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

		mode.run((int) (millis - lastMillis));
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
		mode.paint(g);
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
		mode = newMode;
		if (mode instanceof MainGame) {
			actors.clear();
			score = 0;
		}
	}

	/**
	 * Called when a click occurs, sends the click to the current gameMode.
	 * 
	 * @param x
	 * @param y
	 */
	public void clickedAt(MouseEvent e) {
		mode.clicked(e.getX(), e.getY());
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
		else if (e.getKeyChar() == KeyEvent.VK_ENTER)
			ENTER = true;
	}

	/**
	 * Checks the current mode to see if mouse is hovering over a Button.
	 * 
	 * @param e
	 *            MouseEvent to check
	 * @return True if mouse is over a Button
	 */
	public boolean isOver(MouseEvent e) {
		return mode.isOver(e.getX(), e.getY());
	}
}
