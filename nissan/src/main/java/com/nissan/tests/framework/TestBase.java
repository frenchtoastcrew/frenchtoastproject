package com.nissan.tests.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;

import com.nissan.tests.utils.StringUtil;

/**
 * A base class for all tests. Handles testNG logging and screenshots, data-
 * driven stuff, etc.
 *
 * @author vlado a.
 *
 */
public class TestBase extends WdEx {

  protected enum ScreenshotMode {
    NO_SCREENSHOTS, SCREENSHOTS_ON_FAILURE, SCREENSHOTS
  }
  protected final String BASE_URL = getSystemProperty("WebsiteURL");
  private static int testIndex;

  private ScreenshotMode screenshotMode;
  private String screenshotsFileName;

  private int currentStep;
  private boolean stepFailuresPresent;
  private boolean stepFinalized;

  private Properties profileProperties;
  
  protected StringUtil stringUtil;
  
  /**
   * Make a screenshot at the end in case of an exception in the test and
   * close the browser
   */
  @AfterMethod
  public void cleanup() {
    if (!stepFinalized) {
      // An exception must have occurred during the test, take a
      // screenshot at the current stage
      if (screenshotMode == ScreenshotMode.SCREENSHOTS_ON_FAILURE
          || screenshotMode == ScreenshotMode.SCREENSHOTS) {
        takeScreenShot(screenshotsFileName + (currentStep - 1));
        Reporter.log("Did not finish");
      }
    }
    // Close the browser
    try {
      wd.quit();
    } catch (UnreachableBrowserException e) {
      // Can happen if the test already closed the browser explicitly
      e.printStackTrace();
    }
  }

  /**
   * Starts a test
   *
   * @param testName
   *            - test title
   * @param mode
   *            - if/when screenshots should be taken
   * @param selectedProfile
   *            - which browser profile to use to run the test
   * @throws Exception
   */
  public void startTest(String testName, String selectedProfile, ScreenshotMode mode) throws Exception {

    profileProperties = getProperties(selectedProfile);
    String browser = getProfileProperty("Browser", "Firefox");
    int browserWidth = Integer.parseInt(getProfileProperty("BrowserWidth", "1024"));
    int browserHeight = Integer.parseInt(getProfileProperty("BrowserHeight", "768"));

    System.out.println("**** Starting test: " + testName);
    System.out.println(String.format("%s(%dx%d)", browser, browserWidth, browserHeight));
    Reporter.log("<h2>" + testName + "</h2>\n");
    Reporter.log(String.format("\t<h4>%s(%dx%d)</h4>\n", browser, browserWidth, browserHeight));

    currentStep = 1;
    stepFailuresPresent = false;
    screenshotMode = mode;
    screenshotsFileName = String.format("%s.%s.%dx%d.%d_", testName.replaceAll("[^a-zA-Z0-9]", ""), browser,
        browserWidth, browserHeight, testIndex++);

    wd = initWebDriver();
    wd.manage().window().setSize(new Dimension(browserWidth, browserHeight));
    wd.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIMEOUT, TimeUnit.SECONDS);

  }

  /**
   * Starts a test with screenshots at each step
   *
   * @param testName
   *            - test title
   * @param selectedProfile
   *            - which browser profile to use to run the test
   * @throws Exception
   */
  public void startTest(String testName, String selectedProfile) throws Exception {
    startTest(testName, selectedProfile, ScreenshotMode.SCREENSHOTS);
  }

  /**
   * Starts a test with screenshots at each step using the Default browser
   * profiles
   *
   * @param testName
   *            - test title
   * @throws Exception
   */
  public void startTest(String testName) throws Exception {
    startTest(testName, "Default", ScreenshotMode.SCREENSHOTS);
  }

  /**
   * Ends a test
   */
  public void endTest() throws Exception {
    if (stepFailuresPresent) {
      Assert.fail("Some test steps did not pass. Check the TestNG reporter output for more info.");
    }
  }

  /**
   * Logs the start of a test step
   *
   * @param stepDescription
   */
  public void startStep(String stepDescription) {
    stepFinalized = false;
    Reporter.log("<!--" + new Date() + "-->\n");
    Reporter.log("<p>" + currentStep++ + ". " + stepDescription + "...");
    System.out.println(" -  " + stepDescription + "...");
  }

  /**
   * Logs the completion of a test step
   *
   * @param isPassing
   *            - whether the test step passed successfully or not
   */
  public void endStep(boolean isPassing) {
    Reporter.log("<font color='" + (isPassing ? "green" : "red") + "'>"
        + (isPassing ? "PASSED" : "FAILED") + "</font>");
    if (((screenshotMode == ScreenshotMode.SCREENSHOTS_ON_FAILURE) && !isPassing)
        || (screenshotMode == ScreenshotMode.SCREENSHOTS)) {
      takeScreenShot(screenshotsFileName + (currentStep - 1));
      Reporter.log("<a href='./" + screenshotsFileName + (currentStep - 1) + ".tst.png'>#</a>");
    }
    Reporter.log("</p>");
    if (!isPassing)
      stepFailuresPresent = true;
    stepFinalized = true;
  }

  /**
   * Logs the completion of a test step as successful
   */
  public void endStep() {
    endStep(true);
  }

  /**
   * Takes a screenshot and saves it into the testNG folder
   *
   * @param name
   *            - the file name
   */
  public void takeScreenShot(String name) {
    try {
      File scrFile = ((TakesScreenshot) wd)
          .getScreenshotAs(OutputType.FILE);
      FileUtils.copyFile(scrFile, new File("./test-output/" + name + ".tst.png"));
    } catch (Exception e) {
      // Well...
      e.printStackTrace();
    }
  }

  /**
   * Reads a configuration parameter from the currently selected browser
   * profile file. Will return defaultValue if the file or the property is not
   * present
   *
   * @param key
   * @param defaultValue
   * @return
   */
  public String getProfileProperty(String key, String defaultValue) {
    return profileProperties.getProperty(key, defaultValue);
  }

  /**
   * Reads a configuration parameter from the currently selected browser
   * profile file. Will return null if the file or the property is not present
   *
   * @param key
   * @param defaultValue
   * @return
   */
  public String getProfileProperty(String key) {
    return getProfileProperty(key, null);
  }

  /**
   * Create a testNG data provider, reading the data from an excel file. The
   * name and the location of the Excel file is derived from the test class name
   * and package.
   *
   * @param worksheet
   *          - the worksheet from which to read the data
   * @return
   * @throws InvalidFormatException
   * @throws IOException
   */
  public Object[][] excelDataProvider(String worksheet) throws InvalidFormatException, IOException {
    // Locate the correct excel file
    String filePath = "src/test/resources/datasheets/"
        + getClass().getName().replace("com.nissan.tests.website.tests", "").replace(".", File.separator)
        + ".xlsx";
    return ExcelReader.getDataFromExcel(filePath, worksheet);
  }

  /**
   * Create a testNG data provider, reading the data from an excel file. The
   * name and the location of the Excel file is derived from the test class
   * name and package. The worksheet name should be the same as the method
   * name of the data provider.
   *
   * @return
   * @throws InvalidFormatException
   * @throws IOException
   */
  public Object[][] excelDataProvider() throws InvalidFormatException, IOException {
    // Find the name of the calling method
    // The name of the worksheet should be the same
    String worksheet = new Throwable().fillInStackTrace().getStackTrace()[1].getMethodName();
    return excelDataProvider(worksheet);
  }


  private Properties getProperties(String fileName) {
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream(String.format("src/test/resources/%s.properties", fileName)));
    } catch (IOException e) {
      // Leave the properties empty
    }
    return prop;
  }


  private WebDriver initWebDriver() throws MalformedURLException {

    boolean local = getProfileProperty("WebDriver", "local").toLowerCase().equals("local");
    String browser = getProfileProperty("Browser", "firefox").toLowerCase();

    DesiredCapabilities capabilities = null;

    switch (browser) {
      case "firefox":
        if (local) {
          return new FirefoxDriver();
        }
        capabilities = DesiredCapabilities.firefox();
        break;
      case "chrome":
        if (local) {
          System.setProperty("webdriver.chrome.driver",
              getProfileProperty("ChromeDriverPath", "/Applications/Testing/Webdriver/chromedriver"));
          return new ChromeDriver();
        }
        capabilities = DesiredCapabilities.chrome();
        break;
      case "safari":
        if (local) {
          return new SafariDriver();
        }
        capabilities = DesiredCapabilities.safari();
        break;
      case "internetexplorer":
        if (local) {
          System.setProperty("webdriver.ie.driver",
              getProfileProperty("IEDriverPath", "c:\\selenium\\IEDriverServer.exe"));
          return new InternetExplorerDriver();
        }
        capabilities = DesiredCapabilities.internetExplorer();
        break;
      default:
        throw new IllegalArgumentException("Wrong browser type:" + browser);
    }

    String remoteWebDriverUrl = getSystemProperty("RemoteDriverURL");
    return new RemoteWebDriver(new URL(remoteWebDriverUrl), capabilities);

  }
}
