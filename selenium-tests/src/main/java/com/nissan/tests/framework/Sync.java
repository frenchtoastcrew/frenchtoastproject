package com.nissan.tests.framework;

import java.util.function.BooleanSupplier;

/**
 * A synchronization class that provides methods to wait for an arbitrary
 * condition
 *
 * @author vlado a.
 *
 */
public class Sync {

	private static final int DEFAULT_WAIT_TIMEOUT = 60; // One minute

	/**
	 * Waits for arbitrary condition
	 *
	 * @param condition
	 *            supplied as a BooleanSupplier lambda (() ->
	 *            boolean_expression)
	 * @param seconds
	 * @return true if the condition became true during the seconds timeout,
	 *         false otherwise
	 */
	public static boolean wait(BooleanSupplier condition, int seconds) {
		long endTime = getCurrentTime() + seconds;
		do {
			if (condition.getAsBoolean()) {
				return true;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return false;
			}
		} while (getCurrentTime() < endTime);
		return false;
	}

	/**
	 * Waits for arbitrary condition the default timeout time
	 *
	 * @param condition
	 *            supplied as a BooleanSupplier lambda (() ->
	 *            boolean_expression)
	 * @return true if the condition became true during the default timeout,
	 *         false otherwise
	 */
	public static boolean wait(BooleanSupplier condition) {
		return wait(condition, DEFAULT_WAIT_TIMEOUT);
	}

	private static long getCurrentTime() {
		return System.currentTimeMillis() / 1000l;
	}

}
