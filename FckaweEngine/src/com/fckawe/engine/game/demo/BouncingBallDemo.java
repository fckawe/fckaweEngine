package com.fckawe.engine.game.demo;

import java.awt.event.KeyEvent;

import com.fckawe.engine.game.Game;
import com.fckawe.engine.game.demo.entity.BouncingBall;
import com.fckawe.engine.input.InputHandler;

public class BouncingBallDemo extends DemoModule {

	public BouncingBallDemo(final Game game) {
		super(game);
	}

	@Override
	protected String getModuleName() {
		return "BouncingBall Demo";
	}

	@Override
	protected void loadEntities() {
		for (int i = 0; i < 3; i++) {
			entities.put("bouncingball" + (i + 1), new BouncingBall(game));
		}
	}

	@Override
	public void tick(final InputHandler inputHandler, final long elapsedTime) {
		super.tick(inputHandler, elapsedTime);

		if (inputHandler.isPressed(KeyEvent.VK_ESCAPE)) {
			inputHandler.consume(KeyEvent.VK_ESCAPE);
			setEnd(null);
		}
	}
	
}
