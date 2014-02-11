package com.fckawe.engine.core;

import java.util.Locale;

import org.slf4j.Logger;

import com.fckawe.engine.utils.FckaweFactory;
import com.fckawe.engine.utils.LoggerFactory;
import com.fckawe.engine.utils.NLS;

/**
 * The user session keeps all the important things like the configuration
 * together and makes these things accessible from all over the application
 * code.
 * 
 * @author fckawe
 */
public class Session {

	private final FckaweFactory fckaweFactory = new FckaweFactory();

	private LoggerFactory loggerFactory;

	private Configuration configuration;

	private NLS nls;

	private static Session session;

	/**
	 * Creates a new user session. Will be usually called only once.
	 * 
	 * @return A new user session instance.
	 */
	public static Session newSession() {
		session = new Session();
		session.init();
		return session;
	}

	/**
	 * Returns the current user session object.
	 * 
	 * @return The current user session.
	 */
	public static Session getSession() {
		return session;
	}

	/**
	 * Initializes the user session. Will be usually called only once (after
	 * creating a new session).
	 */
	protected void init() {
		// first create loggerFactory since other initialising logics want to
		// log some messages...
		loggerFactory = fckaweFactory.newLoggerFactory();

		configuration = fckaweFactory.newConfiguration();
		Locale locale = configuration.getSessionCat().getLocale();
		Logger logger = getLogger(LoggerFactory.MAIN_LOGGER);
		if (logger.isInfoEnabled()) {
			logger.info("Set locale to '" + locale + "'");
		}
		Locale.setDefault(locale);

		nls = fckaweFactory.newNLS();
	}

	/**
	 * Returns the logger with the specified ID.
	 * 
	 * @param loggerId
	 *            The logger's ID.
	 * @return The logger.
	 */
	public Logger getLogger(final String loggerId) {
		return loggerFactory.getLogger(loggerId);
	}

	/**
	 * Returns the configuration object that grants access to all the
	 * configuration values.
	 * 
	 * @return The application's configuration.
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Returns the NLS utility object which can be used to get language or
	 * country specific strings and values.
	 * 
	 * @return The NLS utility.
	 */
	public NLS getNLS() {
		return nls;
	}

}
