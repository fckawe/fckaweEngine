package com.fckawe.engine.utils;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

import com.fckawe.engine.core.Session;

public class NLSTest {

	/**
	 * Test an example string out of the core resource bundle. Get the german
	 * and the english translation and check if NLS returns the expected result.
	 */
	@Test
	public void testGetStringString() {
		NLS nls;
		String str;
		Locale defaultLocale = Locale.getDefault();

		Locale.setDefault(new Locale("de", "DE"));
		nls = new NLS((Session)null);
		str = nls.getString("HELLO_WORLD");
		assertEquals("Hallo Welt", str);

		Locale.setDefault(new Locale("en", "US"));
		nls = new NLS((Session)null);
		str = nls.getString("HELLO_WORLD");
		assertEquals("Hello World", str);

		Locale.setDefault(defaultLocale);
	}

}
