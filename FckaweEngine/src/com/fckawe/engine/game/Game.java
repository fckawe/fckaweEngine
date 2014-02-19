package com.fckawe.engine.game;

import com.fckawe.engine.core.Session;
import com.fckawe.engine.game.demo.BouncingBallDemo;
import com.fckawe.engine.input.InputHandler;
import com.fckawe.engine.ui.Screen;
import com.fckawe.engine.ui.UserInterface;

public class Game {

	private UserInterface ui;

	private Module module;

	public Game(final UserInterface ui) {
		this.ui = ui;
		Module module = getStartingModule();
		setModule(module);
	}

	protected Module getStartingModule() {
		return new BouncingBallDemo(this);
	}

	public Module getCurrentModule() {
		return module;
	}

	public void tick(final InputHandler inputHandler, final long elapsedTime) {
		if (module != null) {
			module.tick(inputHandler, elapsedTime);
			if (module.isEnded()) {
				setModule(module.getContinueWithModule());
			}
		}
		if (module == null) {
			Session.getSession().getHeart().requestStop();
		}
	}

	public void render(final Screen screen) {
		if (module != null) {
			module.render(screen);
		}
	}

	public void stop() {
		if (module != null) {
			module.setEnd(null);
		}
	}

	public void setModule(final Module module) {
		if(this.module != null) {
			this.module.end();
		}
		if (module == null) {
			this.module = null;
		} else if (this.module != module) {
			module.load();
			this.module = module;
		}
	}

	public UserInterface getUserInterface() {
		return ui;
	}

}
