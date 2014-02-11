package com.fckawe.engine.grafix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fckawe.engine.core.Session;
import com.fckawe.engine.ui.UserInterface;
import com.fckawe.engine.utils.XmlLoader;

public class Fonts {

	public static final String DEFAULT_FONT = "DEFAULT";

	private final Map<String, Font> fonts = new HashMap<String, Font>();

	private final UserInterface ui;

	public Fonts(final UserInterface ui) {
		this.ui = ui;
	}

	public void loadFonts() {
		XmlLoader loader = new XmlLoader("/fonts/fonts.xml");
		Map<String, String> parameters = loader.getValues();

		Map<String, Map<String, String>> paramsPerFontId = groupParamsByFontId(parameters);
		for (String fontId : paramsPerFontId.keySet()) {
			Map<String, String> fontParams = paramsPerFontId.get(fontId);
			Font font = createFont(fontParams);
			String name = fontParams.get("name");
			fonts.put(name.toUpperCase(), font);
		}
	}

	protected Map<String, Map<String, String>> groupParamsByFontId(
			final Map<String, String> parameters) {
		Map<String, Map<String, String>> paramsPerFont = new HashMap<String, Map<String, String>>();

		for (String key : parameters.keySet()) {
			String[] parts = key.split("\\.");
			String fontId = parts[2];

			StringBuilder subKey = new StringBuilder();
			for (int i = 3; i < parts.length; i++) {
				if (subKey.length() > 0) {
					subKey.append(".");
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
		String bitmapFile = fontParams.get("bitmap.file");

		List<String> lines = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			if (fontParams.containsKey("bitmap.line." + i)) {
				lines.add(fontParams.get("bitmap.line." + i));
			} else {
				break;
			}
		}

		String[] chars = new String[lines.size()];
		for (int i = 0; i < lines.size(); i++) {
			chars[i] = lines.get(i);
		}

		Bitmap[][] bitmapData = ui.getBitmapsLogic().cut(
				"/fonts/" + bitmapFile, width, height);
		Font font = new Font(bitmapData, chars, width, height, letterSpacing,
				lineSpacing);
		// TODO: log
		// Session.getSession()
		// .getGrafixLogger()
		// .info(" Loaded font {} ({}x{})",
		// new String[] { fontName, String.valueOf(fontWidth),
		// String.valueOf(fontHeight) });
		return font;
	}

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

}
