package petri.api.example;

import java.awt.Graphics;
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
	public void clicked(int x, int y) {
		ExampleActor ea = new ExampleActor(engine, exampleImage);
		ea.setCenter(x, y);
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
