package com.fckawe.engine.physics;


public class RectangularBounds {

	protected int topLeftX, topLeftY;

	protected int bottomRightX, bottomRightY;

	public RectangularBounds(final int topLeftX, final int topLeftY,
			final int bottomRightX, final int bottomRightY) {
		this.topLeftX = topLeftX;
		this.topLeftY = topLeftY;
		this.bottomRightX = bottomRightX;
		this.bottomRightY = bottomRightY;
	}
	
	public int getTopLeftX() {
		return topLeftX;
	}
	
	public int getTopLeftY() {
		return topLeftY;
	}
	
	public int getBottomRightX() {
		return bottomRightX;
	}
	
	public int getBottomRightY() {
		return bottomRightY;
	}

	public boolean intersects(final RectangularBounds other, final int shiftX,
			final int shiftY) {
		int otherTopLeftX = other.getTopLeftX();
		if(otherTopLeftX + shiftX > bottomRightX) {
			return false;
		}
		
		int otherTopLeftY = other.getTopLeftY();
		if(otherTopLeftY + shiftY > bottomRightY) {
			return false;
		}
		
		int otherBottomRightX = other.getBottomRightX();
		if(otherBottomRightX + shiftX < topLeftX) {
			return false;
		}
		
		int otherBottomRightY = other.getBottomRightY();
		if(otherBottomRightY + shiftY < topLeftY) {
			return false;
		}
		
		return true;
	}

//	public boolean intersects(final RectangularBounds other) {
//		if (topLeft == null || currentBitmap == null) {
//			return false;
//		}
//		if (other.getTopLeft() == null || other.getCurrentBitmap() == null) {
//			return false;
//		}
//
//		int width = currentBitmap.getWidth();
//		int bottomRightX = topLeft.getX() + width;
//		int otherTopLeftX = other.getTopLeft().getX();
//		if (otherTopLeftX > bottomRightX) {
//			return false;
//		}
//
//		int height = currentBitmap.getHeight();
//		int bottomRightY = topLeft.getY() + height;
//		int otherTopLeftY = other.getTopLeft().getY();
//		if (otherTopLeftY > bottomRightY) {
//			return false;
//		}
//
//		int otherWidth = other.getCurrentBitmap().getWidth();
//		int topLeftX = topLeft.getX();
//		int otherBottomRightX = other.getTopLeft().getX() + otherWidth;
//		if (otherBottomRightX < topLeftX) {
//			return false;
//		}
//
//		int otherHeight = other.getCurrentBitmap().getHeight();
//		int topLeftY = topLeft.getY();
//		int otherBottomRightY = other.getTopLeft().getY() + otherHeight;
//		if (otherBottomRightY < topLeftY) {
//			return false;
//		}
//
//		return true;
//	}
//
}
