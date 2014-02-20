package com.fckawe.engine.game.demo;

import com.fckawe.engine.game.Game;
import com.fckawe.engine.game.Module;
import com.fckawe.engine.grafix.Fonts;
import com.fckawe.engine.input.InputHandler;
import com.fckawe.engine.physics.Position;
import com.fckawe.engine.ui.Screen;

public abstract class DemoModule extends Module {

	protected int showDurationInSecs = 15;

	private long startTime;

	private long currentTime;

	public DemoModule(final Game game) {
		super(game);
		startTime = 0;
	}

	protected int getSecondsLeft() {
		return showDurationInSecs - getSecondsPassed();
	}

	protected int getSecondsPassed() {
		return (int) ((currentTime - startTime) / 1000);
	}

	@Override
	public void render(final Screen screen) {
		super.render(screen);

		String str = getModuleName() + " (" + getSecondsLeft() + ")";

		Fonts fonts = game.getUserInterface().getFonts();
		fonts.draw(screen, str, new Position(5, screen.getHeight() - 20));
	}
	
	@Override
	public void tick(final InputHandler inputHandler, final long elapsedTime) {
		super.tick(inputHandler, elapsedTime);
		
		currentTime = System.currentTimeMillis();
		if (startTime == 0) {
			startTime = currentTime;
		} else if(getSecondsLeft() <= 0) {
			setEnd(null);
		}
	}
	
}
