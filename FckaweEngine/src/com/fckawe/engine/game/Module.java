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

public abstract class Module {

	protected Game game;

	private boolean ended;

	private Module continueWithModule;

	private Logger logger;

	private String name;

	protected Bitmaps bitmaps;

	protected final Map<String, Entity> entities;

	public Module(final Game game) {
		this.game = game;
		name = getModuleName();
		Session session = Session.getSession();
		logger = session == null ? null : session.getMainLogger();
		bitmaps = game.getUserInterface().getBitmaps();
		entities = new HashMap<String, Entity>();
	}

	protected abstract String getModuleName();

	protected abstract void loadEntities();

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
				if(!bitmaps.isLoaded(globalId)) {
					entity.loadRequiredBitmap(bmpId, globalId);
				}
			}
		}
	}

	public void unload() {
		if (logger != null && logger.isInfoEnabled()) {
			logger.info("Unload resources for module '{}'.", name);
		}

		for(String entityId : entities.keySet()) {
			Entity entity = entities.get(entityId);
			List<String> bmpIds = entity.getRequiredBitmapIds();
			for(String bmpId : bmpIds) {
				String globalId = entity.getGlobalBitmapId(bmpId);
				if(bitmaps.isLoaded(globalId)) {
					bitmaps.remove(globalId);
				}
			}
		}
	}

	public void tick(final InputHandler inputHandler, final long elapsedTime) {
		for (Entity entity : entities.values()) {
			entity.tick(inputHandler, elapsedTime);
		}
	}

	public void render(final Screen screen) {
		for (Entity entity : entities.values()) {
			entity.render(screen);
		}
	}
	
	protected void setEnd(final Module continueWithModule) {
		ended = true;
		this.continueWithModule = continueWithModule;
	}
	
	protected void end() {
		unload();
	}

	public boolean isEnded() {
		return ended;
	}

	public Module getContinueWithModule() {
		return continueWithModule;
	}

}
