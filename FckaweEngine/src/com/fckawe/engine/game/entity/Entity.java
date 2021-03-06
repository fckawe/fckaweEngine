package com.fckawe.engine.game.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fckawe.engine.game.Game;
import com.fckawe.engine.grafix.Bitmap;
import com.fckawe.engine.grafix.Bitmaps;
import com.fckawe.engine.input.InputHandler;
import com.fckawe.engine.physics.RectangularBounds;
import com.fckawe.engine.physics.Position;
import com.fckawe.engine.physics.Vector;
import com.fckawe.engine.ui.Screen;

public abstract class Entity {

	protected Game game;

	protected Bitmap currentBitmap;

	protected Position pos;

	protected List<RectangularBounds> boundaries;

	protected Vector velocity, acceleration;

	protected Vector velocityMax, accelerationMax;

	protected Vector accelerationSpeed, decelerationSpeed;

	protected Vector friction;

	protected Vector weight;

	protected boolean usePixelPerfectCollision;

	public Entity(final Game game) {
		this.game = game;
		boundaries = new ArrayList<RectangularBounds>();
	}

	public void init() {
		pos = new Position(0, 0);
		velocity = new Vector(0, 0);
		acceleration = new Vector(0, 0);
		accelerationSpeed = new Vector(50, 50);
		decelerationSpeed = new Vector(50, 50);
		accelerationMax = new Vector(200, 200);
		velocityMax = new Vector(200, 200);
		friction = new Vector(1, 1);
		weight = new Vector(0, 100);
		usePixelPerfectCollision = true;

		initMore();
		initBoundaries(boundaries);
	}

	protected abstract void initMore();

	protected abstract void initBoundaries(List<RectangularBounds> boundaries);

	protected String getBitmapsIdPrefix() {
		return getClass().getName();
	}

	public String getGlobalBitmapId(final String id) {
		return getBitmapsIdPrefix() + "." + id;
	}

	public abstract List<String> getRequiredBitmapIds();

	public abstract void loadRequiredBitmap(String id, String globalId);

	public void tick(final InputHandler inputHandler, final long elapsedTime) {
		long statisticalFps = 1000 / Math.max(elapsedTime, 1);
		velocity.applyDivisor(statisticalFps);
		acceleration.applyDivisor(statisticalFps);

		checkEntityCollision();
		checkBorderCollision();

		if (weight.getY() != 0) {
			acceleration.increaseY(weight.getY());
		}

		processMovementX();
		processMovementY();

		// update position
		pos.move(velocity);
	}

	protected void processMovementX() {
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
	}

	protected void processMovementY() {
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
	}

	public void render(final Screen screen) {
		if (currentBitmap != null) {
			screen.transparencyBlit(currentBitmap, pos);
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

	protected void checkEntityCollision() {
		Map<String, Entity> entities = game.getCurrentModule().getEntities();
		for (Entity entity : entities.values()) {
			if (entity != this) {
				checkCollisionWith(entity);
			}
		}
	}

	protected void checkCollisionWith(final Entity other) {
		int shiftX = other.getPosition().getX() - pos.getX();
		int shiftY = other.getPosition().getY() - pos.getY();

		for (RectangularBounds trb : boundaries) {
			for (RectangularBounds orb : other.getBoundaries()) {
				if (trb.intersects(orb, shiftX, shiftY)) {
					boolean pixelPerfect = usePixelPerfectCollision
							|| other.isUsePixelPerfectCollision();
					if (currentBitmap == null
							|| other.getCurrentBitmap() == null) {
						pixelPerfect = false;
					}

					if (!pixelPerfect || collidesPixelPerfectWith(other)) {
						collisionWith(other);
						break;
					}
				}
			}
		}
	}

	protected boolean collidesPixelPerfectWith(final Entity other) {
		int[] pxls = currentBitmap.getPixels();
		int[] opxls = other.getCurrentBitmap().getPixels();

		// find top left and bottom right coords of this
		int tlx = pos.getX(), tly = pos.getY();
		int w = currentBitmap.getWidth();
		int h = currentBitmap.getHeight();
		int brx = tlx + w - 1, bry = tly + h - 1;

		// find top left and bottom right coords of other
		int otlx = other.getPosition().getX(), otly = other.getPosition()
				.getY();
		int ow = other.getCurrentBitmap().getWidth();
		int oh = other.getCurrentBitmap().getHeight();
		int obrx = otlx + ow - 1, obry = otly + oh - 1;

		// find top left and bottom right coords of intersection
		int itlx = Math.max(tlx, otlx), itly = Math.max(tly, otly);
		int ibrx = Math.min(brx, obrx), ibry = Math.min(bry, obry);

		for (int iy = itly; iy <= ibry; iy++) {
			int py = iy - tly;
			int opy = iy - otly;

			for (int ix = itlx; ix <= ibrx; ix++) {
				int px = ix - tlx;
				int opx = ix - otlx;

				int pidx = py * w + px;
				int opidx = opy * ow + opx;

				if ((pxls[pidx] & 0xFF000000) != 0x00
						&& (opxls[opidx] & 0xFF000000) != 0x00) {
					return true;
				}
			}
		}

		return false;
	}

	protected void collisionWith(final Entity other) {
	}

	protected void checkBorderCollision() {
		int x = pos.getX();
		int mostLeft = getMostLeftPosition();
		if (x < mostLeft) {
			collisionWithLeftBorder(mostLeft);
		} else {
			int mostRight = getMostRightPosition();
			if (x > mostRight) {
				collisionWithRightBorder(mostRight);
			}
		}

		int y = pos.getY();
		int mostTop = getMostTopPosition();
		if (y < mostTop) {
			collisionWithTopBorder(mostTop);
		} else {
			int mostBottom = getMostBottomPosition();
			if (y > mostBottom) {
				collisionWithBottomBorder(mostBottom);
			}
		}
	}

	protected Position getPosition() {
		return pos;
	}

	protected List<RectangularBounds> getBoundaries() {
		return boundaries;
	}

	protected int getMostLeftPosition() {
		return 0;
	}

	protected int getMostRightPosition() {
		Screen screen = game.getUserInterface().getScreen();
		return screen.getWidth() - currentBitmap.getWidth();
	}

	protected int getMostTopPosition() {
		return 0;
	}

	protected int getMostBottomPosition() {
		Screen screen = game.getUserInterface().getScreen();
		return screen.getHeight() - currentBitmap.getHeight();
	}

	protected void collisionWithLeftBorder(final int mostLeft) {
		pos.setX(mostLeft);
		acceleration.setX(0);
		velocity.setX(0);
	}

	protected void collisionWithRightBorder(final int mostRight) {
		pos.setX(mostRight);
		acceleration.setX(0);
		velocity.setX(0);
	}

	protected void collisionWithTopBorder(final int mostTop) {
		pos.setY(mostTop);
		acceleration.setY(0);
		velocity.setY(0);
	}

	protected void collisionWithBottomBorder(final int mostBottom) {
		pos.setY(mostBottom);
		acceleration.setY(0);
		velocity.setY(0);
	}

	public Vector getVelocity() {
		return velocity;
	}

	public Bitmap getCurrentBitmap() {
		return currentBitmap;
	}

	public boolean isUsePixelPerfectCollision() {
		return usePixelPerfectCollision;
	}

}
