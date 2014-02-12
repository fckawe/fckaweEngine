package com.fckawe.engine.core;

import java.util.Observable;
import java.util.Observer;

import com.fckawe.engine.ui.UserInterface;

// TODO: comment class and methods
public class Application implements Observer, Heart.ExitListener {

	private Heart heart;

	private UserInterface ui;

	public Application() {
		Session.newSession(this);
	}

	public void start() {
		init();
	}

	protected void init() {
		Session.getSession().getHeart().start();
		heart = Session.getSession().getHeart();
		heart.addObserver(this);
		initUserInterface();
	}

	protected void initUserInterface() {
		ui = Session.getSession().getFckaweFactory().newUserInterface();
		ui.start();
		heart.addObserver(ui);
	}

	public UserInterface getUserInterface() {
		return ui;
	}

	@Override
	public void exit() {
		ui.exit();
	}

	@Override
	public void update(final Observable observable, final Object data) {
		if (observable == Session.getSession().getHeart()) {
			Heart.Event event = null;
			if (data instanceof Heart.Event) {
				event = (Heart.Event) data;
			} else if (data instanceof Heart.EventData) {
				Heart.EventData o = (Heart.EventData) data;
				event = o.getEvent();
			} else {
				throw new IllegalStateException("Unexpected data type: "
						+ data.getClass().getName());
			}

			// TODO: tick in UserInterface verschoben. Kann Application als
			// Observable jetzt raus?! Kommt drauf an, ob hier wieder ähnlich
			// wie mit den GameParts etwas reinkommen wird.

			switch (event) {
			case TICK:
				// TODO: tick
				// if (currentGamePart != null) {
				// ui.tick();
				// currentGamePart.tick();
				// }
				break;
			default:
				// do nothing
				break;
			}
		}
	}

}
