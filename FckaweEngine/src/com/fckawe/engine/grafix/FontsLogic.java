package com.fckawe.engine.grafix;

import com.fckawe.engine.ui.Screen;
import com.fckawe.engine.ui.UserInterface;

public class FontsLogic {

	private static final int BACKGROUND_PADDING_X = 5;
	private static final int BACKGROUND_PADDING_Y = 5;
	private static final int BACKGROUND_COLOR = 0xffffff;

	private UserInterface ui;

	public FontsLogic(final UserInterface ui) {
		this.ui = ui;
	}

	public void draw(final Screen screen, final String message,
			final int startX, final int startY) {
		drawMulti(screen, null, message, startX, startY, -1);
	}

	public void draw(final Screen screen, final String fontName,
			final String message, final int startX, final int startY) {
		drawMulti(screen, fontName, message, startX, startY, -1);
	}

	public void drawMulti(final Screen screen, final String message,
			final int startX, final int startY, final int maxWidth) {
		drawMulti(screen, null, startX, startY, maxWidth);
	}

	public void drawMulti(final Screen screen, final String fontName,
			final String message, final int startX, final int startY,
			final int maxWidth) {
		int width = maxWidth < 0 ? screen.getWidth() : maxWidth;
		Font font = ui.getFont(fontName);
		short fontWidth = font.getWidth();
		short fontHeight = font.getHeight();
		short letterSpacing = font.getLetterSpacing();
		short lineSpacing = font.getLineSpacing();
		String msg = font.hasLowercaseLetters() ? message : message
				.toUpperCase();
		int length = msg.length();
		int posX = startX;
		int posY = startY;
		for (int i = 0; i < length; i++) {
			char c = msg.charAt(i);
			if (c == '\n') {
				posX = startX;
				posY += fontHeight + lineSpacing;
				continue;
			}
			Bitmap charBitmap = font.getCharBitmap(c);
			if (charBitmap == null) {
				continue;
			}
			screen.blit(charBitmap, posX, posY);
			posX += fontWidth + letterSpacing;
			if (posX > width) {
				posX = startX;
				posY += fontHeight + lineSpacing;
			}
		}
	}

	public void drawCentered(final Screen screen, final String message) {
		drawCentered(screen, message, screen.getWidth() / 2,
				screen.getHeight() / 2);
	}

	public void drawCentered(final Screen screen, final String message,
			final int centerPosX, final int centerPosY) {
		drawCentered(screen, null, message, centerPosX, centerPosY);
	}

	public void drawCentered(final Screen screen, final String fontName,
			final String message) {
		drawCentered(screen, fontName, message, screen.getWidth() / 2,
				screen.getHeight() / 2);
	}

	public void drawCentered(final Screen screen, final String fontName,
			final String message, final int centerPosX, final int centerPosY) {
		drawCentered(screen, fontName, message, centerPosX, centerPosY, false,
				-1);
	}

	public void drawCentered(final Screen screen, final String fontName,
			final String message, final int centerPosX, final int centerPosY,
			final boolean withBackground, final int backgroundAlpha) {
		Font font = ui.getFont(fontName);
		int fontWidth = font.getStringWidth(message);
		int fontHeight = font.getHeight();
		if (withBackground) {
			int posX = centerPosX - fontWidth / 2 - BACKGROUND_PADDING_X;
			int posY = centerPosY - fontHeight / 2 - BACKGROUND_PADDING_Y;
			int width = fontWidth + 2 * BACKGROUND_PADDING_X;
			int height = fontHeight + 2 * BACKGROUND_PADDING_Y;
			if (backgroundAlpha >= 0) {
				int color = BACKGROUND_COLOR | (backgroundAlpha << 24);
				screen.transparencyFill(posX, posY, width, height, color);
			} else {
				screen.fill(posX, posY, width, height, BACKGROUND_COLOR);
			}
		}
		draw(screen, fontName, message, centerPosX - fontWidth / 2, centerPosY
				- fontHeight / 2);
	}

	public void drawMessage(final Screen screen, final String message) {
		drawMessage(screen, message, screen.getWidth() / 2,
				screen.getHeight() / 2);
	}

	public void drawMessage(final Screen screen, final String message,
			final int centerPosX, final int centerPosY) {
		drawCentered(screen, "SMALLRED", message, centerPosX, centerPosY, true,
				0xcc);
	}

}
