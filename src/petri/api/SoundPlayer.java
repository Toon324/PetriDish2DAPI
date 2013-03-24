/**
 * 
 */
package petri.api;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author Cody Swendrowski
 * 
 */
public class SoundPlayer {

	private ArrayList<ArrayList<String>> queue;
	private ArrayList<Clip> queueClips; //If index X is true, queue X can play a new sound
	private boolean canPlay;

	public SoundPlayer() {
		queue = new ArrayList<ArrayList<String>>();
		queueClips = new ArrayList<Clip>();
		canPlay = true;
	}

	public void addSoundToQueue(String s) {
		queue.get(0).add(s);
	}

	public void addSoundToQueue(int indexOfQueue, String s) {
		while (queue.size() <= indexOfQueue) {
			queue.add(new ArrayList<String>());
			queueClips.ensureCapacity(indexOfQueue+1);
		}

		queue.get(indexOfQueue).add(s);
	}

	public void startPlayer() throws LineUnavailableException,
			UnsupportedAudioFileException, IOException {
		
		canPlay = true;
		
		while (canPlay) {
			
			for (int x=0; x<queue.size(); x++) {
				
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