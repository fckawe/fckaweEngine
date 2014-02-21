package com.fckawe.engine.physics;

/**
 * Rectangular bounds are used to specify the (invisible) boundaries of an
 * object. They are used to detect collisions between entities for example.
 * 
 * @author fckawe
 */
public class RectangularBounds {

	// the coordinates of the top left point
	protected int topLeftX, topLeftY;

	// the coordinates of the bottom right point
	protected int bottomRightX, bottomRightY;

	/**
	 * Constructor to create new rectangular bounds, specifying the top left and
	 * the bottom right point of the area.
	 * 
	 * @param topLeftX
	 *            The x-coordinate of the top left point.
	 * @param topLeftY
	 *            The y-coordinate of the top left point.
	 * @param bottomRightX
	 *            The x-coordinate of the bottom right point.
	 * @param bottomRightY
	 *            The y-coordinate of the bottom right point.
	 */
	public RectangularBounds(final int topLeftX, final int topLeftY,
			final int bottomRightX, final int bottomRightY) {
		this.topLeftX = topLeftX;
		this.topLeftY = topLeftY;
		this.bottomRightX = bottomRightX;
		this.bottomRightY = bottomRightY;
	}

	/**
	 * Returns the x-coordinate of the top left point.
	 * 
	 * @return The x-coordinate of the top left point.
	 */
	public int getTopLeftX() {
		return topLeftX;
	}

	/**
	 * Returns the y-coordinate of the top left point.
	 * 
	 * @return The y-coordinate of the top left point.
	 */
	public int getTopLeftY() {
		return topLeftY;
	}

	/**
	 * Returns the x-coordinate of the bottom right point.
	 * 
	 * @return The x-coordinate of the bottom right point.
	 */
	public int getBottomRightX() {
		return bottomRightX;
	}

	/**
	 * Returns the y-coordinate of the bottom right point.
	 * 
	 * @return The y-coordinate of the bottom right point.
	 */
	public int getBottomRightY() {
		return bottomRightY;
	}

	/**
	 * Checks if this rectangular bounds intersects with a given other
	 * rectangular bounds.
	 * 
	 * @param other
	 *            The other rectangular bounds.
	 * @param shiftX
	 *            The horizontal shift between the two objects.
	 * @param shiftY
	 *            The vertical shift between the two objects.
	 * @return
	 */
	public boolean intersects(final RectangularBounds other, final int shiftX,
			final int shiftY) {
		int otherTopLeftX = other.getTopLeftX();
		if (otherTopLeftX + shiftX > bottomRightX) {
			return false;
		}

		int otherTopLeftY = other.getTopLeftY();
		if (otherTopLeftY + shiftY > bottomRightY) {
			return false;
		}

		int otherBottomRightX = other.getBottomRightX();
		if (otherBottomRightX + shiftX < topLeftX) {
			return false;
		}

		int otherBottomRightY = other.getBottomRightY();
		if (otherBottomRightY + shiftY < topLeftY) {
			return false;
		}

		return true;
	}

}
