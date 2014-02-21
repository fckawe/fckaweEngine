package com.fckawe.engine.core;

import java.util.Observable;
import java.util.Observer;

/**
 * The heart is the impulse generator of the game. It sends signals to the other
 * parts when ticks/updates or rendering has to be performed. It is also
 * responsible to process an constant value (as constant as possible) of frames
 * per second.
 * 
 * @author fckawe
 */
public class Heart extends Observable implements Runnable {

	/**
	 * The event enum defines all events, that the heart can fire to all its
	 * observers.
	 * 
	 * @author fckawe
	 */
	public enum Event {
		HEART_START, TICK, RENDER, SHOW, FPS_UPDATED
	};

	// desired fps
	private final static int TARGET_FPS = 50;

	// maximum number of frames to be skipped (in order to catch up)
	private final static int MAX_FRAME_SKIPS = 5;

	// the frame period
	private final static int FRAME_PERIOD = 1000000000 / TARGET_FPS;

	// interval for performing FPS-statistics (value in ms)
	private final static int STAT_INTERVAL = 1000;

	// FPS-statistics average will be calculated over the last x frames
	private final static int FPS_HISTORY_NR = 10;

	// last time the status was stored
	private long lastStatusStore = 0;

	// the status time counter
	private long statusIntervalTimer = 0l;

	// number of frames skipped since the game started
	private long totalFramesSkipped = 0l;

	// number of frames skipped in a store cycle
	private long framesSkippedPerStatCycle = 0l;

	// number of rendered frames in an interval
	private int frameCountPerStatCycle = 0;

	// the last FPS values
	private double fpsStore[];

	// the number of times the stat has been read
	private long statsCount = 0;

	// the average FPS since the game started
	private double averageFps = 0.0;

	private final StopListener stopListener;

	private boolean exitRequested;

	private boolean hasNewObserver;

	/**
	 * Constructor to create a new Heart, giving a StopListener that gets
	 * informed if the heart will be stopped.
	 * 
	 * @param stopListener
	 *            The StopListener that gets informed if the heart will be
	 *            stopped.
	 */
	public Heart(final StopListener stopListener) {
		this.stopListener = stopListener;
		exitRequested = false;
	}

	/**
	 * Starts the heart within its own thread.
	 */
	public void start() {
		Thread thread = new Thread(this, Session.getSession().getEngineName()
				+ ":Heart");
		thread.start();
	}

	@Override
	public void run() {
		signalEvent(Event.HEART_START);
		Session.getSession().getHeartLogger().info("Heart started.");

		initTimingElements();
		long currentTime = System.currentTimeMillis();
		long beginTime; // the time when the cycle began
		long timeDiff; // the time it took for the cycle to execute
		int sleepTime = 0; // ms to sleep (< 0 if we have to catch up)
		int framesSkipped; // number of frames being skipped

		while (!exitRequested) {
			beginTime = System.nanoTime();
			framesSkipped = 0;

			long elapsedTime = System.currentTimeMillis() - currentTime;
			currentTime += elapsedTime;

			signalEvent(Event.TICK, elapsedTime);
			signalEvent(Event.RENDER);
			signalEvent(Event.SHOW);

			timeDiff = System.nanoTime() - beginTime;
			sleepTime = (int) ((FRAME_PERIOD - timeDiff) / 1000000);

			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					Session.getSession().getHeartLogger()
							.warn("Sleep interrupted.", e);
				}
			}

			while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
				elapsedTime = System.currentTimeMillis() - currentTime;
				currentTime += elapsedTime;
				// we need to catch up: update without rendering
				signalEvent(Event.TICK, elapsedTime);
				// add frame period to check if in next frame
				sleepTime += FRAME_PERIOD;
				framesSkipped++;
			}

			framesSkippedPerStatCycle += framesSkipped;

			if (framesSkipped > 0) {
				Session.getSession()
						.getHeartLogger()
						.debug("Skipped {} frames (total {}).", framesSkipped,
								totalFramesSkipped + framesSkippedPerStatCycle);
			}

			// calling the routine to store the gathered statistics
			updateStats();

			hasNewObserver = false;
			Thread.yield();
		}

		if (stopListener != null) {
			stopListener.heartStopping();
		}

		Session.getSession().getHeartLogger().info("Heart stopped.");
	}

	/**
	 * Request the heart to stop.
	 */
	public void requestStop() {
		exitRequested = true;
	}

	private void initTimingElements() {
		fpsStore = new double[FPS_HISTORY_NR];
		for (int i = 0; i < FPS_HISTORY_NR; i++) {
			fpsStore[i] = 0.0;
		}
		Session.getSession().getHeartLogger()
				.debug("Timing elements for stats initialised.");
	}

	/**
	 * Update timing statistics. This method is called every cycle, but it
	 * calculates the statistics only every STAT_INTERVAL milliseconds.
	 */
	private void updateStats() {
		double averageFpsBefore = averageFps;

		frameCountPerStatCycle++;

		statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);

		if (statsCount + 1 < FPS_HISTORY_NR) {
			averageFps = TARGET_FPS;
		}

		if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {
			double actualFps = (frameCountPerStatCycle / (STAT_INTERVAL / 1000));

			fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;

			statsCount++;

			// calculate the FPS average over the last FPS_HISTORY_NR cycles
			if (statsCount >= FPS_HISTORY_NR) {
				double totalFps = 0.0;
				for (int i = 0; i < FPS_HISTORY_NR; i++) {
					totalFps += fpsStore[i];
				}
				averageFps = totalFps / FPS_HISTORY_NR;
			}

			totalFramesSkipped += framesSkippedPerStatCycle;
			framesSkippedPerStatCycle = 0;
			statusIntervalTimer = 0;
			frameCountPerStatCycle = 0;

			statusIntervalTimer = System.currentTimeMillis();
			lastStatusStore = statusIntervalTimer;
		}

		if (averageFps != averageFpsBefore || hasNewObserver) {
			signalEvent(Event.FPS_UPDATED, averageFps);
		}
	}

	/**
	 * Defines the StopListener interface for classes that have to be informed
	 * if the heart's stopping.
	 * 
	 * @author fckawe
	 */
	public interface StopListener {
		public void heartStopping();
	}

	@Override
	public void addObserver(final Observer observer) {
		super.addObserver(observer);
		hasNewObserver = true;
	}

	/**
	 * Sends an event to all the registered observers.
	 * 
	 * @param event
	 *            The event to fire.
	 */
	protected void signalEvent(final Event event) {
		Heart.this.setChanged();
		Heart.this.notifyObservers(event);
	}

	/**
	 * Sends an event together with an object value to all the registered
	 * observers.
	 * 
	 * @param event
	 *            The event to fire.
	 * @param value
	 *            The value to be sent together with the event.
	 */
	protected void signalEvent(final Event event, final Object value) {
		Heart.this.setChanged();
		EventData data = new EventData(event, value);
		Heart.this.notifyObservers(data);
	}

	/**
	 * A class that is used as transfer packet for sending events.
	 * 
	 * @author fckawe
	 */
	public class EventData {

		private final Event event;
		private final Object data;

		/**
		 * Constructor to create a new event data compound object.
		 * 
		 * @param event
		 *            The event to send.
		 * @param data
		 *            The data object to send together with the signaled event.
		 */
		public EventData(final Event event, final Object data) {
			this.event = event;
			this.data = data;
		}

		/**
		 * Returns the signaled event.
		 * 
		 * @return The signaled event.
		 */
		public Event getEvent() {
			return event;
		}

		/**
		 * Returns the event data object.
		 * 
		 * @return The event data object.
		 */
		public Object getData() {
			return data;
		}

	}

}
