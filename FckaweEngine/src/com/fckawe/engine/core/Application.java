package com.fckawe.engine.core;

import java.util.Observable;
import java.util.Observer;

import com.fckawe.engine.ui.UserInterface;

public class Application implements Observer, Heart.ExitListener {

	private Heart heart;

	private UserInterface ui;

	public Application() {
		Session.newSession(this);
	}

	public void run() {
		init();

		// TODO: Endlosschleife nur zum Test
		boolean ende = false;
		while (!ende) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		heart.requestExit();
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
