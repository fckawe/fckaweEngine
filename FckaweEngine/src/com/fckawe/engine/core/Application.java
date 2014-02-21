package com.fckawe.engine.core;

import java.util.Observable;
import java.util.Observer;

import com.fckawe.engine.ui.UserInterface;

/**
 * The main application to use to initialize all required parts and startup the
 * game.
 * 
 * @author fckawe
 */
public class Application implements Observer, Heart.StopListener {

	private Heart heart;

	private UserInterface ui;

	/**
	 * The default constructor to initialize the application.
	 */
	public Application() {
		Session.newSession(this);
	}

	/**
	 * Start the application.
	 */
	public void start() {
		init();
	}

	/**
	 * Initializes all required parts (e.g. the heart and the user interface).
	 */
	protected void init() {
		Session.getSession().getHeart().start();
		heart = Session.getSession().getHeart();
		heart.addObserver(this);
		initUserInterface();
	}

	/**
	 * Initializes the user interface.
	 */
	protected void initUserInterface() {
		ui = Session.getSession().getFckaweFactory().newUserInterface();
		ui.start();
		heart.addObserver(ui);
	}

	/**
	 * Returns the user interface.
	 * 
	 * @return The user interface.
	 */
	public UserInterface getUserInterface() {
		return ui;
	}

	@Override
	public void heartStopping() {
		ui.stop();
	}

	@Override
	public void update(final Observable observable, final Object data) {
		// TODO: currently nothing to to...
		// if (observable == Session.getSession().getHeart()) {
		// Heart.Event event = null;
		// if (data instanceof Heart.Event) {
		// event = (Heart.Event) data;
		// } else if (data instanceof Heart.EventData) {
		// Heart.EventData o = (Heart.EventData) data;
		// event = o.getEvent();
		// } else {
		// throw new IllegalStateException("Unexpected data type: "
		// + data.getClass().getName());
		// }
		// }
	}

}
