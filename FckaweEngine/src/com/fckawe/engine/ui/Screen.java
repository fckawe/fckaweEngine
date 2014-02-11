package com.fckawe.engine.ui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.fckawe.engine.grafix.Bitmap;

public class Screen extends Bitmap {

	private final BufferedImage image;

	public Screen(final Dimension dimension) {
		this((int) dimension.getWidth(), (int) dimension.getHeight());
	}

	public Screen(final int width, final int height) {
		super(width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

	public BufferedImage getImage() {
		return image;
	}

}
