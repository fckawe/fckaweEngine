package com.fckawe.engine.core;

import java.util.Locale;

import org.slf4j.Logger;

import com.fckawe.engine.ui.UserInterface;
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

	private Heart heart;

	private Application app;

	private static Session session;

	/**
	 * Creates a new user session. Will be usually called only once.
	 * 
	 * @param app
	 *            The application to create a session for.
	 * @return A new user session instance.
	 */
	public static Session newSession(final Application app) {
		session = new Session();
		session.init(app);
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
	 * 
	 * @param app
	 *            The application to create a session for.
	 */
	protected void init(final Application app) {
		this.app = app;

		// first create loggerFactory since other initialising logics want to
		// log some messages...
		loggerFactory = fckaweFactory.newLoggerFactory();

		configuration = fckaweFactory.newConfiguration();
		Locale locale = configuration.getSessionCat().getLocale();
		Logger logger = getMainLogger();
		if (logger.isInfoEnabled()) {
			logger.info("Set locale to '" + locale + "'");
		}
		Locale.setDefault(locale);

		nls = fckaweFactory.newNLS();

		heart = fckaweFactory.newHeart(app);
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

	// TODO: comment
	public Logger getMainLogger() {
		return getLogger(LoggerFactory.MAIN_LOGGER);
	}

	// TODO: comment
	public Logger getConfigLogger() {
		return getLogger(LoggerFactory.CONFIG_LOGGER);
	}

	// TODO: comment
	public Logger getHeartLogger() {
		return getLogger(LoggerFactory.HEART_LOGGER);
	}

	// TODO: comment
	public Logger getGrafixLogger() {
		return getLogger(LoggerFactory.GRAFIX_LOGGER);
	}

	// TODO: comment
	public Logger getSoundLogger() {
		return getLogger(LoggerFactory.SOUND_LOGGER);
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

	// TODO: comment
	public Heart getHeart() {
		return heart;
	}

	// TODO: comment
	public Application getApplication() {
		return app;
	}

	// TODO: comment
	public UserInterface getUserInterface() {
		return app.getUserInterface();
	}

	// TODO: comment
	public FckaweFactory getFckaweFactory() {
		return fckaweFactory;
	}

	// TODO: comment
	public String getEngineName() {
		return "fckaweEngine";
	}

}
