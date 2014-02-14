package com.fckawe.engine.core;

import java.util.Observable;
import java.util.Observer;

// TODO: comment class and methods
public class Heart extends Observable implements Runnable {

	public enum Event {
		HEART_START, TICK, RENDER, SHOW, FPS_UPDATED
	};

	// desired fps
	private final static int TARGET_FPS = 50;
	// maximum number of frames to be skipped
	private final static int MAX_FRAME_SKIPS = 5;
	// the frame period
	private final static int FRAME_PERIOD = 1000000000 / TARGET_FPS;

	// Stuff for stats
	// we'll be reading the stats every second
	private final static int STAT_INTERVAL = 1000; // ms
	// the average will be calculated by storing
	// the last n FPSs
	private final static int FPS_HISTORY_NR = 10;
	// last time the status was stored
	private long lastStatusStore = 0;
	// the status time counter
	private long statusIntervalTimer = 0l;
	// number of frames skipped since the game started
	private long totalFramesSkipped = 0l;
	// number of frames skipped in a store cycle (1 sec)
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

	public Heart(final StopListener exitListener) {
		this.stopListener = exitListener;
		exitRequested = false;
	}

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
		long beginTime; // the time when the cycle begun
		long timeDiff; // the time it took for the cycle to execute
		int sleepTime = 0; // ms to sleep (<0 if we're behind)
		int framesSkipped; // number of frame being skipped

		while (!exitRequested) {
			beginTime = System.nanoTime();
			framesSkipped = 0;

			signalEvent(Event.TICK);
			signalEvent(Event.RENDER);
			signalEvent(Event.SHOW);

			timeDiff = System.nanoTime() - beginTime;
			sleepTime = (int) ((FRAME_PERIOD - timeDiff) / 1000000);

			if (sleepTime > 0) {
				// if sleepTime > 0 we're OK
				try {
					// send the thread to sleep for a short
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					Session.getSession().getHeartLogger()
							.warn("Sleep interrupted.", e);
				}
			}

			while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
				// we need to catch up
				// update without rendering
				signalEvent(Event.TICK);
				// add frame period to check if in next frame
				sleepTime += FRAME_PERIOD;
				framesSkipped++;
			}

			// for statistics
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

	public void requestStop() {
		exitRequested = true;
	}

	private void initTimingElements() {
		// initialize timing elements
		fpsStore = new double[FPS_HISTORY_NR];
		for (int i = 0; i < FPS_HISTORY_NR; i++) {
			fpsStore[i] = 0.0;
		}
		Session.getSession().getHeartLogger()
				.info("Timing elements for stats initialised.");
	}

	/**
	 * Store statistics, called every cycle; it checks if time since last store
	 * is greater than the statistics gathering period (1 sec) and if so it
	 * calculates the FPS for the last period and stores it.
	 * 
	 * It tracks the number of frames per period. The number of frames since the
	 * start of the period are summed up and the calculation takes part only if
	 * the next period and the frame count is reset to 0.
	 */
	private void updateStats() {
		double averageFpsBefore = averageFps;
		
		frameCountPerStatCycle++;

		// check the actual time
		statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);
		
		if(statsCount + 1 < FPS_HISTORY_NR) {
			averageFps = TARGET_FPS;
		}

		if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {
			// calculate the actual frames pers status check interval
			double actualFps = (frameCountPerStatCycle / (STAT_INTERVAL / 1000));

			// stores the latest fps in the array
			fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;

			// increase the number of times statistics was calculated
			statsCount++;

			// obtain the average
			if (statsCount >= FPS_HISTORY_NR) {
				double totalFps = 0.0;
				// sum up the stored fps values
				for (int i = 0; i < FPS_HISTORY_NR; i++) {
					totalFps += fpsStore[i];
				}
				averageFps = totalFps / FPS_HISTORY_NR;
			}
			
			// saving the number of total frames skipped
			totalFramesSkipped += framesSkippedPerStatCycle;
			// resetting the counters after a status record (1 sec)
			framesSkippedPerStatCycle = 0;
			statusIntervalTimer = 0;
			frameCountPerStatCycle = 0;

			statusIntervalTimer = System.currentTimeMillis();
			lastStatusStore = statusIntervalTimer;
		}
		
		if(averageFps != averageFpsBefore || hasNewObserver) {
			signalEvent(Event.FPS_UPDATED, averageFps);
		}
	}

	public interface StopListener {
		public void heartStopping();
	}
	
	@Override
	public void addObserver(final Observer observer) {
		super.addObserver(observer);
		hasNewObserver = true;
	}

	public void signalEvent(final Event event) {
		Heart.this.setChanged();
		Heart.this.notifyObservers(event);
	}

	public void signalEvent(final Event event, final double value) {
		Heart.this.setChanged();
		EventData data = new EventData(event, value);
		Heart.this.notifyObservers(data);
	}

	public class EventData {

		private final Event event;
		private final Object data;

		public EventData(final Event event, final Object data) {
			this.event = event;
			this.data = data;
		}

		public Event getEvent() {
			return event;
		}

		public Object getData() {
			return data;
		}

	}

}
