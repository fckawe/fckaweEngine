package com.fckawe.engine.physics;

/**
 * A position defines a point in an x/y coordinate system. It also offers
 * methods to change the position in several ways.
 * 
 * @author fckawe
 */
public class Position {

	private int x;

	private int y;

	/**
	 * Constructor to create a position on (0, 0).
	 */
	public Position() {
		this(0, 0);
	}

	/**
	 * Constructor to create a position on (x, y).
	 * 
	 * @param x
	 *            The x-coordinate, specifying the horizontal position.
	 * @param y
	 *            The y-coordinate, specifying the vertical position.
	 */
	public Position(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Moves the position by the given vector.
	 * 
	 * @param m
	 *            The vector that is used to update the position.
	 */
	public void move(final Vector m) {
		moveX(m.getX());
		moveY(m.getY());
	}

	/**
	 * Moves the horizontal position by x.
	 * 
	 * @param x
	 *            The amount to move the horizontal position.
	 */
	public void moveX(final double x) {
		moveX((int) x);
	}

	/**
	 * Moves the horizontal position by x.
	 * 
	 * @param x
	 *            The amount to move the horizontal position.
	 */
	public void moveX(final int x) {
		this.x += x;
	}

	/**
	 * Updates the horizontal position to the given value.
	 * 
	 * @param x
	 *            The new horizontal position.
	 */
	public void setX(final int x) {
		this.x = x;
	}

	/**
	 * Returns the current horizontal position.
	 * 
	 * @return The current horizontal position.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Moves the vertical position by y.
	 * 
	 * @param y
	 *            The amount to move the vertical position.
	 */
	public void moveY(final double y) {
		moveY((int) y);
	}

	/**
	 * Moves the vertical position by y.
	 * 
	 * @param y
	 *            The amount to move the vertical position.
	 */
	public void moveY(final int y) {
		this.y += y;
	}

	/**
	 * Updates the vertical position to the given value.
	 * 
	 * @param y
	 *            The new vertical position.
	 */
	public void setY(final int y) {
		this.y = y;
	}

	/**
	 * Returns the current vertical position.
	 * 
	 * @return The current vertical position.
	 */
	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "Position(" + x + "," + y + ")";
	}

}
