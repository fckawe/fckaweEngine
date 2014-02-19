package com.fckawe.engine.grafix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.fckawe.engine.core.Session;
import com.fckawe.engine.physics.Position;
import com.fckawe.engine.ui.Screen;
import com.fckawe.engine.ui.UserInterface;
import com.fckawe.engine.utils.Breadcrumb;
import com.fckawe.engine.utils.XmlLoader;

/**
 * Manages the access to fonts. This class loads all fonts to memory and is as
 * well responsible to draw strings with a specified font onto the screen.
 * 
 * @author fckawe
 */
public class Fonts {

	private static final String FONTS_DIR = "/fonts/";

	private static final String FONTS_FILE = "fonts.xml";

	public static final String DEFAULT_FONT = "DEFAULT";

	private final Map<String, Font> fonts;

	private final UserInterface ui;

	private final Logger logger;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param ui
	 *            A reference to the user interface.
	 */
	public Fonts(final UserInterface ui) {
		fonts = new HashMap<String, Font>();

		this.ui = ui;

		Session session = Session.getSession();
		logger = session == null ? null : session.getGrafixLogger();
	}

	/**
	 * Load all fonts to memory.
	 */
	public void loadFonts() {
		fonts.clear();

		String fontsFilePath = FONTS_DIR + FONTS_FILE;
		Loader loader = new Loader();
		loader.load(fontsFilePath);
	}

	/**
	 * Returns the font with the specified name. If a font with the specified
	 * name cannot be found the method returns the default font instead.
	 * 
	 * @param fontName
	 *            The name of the font to return.
	 * @return The font with the specified name.
	 */
	public Font getFont(final String fontName) {
		Font font;
		if (fontName == null) {
			font = fonts.get(DEFAULT_FONT);
		} else {
			font = fonts.get(fontName.toUpperCase());
		}
		if (font == null) {
			Session.getSession()
					.getGrafixLogger()
					.error("Font with name {} does not exist (or was not loaded successfully)!",
							fontName.toUpperCase());
			font = fonts.get(DEFAULT_FONT);
			fonts.put(fontName.toUpperCase(), font);
		}
		return font;
	}

	/**
	 * Draws a message with the default font on the given position on the
	 * screen.
	 * 
	 * @param screen
	 *            The screen.
	 * @param message
	 *            The message string to be drawn.
	 * @param start
	 *            The starting position.
	 */
	public void draw(final Screen screen, final String message,
			final Position start) {
		drawMulti(screen, null, message, start, -1);
	}

	/**
	 * Draws a message with the given font on the given position on the screen.
	 * 
	 * @param screen
	 *            The screen.
	 * @param fontName
	 *            The name of the font to use.
	 * @param message
	 *            The message string to be drawn.
	 * @param start
	 *            The starting position.
	 */
	public void draw(final Screen screen, final String fontName,
			final String message, final Position start) {
		drawMulti(screen, fontName, message, start, -1);
	}

	/**
	 * Draws a message with the given font on the given position on the screen.
	 * The width of the text can be limited setting maxWidth to the maximum
	 * width. If maxWidth is -1 the screen's the limit.
	 * 
	 * @param screen
	 *            The screen.
	 * @param fontName
	 *            The name of the font to use.
	 * @param message
	 *            The message string to be drawn.
	 * @param start
	 *            The starting position.
	 * @param maxWidth
	 *            The maximum with for the text or -1 if "unlimited".
	 */
	public void drawMulti(final Screen screen, final String fontName,
			final String message, final Position start,
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
		int posX = start.getX();
		int posY = start.getY();
		for (int i = 0; i < length; i++) {
			char c = msg.charAt(i);
			if (c == '\n') {
				posX = start.getX();
				posY += fontHeight + lineSpacing;
				continue;
			}
			Bitmap charBitmap = font.getCharBitmap(c);
			if (charBitmap == null) {
				continue;
			}
			Position pos = new Position(posX, posY);
			screen.blit(charBitmap, pos);
			posX += fontWidth + letterSpacing;
			if (posX > width) {
				posX = start.getX();
				posY += fontHeight + lineSpacing;
			}
		}
	}

	/**
	 * Draws a message with the default font on centered position on the screen.
	 * 
	 * @param screen
	 *            The screen.
	 * @param message
	 *            The message string to be drawn.
	 */
	public void drawCentered(final Screen screen, final String message) {
		drawCentered(screen, DEFAULT_FONT, message);
	}

	/**
	 * Draws a message with the default font on a specified position centered on
	 * the screen.
	 * 
	 * @param screen
	 *            The screen.
	 * @param message
	 *            The message string to be drawn.
	 * @param centerPos
	 *            The center position.
	 */
	public void drawCentered(final Screen screen, final String message,
			final Position centerPos) {
		drawCentered(screen, DEFAULT_FONT, message, centerPos);
	}

	/**
	 * Draws a message with the given font on centered position on the screen.
	 * 
	 * @param screen
	 *            The screen.
	 * @param fontName
	 *            The name of the font to use.
	 * @param message
	 *            The message string to be drawn.
	 */
	public void drawCentered(final Screen screen, final String fontName,
			final String message) {
		int posX = screen.getWidth() / 2;
		int posY = screen.getHeight() / 2;
		Position pos = new Position(posX, posY);
		drawCentered(screen, fontName, message, pos);
	}

	/**
	 * Draws a message with the given font on a specified position centered on
	 * the screen.
	 * 
	 * @param screen
	 *            The screen.
	 * @param fontName
	 *            The name of the font to use.
	 * @param message
	 *            The message string to be drawn.
	 * @param centerPos
	 *            The center position.
	 */
	public void drawCentered(final Screen screen, final String fontName,
			final String message, final Position centerPos) {
		Font font = ui.getFont(fontName);
		int fontWidth = font.getStringWidth(message);
		int fontHeight = font.getHeight();
		int posX = centerPos.getX() - fontWidth / 2;
		int posY = centerPos.getY() - fontHeight / 2;
		Position pos = new Position(posX, posY);
		draw(screen, fontName, message, pos);
	}

	/**
	 * Helper class that loads all fonts.
	 * 
	 * @author fckawe
	 */
	private class Loader {

		/**
		 * Load all fonts specified in the given fonts file (XML file which
		 * defines all fonts).
		 * 
		 * @param fontsFilePath
		 *            Path to the fonts file (XML file which defines all fonts).
		 */
		private void load(final String fontsFilePath) {
			if (logger != null && logger.isInfoEnabled()) {
				logger.info("Loading fonts from file '{}'...", fontsFilePath);
			}

			XmlLoader loader = new XmlLoader(fontsFilePath);
			Map<String, String> parameters = loader.getValues();

			Map<String, Map<String, String>> paramsPerFontId = groupParamsByFontId(parameters);
			for (String fontId : paramsPerFontId.keySet()) {
				Map<String, String> fontParams = paramsPerFontId.get(fontId);
				Font font = createFont(fontParams);
				String name = fontParams.get("name");
				fonts.put(name.toUpperCase(), font);

				if (logger != null && logger.isInfoEnabled()) {
					logger.info(
							"Font '{}' (width={}, height={}, letterSpacing={}, lineSpacing={}) loaded.",
							name, String.valueOf(font.getWidth()),
							String.valueOf(font.getHeight()),
							String.valueOf(font.getLetterSpacing()),
							String.valueOf(font.getLineSpacing()));
				}
			}
		}

		/**
		 * Groups all parameters retrieved from the fonts file by the font ID.
		 * 
		 * @param parameters
		 *            All the parameters retrieved from the fonts file.
		 * @return A map with the parameters grouped by font ID.
		 */
		private Map<String, Map<String, String>> groupParamsByFontId(
				final Map<String, String> parameters) {
			Map<String, Map<String, String>> paramsPerFont = new HashMap<String, Map<String, String>>();

			for (String key : parameters.keySet()) {
				String[] parts = key.split(Breadcrumb.PATH_SEPARATOR_REGEXED);
				String fontId = parts[2];

				StringBuilder subKey = new StringBuilder();
				for (int i = 3; i < parts.length; i++) {
					if (subKey.length() > 0) {
						subKey.append(Breadcrumb.PATH_SEPARATOR);
					}
					subKey.append(parts[i]);
				}

				Map<String, String> subKeys = paramsPerFont.get(fontId);
				if (subKeys == null) {
					subKeys = new HashMap<String, String>();
					paramsPerFont.put(fontId, subKeys);
				}
				subKeys.put(subKey.toString(), parameters.get(key));
			}

			return paramsPerFont;
		}

		/**
		 * Creates a font by the given parameters.
		 * 
		 * @param fontParams
		 *            The parameters to create the font with.
		 * @return The newly created font object.
		 */
		protected Font createFont(final Map<String, String> fontParams) {
			String str;
			short width = Short.parseShort(fontParams.get("width"));
			short height = Short.parseShort(fontParams.get("height"));
			str = fontParams.get("letterSpacing");
			short letterSpacing;
			try {
				letterSpacing = Short.parseShort(str);
			} catch (NumberFormatException e) {
				letterSpacing = Font.DEFAULT_LETTER_SPACING;
			}
			str = fontParams.get("lineSpacing");
			short lineSpacing;
			try {
				lineSpacing = Short.parseShort(str);
			} catch (NumberFormatException e) {
				lineSpacing = Font.DEFAULT_LINE_SPACING;
			}
			String bitmapFile = fontParams.get("bitmap"
					+ Breadcrumb.PATH_SEPARATOR + "file");

			List<String> lines = new ArrayList<String>();
			for (int i = 0; i < 100; i++) {
				String lineKey = "bitmap" + Breadcrumb.PATH_SEPARATOR + "line"
						+ Breadcrumb.PATH_SEPARATOR + (i + 1);
				if (fontParams.containsKey(lineKey)) {
					lines.add(fontParams.get(lineKey));
				} else {
					break;
				}
			}

			String[] chars = new String[lines.size()];
			for (int i = 0; i < lines.size(); i++) {
				chars[i] = lines.get(i);
			}

			Bitmap[][] bitmapData = ui.getBitmaps().loadSprite(
					FONTS_DIR + bitmapFile, width, height, 0, 0, 1.0, null, -1);

			Font font = new Font(bitmapData, chars, width, height,
					letterSpacing, lineSpacing);

			return font;
		}

	}

}
