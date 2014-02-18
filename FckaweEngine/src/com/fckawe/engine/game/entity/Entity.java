package com.fckawe.engine.game.entity;

import java.util.List;

import com.fckawe.engine.core.Vector;
import com.fckawe.engine.core.Position;
import com.fckawe.engine.game.Game;
import com.fckawe.engine.grafix.Bitmap;
import com.fckawe.engine.grafix.Bitmaps;
import com.fckawe.engine.input.InputHandler;
import com.fckawe.engine.ui.Screen;

public abstract class Entity {

	protected Game game;

	protected Bitmap currentBitmap;
	
	protected Position pos;
	
	protected Vector velocity;
	
	protected Vector acceleration;
	
	protected Vector accelerationSpeed;
	
	protected Vector decelerationSpeed;

	protected Vector accelerationMax;
	
	protected Vector velocityMax;
	
	protected Vector friction;

	protected boolean canMoveX;
	
	protected boolean canMoveY;
	
	public Entity(final Game game) {
		this.game = game;
		initDefaults();
		init();
	}

	protected void initDefaults() {
		pos = new Position(0, 0);
		velocity = new Vector(0, 0);
		acceleration = new Vector(0, 0);
		accelerationSpeed = new Vector(50, 50);
		decelerationSpeed = new Vector(50, 50);
		accelerationMax = new Vector(200, 200);
		velocityMax = new Vector(200, 200);
		friction = new Vector(1, 1);
		canMoveX = true;
		canMoveY = true;
	}

	protected abstract void init();

	protected String getBitmapsIdPrefix() {
		return getClass().getName();
	}

	protected String getGlobalBitmapId(final String id) {
		return getBitmapsIdPrefix() + "." + id;
	}

	public abstract List<String> getRequiredBitmapIds();

	public abstract void loadRequiredBitmap(String id);

	public void tick(final InputHandler inputHandler, final long elapsedTime) {
		long divisor = Math.max(elapsedTime, 1);
		velocity.applyDivisor(divisor);
		acceleration.applyDivisor(divisor);
		
		if(canMoveX) {
			// limit acceleration
			acceleration.applyMaxX(accelerationMax.getX());
			if (acceleration.getX() == 0) {
				// slow down (friction)
				velocity.xTowardsZero(friction.getX());
			} else {
				// speed up
				velocity.increaseX(acceleration);
			}
			// limit velocity
			velocity.applyMaxX(velocityMax.getX());
		} else {
			acceleration.setX(0);
			velocity.setX(0);
		}
		
		if(canMoveY) {
			// limit acceleration
			acceleration.applyMaxY(accelerationMax.getY());
			if (acceleration.getY() == 0) {
				// slow down (friction)
				velocity.yTowardsZero(friction.getY());
			} else {
				// speed up
				velocity.increaseY(acceleration);
			}
			// limit velocity
			velocity.applyMaxY(velocityMax.getY());
		} else {
			acceleration.setY(0);
			velocity.setY(0);
		}
		
		// update position
		pos.move(velocity);
	}

	public void render(final Screen screen) {
		if (currentBitmap != null) {
			screen.blit(currentBitmap, pos);
		}
	}

	protected Bitmaps getBitmaps() {
		return game.getUserInterface().getBitmaps();
	}

	protected InputHandler getInputHandler() {
		return game.getUserInterface().getInputHandler();
	}

	protected void accelerateX(final int dir) {
		if (dir > 0) { // right
			acceleration.increaseX(accelerationSpeed);
		} else if (dir < 0) { // left
			acceleration.decreaseX(accelerationSpeed);
		}
	}

	protected void accelerateY(final int dir) {
		if (dir > 0) { // down
			acceleration.increaseY(accelerationSpeed);
		} else if (dir < 0) { // up
			acceleration.decreaseY(accelerationSpeed);
		}
	}

	protected void decelerateX() {
		acceleration.xTowardsZero(decelerationSpeed.getX());
	}

	protected void decelerateY() {
		acceleration.yTowardsZero(decelerationSpeed.getY());
	}

}
