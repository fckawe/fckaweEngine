package com.fckawe.engine.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.fckawe.engine.core.Session;
import com.fckawe.engine.game.entity.Entity;
import com.fckawe.engine.grafix.Bitmaps;
import com.fckawe.engine.input.InputHandler;
import com.fckawe.engine.ui.Screen;

/**
 * A module is a game part (menu, level or something like that).
 * 
 * @author fckawe
 */
public abstract class Module {

	protected Game game;

	private boolean ended;

	private Module continueWithModule;

	private Logger logger;

	private String name;

	protected Bitmaps bitmaps;

	protected final Map<String, Entity> entities;

	/**
	 * Constructor to create a new Module.
	 * 
	 * @param game
	 *            The game of which this module is a part.
	 */
	public Module(final Game game) {
		this.game = game;
		name = getModuleName();
		Session session = Session.getSession();
		logger = session == null ? null : session.getMainLogger();
		bitmaps = game.getUserInterface().getBitmaps();
		entities = new HashMap<String, Entity>();
	}

	/**
	 * Returns the name of this module.
	 * 
	 * @return The name of this module.
	 */
	protected abstract String getModuleName();

	/**
	 * Loads this module and all its required resources.
	 */
	public void load() {
		if (logger != null && logger.isInfoEnabled()) {
			logger.info("Load resources for module '{}'.", name);
		}

		entities.clear();
		loadEntities();

		for (String entityId : entities.keySet()) {
			Entity entity = entities.get(entityId);
			List<String> bmpIds = entity.getRequiredBitmapIds();
			for (String bmpId : bmpIds) {
				String globalId = entity.getGlobalBitmapId(bmpId);
				if (!bitmaps.isLoaded(globalId)) {
					entity.loadRequiredBitmap(bmpId, globalId);
				}
			}
			entity.init();
		}
	}

	/**
	 * Loads all required entities.
	 */
	protected abstract void loadEntities();

	/**
	 * Unloads this module and all its required resources.
	 */
	public void unload() {
		if (logger != null && logger.isInfoEnabled()) {
			logger.info("Unload resources for module '{}'.", name);
		}

		for (String entityId : entities.keySet()) {
			Entity entity = entities.get(entityId);
			List<String> bmpIds = entity.getRequiredBitmapIds();
			for (String bmpId : bmpIds) {
				String globalId = entity.getGlobalBitmapId(bmpId);
				if (bitmaps.isLoaded(globalId)) {
					bitmaps.remove(globalId);
				}
			}
		}
	}

	/**
	 * Performs a tick on this module.
	 * 
	 * @param inputHandler
	 *            The input handler.
	 * @param elapsedTime
	 *            The time with which the statistical FPS can be calculated.
	 */
	public void tick(final InputHandler inputHandler, final long elapsedTime) {
		for (Entity entity : entities.values()) {
			entity.tick(inputHandler, elapsedTime);
		}
	}

	/**
	 * Renders the current state of this module on the given screen.
	 * 
	 * @param screen
	 *            The application's screen.
	 */
	public void render(final Screen screen) {
		for (Entity entity : entities.values()) {
			entity.render(screen);
		}
	}

	/**
	 * Tell the module that it has to end.
	 * 
	 * @param continueWithModule
	 *            Specifies the module with which the game should continue.
	 */
	protected void setEnd(final Module continueWithModule) {
		ended = true;
		this.continueWithModule = continueWithModule;
	}

	/**
	 * Ends this module.
	 */
	protected void end() {
		unload();
	}

	/**
	 * Returns if the end of this module is requested.
	 * 
	 * @return True, if the end of this module is requested.
	 */
	public boolean isEnded() {
		return ended;
	}

	/**
	 * Returns the module with which the game should continue.
	 * 
	 * @return The module with which the game should continue.
	 */
	public Module getContinueWithModule() {
		return continueWithModule;
	}

	/**
	 * Returns the map with all the entities used by this module.
	 * 
	 * @return The map with all the entities used by this module.
	 */
	public Map<String, Entity> getEntities() {
		return entities;
	}

}
