package com.fckawe.engine.game.demo.entity;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.fckawe.engine.game.Game;
import com.fckawe.engine.game.entity.Entity;
import com.fckawe.engine.grafix.Bitmaps;
import com.fckawe.engine.input.InputHandler;
import com.fckawe.engine.ui.Screen;

public class BouncingBall extends Entity {

	private static final String BMP_BOUNCING_BALL = "BouncingBall";

	public BouncingBall(final Game game) {
		super(game);
	}

	@Override
	public List<String> getRequiredBitmapIds() {
		List<String> ids = new ArrayList<String>();
		ids.add(BMP_BOUNCING_BALL);
		return ids;
	}

	@Override
	public void loadRequiredBitmap(final String id) {
		Bitmaps bitmaps = getBitmaps();
		String globalId = getGlobalBitmapId(id);
		if (id.equals(BMP_BOUNCING_BALL)) {
			bitmaps.loadBitmap(globalId, "/images/demo/bouncingball.png");
		}
	}

	@Override
	public void tick() {
		if (currentBitmap == null) {
			Bitmaps bitmaps = getBitmaps();
			String globalId = getGlobalBitmapId(BMP_BOUNCING_BALL);
			currentBitmap = bitmaps.getBitmap(globalId);
		}
		InputHandler inputHandler = getInputHandler();
		if (inputHandler != null && inputHandler.isPressed(KeyEvent.VK_RIGHT)) {
			accelerateRight();
		} else if (inputHandler != null
				&& inputHandler.isPressed(KeyEvent.VK_LEFT)) {
			accelerateLeft();
		} else {
			accelX = 0;
		}
		if (inputHandler != null && inputHandler.isPressed(KeyEvent.VK_UP)) {
			accelerateUp();
		} else if (inputHandler != null
				&& inputHandler.isPressed(KeyEvent.VK_DOWN)) {
			accelerateDown();
		} else {
			accelY = 0;
		}
		super.tick();
	}

	@Override
	public void render(final Screen screen) {
		super.render(screen);
	}

	@Override
	protected double getAccelerationSpeed() {
		return 40;
	}

	@Override
	protected double getDecelerationSpeed() {
		return 130;
	}

	@Override
	protected double getMaxAcceleration() {
		return 200;
	}

	@Override
	protected double getMaxVelocity() {
		return 200;
	}

}
