package petri.api.Examples.Pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import petri.api.GameEngine;
import petri.api.GameMode;

/**
 * 
 * @author Cody Swendrowski
 */
public class MainMenu extends GameMode {

	private final Font terminal = new Font("Monospaced", Font.PLAIN, 50);
	/**
	 * @param eng
	 */
	public MainMenu(GameEngine eng) {
		super(eng);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "MainMenu";
	}

	/* (non-Javadoc)
	 * @see petri.api.GameMode#run(int)
	 */
	@Override
	public void run(int ms) {
		// TODO Auto-generated method stub
		super.run(ms);
	}

	/* (non-Javadoc)
	 * @see petri.api.GameMode#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, engine.getEnvironmentSize().x, engine.getEnvironmentSize().y);
		g.setColor(Color.green);
		g.setFont(terminal);
		g.drawString("Pong", (engine.getEnvironmentSize().x/2) - 30, 60);
		super.paint(g);
	}
}
