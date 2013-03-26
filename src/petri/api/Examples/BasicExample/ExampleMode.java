package petri.api.Examples.BasicExample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.IOException;

import petri.api.AnimatedImage;
import petri.api.Button;
import petri.api.GameEngine;
import petri.api.GameMode;

/**
 * @author Cody Swendrowski
 *
 */
public class ExampleMode extends GameMode {
	
	private AnimatedImage exampleImage;
	
	public ExampleMode(GameEngine e) {
		super(e);
		try {
			exampleImage = new AnimatedImage("Examples/BasicExample/Resources/", "exampleImage.png", 5, 2);
			Button clear = new Button("Clear Actors", (engine.getEnvironmentSize().x/2) - 30, engine.getEnvironmentSize().y-50);
			clear.setColorScheme(Color.black, Color.red, Color.black);
			buttons.add(clear);
		} catch (IOException e1) {
			GameEngine.log(e1.toString());
		}
	}

	@Override
	public void run(int ms) {
		if (buttons.get(0).isClicked())
			engine.actors.clear();
		engine.actors.handleActors(ms);
		super.run(ms);
	}

	@Override
	public void clickedAt(MouseEvent e) {
		ExampleActor ea = new ExampleActor(engine, exampleImage);
		ea.setCenter(e.getX()-(ea.getSize().x/2), e.getY()-(ea.getSize().y/2));
		engine.actors.add(ea);
		super.clickedAt(e);
	}

	@Override
	public void paint(Graphics g) {
		engine.actors.drawActors(g);
		super.paint(g);
	}
	
	@Override
	public String toString() {
		return "ExampleMode";
	}
}
