package com.fckawe.engine.grafix;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.fckawe.engine.core.Session;

public class BitmapsLogic {

	public enum DestSizeType {
		X, Y
	}

	public Bitmap[][] cut(final String path, final int cutSizeX,
			final int cutSizeY) {
		return cut(path, cutSizeX, cutSizeY, 1.0);
	}

	public Bitmap[][] cut(final String path, final int cutSizeX,
			final int cutSizeY, final double scale) {
		return cut(path, cutSizeX, cutSizeY, 0, 0, scale, null, -1);
	}

	public Bitmap[][] cut(final String path, final int cutSizeX,
			final int cutSizeY, final int marginX, final int marginY,
			final DestSizeType destSizeType, final int destSize) {
		return cut(path, cutSizeX, cutSizeY, marginX, marginY, -1,
				destSizeType, destSize);
	}

	public Bitmap[][] cut(final String path, final int cutSizeX,
			final int cutSizeY, final int marginX, final int marginY,
			final double scale) {
		return cut(path, cutSizeX, cutSizeY, marginX, marginY, scale, null, -1);
	}

	private Bitmap[][] cut(final String path, final int cutSizeX,
			final int cutSizeY, final int marginX, final int marginY,
			final double scale, final DestSizeType destSizeType,
			final int destSize) {
		try {
			URL url = BitmapsLogic.class.getResource(path);
			BufferedImage bi = ImageIO.read(url);
			int useCutSizeX = cutSizeX;
			int useCutSizeY = cutSizeY;
			int useMarginX = marginX;
			int useMarginY = marginY;
			double useScale = getScale(scale, cutSizeX, cutSizeY, destSizeType,
					destSize);
			if (useScale > 0 && useScale != 1.0) {
				bi = scaleImage(bi, useScale);
				useCutSizeX = (int) (useCutSizeX * useScale);
				useCutSizeY = (int) (useCutSizeY * useScale);
				useMarginX = (int) (useMarginX * useScale);
				useMarginY = (int) (useMarginY * useScale);
			}

			int xTiles = (bi.getWidth() - useMarginX) / useCutSizeX;
			int yTiles = (bi.getHeight() - useMarginY) / useCutSizeY;

			Bitmap[][] result = new Bitmap[xTiles][yTiles];

			for (int x = 0; x < xTiles; x++) {
				for (int y = 0; y < yTiles; y++) {
					Bitmap bitmap = new Bitmap(useCutSizeX, useCutSizeY);
					int startX = useMarginX + x * useCutSizeX;
					int startY = useMarginY + y * useCutSizeY;
					int[] pixels = bitmap.getPixels();
					bi.getRGB(startX, startY, useCutSizeX, useCutSizeY, pixels,
							0, useCutSizeX);
					bitmap.setPixels(pixels);
					result[x][y] = bitmap;
				}
			}

			return result;
		} catch (IOException e) {
			Session.getSession().getGrafixLogger()
					.error("Error while cutting bitmap!", e);
		}

		return null;
	}

	public Bitmap load(final String path) {
		return load(path, 1.0);
	}

	public Bitmap load(final String path, final DestSizeType destSizeType,
			final int destSize) {
		return load(path, -1, destSizeType, destSize);
	}

	public Bitmap load(final String path, final double scale) {
		return load(path, scale, null, -1);
	}

	private Bitmap load(final String path, final double scale,
			final DestSizeType destSizeType, final int destSize) {
		try {
			BufferedImage bi = ImageIO.read(BitmapsLogic.class
					.getResource(path));
			int width = bi.getWidth();
			int height = bi.getHeight();
			Bitmap result = new Bitmap(width, height);
			bi.getRGB(0, 0, width, height, result.pixels, 0, width);
			return result;
		} catch (IOException e) {
			Session.getSession().getGrafixLogger()
					.error("Error while loading bitmap!", e);
		}
		return null;
	}

	private double getScale(final double scale, final int cutSizeX,
			final int cutSizeY, final DestSizeType destSizeType,
			final int destSize) {
		if (scale < 0 && destSizeType != null && destSize > 0) {
			switch (destSizeType) {
			case X:
				return destSize / (double) cutSizeX;
			case Y:
				return destSize / (double) cutSizeY;
			}
		}
		return scale;
	}

	private BufferedImage scaleImage(final BufferedImage bi, final double scale) {
		int newWidth = (int) (bi.getWidth() * scale);
		int newHeight = (int) (bi.getHeight() * scale);
		BufferedImage scaledBi = new BufferedImage(newWidth, newHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = scaledBi.createGraphics();
		Image scaledImage = bi.getScaledInstance(newWidth, newHeight,
				BufferedImage.SCALE_SMOOTH);
		g2.drawImage(scaledImage, 0, 0, null);
		g2.dispose();
		return scaledBi;
	}

}
