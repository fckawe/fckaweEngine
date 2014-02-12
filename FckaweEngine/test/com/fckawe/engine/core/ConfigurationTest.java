package com.fckawe.engine.core;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

public class ConfigurationTest {

	@Test
	public void testConfiguration() {
		Configuration c = new Configuration();
		
		// "application" category
		String name = c.getApplicationCat().getName();
		String iconPath = c.getApplicationCat().getIconPath();
		assertEquals(name, "The NoName-Game");
		assertEquals(iconPath, "images/icon.png");
		
		// "screen" category
		int width = c.getScreenCat().getWidth();
		int height = c.getScreenCat().getHeight();
		double scale = c.getScreenCat().getScale();
		int bitDepth = c.getScreenCat().getBitDepth();
		int refreshRate = c.getScreenCat().getRefreshRate();
		boolean fullScreen = c.getScreenCat().isFullScreen();
		boolean showFps = c.getScreenCat().isShowFps();
		assertEquals(800, width);
		assertEquals(600, height);
		assertEquals(1.0, scale, 0.01);
		assertEquals(32, bitDepth);
		assertEquals(0, refreshRate);
		assertEquals(false, fullScreen);
		assertEquals(true, showFps);
		
		// "session" category
		Locale locale = c.getSessionCat().getLocale();
		assertEquals(locale, Locale.getDefault());
	}

}
