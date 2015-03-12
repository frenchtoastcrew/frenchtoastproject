package com.nissan.tests.framework;

import org.testng.Reporter;

/**
 * A few helper methods to output formatted log messages in the TestNG log
 *
 * @author vlado a.
 *
 */
public class Log {

	/**
	 * Logs a message
	 *
	 * @param text
	 */
	public static void message(String text) {
		System.out.println("\t" + text);
		Reporter.log("<ul><font size='2'>" + text + "</font></ul>\n");
	}

	/**
	 * Logs a message as a HTML comment (it will be visible only when you view
	 * the source of the log)
	 *
	 * @param text
	 */
	public static void comment(String text) {
		Reporter.log("<!--" + text + "-->\n");
	}

	/**
	 * Logs a red message
	 *
	 * @param text
	 */
	public static void messageRed(String text) {
		System.out.println(" !!! " + text);
		Reporter.log("<ul><font size='2' color='red'>" + text
				+ "</font></ul>\n");
	}

}
