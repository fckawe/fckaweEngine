package com.fckawe.engine.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;

import com.fckawe.engine.core.Session;

/**
 * Utility class which can be used to retrieve language or country specific
 * strings and values from the resource bundles.
 * 
 * @author fckawe
 */
public class NLS {

	/**
	 * The ID of the core bundle. Used as key for the resource bundle map
	 * ("bundles").
	 */
	public static final String CORE = "FCKAWE_CORE";

	private final Map<String, ResourceBundle> bundles;

	private final Logger logger;

	/**
	 * Default constructor with which any new NLS instance is created. Within
	 * the constructor all the resource bundles get registered in the bundle map
	 * ("bundles").
	 */
	public NLS() {
		this(Session.getSession());
	}

	/**
	 * Constructor to initialize the new NLS instance with a specified session.
	 * The parameter session may also be null if NLS should be used without any
	 * session.
	 * 
	 * @param session
	 *            The session to initialize NLS with.
	 */
	public NLS(final Session session) {
		logger = session == null ? null : session.getMainLogger();
		bundles = new HashMap<String, ResourceBundle>();
		registerResourceBundle(CORE, "nls.fckaweCore");
	}

	/**
	 * Retrieves a "translated" string out of the core bundle.
	 * 
	 * @param key
	 *            The key of the string to retrieve.
	 * @return The "translated" string.
	 */
	public String getString(final String key) {
		return getString(CORE, key);
	}

	/**
	 * Retrieves a "translated" string out of the bundle with the specified ID.
	 * 
	 * @param id
	 *            The ID of the bundle to retrieve the string out.
	 * @param key
	 *            The key of the string to retrieve.
	 * @return The "translated" string.
	 */
	public String getString(final String id, final String key) {
		ResourceBundle bundle = bundles.get(id);
		if (bundle == null) {
			if (logger != null && logger.isWarnEnabled()) {
				logger.warn("No language resource bundle with id '" + id
						+ "' registered!");
			}
			return null;
		}
		return bundle.getString(key);
	}

	/**
	 * Registeres a new resource bundle.
	 * 
	 * @param id
	 *            The ID of the resource bundle (used as key in the bundles map
	 *            "bundles").
	 * @param path
	 *            The path to the resource bundle.
	 */
	protected void registerResourceBundle(final String id, final String path) {
		ResourceBundle bundle = loadResourceBundle(path);
		bundles.put(id, bundle);
	}

	private ResourceBundle loadResourceBundle(final String path) {
		try {
			return ResourceBundle.getBundle(path, Locale.getDefault());
		} catch (MissingResourceException e) {
			if (logger != null && logger.isErrorEnabled()) {
				logger.error("Language resource bundle '" + path
						+ "' not found!", e);
			}
			return null;
		}
	}

}
