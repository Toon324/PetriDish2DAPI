package petri.api.Examples.PongExample;

import java.awt.Graphics;
import java.io.DataOutputStream;
import java.io.IOException;

import petri.api.GameEngine;
import petri.api.GameMode;

/**
 * @author Cody Swendrowski
 *
 */
public class Host extends GameMode {
	
	/**
	 * @param eng
	 */
	public Host(GameEngine eng) {
		super(eng);
		try {
			eng.networkAdapter.host(42);
		} catch (IOException e) {
			GameEngine.log(e.getMessage());
		}
		InternetGame.setOutput(eng.networkAdapter.getOutputStream());
	}

	
	@Override
	public void paint(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, engine.getEnvironmentSize().x, engine.getEnvironmentSize().y);
		g.drawString("Currently hosting from ", 0, 0);
	}
}
