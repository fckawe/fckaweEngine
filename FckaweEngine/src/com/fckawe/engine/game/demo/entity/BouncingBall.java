package com.fckawe.engine.game.demo.entity;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		accelerationSpeed = new Vector(700, 700);
		decelerationSpeed = new Vector(500, 500);
		accelerationMax = new Vector(800, 800);
		velocityMax = new Vector(800, 800);
		friction = new Vector(0, 0);
		weight = new Vector(0, 0);
		
		Screen screen = game.getUserInterface().getScreen();
		pos.setX(screen.getWidth() / 2);
		pos.setY(screen.getHeight() / 2);
		Random rnd;
		int tmp;
		int max;
		max = (int)velocityMax.getX();
		rnd = new Random();
		tmp = rnd.nextInt(max * 2 - 1);
		velocity.setX(tmp - max);
		max = (int)velocityMax.getY();
		rnd = new Random();
		tmp = rnd.nextInt(max * 2 - 1);
		velocity.setY(tmp - max);
	}

	@Override
	public List<String> getRequiredBitmapIds() {
		List<String> ids = new ArrayList<String>();
		ids.add(BMP_BOUNCING_BALL);
		return ids;
	}

	@Override
	public void loadRequiredBitmap(final String id, final String globalId) {
		Bitmaps bitmaps = getBitmaps();
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
	protected void collisionWithLeftBorder(final int mostLeft) {
		double newVelocityX = velocity.getDirectX() * -1;
		super.collisionWithLeftBorder(mostLeft);
		velocity.setX(newVelocityX);
	}

	@Override
	protected void collisionWithRightBorder(final int mostRight) {
		double newVelocityX = velocity.getDirectX() * -1;
		super.collisionWithRightBorder(mostRight);
		velocity.setX(newVelocityX);
	}

	@Override
	protected void collisionWithTopBorder(final int mostTop) {
		double newVelocityY = velocity.getDirectY() * -1;
		super.collisionWithTopBorder(mostTop);
		velocity.setY(newVelocityY);
	}

	@Override
	protected void collisionWithBottomBorder(final int mostBottom) {
		double newVelocityY = velocity.getDirectY() * -1;
		super.collisionWithBottomBorder(mostBottom);
		velocity.setY(newVelocityY);
	}

}
