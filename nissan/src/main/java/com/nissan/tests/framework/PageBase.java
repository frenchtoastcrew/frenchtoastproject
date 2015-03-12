package com.nissan.tests.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.easyproperty.tests.website.pages.Home;
import com.easyproperty.tests.website.pages.Inbox;
import com.easyproperty.tests.website.pages.ListFavouriteProperties;
import com.easyproperty.tests.website.pages.ListOwnedProperties;
import com.easyproperty.tests.website.pages.Profile;
import com.easyproperty.tests.website.pages.Search;

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
   * Responds to a confirmation message by clicking the confirm or cancel
   * buttons
   * 
   * @param message
   * @param confirm
   * @return true if the confirmation message was successfully closed
   */
  public boolean respondToConfirmationMessage(String message, boolean confirm) {
    // Locate the confirmation message
    By confirmationMessageLocator = By.xpath(String.format("//div[@id='ajax-modal']/h2[text()='%s']", message));
    if (!Sync.wait(() -> wd.findElement(confirmationMessageLocator).isDisplayed(), LONG_WAIT)) {
      return false;
    }
    // Click the appropriate button
    if (confirm) {
      wd.findElement(By.className("confirm_ok_icon")).click();
    } else {
      wd.findElement(By.className("confirm_cross_icon")).click();
    }
    return Sync.wait(() -> isMissing(confirmationMessageLocator), LONG_WAIT);
  }

	/**
	 * Header web elements
	 * 
	 * @author Vladimir Mihov
	 */

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'LIST YOUR PROPERTY')]")
	private WebElement listYourPropertyHeaderButton;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'SERVICES')]")
	private WebElement servicesHeaderButton;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'SEARCH')]")
	private WebElement searchHeaderButton;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'CONTROL PANEL')]")
	private WebElement controlPanelHeaderButton;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'SIGN IN')]")
	private WebElement signInHeaderButton;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'SIGN OUT')]")
	private WebElement signOutHeaderButton;

	@FindBy(how = How.XPATH, using = "//a[@href='#']")
  private WebElement mobieMenuIcon;

  @FindBy(how = How.XPATH, using = "//span[@id='number-of-unread-messages']/span")
  private WebElement numberOfUnreadMessages;

  @FindBy(how = How.XPATH, using = "//span[@id='number-of-unread-messages-mobile']/span")
  private WebElement numberOfUnreadMessagesMobile;

	// TODO basket empty/full, logged in/logged out

	// Buttons visible in sub-header once Control panel is clicked

	@FindBy(how = How.XPATH, using = "//li[not(@class='mobile_only')]/a[text()='PROPERTIES']")
	private WebElement propertiesSubHeaderButton;

	@FindBy(how = How.XPATH, using = "//li[@class='mobile_only']/a[text()='PROPERTIES']")
	private WebElement propertiesMobileSubHeaderButton;

	@FindBy(how = How.XPATH, using = "//li[not(@class='mobile_only')]/a[text()='FAVOURITES']")
	private WebElement favouritesSubHeaderButton;

	@FindBy(how = How.XPATH, using = "//li[@class='mobile_only']/a[text()='FAVOURITES']")
	private WebElement favouritesMobileSubHeaderButton;

  @FindBy(how = How.XPATH, using = "//li[not(@class='mobile_only')]/a[contains(text(),'INBOX')]")
	private WebElement inboxSubHeaderButton;

  @FindBy(how = How.XPATH, using = "//li[@class='mobile_only']/a[contains(text(),'INBOX')]")
	private WebElement inboxMobileSubHeaderButton;

	@FindBy(how = How.XPATH, using = "//li[not(@class='mobile_only')]/a[text()='PROFILE']")
	private WebElement profileSubHeaderButton;

	@FindBy(how = How.XPATH, using = "//li[@class='mobile_only']/a[text()='PROFILE']")
	private WebElement profileMobileSubHeaderButton;

	// Main header buttons methods

	/**
	 * Click List Your Property button
	 */
	public void clickListYourPropertyButton() {
		listYourPropertyHeaderButton.click();
		// TODO add return type
	}

	/**
	 * Click Services button
	 */
	public void clickServicesButton() {
		servicesHeaderButton.click();
		// TODO add return type
	}

	/**
	 * Click Search button
	 */
	public Search clickSearchButton() {
		searchHeaderButton.click();
		return PageFactory.initElements(wd, Search.class);
	}

	/**
	 * Click Control Panel button
	 */
	public ListOwnedProperties clickControlPanelButton() {
		// TODO add check if user is LL or TE since there should be different
		// redirect (properties or favorites)
		controlPanelHeaderButton.click();
		return PageFactory.initElements(wd, ListOwnedProperties.class);
	}

	/**
	 * Click Sign In button
	 */
	public void clickSignInButton() {
		signInHeaderButton.click();
		// TODO add return type
	}

	/**
	 * Click Sign Out button
	 */
  public Home clickSignOutButton() {
		signOutHeaderButton.click();
    return PageFactory.initElements(wd, Home.class);
	}

	// Sub header buttons methods
	/**
	 * Click Properties button
	 * 
	 * @return ListOwnedProeprties page
	 */
	public ListOwnedProperties clickPropertiesSubHeaderButton() {
		if (getCurrentResponsiveSiteVersion() != ResponsiveSiteVersion.WEB) {
			clickMenuIcon(propertiesMobileSubHeaderButton);
		} else {
			clickMenuIcon(propertiesSubHeaderButton);
		}
		return PageFactory.initElements(wd, ListOwnedProperties.class);
	}

	/**
	 * Click Favourites button
	 */
  public ListFavouriteProperties clickFavouritesSubHeaderButton() {
		if (getCurrentResponsiveSiteVersion() != ResponsiveSiteVersion.WEB) {
			clickMenuIcon(favouritesMobileSubHeaderButton);
		} else {
			clickMenuIcon(favouritesSubHeaderButton);
		}
    return PageFactory.initElements(wd, ListFavouriteProperties.class);
	}

	/**
	 * Click Inbox button
	 */
  public Inbox clickInboxSubHeaderButton() {
		if (getCurrentResponsiveSiteVersion() != ResponsiveSiteVersion.WEB) {
			clickMenuIcon(inboxMobileSubHeaderButton);
		} else {
			clickMenuIcon(inboxSubHeaderButton);
		}
    return PageFactory.initElements(wd, Inbox.class);
	}

	/**
	 * Click Profile button
	 */
  public Profile clickProfileSubHeaderButton() {
		if (getCurrentResponsiveSiteVersion() != ResponsiveSiteVersion.WEB) {
			clickMenuIcon(profileMobileSubHeaderButton);
		} else {
			clickMenuIcon(profileSubHeaderButton);
		}
    return PageFactory.initElements(wd, Profile.class);
	}

	/**
	 * Click the menu icon in case mobile or tablet version of the WS is
	 * displayed.
	 */
	public void clickMenuIcon(WebElement subHeaderButton) {
		if (!subHeaderButton.isDisplayed()) {
			if (getCurrentResponsiveSiteVersion() != ResponsiveSiteVersion.WEB) {
				mobieMenuIcon.click();
				// Wait the 'reveal' animation to complete
				Sync.wait(() -> subHeaderButton.isDisplayed());
				subHeaderButton.click();
			} else {
				throw new IllegalStateException(
						"The filters should be always visible in the web version "
								+ "of the reponsive design");
			}
		} else {
			subHeaderButton.click();
		}
	}

  public int getNumberOfUnreadMessages() {
    if (isMissing(By.xpath("//span[@id='number-of-unread-messages']/span"))) {
      // The web indicator for the number of messages is missing
      if (getCurrentResponsiveSiteVersion() != ResponsiveSiteVersion.WEB) {
        // We are in mobile version, so try to show the mobile version of the
        // indicator
        mobieMenuIcon.click();
        // Wait the 'reveal' animation to complete
        Sync.wait(() -> !isMissing(By.xpath("//span[@id='number-of-unread-messages-mobile']")));
        if (isMissing(By.xpath("//span[@id='number-of-unread-messages']/span"))) {
          // There are still no unread messages
          return 0;
        } else {
          return Integer.parseInt(numberOfUnreadMessagesMobile.getText().trim());
        }
      } else {
        // We are in the web version, but the indicator is missing
        return 0;
      }
    } else {
      // We are in the web version and we have an indicator
      return Integer.parseInt(numberOfUnreadMessages.getText().trim());
    }
  }
}
