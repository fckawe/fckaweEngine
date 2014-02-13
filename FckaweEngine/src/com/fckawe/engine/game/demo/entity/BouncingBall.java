package com.fckawe.engine.game.demo.entity;

import java.util.ArrayList;
import java.util.List;

import com.fckawe.engine.game.entity.Entity;
import com.fckawe.engine.grafix.Bitmaps;
import com.fckawe.engine.ui.Screen;

public class BouncingBall extends Entity {

	private static final String BMP_BOUNCING_BALL = "BouncingBall";
	
	private int testPosY;
	
	private int testDelay;
	
	public BouncingBall(Bitmaps bitmaps) {
		super(bitmaps);
		testPosY = 30;
		testDelay = 0;
	}

	@Override
	public List<String> getRequiredBitmapIds() {
		List<String> ids = new ArrayList<String>();
		ids.add(BMP_BOUNCING_BALL);
		return ids;
	}

	@Override
	public void loadRequiredBitmap(final String id) {
		String globalId = getGlobalBitmapId(id);
		if(id.equals(BMP_BOUNCING_BALL)) {
			bitmaps.loadBitmap(globalId, "/images/demo/bouncingball.png");
		}
	}

	@Override
	public void tick() {
		if(currentBitmap == null) {
			String globalId = getGlobalBitmapId(BMP_BOUNCING_BALL);
			currentBitmap = bitmaps.getBitmap(globalId);
		}
		
		testDelay++;
		if(testDelay > 0) {
			testPosY += 3;
			testDelay = 0;
		}
	}

	@Override
	public void render(final Screen screen) {
		screen.blit(currentBitmap, 100, testPosY);
	}

}
