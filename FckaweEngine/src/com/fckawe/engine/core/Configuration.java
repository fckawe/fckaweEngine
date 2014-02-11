package com.fckawe.engine.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;

import com.fckawe.engine.utils.Breadcrumb;
import com.fckawe.engine.utils.LoggerFactory;
import com.fckawe.engine.utils.XmlLoader;

/**
 * Loads the game's configuration XML file.
 * 
 * @author fckawe
 */
public class Configuration {

	private final Logger logger;

	private ApplicationCategory applicationCat;

	private ScreenCategory screenCat;

	private SessionCategory sessionCat;

	/**
	 * Creates a new instance of the Configuration class and instantly loads the
	 * configuration file.
	 */
	public Configuration() {
		this(Session.getSession());
	}

	/**
	 * Creates a new instance of the Configuration class with a specified
	 * session and instantly loads the configuration file.
	 * 
	 * @param session
	 *            The session to create the Configuration instance with.
	 */
	public Configuration(final Session session) {
		logger = session == null ? null : session
				.getLogger(LoggerFactory.CONFIG_LOGGER);

		String configFilePath = getConfigFilePath();
		if (logger != null && logger.isInfoEnabled()) {
			logger.info("Read configuration from file '" + configFilePath + "'");
		}

		XmlLoader xmlLoader;
		try {
			xmlLoader = new XmlLoader(configFilePath);
		} catch (RuntimeException e) {
			if (logger != null && logger.isErrorEnabled()) {
				logger.error("Error reading configuration file.", e);
			}
			throw e;
		}

		Map<String, String> values = xmlLoader.getValues();
		if (logger != null && logger.isDebugEnabled()) {
			logger.debug("The XML loader returned the following key/value pairs:");
			for (String key : values.keySet()) {
				String value = values.get(key);
				logger.debug("  " + key + " = " + value);
			}
		}

		Breadcrumb path = new Breadcrumb();
		path.push("fckawe");
		try {
			List<AbstractCategory> cats = createCategories();
			for (AbstractCategory cat : cats) {
				cat.parse(path, values);
			}
		} finally {
			path.pop();
		}
	}

	/**
	 * Simply returns the path of the configuration file.
	 * 
	 * @return The path of the configuration file.
	 */
	protected String getConfigFilePath() {
		return "/config.xml";
	}

	/**
	 * Returns the configuration category "application".
	 * 
	 * @return The configuration category "application" with its values.
	 */
	public ApplicationCategory getApplicationCat() {
		return applicationCat;
	}

	/**
	 * Returns the configuration category "screen".
	 * 
	 * @return The configuration category "screen" with its values.
	 */
	public ScreenCategory getScreenCat() {
		return screenCat;
	}

	/**
	 * Returns the configuration category "session".
	 * 
	 * @return The configuration category "session" with its values.
	 */
	public SessionCategory getSessionCat() {
		return sessionCat;
	}

	/**
	 * Creates and returns a list of all configuration categories that have to
	 * get parsed. You can extend the list if you like to have an own category.
	 * 
	 * @return A list of all configuration categories.
	 */
	protected List<AbstractCategory> createCategories() {
		List<AbstractCategory> cats = new ArrayList<AbstractCategory>();

		applicationCat = createApplicationCategory();
		cats.add(applicationCat);

		screenCat = createScreenCategory();
		cats.add(screenCat);

		sessionCat = createSessionCategory();
		cats.add(sessionCat);

		return cats;
	}

	/**
	 * Creates and returns a new and empty instance of the "application"
	 * category. You can override this method to extend this category (creating
	 * an object from an own class which extends the default class).
	 * 
	 * @return A new and empty instance of the "application" category.
	 */
	protected ApplicationCategory createApplicationCategory() {
		return new ApplicationCategory();
	}

	/**
	 * Creates and returns a new and empty instance of the "screen" category.
	 * You can override this method to extend this category (creating an object
	 * from an own class which extends the default class).
	 * 
	 * @return A new and empty instance of the "screen" category.
	 */
	protected ScreenCategory createScreenCategory() {
		return new ScreenCategory();
	}

	/**
	 * Creates and returns a new and empty instance of the "session" category.
	 * You can override this method to extend this category (creating an object
	 * from an own class which extends the default class).
	 * 
	 * @return A new and empty instance of the "session" category.
	 */
	protected SessionCategory createSessionCategory() {
		return new SessionCategory();
	}

	/**
	 * Helper method that parses an integer value from the given configuration
	 * path. Can cause a RuntimeException if the value cannot be parsed to a
	 * valid integer.
	 * 
	 * @param values
	 *            All the configuration values.
	 * @param path
	 *            The path to parse the integer value from.
	 * @return The parsed integer value.
	 */
	protected int parseInt(final Map<String, String> values, final String path) {
		String str = values.get(path.toLowerCase());
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			throw new RuntimeException("Cannot parse '" + path + "' (= '" + str
					+ "') to integer!", e);
		}
	}

	/**
	 * Helper method that parses a double value from the given configuration
	 * path. Can cause a RuntimeException if the value cannot be parsed to a
	 * valid double.
	 * 
	 * @param values
	 *            All the configuration values.
	 * @param path
	 *            The path to parse the double value from.
	 * @return The parsed double value.
	 */
	protected double parseDouble(final Map<String, String> values,
			final String path) {
		String str = values.get(path.toLowerCase());
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			throw new RuntimeException("Cannot parse '" + path + "' (= '" + str
					+ "') to double!", e);
		}
	}

	/**
	 * Helper method that parses a boolean value from the given configuration
	 * path.
	 * 
	 * @param values
	 *            All the configuration values.
	 * @param path
	 *            The path to parse the boolean value from.
	 * @return The parsed boolean value.
	 */
	protected boolean parseBoolean(final Map<String, String> values,
			final String path) {
		String str = values.get(path.toLowerCase());
		return Boolean.parseBoolean(str);
	}

	/**
	 * All the categories or sections within the configuration file will be
	 * represented by an AbstractCategory or rather by one of its subclasses.
	 * 
	 * @author fckawe
	 */
	protected abstract class AbstractCategory {

		/**
		 * Parses all the keys of this category out of the values map and puts
		 * it to member variables.
		 * 
		 * @param path
		 *            The breadcrumb/path that represents the parent path of
		 *            this category.
		 * @param values
		 *            All the configuration values in form of a String/String
		 *            map.
		 */
		protected void parse(final Breadcrumb path,
				final Map<String, String> values) {
			path.push(getElementPath());
			try {
				if (logger != null && logger.isDebugEnabled()) {
					logger.debug("Parse configuration category '" + path + "'");
				}
				parseInternal(path, values);
			} finally {
				path.pop();
			}
		}

		/**
		 * This abstract method has to return the name or the path part of this
		 * category like it's specified within the configuration file.
		 * 
		 * @return The name of the category as specified within the
		 *         configuration file.
		 */
		protected abstract String getElementPath();

		/**
		 * This abstract method has to do the parsing work, actually.
		 * 
		 * @param path
		 *            The breadcrumb/path that represents the path of this
		 *            category.
		 * @param values
		 *            All the configuration values in form of a String/String
		 *            map.
		 */
		protected abstract void parseInternal(Breadcrumb path,
				Map<String, String> values);

	}

	/**
	 * The configuration category which contains configuration values about the
	 * application in general.
	 * 
	 * @author fckawe
	 */
	public class ApplicationCategory extends AbstractCategory {

		private String name;

		private String iconPath;

		@Override
		protected String getElementPath() {
			return "application";
		}

		@Override
		protected void parseInternal(final Breadcrumb path,
				final Map<String, String> values) {
			name = values.get(path + ".name");
			iconPath = values.get(path + ".icon.path");
			if (logger != null && logger.isInfoEnabled()) {
				logger.info("Application.name = " + name);
				logger.info("Application.iconPath = " + iconPath);
			}
		}

		/**
		 * Returns the application's name.
		 * 
		 * @return The application's name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns the path to the application's icon.
		 * 
		 * @return The path to the application's icon.
		 */
		public String getIconPath() {
			return iconPath;
		}

	}

	/**
	 * The configuration category which contains configuration values related
	 * with the screen or the display.
	 * 
	 * @author fckawe
	 */
	public class ScreenCategory extends AbstractCategory {

		private int width;

		private int height;

		private double scale;

		private int bitDepth;

		private int refreshRate;

		private boolean fullScreen;

		@Override
		protected String getElementPath() {
			return "screen";
		}

		@Override
		protected void parseInternal(final Breadcrumb path,
				final Map<String, String> values) {
			width = parseInt(values, path + ".width");
			height = parseInt(values, path + ".height");
			scale = parseDouble(values, path + ".scale");
			bitDepth = parseInt(values, path + ".bitdepth");
			refreshRate = parseInt(values, path + ".refreshrate");
			fullScreen = parseBoolean(values, path + ".fullscreen");
			if (logger != null && logger.isInfoEnabled()) {
				logger.info("Screen.width = " + width);
				logger.info("Screen.height = " + height);
				logger.info("Screen.scale = " + scale);
				logger.info("Screen.bitDepth = " + bitDepth);
				logger.info("Screen.refreshRate = " + refreshRate);
				logger.info("Screen.fullScreen = " + fullScreen);
			}
		}

		/**
		 * Returns the aimed width of the application's screen.
		 * 
		 * @return The width of the application's screen.
		 */
		public int getWidth() {
			return width;
		}

		/**
		 * Returns the aimed height of the application's screen.
		 * 
		 * @return The height of the application's screen.
		 */
		public int getHeight() {
			return height;
		}

		/**
		 * Returns a scale factor which has to be used to display the screen.
		 * 
		 * @return The scale factor to display the screen content.
		 */
		public double getScale() {
			return scale;
		}

		/**
		 * Returns the aimed bit depth.
		 * 
		 * @return The bit depth.
		 */
		public int getBitDepth() {
			return bitDepth;
		}

		/**
		 * Returns the aimed refresh rate.
		 * 
		 * @return The refresh rate.
		 */
		public int getRefreshRate() {
			return refreshRate;
		}

		/**
		 * Returns true, if the application should run in fullscreen mode.
		 * 
		 * @return True, if the application shoudl run in fullscreen mode.
		 */
		public boolean isFullScreen() {
			return fullScreen;
		}

	}

	/**
	 * The configuration category which contains configuration values related
	 * with the user session.
	 * 
	 * @author fckawe
	 */
	public class SessionCategory extends AbstractCategory {

		private static final String AUTO_LOCALE = "AUTO";

		private Locale locale;

		@Override
		protected String getElementPath() {
			return "session";
		}

		@Override
		protected void parseInternal(final Breadcrumb path,
				final Map<String, String> values) {
			String str = values.get(path + ".locale");
			parseLocale(str);
		}

		private void parseLocale(final String str) {
			if (str.toUpperCase().equals(AUTO_LOCALE.toUpperCase())) {
				locale = Locale.getDefault();
				if (logger != null && logger.isDebugEnabled()) {
					logger.debug("Setting default locale '" + locale + "'");
				}
			} else {
				Locale languageFallback = null;
				String[] localeParts = str.indexOf('-') >= 0 ? str.split("-")
						: str.split("_");
				String givenLanguage = localeParts[0];
				String givenCountry = localeParts.length >= 2 ? localeParts[1]
						: "";
				for (Locale tmp : Locale.getAvailableLocales()) {
					if (tmp.getLanguage().equals(givenLanguage)) {
						if (tmp.getCountry().equals(givenCountry)) {
							locale = new Locale(givenLanguage, givenCountry);
							break;
						}
						if (languageFallback == null
								|| tmp.getCountry().equals("")) {
							languageFallback = tmp;
						}
					}
				}
				if (locale == null) {
					if (logger != null && logger.isWarnEnabled()) {
						logger.warn("Locale '"
								+ str
								+ "' not found. Setting language fallback locale '"
								+ languageFallback + "'");
					}
					locale = languageFallback;
				}
			}
			if (logger != null && logger.isInfoEnabled()) {
				logger.info("Session.locale = " + locale);
			}
		}

		/**
		 * Returns the locale to use to display language or country specific
		 * strings and values.
		 * 
		 * @return The locale to use to internationalize the application.
		 */
		public Locale getLocale() {
			return locale;
		}

	}

}
