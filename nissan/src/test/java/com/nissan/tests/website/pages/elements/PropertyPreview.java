package com.nissan.tests.website.pages.elements;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;

import com.nissan.tests.framework.Log;
import com.nissan.tests.framework.Sync;
import com.nissan.tests.framework.WdEx;
import com.nissan.tests.website.pages.ContactLandlord;
import com.nissan.tests.website.pages.Login;
import com.nissan.tests.website.pages.MakeAnOffer;

public class PropertyPreview extends WdEx {

  public final int GALLERY_ROTATION_INTERVAL = 5; // seconds

  private final By CLOSE_BUTTON_LOCATOR = By.xpath(".//a[contains(@class,'preview_close')]");

  WebElement container;

  public PropertyPreview(WebDriver wd, WebElement container) {
    this.container = container;
    this.wd = wd;
    // Wait for the opening animation to finish
    Sync.wait(() -> !isMissing(CLOSE_BUTTON_LOCATOR));
  }

  /**
   * Gets the text in the header of the property
   *
   * @return
   * @throws Exception
   */
  public String getPropertyHeader() throws Exception {
    WebElement header = container.findElement(By.xpath(".//div[contains(@class,'preview_header')]/span"));
    return header.getText();
  }

  /**
   * Closes the preview
   *
   * @return false if the preview failed to close
   */
  public boolean close() {
    WebElement closeButton = container.findElement(CLOSE_BUTTON_LOCATOR);
    closeButton.click();
    // Wait for the close animation to finish
    return Sync.wait(() -> !closeButton.isDisplayed());
  }

  /**
   * Gets the text for a details entry (i.e. Reference ID, Available from, etc)
   *
   * @param entry
   * @return
   * @throws Exception
   */
  public String getTextDetailsEntry(String entry) throws Exception {
    WebElement td = container.findElement(By.xpath(String.format(
        ".//table//td[contains(text(),'%s')]/following-sibling::td[1]", entry)));
    // We need to scroll the item into view in order to workaround a selenium
    // bug in FF: http://stackoverflow.com/questions/15398520/
    if (wd instanceof FirefoxDriver) {
      hover(td);
    }
    return td.getText();
  }

  /**
   * Get the features of the ad as a list
   *
   * @return
   */
  public List<String> getFeatures() {
    return container.findElements(By.xpath(".//div[@class='preview_features']//blockquote")).stream()
        .map(element -> element.getText()).collect(Collectors.toList());
  }

  /**
   * Get the description of the property
   *
   * @return
   */
  public String getDescription() {
    return container.findElement(By.xpath(".//div[@class='preview_description']//p")).getText();
  }

  /**
   * Get the monthly price of the property
   *
   * @return
   */
  public double getMonthlyPrice() {
    String priceText = container.findElement(
        By.xpath(".//div[contains(@class,'info_monthly')]/div[contains(@class,'price')]")).getText();
    return parsePoundPrice(priceText);
  }

  /**
   * Click the 'MAKE OFFER' button while logged in and return the page object
   * for the form that appears
   * 
   * @return
   */
  public MakeAnOffer makeOfferLoggedIn() {
    container.findElement(By.className("make_offer_button")).click();
    return PageFactory.initElements(wd, MakeAnOffer.class);
  }

  /**
   * Click the 'MAKE OFFER' button while not logged in and return the
   * Login/Register page object for the form that appears
   * 
   * @return
   */
  public Login makeOfferLoggedOut() {
    container.findElement(By.className("make_offer_button")).click();
    return PageFactory.initElements(wd, Login.class);
  }

  /**
   * Click the 'CONTACT LANDLORD' button while logged in and return the page
   * object for the form that appears
   * 
   * @return
   */
  public ContactLandlord contactLandlordLoggedIn() {
    container.findElement(By.className("landlord_button")).click();
    return PageFactory.initElements(wd, ContactLandlord.class);
  }

  /**
   * Click the 'CONTACT LANDLORD' button while logged out and return the
   * Login/Register page object for the form that appears
   * 
   * @return
   */
  public Login contactLandlordLoggedOut() {
    container.findElement(By.className("landlord_button")).click();
    return PageFactory.initElements(wd, Login.class);
  }

  /**
   * Click the 'MAKE OFFER' button and verify that the expected error message is
   * displayed
   * 
   * @return
   */
  public boolean makeOfferExpectingError(String error) {
    container.findElement(By.className("make_offer_button")).click();
    WebElement errorMessage = wd.findElement(By.xpath(String.format("//div[@class='alerts_messages error']"
        + "/h2[text()='%s']", error)));
    return (Sync.wait(() -> errorMessage.isDisplayed(), LONG_WAIT) && Sync.wait(() -> !errorMessage.isDisplayed(),
        MEDIUM_WAIT));
  }

  /**
   * Returns the id of the property that is displayed in the preview
   * 
   * @return
   */
  public String getId() {
    String id = container.findElement(By.xpath("./div[@class='preview']")).getAttribute("id");
    id = id.replace("ep-view-", "");
    Log.comment("Property ID:" + id);
    return id;
  }

  /**
   * Returns true if the preview shows the data from a search result. Used to
   * verify the data in the preview against the data in the search result
   *
   * @param res
   *          the search result
   * @return
   * @throws Exception
   */
  public boolean isShowing(SquareSearchResult res) throws Exception {
    String header = getPropertyHeader();

    int resBeds = res.getNumberOfBedrooms();
    if (resBeds == 0) {
      if (!header.contains(String.format("Studio", resBeds))) {
        Log.messageRed("Studio should be present in the preview header");
        return false;
      }
      if (!getTextDetailsEntry("Bedrooms").equals("Studio")) {
        Log.messageRed("Studio should be present in the details under Bedrooms");
        return false;
      }
    } else {
      if (!header.startsWith(String.format("%d bed", resBeds))) {
        Log.messageRed("Incorrect number of beds in the header");
        return false;
      }
      if (!getTextDetailsEntry("Bedrooms").equals(Integer.toString(resBeds))) {
        Log.messageRed("Incorrect number of beds in the details");
        return false;
      }
    }

    String resPropertyType = res.getPropertyType().replace("semidetached", "semi-detached");
    if (!header.toLowerCase().contains(resPropertyType)) {
      Log.messageRed("Incorrect property type in the header");
      return false;
    }
    if (!getTextDetailsEntry("Property type").toLowerCase().contains(resPropertyType)) {
      Log.messageRed("Incorrect property type in the details");
      return false;
    }

    if (!header.contains(res.getAddressText().replace("...", "").trim())) {
      Log.messageRed("Incorrect address in the header");
      return false;
    }

    if (getMonthlyPrice() != res.getMonthlyPrice()) {
      Log.messageRed("Incorrect monthly pricess");
      return false;
    }

    return true;

  }

  /**
   * Returns true if the pixel in the centre of the currently displayed gallery
   * image in the property preview is of some expected color
   * 
   * @param expectedColour
   * @return
   * @throws Exception
   */
  public boolean verifyCurrentGalleryImageColor(int expectedColour) throws Exception {
    WebElement img = container.findElement(By
        .xpath(".//div[contains(@class,'active')]/ul[contains(@class,'slick-gallery')]"));
    return areColorsSimilar(expectedColour, getCentralPixelColor(img));
  }

  /**
   * Switches the gallery view to floorplan
   */
  public void switchToFloorplan() {
    container.findElement(By.linkText("FLOORPLAN")).click();
    sleep(VERY_SHORT_WAIT);
  }

}
