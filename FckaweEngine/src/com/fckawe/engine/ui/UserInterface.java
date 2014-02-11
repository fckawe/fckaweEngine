package com.fckawe.engine.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Observable;
import java.util.Observer;

import com.fckawe.engine.core.Configuration;
import com.fckawe.engine.core.Heart;
import com.fckawe.engine.core.Session;
import com.fckawe.engine.grafix.BitmapsLogic;
import com.fckawe.engine.grafix.Font;
import com.fckawe.engine.grafix.Fonts;
import com.fckawe.engine.grafix.FontsLogic;

public class UserInterface extends Canvas implements Observer {

	private static final long serialVersionUID = -5961759679622983414L;

	// TODO:
	// protected GamePart currentGamePart;

	private Frame frame;
	private final Screen screen;
	private double screenScale;
	private Dimension uiDimension;
	private double framesPerSecond;

	// TODO: private InputHandler inputHandler;

	private Fonts fonts;
	private FontsLogic fontsLogic;
	private BitmapsLogic bitmapsLogic;

	public UserInterface() {
		Dimension uiDimension = getUiDimension();
		setPreferredSize(uiDimension);
		setMinimumSize(uiDimension);
		setMaximumSize(uiDimension);
		setBackground(Color.BLACK);

		Dimension screenDimension = getScreenDimension(uiDimension);
		screen = new Screen(screenDimension);

		initGrafix();
	}

	protected void initGrafix() {
		fontsLogic = createFontsLogic();
		bitmapsLogic = createBitmapsLogic();
		fonts = createFonts();
		fonts.loadFonts();
	}

	protected Fonts createFonts() {
		return new Fonts(this);
	}

	protected FontsLogic createFontsLogic() {
		return new FontsLogic(this);
	}

	protected BitmapsLogic createBitmapsLogic() {
		return new BitmapsLogic();
	}

	public void start() {
		frame = new Frame(this);
		Session session = Session.getSession();
		Configuration cfg = session.getConfiguration();
		frame.setTitle(cfg.getApplicationCat().getName() + " [powered by "
				+ session.getEngineName() + "]");
		// TODO: inputHandler = new InputHandler();
		// TODO: addKeyListener(inputHandler);
		frame.setVisible(true);
	}

	public void exit() {
		frame.setVisible(false);
		frame.dispose();
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

		// TODO:
		// if (currentGamePart != null) {
		// currentGamePart.render(screen);
		// }
		//
		fontsLogic.draw(screen, "FPS:" + (int) framesPerSecond, 10, 10);

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
		} else {
			Graphics g = bs.getDrawGraphics();
			render(g);
		}
	}

	private synchronized void render(final Graphics g) {
		int width = (int) uiDimension.getWidth();
		int height = (int) uiDimension.getHeight();

		int trgtWidth = (int) (getWidth() - ((width * screenScale)) / 2);
		int trgtHeight = (int) (getHeight() - ((height * screenScale)) / 2);

		g.translate(trgtWidth, trgtHeight);
		g.clipRect(0, 0, width, height);

		g.drawImage(screen.getImage(), 0, 0, width, height, null);
	}

	private void showRenderedImage() {
		BufferStrategy bs = getBufferStrategy();
		if (bs != null) {
			bs.show();
		}
	}

	// TODO:
	// public void setCurrentGamePart(final GamePart currentGamePart) {
	// this.currentGamePart = currentGamePart;
	// }

	public Screen getScreen() {
		return screen;
	}

	public void tick() {
		// TODO: inputHandler.tick();
	}

	public Font getFont(final String fontName) {
		return fonts.getFont(fontName);
	}

	public BitmapsLogic getBitmapsLogic() {
		return bitmapsLogic;
	}

}