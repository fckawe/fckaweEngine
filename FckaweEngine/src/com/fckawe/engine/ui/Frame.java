package com.fckawe.engine.ui;

import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;

import com.fckawe.engine.core.Configuration;
import com.fckawe.engine.core.Session;

/**
 * The frame is the application's main window, which contains the screen canvas.
 * 
 * @author fckawe
 */
public class Frame extends JFrame implements WindowListener {

	private static final long serialVersionUID = -2039970114154356523L;

	private GraphicsDevice device;

	private boolean fullScreen;

	/**
	 * Constructor to create a new application main window.
	 * 
	 * @param ui
	 *            The user interface to create the window for.
	 */
	public Frame(final UserInterface ui) {
		Configuration conf = Session.getSession().getConfiguration();
		String appIcon = conf.getApplicationCat().getIconPath();
		if (appIcon != null) {
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image img = tk.createImage(ClassLoader.getSystemResource(appIcon));
			setIconImage(img);
		}

		fullScreen = conf.getScreenCat().isFullScreen();

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(ui, BorderLayout.CENTER);
		setContentPane(panel);

		GraphicsEnvironment environment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();

		if (fullScreen) {
			int width = conf.getScreenCat().getWidth();
			int height = conf.getScreenCat().getHeight();
			int bitDepth = conf.getScreenCat().getBitDepth();
			int refreshRate = conf.getScreenCat().getRefreshRate();
			DisplayMode displayMode = new DisplayMode(width, height, bitDepth,
					refreshRate);

			setUndecorated(true);
			setResizable(false);
			device.setFullScreenWindow(this);

			if (displayMode != null && device.isDisplayChangeSupported()) {
				try {
					device.setDisplayMode(displayMode);
				} catch (IllegalArgumentException e) {
					logDisplayModeError(displayMode, e);
					fullScreen = false;
				}
			} else {
				fullScreen = false;
			}
		}

		if (!fullScreen) {
			pack();
			setLocationRelativeTo(null);
			setResizable(false);
		}

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		ui.requestFocus();
	}

	/**
	 * Used to log an error message, if the configured display mode can't be set
	 * with the current display device. Besides the error message a few hints
	 * get logged to which valid display mode the configuration could be
	 * changed.
	 * 
	 * @param displayMode
	 *            The illegal display mode, that was tried to be set.
	 * @param e
	 *            The exception that has occured.
	 */
	private void logDisplayModeError(final DisplayMode displayMode,
			final Throwable e) {
		Logger logger = Session.getSession().getGrafixLogger();

		if (logger.isErrorEnabled()) {
			String modeStr = displayModeToString(displayMode, false);
			logger.error("Illegal display mode (" + modeStr
					+ ") for this device!", e);
		}

		List<DisplayMode> sameResModes = new ArrayList<DisplayMode>();
		List<DisplayMode> otherModes = new ArrayList<DisplayMode>();
		int setWidth = displayMode.getWidth();
		int setHeight = displayMode.getHeight();

		DisplayMode[] compatibleModes = device.getDisplayModes();
		for (DisplayMode compatibleMode : compatibleModes) {
			int width = compatibleMode.getWidth();
			int height = compatibleMode.getHeight();

			if (width == setWidth && height == setHeight) {
				sameResModes.add(compatibleMode);
			} else {
				otherModes.add(compatibleMode);
			}
		}

		if (logger.isInfoEnabled()) {
			DisplayModeSorter sorter = new DisplayModeSorter();
			if (sameResModes.isEmpty()) {
				logger.info("Set one of these completely different modes:");
			} else {
				logger.info("Set bit depth and refresh rate to one of these:");
				Collections.sort(sameResModes, sorter);
				List<String> modeStrs = new ArrayList<String>();
				for (DisplayMode sameResMode : sameResModes) {
					String modeStr = displayModeToString(sameResMode, true);
					if (!modeStrs.contains(modeStr)) {
						modeStrs.add(modeStr);
						logger.info("  " + modeStr);
					}
				}
				logger.info("Or set one of these completely different modes:");
			}
			Collections.sort(otherModes, sorter);
			List<String> modeStrs = new ArrayList<String>();
			for (DisplayMode otherMode : otherModes) {
				String modeStr = displayModeToString(otherMode, false);
				if (!modeStrs.contains(modeStr)) {
					modeStrs.add(modeStr);
					logger.info("  " + modeStr);
				}
			}
		}
	}

	/**
	 * Returns the string represantation of the given display mode.
	 * 
	 * @param displayMode
	 *            The display mode to get the string representation for.
	 * @param withoutRes
	 *            If true, the resolution info is omitted in the string
	 *            representation.
	 * @return The string representation for the given display mode.
	 */
	protected String displayModeToString(final DisplayMode displayMode,
			final boolean withoutRes) {
		int width = displayMode.getWidth();
		int height = displayMode.getHeight();
		int bitDepth = displayMode.getBitDepth();
		int refreshRate = displayMode.getRefreshRate();

		StringBuilder sb = new StringBuilder();
		if (!withoutRes) {
			sb.append(width + "x" + height);
			sb.append("/");
		}
		sb.append(bitDepth + "bit");
		sb.append("/");

		if (refreshRate == 0) {
			sb.append("Auto");
		} else {
			sb.append(refreshRate + "Hz");
		}

		return sb.toString();
	}

	/**
	 * Closes this main window and, if the application ran in fullscreen mode,
	 * resets the fullscreen mode.
	 */
	public void close() {
		if (fullScreen) {
			Window window = device.getFullScreenWindow();
			if (window != null) {
				window.dispose();
			}
			device.setFullScreenWindow(null);
		} else {
			setVisible(false);
			dispose();
		}
	}

	@Override
	public void windowActivated(final WindowEvent e) {
	}

	@Override
	public void windowClosed(final WindowEvent e) {
	}

	@Override
	public void windowClosing(final WindowEvent e) {
		Session.getSession().getHeart().requestStop();
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
	}

	@Override
	public void windowIconified(final WindowEvent e) {
	}

	@Override
	public void windowOpened(final WindowEvent e) {
	}

	/**
	 * Comparator for display modes to help to sort them in descending order.
	 * 
	 * @author fckawe
	 */
	private class DisplayModeSorter implements Comparator<DisplayMode> {

		@Override
		public int compare(final DisplayMode m1, final DisplayMode m2) {
			int diff;

			int width1 = m1.getWidth();
			int width2 = m2.getWidth();
			diff = width2 - width1;
			if (diff == 0) {
				int height1 = m1.getHeight();
				int height2 = m2.getHeight();
				diff = height2 - height1;
				if (diff == 0) {
					int bitDepth1 = m1.getBitDepth();
					int bitDepth2 = m2.getBitDepth();
					diff = bitDepth2 - bitDepth1;
					if (diff == 0) {
						int refreshRate1 = m1.getRefreshRate();
						int refreshRate2 = m2.getRefreshRate();
						diff = refreshRate2 - refreshRate1;
					}
				}
			}
			return diff;
		}

	}

}
