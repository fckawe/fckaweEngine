package com.fckawe.engine.grafix;

public class Font {
	
	public static final short DEFAULT_LETTER_SPACING = 0;
	public static final short DEFAULT_LINE_SPACING = 2;
	
	private Bitmap[][] bitmapData;
	private String[] chars;
	private short width;
	private short height;
	private short letterSpacing;
	private short lineSpacing;
	
	public Font(final Bitmap[][] bitmapData, final String[] chars,
			final short width, final short height) {
		this(bitmapData, chars, width, height,
				DEFAULT_LETTER_SPACING, DEFAULT_LINE_SPACING);
	}
	
	public Font(final Bitmap[][] bitmapData, final String[] chars,
			final short width, final short height,
			final short letterSpacing, final short lineSpacing) {
		this.bitmapData = bitmapData;
		this.chars = chars;
		this.width = width;
		this.height = height;
		this.letterSpacing = letterSpacing;
		this.lineSpacing = lineSpacing;
	}
	
	public String[] getChars() {
		return chars;
	}
	
	public short getWidth() {
		return width;
	}
	
	public short getHeight() {
		return height;
	}
	
	public short getLetterSpacing() {
		return letterSpacing;
	}
	
	public short getLineSpacing() {
		return lineSpacing;
	}
	
	public Bitmap getCharBitmap(final char c) {
		Bitmap bitmap = null;
		for(int y = 0; y < chars.length; y++) {
			String charsLine = chars[y];
			int x = charsLine.indexOf(c);
			if(x >= 0) {
				bitmap = bitmapData[x][y];
			}
		}
		return bitmap;
	}
	
	public int getStringWidth(final String str) {
		return str.length() * width;
	}
	
	public boolean hasLowercaseLetters() {
		for(String charsLine : chars) {
			if(charsLine.indexOf('a') >= 0) {
				return true;
			}
		}
		return false;
	}
	
}