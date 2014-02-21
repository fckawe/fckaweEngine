package com.fckawe.engine.physics;

/**
 * A vector defines the value of a (x/y) movement, a force or something like
 * that.
 * 
 * @author fckawe
 */
public class Vector {

	// the horizontal movement value
	private double x;

	// the vertical movement value
	private double y;

	// the divisor by which the movement values get divided in the getters.
	private long divisor;

	/**
	 * Constructor to create a new vector (0, 0).
	 */
	public Vector() {
		this(0, 0);
	}

	/**
	 * Constructor to create a new vector (x, y).
	 * 
	 * @param x
	 *            The horizontal movement value.
	 * @param y
	 *            The vertical movement value.
	 */
	public Vector(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Applies a divisor by which the movement values get divided in the
	 * getters.
	 * 
	 * @param divisor
	 *            The divisor value.
	 */
	public void applyDivisor(final long divisor) {
		this.divisor = divisor;
	}

	/**
	 * Applies a maximum value, causing this vector to cut its movement values
	 * to their maximum.
	 * 
	 * @param m
	 *            A vector specifying the maximum.
	 */
	public void applyMax(final Vector m) {
		applyMaxX(m.getX());
		applyMaxY(m.getY());
	}

	/**
	 * Applies a maximum value to the horizontal movement value.
	 * 
	 * @param max
	 *            The maximum horizontal movement value to apply.
	 */
	public void applyMaxX(final double max) {
		x = limitDoubleToMax(x, max);
	}

	/**
	 * Moves the horizontal value by the given value towards zero. The
	 * horizontal value does not change its sign by ends in zero instead.
	 * 
	 * @param value
	 *            The value to change the horizontal value by.
	 */
	public void xTowardsZero(final double value) {
		if (value != 0) {
			if (x > 0) {
				x = (x < value) ? 0 : x - value;
			} else if (x < 0) {
				x = (x > value * -1) ? 0 : x + value;
			}
		}
	}

	/**
	 * Increases the horizontal value by the horizontal value of the given
	 * vector.
	 * 
	 * @param m
	 *            The vector to increase the horizontal value by.
	 */
	public void increaseX(final Vector m) {
		increaseX(m.getX());
	}

	/**
	 * Increases the horizontal value by the given double value.
	 * 
	 * @param value
	 *            The double value to increase the horizontal value by.
	 */
	public void increaseX(final double value) {
		this.x += value;
	}

	/**
	 * Decreases the horizontal value by the horizontal value of the given
	 * vector.
	 * 
	 * @param m
	 *            The vector to decrease the horizontal value by.
	 */
	public void decreaseX(final Vector m) {
		decreaseX(m.getY());
	}

	/**
	 * Decreases the horizontal value by the given double value.
	 * 
	 * @param value
	 *            The double value to decrease the horizontal value by.
	 */
	public void decreaseX(final double value) {
		this.x -= value;
	}

	/**
	 * Updates the horizontal value to the given double value.
	 * 
	 * @param x
	 *            The double value to set the horizontal value to.
	 */
	public void setX(final double x) {
		this.x = x;
	}

	/**
	 * Returns the current horizontal movement value (divided by the divisor if
	 * specified).
	 * 
	 * @return The current horizontal movement value.
	 */
	public double getX() {
		return divisor == 0 ? x : x / divisor;
	}

	/**
	 * Returns the current horizontal movement value (ignoring the divisor).
	 * 
	 * @return The current horizontal movement value.
	 */
	public double getDirectX() {
		return x;
	}

	/**
	 * Applies a maximum value to the vertical movement value.
	 * 
	 * @param max
	 *            The maximum vertical movement value to apply.
	 */
	public void applyMaxY(final double max) {
		y = limitDoubleToMax(y, max);
	}

	/**
	 * Moves the vertical value by the given value towards zero. The vertical
	 * value does not change its sign by ends in zero instead.
	 * 
	 * @param value
	 *            The value to change the vertical value by.
	 */
	public void yTowardsZero(final double value) {
		if (value != 0) {
			if (y > 0) {
				y = (y < value) ? 0 : y - value;
			} else if (y < 0) {
				y = (y > value * -1) ? 0 : y + value;
			}
		}
	}

	/**
	 * Increases the vertical value by the vertical value of the given vector.
	 * 
	 * @param m
	 *            The vector to increase the vertical value by.
	 */
	public void increaseY(final Vector m) {
		increaseY(m.getY());
	}

	/**
	 * Increases the vertical value by the given double value.
	 * 
	 * @param value
	 *            The double value to increase the vertical value by.
	 */
	public void increaseY(final double value) {
		this.y += value;
	}

	/**
	 * Decreases the vertical value by the vertical value of the given vector.
	 * 
	 * @param m
	 *            The vector to decrease the vertical value by.
	 */
	public void decreaseY(final Vector m) {
		decreaseY(m.getY());
	}

	/**
	 * Decreases the vertical value by the given double value.
	 * 
	 * @param value
	 *            The double value to decrease the vertical value by.
	 */
	public void decreaseY(final double value) {
		this.y -= value;
	}

	/**
	 * Updates the vertical value to the given double value.
	 * 
	 * @param x
	 *            The double value to set the vertical value to.
	 */
	public void setY(final double y) {
		this.y = y;
	}

	/**
	 * Returns the current vertical movement value (divided by the divisor if
	 * specified).
	 * 
	 * @return The current vertical movement value.
	 */
	public double getY() {
		return divisor == 0 ? y : y / divisor;
	}

	/**
	 * Returns the current vertical movement value (ignoring the divisor).
	 * 
	 * @return The current vertical movement value.
	 */
	public double getDirectY() {
		return y;
	}

	/**
	 * Limits the specified value to the also specified maximum value.
	 * 
	 * @param val
	 *            The value.
	 * @param max
	 *            The maximum value.
	 * @return The value limited to the maximum value (if necessary).
	 */
	protected double limitDoubleToMax(final double val, final double max) {
		if (Math.abs(val) > max) {
			return val < 0 ? max * -1 : max;
		}
		return val;
	}

	@Override
	public String toString() {
		return "Vector(" + x + "," + y + "/" + divisor + ")";
	}

}
