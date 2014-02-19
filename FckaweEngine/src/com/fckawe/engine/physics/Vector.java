package com.fckawe.engine.physics;

public class Vector {

	private double x;

	private double y;

	private long divisor;

	public Vector() {
		this(0, 0);
	}

	public Vector(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	public void applyDivisor(final long divisor) {
		this.divisor = divisor;
	}

	public void applyMax(final Vector m) {
		applyMaxX(m.getX());
		applyMaxY(m.getY());
	}

	public void applyMaxX(final double max) {
		x = limitDoubleToMax(x, max);
	}

	public void xTowardsZero(final double value) {
		if (value != 0) {
			if (x > 0) {
				x = (x < value) ? 0 : x - value;
			} else if (x < 0) {
				x = (x > value * -1) ? 0 : x + value;
			}
		}
	}

	public void increaseX(final Vector m) {
		increaseX(m.getX());
	}

	public void increaseX(final double value) {
		this.x += value;
	}

	public void decreaseX(final Vector m) {
		decreaseX(m.getY());
	}

	public void decreaseX(final double value) {
		this.x -= value;
	}

	public void setX(final double x) {
		this.x = x;
	}

	public double getX() {
		return divisor == 0 ? x : x / divisor;
	}

	public double getDirectX() {
		return x;
	}

	public void yTowardsZero(final double value) {
		if (value != 0) {
			if (y > 0) {
				y = (y < value) ? 0 : y - value;
			} else if (y < 0) {
				y = (y > value * -1) ? 0 : y + value;
			}
		}
	}

	public void applyMaxY(final double max) {
		y = limitDoubleToMax(y, max);
	}

	public void increaseY(final Vector m) {
		increaseY(m.getY());
	}

	public void increaseY(final double value) {
		this.y += value;
	}

	public void decreaseY(final Vector m) {
		decreaseY(m.getY());
	}

	public void decreaseY(final double value) {
		this.y -= value;
	}

	public void setY(final double y) {
		this.y = y;
	}

	public double getY() {
		return divisor == 0 ? y : y / divisor;
	}

	public double getDirectY() {
		return y;
	}

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
