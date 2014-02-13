package com.fckawe.engine.game.entity;

import java.util.List;

import com.fckawe.engine.grafix.Bitmap;
import com.fckawe.engine.grafix.Bitmaps;
import com.fckawe.engine.ui.Screen;

public abstract class Entity {
	
	protected Bitmaps bitmaps;
	
	protected Bitmap currentBitmap;
	
	public Entity(final Bitmaps bitmaps) {
		this.bitmaps = bitmaps;
	}
	
	protected String getBitmapsIdPrefix() {
		return getClass().getName();
	}
	
	protected String getGlobalBitmapId(final String id) {
		return getBitmapsIdPrefix() + "." + id;
	}
	
	public abstract List<String> getRequiredBitmapIds();

	public abstract void loadRequiredBitmap(String id);

	public abstract void tick();

	public abstract void render(Screen screen);

}
