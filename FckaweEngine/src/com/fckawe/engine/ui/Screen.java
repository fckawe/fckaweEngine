package com.fckawe.engine.ui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.fckawe.engine.grafix.Bitmap;

/**
 * The screen is the main bitmap on which all the content gets pasted.
 * 
 * @author fckawe
 */
public class Screen extends Bitmap {

	private final BufferedImage image;

	/**
	 * Constructor to create a new screen instance with the given dimension.
	 * 
	 * @param dimension
	 *            The dimension to create the screen with.
	 */
	public Screen(final Dimension dimension) {
		this((int) dimension.getWidth(), (int) dimension.getHeight());
	}

	/**
	 * Constructor to create a new screen instance with the given width and
	 * height.
	 * 
	 * @param width
	 *            The width in pixels to create the screen with.
	 * @param height
	 *            The height in pixels to create the screen with.
	 */
	public Screen(final int width, final int height) {
		super(width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

	/**
	 * Returns the screen's current image.
	 * 
	 * @return The screen's current image.
	 */
	public BufferedImage getImage() {
		return image;
	}

}