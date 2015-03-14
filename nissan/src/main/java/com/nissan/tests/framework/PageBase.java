package com.nissan.tests.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.nissan.tests.website.pages.Home;

/**
 * A base class for the different page objects
 *
 * @author vlado a.
 *
 */
public class PageBase extends WdEx {

	/**
	 * The three different 'responsive design' versions of the site
	 *
	 * @author vlado a.
	 *
	 */
	public static enum ResponsiveSiteVersion {
		MOBILE, TABLET, WEB
	};

	// keep a copy of the web dirver
	public PageBase(WebDriver wd) {
		this.wd = wd;
	}

	/**
	 * Returns the current responsive design version of the site based on the
	 * browser width
	 *
	 * @return
	 */
	public ResponsiveSiteVersion getCurrentResponsiveSiteVersion() {
		int width = wd.manage().window().getSize().getWidth();
		if (width > 1024)
			return ResponsiveSiteVersion.WEB;
		if (width > 480)
			return ResponsiveSiteVersion.TABLET;
		return ResponsiveSiteVersion.MOBILE;
	}

  /**
   * Waits for a success message and returns true if the message appears and
   * then disappears
   * 
   * @param message
   * @return
   */
  public boolean verifySuccessMessage(String message) {
    By successMessageLocator = By.xpath(String.format("//div[@class='alerts_messages success']/h2[text()='%s']",
        message));
    return Sync.wait(() -> !isMissing(successMessageLocator), LONG_WAIT)
        && Sync.wait(() -> isMissing(successMessageLocator), MEDIUM_WAIT);
  }

  /**
   * Waits for a modal message, dismisses it and returns true if it has appeared
   * and was dismissed successfully
   * 
   * @param message
   * @return
   */
  public boolean verifyMessageAndDismiss(String message) {
    By messageLocator = By.xpath(String.format("//div[@id='ajax-modal']//h1[contains(text(),'%s')]",
        message));
    if (Sync.wait(() -> !isMissing(messageLocator), LONG_WAIT)) {
      // Dismiss the message
      By grayModalBackground = By.className("reveal-modal-bg");
      wd.findElement(grayModalBackground).click();
      return Sync.wait(() -> isMissing(grayModalBackground));
    } else {
      return false;
    }
  }


	/**
	 * Header web elements
	 * 
	 * @author ivo v.
	 */

  //MAIN PAGE NAVIGATION TABS
  
  @FindBy(how = How.XPATH, using = ".//*[@id='nsmn-vehicules']/a/span")
  private WebElement newCars;
//  .//*[@id='nsnmn-passengers-cars-cars']/div[1]/ul/li[1]/ul/li[1]
//  .//*[@id='nsnmn-passengers-cars-cars']/div[1]/ul/li[1]/ul/li[2]
//  .//*[@id='nsnmn-passengers-cars-cars']/div[1]/ul/li[2]/ul/li[1]
//  .//*[@id='nsnmn-electric-vehicles-cars']/div[1]/ul/li/ul/li[1]
  
  @FindBy(how = How.XPATH, using = ".//*[@id='nsmn-usedcars']/a/span")
  private WebElement usedCars;
  @FindBy(how = How.XPATH, using = ".//*[@id='nsmn-tools']/a/span")
  private WebElement tools;
  @FindBy(how = How.XPATH, using = ".//*[@id='nsmn-fleet']/a/span")
  private WebElement fleet;
  @FindBy(how = How.XPATH, using = ".//*[@id='nsmn-contact']/a/span")
  private WebElement contact;
  @FindBy(how = How.XPATH, using = ".//*[@id='nsmn-innovation']/a/span")
  private WebElement experience;
  @FindBy(how = How.XPATH, using = ".//*[@id='nsmn-youplusnissan']/a/span")
  private WebElement nissanOwners;
  

	// Main header buttons methods


	
}
