package com.fckawe.engine.game.demo;

import java.awt.event.KeyEvent;

import com.fckawe.engine.game.Game;
import com.fckawe.engine.game.Module;
import com.fckawe.engine.game.demo.entity.BouncingBall;
import com.fckawe.engine.input.InputHandler;

public class BouncingBallDemo extends Module {

	private long startTime;

	public BouncingBallDemo(final Game game) {
		super(game);
		startTime = 0;
	}

	@Override
	protected String getModuleName() {
		return "Bouncing Demo";
	}

	@Override
	protected void loadEntities() {
		entities.put("bouncingball", new BouncingBall(game));
	}

	@Override
	public void tick(final InputHandler inputHandler, final long elapsedTime) {
		super.tick(inputHandler, elapsedTime);

		if (inputHandler.isPressed(KeyEvent.VK_ESCAPE)) {
			inputHandler.consume(KeyEvent.VK_ESCAPE);
			end(null);
		} else {
			// long currentTime = System.currentTimeMillis();
			// if (startTime == 0) {
			// startTime = currentTime;
			// } else if (currentTime - startTime >= 15000) {
			// end(null);
			// }
		}
	}

}
