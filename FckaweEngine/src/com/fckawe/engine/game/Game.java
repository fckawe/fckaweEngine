package com.fckawe.engine.game;

import com.fckawe.engine.core.Session;
import com.fckawe.engine.game.demo.BouncingBallDemo;
import com.fckawe.engine.input.InputHandler;
import com.fckawe.engine.ui.Screen;
import com.fckawe.engine.ui.UserInterface;

/**
 * The game's main logic.
 * 
 * @author fckawe
 */
public class Game {

	private UserInterface ui;

	private Module module;

	/**
	 * Constructor to create a new game.
	 * 
	 * @param ui
	 *            The user interface.
	 */
	public Game(final UserInterface ui) {
		this.ui = ui;
		Module module = getStartingModule();
		setModule(module);
	}

	/**
	 * Returns or defines the module with which the game should start.
	 * 
	 * @return The module with which the game should start.
	 */
	protected Module getStartingModule() {
		return new BouncingBallDemo(this);
	}

	/**
	 * Returns the currently running module.
	 * 
	 * @return The currently running module.
	 */
	public Module getCurrentModule() {
		return module;
	}

	/**
	 * Performs a tick on the currently running module.
	 * 
	 * @param inputHandler
	 *            The input handler.
	 * @param elapsedTime
	 *            The time with which the statistical FPS can be calculated.
	 */
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

	/**
	 * Renders the currently running module on the given screen.
	 * 
	 * @param screen
	 *            The application's screen.
	 */
	public void render(final Screen screen) {
		if (module != null) {
			module.render(screen);
		}
	}

	/**
	 * Stops the currently running module.
	 */
	public void stop() {
		if (module != null) {
			module.setEnd(null);
		}
	}

	/**
	 * Switches to another module.
	 * 
	 * @param module
	 *            The module to switch to.
	 */
	public void setModule(final Module module) {
		if (this.module != null) {
			this.module.end();
		}
		if (module == null) {
			this.module = null;
		} else if (this.module != module) {
			module.load();
			this.module = module;
		}
	}

	/**
	 * Returns the user interface.
	 * 
	 * @return The user interface.
	 */
	public UserInterface getUserInterface() {
		return ui;
	}

}
