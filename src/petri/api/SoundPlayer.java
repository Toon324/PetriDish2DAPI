package petri.api;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * A controller that allows the user to easily load and manage sounds in queues.
 * Sounds can be played instantly, or added to a queue. The queue will play one
 * sound after another until it runs out of available sounds to play.
 * 
 * The queues are managed by an ArrayList, and are thus only limited in number
 * to the max size of ArrayList.
 * 
 * @author Cody Swendrowski
 */
public class SoundPlayer {

	protected ArrayList<ArrayList<String>> queue;
	protected ArrayList<Clip> queueClips; // If index X is true, queue X can
											// play
											// a new sound
	protected boolean canPlay;

	/**
	 * Creates a new SoundPlayer.
	 */
	public SoundPlayer() {
		queue = new ArrayList<ArrayList<String>>();
		queueClips = new ArrayList<Clip>();
		canPlay = true;
	}

	/**
	 * Adds a sound to Queue 0.
	 * 
	 * @param s
	 *            Name of sound to play
	 */
	public void addSoundToQueue(String s) {
		queue.get(0).add(s);
	}

	/**
	 * Adds a sound to the given Queue.
	 * 
	 * @param indexOfQueue
	 *            Queue to add sound to
	 * @param s
	 *            Name of sound to play
	 */
	public void addSoundToQueue(int indexOfQueue, String s) {
		while (queue.size() <= indexOfQueue) {
			queue.add(new ArrayList<String>());
			queueClips.ensureCapacity(indexOfQueue + 1);
		}

		queue.get(indexOfQueue).add(s);
	}

	/**
	 * Starts the player. Will play sound in queues as player is able to.
	 * 
	 * @throws LineUnavailableException
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 */
	public void startPlayer() throws LineUnavailableException,
			UnsupportedAudioFileException, IOException {

		canPlay = true;

		while (canPlay) {

			for (int x = 0; x < queue.size(); x++) {

				if (queueClips.get(x) == null || !queueClips.get(x).isRunning()) {

					String s = queue.get(x).get(0);
					queueClips.set(x, AudioSystem.getClip());

					AudioInputStream input = AudioSystem
							.getAudioInputStream(SoundPlayer.class
									.getResourceAsStream(s));

					queueClips.get(x).open(input);
					queueClips.get(x).start();

					queue.remove(x);
					GameEngine.log("Now playing " + s);
				}
			}
		}
	}

	/**
	 * Stops the player.
	 */
	public void stopPlayer() {
		canPlay = false;
	}

	/**
	 * Uses AudioSystem to get a clip of name s and play it immediately.
	 * 
	 * @param s
	 *            Name of sound clip to play
	 * @param loop
	 *            If true, loops sound forever
	 * @throws LineUnavailableException
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 */
	public void playSound(String s, boolean loop)
			throws LineUnavailableException, UnsupportedAudioFileException,
			IOException {

		Clip clip = AudioSystem.getClip();
		AudioInputStream inputStream = AudioSystem
				.getAudioInputStream(SoundPlayer.class.getResourceAsStream(s));
		clip.open(inputStream);
		if (loop) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			clip.start();
		}
		GameEngine.log("Now playing " + s);
	}

}