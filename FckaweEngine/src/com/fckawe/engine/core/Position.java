package com.fckawe.engine.core;

public class Position {
	
	private int x;
	
	private int y;
	
	public Position() {
		this(0, 0);
	}
	
	public Position(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
	
	public void move(final Vector m) {
		moveX(m.getX());
		moveY(m.getY());
	}
	
	public void moveX(final double x) {
		moveX((int)x);
	}
	
	public void moveX(final int x) {
		this.x += x;
	}
	
	public void setX(final int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	
	public void moveY(final double y) {
		moveY((int)y);
	}
	
	public void moveY(final int y) {
		this.y += y;
	}
	
	public void setY(final int y) {
		this.y = y;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return "Position(" + x + "," + y + ")";
	}

}
