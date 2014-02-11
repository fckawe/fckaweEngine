package com.fckawe.engine.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class BreadcrumbTest {

	/**
	 * Test the toString method of the Breadcrumb class.
	 */
	@Test
	public void testToString() {
		Breadcrumb p = new Breadcrumb();

		String str;
		
		p.push("bread");
		str = p.toString();
		assertEquals("bread", str);

		p.push("crumb");
		str = p.toString();
		assertEquals("bread.crumb", str);
		
		p.push("test");
		str = p.toString();
		assertEquals("bread.crumb.test", str);
		
		p.pop();
		str = p.toString();
		assertEquals("bread.crumb", str);
	}

}
