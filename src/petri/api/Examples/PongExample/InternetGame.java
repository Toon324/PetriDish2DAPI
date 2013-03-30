package petri.api.Examples.PongExample;

import java.awt.event.KeyEvent;
import java.io.DataOutputStream;

import petri.api.GameEngine;
import petri.api.GameMode;

/**
 * 
 * @author Cody Swendrowski
 */
public class InternetGame extends GameMode {

	/**
	 * @param eng
	 */
	public InternetGame(GameEngine eng) {
		super(eng);
	}
	
	@Override
	public void run(int ms) {
		// TODO Auto-generated method stub
		super.run(ms);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		super.keyPressed(e);
	}

	/**
	 * @param outputStream
	 */
	public static void setOutput(DataOutputStream outputStream) {
		// TODO Auto-generated method stub
		
	}
}
