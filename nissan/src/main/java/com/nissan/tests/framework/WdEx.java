package com.nissan.tests.framework;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;

/**
 * WebDriver extensions & utility methods. This is a base class for all test
 * code that uses WebDriver
 *
 * @author vlado a.
 *
 */
public class WdEx {

  protected final static String POUND = "\u00A3";

  // Test timeouts
  protected final static int VERY_SHORT_WAIT = 1;
  protected final static int SHORT_WAIT = 5;
  protected final static int MEDIUM_WAIT = 15;
  protected final static int LONG_WAIT = 60;

  protected final static int IMPLICIT_WAIT_TIMEOUT = 20;
  protected final static int TIMEOUT_WHEN_MISSING = 1;

  protected final static SimpleDateFormat dateFormatter = new SimpleDateFormat("d-M-y");

  public WebDriver wd;
  private static Properties systemProperties;

  /**
   * Returns true if an element is missing, does not wait the implicit wait
   * timeout
   *
   * @param locator
   * @return
   */
  public boolean isMissing(By locator) {
    // We don't want to wait too long for something that is expected to be
    // missing
    wd.manage().timeouts().implicitlyWait(TIMEOUT_WHEN_MISSING, TimeUnit.SECONDS);
    List<WebElement> elements = wd.findElements(locator);
    // Go back to the default implicit wait
    wd.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIMEOUT, TimeUnit.SECONDS);
    // If there are no such elements in the DOM tree return true
    if (elements.isEmpty()) {
      return true;
    } else {
      // In case all found elements are invisible still return true
      for (WebElement element : elements) {
        try {
          if (element.isDisplayed()) {
            return false;
          }
        }
        catch (StaleElementReferenceException e) {
          // .isDisplayed() can throw this sometimes (very rarely), when the
          // page changes while this is running:
          // "Element not found in the cache - perhaps the page has changed since it was looked up"
          // In this case assume the element where this happened is gone
          Log.messageRed("STALE");
        }
      }
      return true;
    }

  }
  
  /**
   * Returns true if an element is missing from container, does not wait the implicit wait
   * timeout
   *
   * @param container
   * @param locator
   * @return
   */
  public boolean isMissingFromContainer(WebElement container, By locator) {
    // We don't want to wait too long for something that is expected to be
    // missing
    wd.manage().timeouts().implicitlyWait(TIMEOUT_WHEN_MISSING, TimeUnit.SECONDS);
    int count = container.findElements(locator).size();
    // Go back to the default implicit wait
    wd.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIMEOUT, TimeUnit.SECONDS);
    return (count == 0);
  }

  /**
   * Waits for a number of seconds. If possible try to use Sync.wait instead, to
   * wait for a specific event
   *
   * @param seconds
   */
  public void sleep(int seconds) {
    try {
      Thread.sleep(1000L * seconds);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void hover(WebElement element) throws Exception {
    if (!(wd instanceof SafariDriver)) {
      (new Actions(wd)).moveToElement(element).perform();
    } else {
      // The SafariDriver does not support the standard way
      // http://code.google.com/p/selenium/issues/detail?id=4136
      // https://code.google.com/p/selenium/issues/detail?id=8390
      throw new Exception("Can not mouse over on this browser");
    }
  }

  /**
   * Read a property from the system properties. If the property is not defined
   * at the system properties level (i.e. from the command prompt, pom.xml, etc.
   * try to read it from the System.properties file
   *
   * @param key
   * @return
   */
  public static String getSystemProperty(String key) {
    // Try to find a real system property first
    String prop = System.getProperty(key);
    if (prop == null) {
      // No real system property defined, try to read it from the
      // System.properties file
      if (systemProperties == null) {
        try {
          systemProperties = new Properties();
          systemProperties.load(new FileInputStream("src/test/resources/System.properties"));
        }
        catch (IOException e) {
          // Leave the properties empty
        }
      }
      prop = systemProperties.getProperty(key);
    }
    return prop;
  }

  /**
   * Parses a date with the format used on the site into a Date object
   * 
   * @param dateToParse
   * @return
   * @throws ParseException
   */
  public static Date parseDate(String dateToParse) throws ParseException {
    return dateFormatter.parse(dateToParse);
  }
  
  /**
   * Parses a price in the POUNDnnn[.nn] format into a double value
   * 
   * @param poundPrice
   * @return
   */
  public static double parsePoundPrice(String poundPrice) {
    return Double.parseDouble(poundPrice.replace(POUND, "").trim());
  }

  /**
   * Gets the color of a pixel with x, y coordinates in a web element
   * 
   * @param element
   * @param x
   * @param y
   * @return the color
   * @throws Exception
   */
  public int getPixelColor(WebElement element, int x, int y) throws Exception {
    // Get the screenshot into a byte array (from WebDriver)
    byte[] scr = ((TakesScreenshot) wd).getScreenshotAs(OutputType.BYTES);
    // Convert it to a BufferedImage
    BufferedImage img = ImageIO.read(new ByteArrayInputStream(scr));
    // Crop the screenshot to the desired web element
    Point location = element.getLocation();
    Dimension size = element.getSize();
    BufferedImage crop = img.getSubimage(location.x, location.y, size.width, size.height);
    int color = crop.getRGB(x, y);
    Log.comment("Pixel color: " + color);
    Log.comment(" R:" + ((color & 0x00ff0000) >> 16));
    Log.comment(" G:" + ((color & 0x0000ff00) >> 8));
    Log.comment(" B:" + (color & 0x000000ff));

    // Return the desired pixel color
    return color;
  }

  /**
   * Gets the color of the pixel in the center of the web element
   * 
   * @param element
   * @return the color
   * @throws Exception
   */
  public int getCentralPixelColor(WebElement element) throws Exception {
    return getPixelColor(element, element.getSize().width / 2, element.getSize().height / 2);
  }

  /**
   * Checks if two colors are similar (exact colors in jpeg images might get
   * affected by compression)
   * 
   * @param c1
   *          first color
   * @param c2
   *          second color
   * @return
   */
  public static boolean areColorsSimilar(int c1, int c2) {
    final int delta = 20;

    Log.comment("Color silimarity check: " + c1 + "," + c2);

    int redDelta = Math.abs(((c1 & 0x00ff0000) >> 16) - ((c2 & 0x00ff0000) >> 16));
    int greenDelta = Math.abs(((c1 & 0x0000ff00) >> 8) - ((c2 & 0x0000ff00) >> 8));
    int blueDelta = Math.abs((c1 & 0x000000ff) - (c2 & 0x000000ff));

    Log.comment("-blue delta: " + blueDelta);
    Log.comment("-red delta: " + redDelta);
    Log.comment("-green delta: " + greenDelta);

    if ((blueDelta > delta) || (redDelta > delta) || (greenDelta > delta)) {
      return false;
    }

    Log.comment("Colors are similar");
    return true;
  }

}
