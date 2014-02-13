package com.fckawe.engine.game.entity;

import java.util.List;

import com.fckawe.engine.game.Game;
import com.fckawe.engine.grafix.Bitmap;
import com.fckawe.engine.grafix.Bitmaps;
import com.fckawe.engine.input.InputHandler;
import com.fckawe.engine.ui.Screen;

public abstract class Entity {

	protected Game game;

	protected Bitmap currentBitmap;

	protected int posX;

	protected int posY;

	protected double velX;

	protected double velY;

	protected double accelX;

	protected double accelY;

	public Entity(final Game game) {
		this.game = game;
	}

	protected String getBitmapsIdPrefix() {
		return getClass().getName();
	}

	protected String getGlobalBitmapId(final String id) {
		return getBitmapsIdPrefix() + "." + id;
	}

	public abstract List<String> getRequiredBitmapIds();

	public abstract void loadRequiredBitmap(String id);

	public void tick() {
		double fps = game.getUserInterface().getCurrentFps();
		velX += accelX / Math.max(fps, 1);
		velY += accelY / Math.max(fps, 1);
		double maxVel = getMaxVelocity();
		if (Math.abs(velX) > maxVel) {
			velX = velX < 0 ? maxVel * -1 : maxVel;
		}
		if (Math.abs(velY) > maxVel) {
			velY = velY < 0 ? maxVel * -1 : maxVel;
		}
		posX += velX / Math.max(fps, 1);
		posY += velY / Math.max(fps, 1);
	}

	public void render(final Screen screen) {
		screen.blit(currentBitmap, posX, posY);
	}

	protected Bitmaps getBitmaps() {
		return game.getUserInterface().getBitmaps();
	}

	protected InputHandler getInputHandler() {
		return game.getUserInterface().getInputHandler();
	}

	protected void accelerateRight() {
		if (velX < 0) {
			accelX += getDecelerationSpeed();
		} else {
			accelX += getAccelerationSpeed();
		}

		double maxAccel = getMaxAcceleration();
		if (Math.abs(accelX) > maxAccel) {
			accelX = accelX < 0 ? maxAccel * -1 : maxAccel;
		}
	}

	protected void accelerateLeft() {
		if (velX > 0) {
			accelX -= getDecelerationSpeed();
		} else {
			accelX -= getAccelerationSpeed();
		}
		double maxAccel = getMaxAcceleration();
		if (Math.abs(accelX) > maxAccel) {
			accelX = accelX < 0 ? maxAccel * -1 : maxAccel;
		}
	}

	protected void accelerateUp() {
		if (velY > 0) {
			accelY -= getDecelerationSpeed();
		} else {
			accelY -= getAccelerationSpeed();
		}
		double maxAccel = getMaxAcceleration();
		if (Math.abs(accelY) > maxAccel) {
			accelY = accelY < 0 ? maxAccel * -1 : maxAccel;
		}
	}

	protected void accelerateDown() {
		if (velY < 0) {
			accelY += getDecelerationSpeed();
		} else {
			accelY += getAccelerationSpeed();
		}

		double maxAccel = getMaxAcceleration();
		if (Math.abs(accelY) > maxAccel) {
			accelY = accelY < 0 ? maxAccel * -1 : maxAccel;
		}
	}

	protected abstract double getAccelerationSpeed();

	protected abstract double getDecelerationSpeed();

	protected abstract double getMaxAcceleration();

	protected abstract double getMaxVelocity();

}
