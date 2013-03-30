package petri.api.Examples.PongExample;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;

import petri.api.GameEngine;
import petri.api.GameMode;
import petri.api.Button;

/**
 * @author Cody
 * 
 */
public final class Connect extends GameMode {

	private StringBuffer connectIP;
	private final Font terminal = new Font("Monospaced", Font.PLAIN, 25);

	/**
	 * @param eng
	 */
	public Connect(GameEngine eng) {
		super(eng);
		connectIP = new StringBuffer();
		Button cancel = new Button("Cancel", 250, 400);
		cancel.setColorScheme(Color.green, Color.black, Color.green);
		Button connect = new Button("Connect", 450, 400);
		cancel.copyColorSchemeTo(connect);
		
		buttons.add(cancel);
		buttons.add(connect);
	}

	@Override
	public void run(int ms) {
		if (buttons.get(0).isClicked())
			engine.setCurrentGameMode("InternetLobby");
		else if (buttons.get(1).isClicked()) {
			try {
				engine.networkAdapter.connect(connectIP.toString(), 42);
			} catch (IOException e) {
				GameEngine.log(e.getMessage());
			}
			engine.setCurrentGameMode("InternetGame");
		}
			
		super.run(ms);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, engine.getEnvironmentSize().x, engine.getEnvironmentSize().y);
		g.setColor(Color.green);
		g.setFont(terminal);
		g.drawString("Enter IP to connect to:", 40, 170);
		g.drawString(connectIP.toString() + "|", 50, 200);
		super.paint(g);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see petri.api.GameMode#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		if (((int)e.getKeyChar()) == KeyEvent.VK_BACK_SPACE && connectIP.length() != 0)
			connectIP.deleteCharAt(connectIP.length()-1);
		else if (((int)e.getKeyChar()) == KeyEvent.VK_ENTER)
			try {
				engine.networkAdapter.connect(connectIP.toString(), 42);
				engine.setCurrentGameMode("InternetGame");
			} catch (IOException e1) {
				GameEngine.log(e1.getMessage());
			}
		else
			connectIP.append(e.getKeyChar());
		super.keyTyped(e);
	}

}
