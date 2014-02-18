package com.fckawe.engine.game.demo.entity;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.fckawe.engine.core.Vector;
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
	protected void init() {
		accelerationSpeed = new Vector(50, 50);
		decelerationSpeed = new Vector(50, 50);
		accelerationMax = new Vector(200, 200);
		velocityMax = new Vector(200, 200);
		friction = new Vector(10, 10);
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
	public void tick(final InputHandler inputHandler, final long elapsedTime) {
		if (currentBitmap == null) {
			Bitmaps bitmaps = getBitmaps();
			String globalId = getGlobalBitmapId(BMP_BOUNCING_BALL);
			currentBitmap = bitmaps.getBitmap(globalId);
		}

		if (inputHandler != null && inputHandler.isPressed(KeyEvent.VK_RIGHT)) {
			inputHandler.consume(KeyEvent.VK_RIGHT);
			accelerateX(1);
		} else if (inputHandler != null
				&& inputHandler.isPressed(KeyEvent.VK_LEFT)) {
			inputHandler.consume(KeyEvent.VK_LEFT);
			accelerateX(-1);
		} else {
			decelerateX();
		}

		if (inputHandler != null && inputHandler.isPressed(KeyEvent.VK_UP)) {
			inputHandler.consume(KeyEvent.VK_UP);
			accelerateY(-1);
		} else if (inputHandler != null
				&& inputHandler.isPressed(KeyEvent.VK_DOWN)) {
			inputHandler.consume(KeyEvent.VK_DOWN);
			accelerateY(1);
		} else {
			decelerateY();
		}

		super.tick(inputHandler, elapsedTime);
	}

	@Override
	public void render(final Screen screen) {
		super.render(screen);
	}

	@Override
	protected void collisionWithBottomBorder(final int mostBottom) {
		double newAccelerationY = acceleration.getDirectY() * -0.9;

		super.collisionWithBottomBorder(mostBottom);

		acceleration.setY(newAccelerationY);
	}

}
