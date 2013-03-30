package petri.api.Examples.PongExample;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import petri.api.Button;
import petri.api.GameEngine;
import petri.api.GameMode;

/**
 * 
 * @author Cody Swendrowski
 */
public class InternetLobby extends GameMode {
	private final Font terminal = new Font("Monospaced", Font.PLAIN, 50);
	
	/**
	 * @param eng
	 */
	public InternetLobby(GameEngine eng) {
		super(eng);
		Button host = new Button("Host", 400, 150);
		Button connect = new Button("Connect", 400, 300);
		Button home = new Button("Home", 400, 450);
		
		host.setColorScheme(Color.green, Color.black, Color.green);
		host.copyColorSchemeTo(connect);
		host.copyColorSchemeTo(home);
		
		buttons.add(host);
		buttons.add(connect);
		buttons.add(home);
	}
	
	@Override
	public void run(int ms) {
		if (buttons.get(0).isClicked()) {
			engine.setCurrentGameMode("Host");
			((Host)engine.getCurrentGameMode()).startHosting();
		}
		else if (buttons.get(1).isClicked())
			engine.setCurrentGameMode("Connect");
		else if (buttons.get(2).isClicked())
			engine.setCurrentGameMode("MainMenu");
		
		super.run(ms);
	}

	@Override
	public void paint(Graphics g) {
		buttons.get(0).set((engine.getEnvironmentSize().x/2)-(buttons.get(0).getWidth()/2), buttons.get(0).getY());
		buttons.get(1).set((engine.getEnvironmentSize().x/2)-(buttons.get(1).getWidth()/2), buttons.get(1).getY());
		buttons.get(2).set((engine.getEnvironmentSize().x/2)-(buttons.get(2).getWidth()/2), buttons.get(2).getY());
		
		g.setColor(Color.black);
		g.fillRect(0, 0, engine.getEnvironmentSize().x, engine.getEnvironmentSize().y);
		
		g.setColor(Color.green);
		g.setFont(terminal);
		engine.centerTextHorizontally(g, "Internet Lobby", 0, engine.getEnvironmentSize().x, 50);
		super.paint(g);
	}
}
