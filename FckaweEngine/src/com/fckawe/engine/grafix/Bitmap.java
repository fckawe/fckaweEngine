package com.fckawe.engine.grafix;

import java.awt.Dimension;
import java.util.Arrays;

/**
 * A bitmap graphic represented by its pixel values.
 * @author Gerald Backmeister
 */
public class Bitmap {

	private final int width;
	private final int height;
	protected int[] pixels;

	/**
	 * Create a new bitmap with the given size.
	 * @param width The width of the bitmap.
	 * @param height The height of the bitmap.
	 */
	public Bitmap(final int width, final int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}

	/**
	 * Returns the bitmap's width.
	 * @return The width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the bitmap's height.
	 * @return The height.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns the bitmap's dimension.
	 * @return The dimension.
	 */
	public Dimension getDimension() {
		return new Dimension(width, height);
	}

	/**
	 * Returns an array of all the bitmap's pixels.
	 * @return The pixels as an integer array.
	 */
	public int[] getPixels() {
		return pixels;
	}

	/**
	 * Sets the bitmap's pixels as an array.
	 * @param pixels The pixels as an integer array.
	 */
	public void setPixels(final int[] pixels) {
		this.pixels = pixels;
	}

	/**
	 * Clears the bitmap (assigns the given background color to all pixels).
	 * @param color The (background) color to assign to all pixels.
	 */
	public void clear(final int color) {
		Arrays.fill(pixels, color);
	}

	public void blit(final Bitmap bitmap, final int posX, final int posY) {
		blitInternal(bitmap, posX, posY, bitmap.getWidth(), bitmap.getHeight(),
				false);
	}

	public void blit(final Bitmap bitmap, final int posX, final int posY,
			final int insWidth, final int insHeight) {
		blitInternal(bitmap, posX, posY, bitmap.getWidth(), bitmap.getHeight(),
				false);
	}

	public void transparencyBlit(final Bitmap bitmap, final int posX,
			final int posY) {
		blitInternal(bitmap, posX, posY, bitmap.getWidth(), bitmap.getHeight(),
				true);
	}

	public void transparencyBlit(final Bitmap bitmap, final int posX,
			final int posY, final int insWidth, final int insHeight) {
		blitInternal(bitmap, posX, posY, bitmap.getWidth(), bitmap.getHeight(),
				true);
	}

	private void blitInternal(final Bitmap bitmap, final int posX,
			final int posY, final int insWidth, final int insHeight,
			boolean withTransparency) {

		int posXTopLeft = posX < 0 ? 0 : posX;
		int posXBottomRight = posX + insWidth;
		if (posXBottomRight > width) {
			posXBottomRight = width;
		}

		int posYTopLeft = posY < 0 ? 0 : posY;
		int posYBottomRight = posY + insHeight;
		if (posYBottomRight > height) {
			posYBottomRight = height;
		}

		int insertWidth = posXBottomRight - posXTopLeft;

		for (int y = posYTopLeft; y < posYBottomRight; y++) {
			int lineStartOffset = (y * width) + posXTopLeft;
			int bitmapOffset = (y - posY) * bitmap.getWidth()
					+ (posXTopLeft - posX);
			for (int x = 0; x < insertWidth; x++) {
				int col = bitmap.pixels[bitmapOffset + x];
				if (col < 0 || withTransparency) {
					int a = (col >> 24) & 0xff;
					if (a == 0) {
						continue;
					}
					if (withTransparency && a < 0xff) {
						int bgColor = pixels[lineStartOffset + x];
						int bgr = bgColor & 0xff0000;
						int bgg = bgColor & 0xff00;
						int bgb = bgColor & 0xff;
						int r = col & 0xff0000;
						int g = col & 0xff00;
						int b = col & 0xff;
						int aa = 256 - a;
						r = ((r * a + bgr * aa) >> 8) & 0xff0000;
						g = ((g * a + bgg * aa) >> 8) & 0xff00;
						b = ((b * a + bgb * aa) >> 8) & 0xff;
						col = 0xff000000 | r | g | b;
					}
					pixels[lineStartOffset + x] = col;
				}
			}
		}
	}

	// TODO: verstehen, ordnen
	public void colorBlit(final Bitmap bitmap, final int x, final int y,
			final int color) {
		int x0 = x;
		int x1 = x + bitmap.getWidth();
		int y0 = y;
		int y1 = y + bitmap.getHeight();
		if (x0 < 0)
			x0 = 0;
		if (y0 < 0)
			y0 = 0;
		if (x1 > width)
			x1 = width;
		if (y1 > height)
			y1 = height;
		int ww = x1 - x0;

		int a2 = (color >> 24) & 0xff;
		int a1 = 256 - a2;

		int rr = color & 0xff0000;
		int gg = color & 0xff00;
		int bb = color & 0xff;

		for (int yy = y0; yy < y1; yy++) {
			int tp = yy * width + x0;
			int sp = (yy - y) * bitmap.getWidth() + (x0 - x);
			for (int xx = 0; xx < ww; xx++) {
				int col = bitmap.pixels[sp + xx];
				if (col < 0) {
					int r = (col & 0xff0000);
					int g = (col & 0xff00);
					int b = (col & 0xff);

					r = ((r * a1 + rr * a2) >> 8) & 0xff0000;
					g = ((g * a1 + gg * a2) >> 8) & 0xff00;
					b = ((b * a1 + bb * a2) >> 8) & 0xff;
					pixels[tp + xx] = 0xff000000 | r | g | b;
				}
			}
		}
	}

	public void fill(final int x, final int y, final int bw, final int bh,
			final int color) {
		fillInternal(x, y, bw, bh, color, false);
	}

	public void transparencyFill(final int x, final int y, final int bw,
			final int bh, final int color) {
		fillInternal(x, y, bw, bh, color, true);
	}

	// TODO: verstehen, ordnen
	private void fillInternal(final int x, final int y, final int bw,
			final int bh, final int color, final boolean withTransparency) {
		int a = (color >> 24) & 0xff;
		if (withTransparency && a == 0) {
			return;
		}

		int x0 = x;
		int x1 = x + bw;
		int y0 = y;
		int y1 = y + bh;
		if (x0 < 0)
			x0 = 0;
		if (y0 < 0)
			y0 = 0;
		if (x1 > width)
			x1 = width;
		if (y1 > height)
			y1 = height;
		int ww = x1 - x0;

		for (int yy = y0; yy < y1; yy++) {
			int tp = yy * width + x0;
			for (int xx = 0; xx < ww; xx++) {
				int col = color;
				if (withTransparency && a < 0xff) {
					int bgColor = pixels[tp + xx];
					int bgr = bgColor & 0xff0000;
					int bgg = bgColor & 0xff00;
					int bgb = bgColor & 0xff;
					int r = col & 0xff0000;
					int g = col & 0xff00;
					int b = col & 0xff;
					int aa = 256 - a;
					r = ((r * a + bgr * aa) >> 8) & 0xff0000;
					g = ((g * a + bgg * aa) >> 8) & 0xff00;
					b = ((b * a + bgb * aa) >> 8) & 0xff;
					col = 0xff000000 | r | g | b;
				}
				pixels[tp + xx] = col;
			}
		}
	}

}