package petri.api.Examples.BasicExample;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.IOException;

import petri.api.AnimatedImage;
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
			exampleImage = new AnimatedImage("example/resources/", "exampleImage.png", 5, 2);
		} catch (IOException e1) {
			GameEngine.log(e1.toString());
		}
	}

	@Override
	public void run(int ms) {
		engine.actors.handleActors(ms);
	}

	@Override
	public void clickedAt(MouseEvent e) {
		ExampleActor ea = new ExampleActor(engine, exampleImage);
		ea.setCenter(e.getX()-(ea.getSize().x/2), e.getY()-(ea.getSize().y/2));
		engine.actors.add(ea);
	}

	@Override
	public void paint(Graphics g) {
		engine.actors.drawActors(g);
	}
	
	@Override
	public String toString() {
		return "ExampleMode";
	}
}
