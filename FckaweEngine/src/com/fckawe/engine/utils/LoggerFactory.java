package com.fckawe.engine.utils;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

/**
 * A simple wrapper for the slf4j-LoggerFactory that defines several logger-IDs.
 * 
 * @author fckawe
 */
public class LoggerFactory {

	/**
	 * The main logger that can be used to log general game messages.
	 */
	public static final String MAIN_LOGGER = "MAIN";

	/**
	 * The configuration logger that logs messages about the configuration of
	 * the game (e.g. loading configuration).
	 */
	public static final String CONFIG_LOGGER = "CONFIG";

	/**
	 * The heart logger that logs messages about the heart that controls the
	 * game logic.
	 */
	public static final String HEART_LOGGER = "HEART";

	/**
	 * The grafix logger that logs messages about the grafix stuff.
	 */
	public static final String GRAFIX_LOGGER = "GRAFIX";

	/**
	 * The sound logger that logs messages about the sound stuff.
	 */
	public static final String SOUND_LOGGER = "SOUND";

	private final Set<String> ids = new HashSet<String>();

	public LoggerFactory() {
		registerLogger(MAIN_LOGGER);
		registerLogger(CONFIG_LOGGER);
		registerLogger(HEART_LOGGER);
		registerLogger(GRAFIX_LOGGER);
		registerLogger(SOUND_LOGGER);
	}

	/**
	 * Registers a new logger.
	 * 
	 * @param id
	 *            The ID of the logger.
	 */
	protected void registerLogger(final String id) {
		if (ids.contains(id)) {
			throw new IllegalStateException("Logger '" + id
					+ "' already registered!");
		}
		ids.add(id);
	}

	/**
	 * Gets and returns a logger by its ID.
	 * 
	 * @param id
	 *            The ID of the logger.
	 * @return The (slf4j-)Logger.
	 */
	public Logger getLogger(final String id) {
		if (!ids.contains(id)) {
			throw new IllegalStateException("No Logger with id '" + id
					+ "' registered!");
		}
		return org.slf4j.LoggerFactory.getLogger(id);
	}

}
