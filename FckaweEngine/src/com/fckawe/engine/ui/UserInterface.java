package com.fckawe.engine.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.Observable;
import java.util.Observer;

import com.fckawe.engine.core.Configuration;
import com.fckawe.engine.core.Heart;
import com.fckawe.engine.core.Session;
import com.fckawe.engine.game.Game;
import com.fckawe.engine.grafix.Bitmaps;
import com.fckawe.engine.grafix.Font;
import com.fckawe.engine.grafix.Fonts;
import com.fckawe.engine.input.InputHandler;
import com.fckawe.engine.physics.Position;

public class UserInterface extends Canvas implements Observer {

	private static final long serialVersionUID = -5961759679622983414L;

	protected Game game;

	private Frame frame;
	private final Screen screen;
	private int screenTranslatedX;
	private int screenTranslatedY;
	private double screenScale;
	private Dimension uiDimension;
	private double framesPerSecond;

	private InputHandler inputHandler;

	private Fonts fonts;
	private Bitmaps bitmaps;

	private boolean showFps;
	private Position showFpsPosition;

	public UserInterface() {
		Dimension uiDimension = getUiDimension();
		setPreferredSize(uiDimension);
		setMinimumSize(uiDimension);
		setMaximumSize(uiDimension);
		setSize(uiDimension);
		setBackground(Color.BLACK);

		Dimension screenDimension = getScreenDimension(uiDimension);
		screenTranslatedX = (int) (getWidth() - uiDimension.getWidth()) / 2;
		screenTranslatedY = (int) (getHeight() - uiDimension.getHeight()) / 2;
		screen = new Screen(screenDimension);

		initGrafix();

		game = Session.getSession().getFckaweFactory().newGame(this);
	}

	protected void initGrafix() {
		bitmaps = createBitmaps();
		fonts = createFonts();
		fonts.loadFonts();
	}

	protected Fonts createFonts() {
		return new Fonts(this);
	}

	protected Bitmaps createBitmaps() {
		return new Bitmaps();
	}

	protected InputHandler createInputHandler() {
		return new InputHandler();
	}

	public void start() {
		frame = new Frame(this);
		Session session = Session.getSession();
		Configuration cfg = session.getConfiguration();
		showFps = cfg.getScreenCat().isShowFps();
		showFpsPosition = new Position(5, 5);
		frame.setTitle(cfg.getApplicationCat().getName() + " [powered by "
				+ session.getEngineName() + "]");
		inputHandler = createInputHandler();
		addKeyListener(inputHandler);
		frame.setVisible(true);
	}

	public void stop() {
		game.stop();
		frame.close();
		Session.getSession().getMainLogger().info("User interface stopped.");
	}

	private Dimension getUiDimension() {
		if (uiDimension == null) {
			Configuration cfg = Session.getSession().getConfiguration();
			int width = cfg.getScreenCat().getWidth();
			int height = cfg.getScreenCat().getHeight();
			uiDimension = new Dimension(width, height);
		}
		return uiDimension;
	}

	private Dimension getScreenDimension(final Dimension uiDimension) {
		Configuration cfg = Session.getSession().getConfiguration();
		screenScale = cfg.getScreenCat().getScale();
		if (screenScale == 1.0) {
			return uiDimension;
		}
		double width = uiDimension.getWidth() / screenScale;
		double height = uiDimension.getHeight() / screenScale;
		Dimension screenDimension = new Dimension((int) width, (int) height);
		return screenDimension;
	}

	@Override
	public void update(final Observable observable, final Object data) {
		if (observable == Session.getSession().getHeart()) {
			Heart.Event event = null;
			Object eventData = null;
			if (data instanceof Heart.Event) {
				event = (Heart.Event) data;
			} else if (data instanceof Heart.EventData) {
				Heart.EventData o = (Heart.EventData) data;
				event = o.getEvent();
				eventData = o.getData();
			} else {
				throw new IllegalStateException("Unexpected data type: "
						+ data.getClass().getName());
			}

			switch (event) {
			case HEART_START:
				setFocusTraversalKeysEnabled(false);
				requestFocus();
				break;
			case TICK:
				long elapsedTime = (Long) eventData;
				tick(elapsedTime);
				break;
			case RENDER:
				render();
				break;
			case SHOW:
				showRenderedImage();
				break;
			case FPS_UPDATED:
				framesPerSecond = (Double) eventData;
				break;
			default:
				// do nothing
				break;
			}
		}
	}

	protected void render() {
		screen.clear(0);

		game.render(screen);

		if (showFps) {
			fonts.draw(screen, "FPS:" + (int) framesPerSecond, showFpsPosition);
		}

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
		} else {
			Graphics g = bs.getDrawGraphics();
			render(g);
			g.dispose();
		}
	}

	private synchronized void render(final Graphics g) {
		int width = (int) getWidth();
		int height = (int) getHeight();

		g.translate(screenTranslatedX, screenTranslatedY);
		g.clipRect(0, 0, width, height);

		g.drawImage(screen.getImage(), 0, 0, width, height, null);
	}

	private void showRenderedImage() {
		BufferStrategy bs = getBufferStrategy();
		if (bs != null && !bs.contentsLost()) {
			bs.show();
		}
		Toolkit.getDefaultToolkit().sync();
	}

	public Screen getScreen() {
		return screen;
	}

	public void tick(final long elapsedTime) {
		if (inputHandler != null) {
			inputHandler.tick();
		}
		game.tick(inputHandler, elapsedTime);
	}
	
	public Fonts getFonts() {
		return fonts;
	}

	public Font getFont(final String fontName) {
		return fonts.getFont(fontName);
	}

	public Bitmaps getBitmaps() {
		return bitmaps;
	}

	public InputHandler getInputHandler() {
		return inputHandler;
	}

	public double getCurrentFps() {
		return framesPerSecond;
	}

}
