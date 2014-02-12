package com.fckawe.engine.utils;

import com.fckawe.engine.core.Configuration;
import com.fckawe.engine.core.Heart;
import com.fckawe.engine.ui.UserInterface;

/**
 * The FckaweFactory should be used to instantiate important parts of the
 * application to make it possible to replace these parts by extending classes.
 * 
 * @author fckawe
 */
public class FckaweFactory {

	/**
	 * Creates and returns a new instance of this factory.
	 * 
	 * @return A newly created FckaweFactory instance.
	 */
	public static FckaweFactory createInstance() {
		String impl = System.getProperty("fckaweFactory",
				FckaweFactory.class.getName());

		try {
			@SuppressWarnings("unchecked")
			Class<FckaweFactory> clazz = (Class<FckaweFactory>) Class.forName(
					impl).asSubclass(FckaweFactory.class);
			return clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"Cannot find overwriting fckaweFactory class '" + impl
							+ "'!", e);
		} catch (InstantiationException e) {
			throw new RuntimeException(
					"Cannot instantiate overwriting fckaweFactory from class '"
							+ impl + "'!", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(
					"Cannot instantiate overwriting fckaweFactory from class '"
							+ impl + "'!", e);
		}
	}

	/**
	 * Creates and returns a new LoggerFactory instance.
	 * 
	 * @return A newly created LoggerFactory instance.
	 */
	public LoggerFactory newLoggerFactory() {
		return new LoggerFactory();
	}

	/**
	 * Creates and returns a new Configuration instance.
	 * 
	 * @return A newly created Configuration instance.
	 */
	public Configuration newConfiguration() {
		return new Configuration();
	}

	/**
	 * Creates and returns a new NLS instance.
	 * 
	 * @return A newly created NLS instance.
	 */
	public NLS newNLS() {
		return new NLS();
	}

	/**
	 * Creates and returns a new Heart instance.
	 * 
	 * @return A newly created Heart instance.
	 */
	public Heart newHeart(final Heart.ExitListener exitListener) {
		return new Heart(exitListener);
	}

	/**
	 * Creates and returns a new UserInterface instance.
	 * 
	 * @return A newly created UserInterface instance.
	 */
	public UserInterface newUserInterface() {
		return new UserInterface();
	}

}
