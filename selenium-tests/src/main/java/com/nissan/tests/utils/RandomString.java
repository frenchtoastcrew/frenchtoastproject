package com.nissan.tests.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.RandomStringUtils;

import com.nissan.tests.framework.Log;

/**
 * A helper class for generation of different random data
 *
 * @author ivo varbanov
 *
 */
public class RandomString {

  /**
   * Returns a random alphanumeric string with optional lowercase setting
   * 
   * @param length
   * @param lowerCase
   * @throws Exception
   * @return
   */
  public static String randomAlphanumeric(int length, boolean lowerCase) {
    String str = null;
    try {
      str = RandomStringUtils.randomAlphanumeric(length);
      if (lowerCase) {
        str = str.toLowerCase();
      }
    }
    catch (Exception e) {
      Log.messageRed("Invalid integer for randomAlphanumeric generator!");
    }

    return str;
  }

  /**
   * Returns a random alphabetic string with optional lowercase setting
   * 
   * @param length
   * @param lowerCase
   * @throws Exception
   * @return
   */
  public static String randomAlphabetic(int length, boolean lowerCase) {
    String str = null;
    try {
      str = RandomStringUtils.randomAlphabetic(length);

      if (lowerCase) {
        str = str.toLowerCase();
      }
    }
    catch (Exception e) {
      Log.messageRed("Invalid integer for randomAlphabetic generator!");
    }

    return str;
  }

  /**
   * Returns a random numeric string
   * 
   * @param length
   * @throws Exception
   * @return
   */
  public static String randomNumeric(int length) {
    String str = null;
    try {
      str = RandomStringUtils.randomNumeric(length);
    }
    catch (Exception e) {
      Log.messageRed("Invalid integer for randomNumeric generator!");
    }
    return (str);
  }

  /**
   * Returns a random ASCII string
   * 
   * @param length
   * @throws Exception
   * @return
   */
  public static String randomASCII(int length) {
    String str = null;
    try {
      str = RandomStringUtils.randomAscii(length);
    }
    catch (Exception e) {
      Log.messageRed("Invalid integer for randomAscii generator!");
    }

    return str;
  }

  /**
   * Returns a valid structured email with the gmail.com domain Example :
   * 250215egwergwerg@gmail.com
   * 
   * @return
   */
  public static String generateEmail() {

    Calendar cal = Calendar.getInstance();
    String currentDate = new SimpleDateFormat("ddMMyy").format(cal.getTime());
    String email = currentDate + randomAlphabetic(10, true) + "@gmail.com";

    return email;
  }

  /**
   * Returns a valid structured and functional email with the gmail.com domain
   * Example : ivoproptest+250215egwergwerg@gmail.com
   * 
   * @return
   */
  public static String generateRealEmail() {

    Calendar cal = Calendar.getInstance();
    String currentDate = new SimpleDateFormat("ddMMyy").format(cal.getTime());
    String email = "ivoproptest+" + currentDate + randomAlphabetic(10, true) + "@gmail.com";

    return email;
  }
}
