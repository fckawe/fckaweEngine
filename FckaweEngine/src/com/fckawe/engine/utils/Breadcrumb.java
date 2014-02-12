package com.fckawe.engine.utils;

import java.util.Stack;

/**
 * A string stack that is used to track a breadcrumb-like path.
 * 
 * @author fckawe
 */
@SuppressWarnings("serial")
public class Breadcrumb extends Stack<String> {
	
	public static final String PATH_SEPARATOR = ".";
	
	public static final String PATH_SEPARATOR_REGEXED = "\\.";

	/**
	 * Returns the string representation of the current path, which means that
	 * it concatenates all path elements separated with dots.
	 * 
	 * @return The string representation of the current path.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String str : this) {
			if (sb.length() > 0) {
				sb.append(PATH_SEPARATOR);
			}
			sb.append(str);
		}
		return sb.toString().toLowerCase();
	}

}
