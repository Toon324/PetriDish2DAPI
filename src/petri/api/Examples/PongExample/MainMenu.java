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
public class MainMenu extends GameMode {

	private final Font terminal = new Font("Monospaced", Font.PLAIN, 50);
	/**
	 * @param eng
	 */
	public MainMenu(GameEngine eng) {
		super(eng);
		
		Button local = new Button("Local Game", 0, -50); //Initialize Buttons off screen so we can place them properly later.
		local.setColorScheme(Color.green, Color.black, Color.green);
		
		Button internet = new Button("Internet Game", 0, -50);
		Button close = new Button("Close", 0, -50);
		
		//Make all the Buttons have the same scheme
		local.copyColorSchemeTo(internet);
		local.copyColorSchemeTo(close);
		
		buttons.add(local);
		buttons.add(internet);
		buttons.add(close);

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
		//Place buttons depending on size of window
		buttons.get(0).set((engine.getEnvironmentSize().x/2)-(buttons.get(0).getWidth()/2), (engine.getEnvironmentSize().y/4) *1);
		buttons.get(1).set((engine.getEnvironmentSize().x/2)-(buttons.get(1).getWidth()/2), (engine.getEnvironmentSize().y/4) *2);
		buttons.get(2).set((engine.getEnvironmentSize().x/2)-(buttons.get(2).getWidth()/2), (engine.getEnvironmentSize().y/4) *3);
		
		//Check buttons
		if (buttons.get(0).isClicked())
			engine.setCurrentGameMode("LocalGame"); //Set to game
		else if (buttons.get(1).isClicked())
			engine.setCurrentGameMode("InternetGame");
		else if (buttons.get(2).isClicked())
			Pong.stopGame(); //Exit
		
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
		engine.centerTextHorizontally(g, "Pong", 0, engine.getEnvironmentSize().x, 60);
		super.paint(g);
	}
}
