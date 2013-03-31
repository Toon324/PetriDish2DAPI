package petri.api.Examples.PongExample;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import petri.api.Button;
import petri.api.GameEngine;
import petri.api.GameMode;

/**
 * @author Cody Swendrowski
 * 
 */
public class Host extends GameMode {

	private final Font terminal = new Font("Monospaced", Font.PLAIN, 25);
	
	/**
	 * @param eng
	 */
	public Host(GameEngine eng) {
		super(eng);
		Button stop = new Button("Stop Hosting", 350, 400);
		stop.setColorScheme(Color.green, Color.black, Color.green);
		
		buttons.add(stop);
	}
	
	@Override
	public void run(int ms) {
		if (buttons.get(0).isClicked())
			engine.setCurrentGameMode("InternetLobby");
		else if (engine.networkAdapter.isConnected())
			engine.setCurrentGameMode("InternetGame");
		
		super.run(ms);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, engine.getEnvironmentSize().x,
				engine.getEnvironmentSize().y);
		try {
			g.setColor(Color.green);
			g.setFont(terminal);
			engine.centerTextHorizontally(g, "Currently hosting from "
					+ InetAddress.getLocalHost() + ":" + engine.networkAdapter.getPort(), 0,
					engine.getEnvironmentSize().x, 50);
		} catch (UnknownHostException e) {
			GameEngine.log(e.getMessage());
		}
		
		super.paint(g);
	}

	public void startHosting() {
		try {
			engine.networkAdapter.host(42);
		} catch (IOException e) {
			GameEngine.log(e.getMessage());
		}
		((InternetGame)engine.getGameMode("InternetGame")).setOutput(engine.networkAdapter.getOutputStream());
	}
}
