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

	protected int pX;

	protected int pY;

	protected double vX;

	protected double vY;

	protected double aX;

	protected double aY;

	private double accelerationSpeed;

	private double decelerationSpeed;

	private double maxAcceleration;

	private double maxVelocity;

	private double frictionX;

	private double frictionY;

	public Entity(final Game game) {
		this.game = game;
		initDefaults();
		init();
	}

	protected void initDefaults() {
		accelerationSpeed = 50;
		decelerationSpeed = 50;
		maxAcceleration = 200;
		maxVelocity = 200;
		frictionX = 1;
		frictionY = 1;
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

		// limit acceleration
		aX = limitDoubleToMax(aX, maxAcceleration);
		aY = limitDoubleToMax(aY, maxAcceleration);

		if (aX == 0) {
			if (frictionX != 0) {
				// slow down (friction)
				if (vX > 0) {
					vX -= (vX < frictionX) ? 0 : frictionX;
				} else if (vX < 0) {
					vX += (Math.abs(vX) < frictionX) ? 0 : frictionX;
				}
			}
		} else {
			// speed up
			vX += aX / divisor;
		}
		if (aY == 0) {
			if (frictionY != 0) {
				// slow down (friction)
				if (vY > 0) {
					vY -= vY < frictionY ? 0 : frictionY;
				} else if (vY < 0) {
					vY += Math.abs(vY) < frictionY ? 0 : frictionY;
				}
			}
		} else {
			// speed up
			vY += aY / divisor;
		}

		// limit velocity
		vX = limitDoubleToMax(vX, maxVelocity);
		vY = limitDoubleToMax(vY, maxVelocity);

		// update position
		pX += vX / divisor;
		pY += vY / divisor;
	}

	protected double limitDoubleToMax(final double val, final double max) {
		if (Math.abs(val) > max) {
			return val < 0 ? max * -1 : max;
		}
		return val;
	}

	public void render(final Screen screen) {
		if (currentBitmap != null) {
			screen.blit(currentBitmap, pX, pY);
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
			aX += accelerationSpeed;
		} else if (dir < 0) { // left
			aX -= accelerationSpeed;
		}
	}

	protected void accelerateY(final int dir) {
		if (dir > 0) { // down
			aY += accelerationSpeed;
		} else if (dir < 0) { // up
			aY -= accelerationSpeed;
		}
	}

	protected void decelerateX() {
		if (aX > decelerationSpeed) {
			aX -= decelerationSpeed;
		} else if (aX > 0) {
			aX = 0;
		} else if (Math.abs(aX) < decelerationSpeed) {
			aX += decelerationSpeed;
		} else if (aX < 0) {
			aX = 0;
		}
	}

	protected void decelerateY() {
		if (aY > decelerationSpeed) {
			aY -= decelerationSpeed;
		} else if (aY > 0) {
			aY = 0;
		} else if (Math.abs(aY) < decelerationSpeed) {
			aY += decelerationSpeed;
		} else if (aY < 0) {
			aY = 0;
		}
	}

	protected void setAccelerationSpeed(final double accelerationSpeed) {
		this.accelerationSpeed = accelerationSpeed;
	}

	protected void setDecelerationSpeed(final double decelerationSpeed) {
		this.decelerationSpeed = decelerationSpeed;
	}

	protected void setMaxAcceleration(final double maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}

	protected void setMaxVelocity(final double maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

	protected void setFrictionX(final double frictionX) {
		this.frictionX = frictionX;
	}

	protected void setFrictionY(final double frictionY) {
		this.frictionY = frictionY;
	}

}
