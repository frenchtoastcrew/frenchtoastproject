package com.nissan.tests.website.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

import com.easyproperty.tests.framework.Log;
import com.easyproperty.tests.framework.PageBase;
import com.easyproperty.tests.framework.Sync;

public class ContactLandlord extends PageBase {

  public ContactLandlord(WebDriver wd) {
    super(wd);
    // Make sure the make offer popup is opened
    Assert.assertTrue(Sync.wait(() -> !isMissing(By.id("make-enquiry"))));
  }

  @FindBy(how = How.ID, using = "make-enquiry")
  private WebElement container;

  @FindBy(how = How.XPATH, using = "//form[@id='make-enquiry']//span[@class='location_left']/strong")
  private WebElement propertyAddressLine;

  @FindBy(how = How.XPATH, using = "//form[@id='make-enquiry']//span[@class='location_left']/span")
  private WebElement propertyShortDescription;

  @FindBy(how = How.XPATH,
      using = "//form[@id='make-enquiry']//span[@class='info_right']/span[contains(@class,'price')]")
  private WebElement propertyMonhlyPrice;
  
  @FindBy(how = How.XPATH, using = "//form[@id='make-enquiry']//div[@class='offer_property_description']/img")
  private WebElement propertyThumbnail;

  @FindBy(how = How.ID, using = "enquiry-viewing")
  private WebElement arrangeViewing;

  @FindBy(how = How.ID, using = "enquiry-question")
  private WebElement askQuestion;

  @FindBy(how = How.NAME, using = "title")
  private WebElement title;

  @FindBy(how = How.NAME, using = "firstName")
  private WebElement firstName;

  @FindBy(how = How.NAME, using = "lastName")
  private WebElement lastName;

  @FindBy(how = How.NAME, using = "username")
  private WebElement email;
  
  @FindBy(how = How.NAME, using = "telephone")
  private WebElement homePhone;

  @FindBy(how = How.NAME, using = "mobile")
  private WebElement mobilePhone;

  @FindBy(how = How.NAME, using = "message")
  private WebElement message;

  @FindBy(how = How.XPATH, using = "//form[@id='make-enquiry']//input[@id='submitbutton']")
  private WebElement send;

  /**
   * Returns the first line of the property description including the number of
   * beds and address
   * 
   * @return
   */
  public String getPropertyAddressLine() {
    return propertyAddressLine.getText().trim();
  }

  /**
   * Returns the second line of the property description that contains the short
   * description
   * 
   * @return
   */
  public String getPropertyShortDescription() {
    return propertyShortDescription.getText().trim();
  }

  /**
   * Returns the displayed monthly price for the property
   * 
   * @return
   */
  public double getMonthlyPrice() {
    return parsePoundPrice(propertyMonhlyPrice.getText());
  }

  /**
   * Returns the displayed email of the user
   * 
   * @return
   */
  public String getEnteredEmail() {
    return email.getAttribute("value");
  }

  /**
   * Selects the 'Arrange a viewing' option
   */
  public void selectArrangeViewing() {
    arrangeViewing.click();
  }

  /**
   * Selects the 'Ask a question' option
   */
  public void selectAskQuestion() {
    askQuestion.click();
  }

  /**
   * Enter a message
   * 
   * @param message
   */
  public void enterMessage(String message) {
    this.message.sendKeys(message);
  }

  /**
   * Enter the first name
   * 
   * @param name
   */
  public void enterFirstName(String name) {
    firstName.sendKeys(name);
  }

  /**
   * Enter the last name
   * 
   * @param name
   */
  public void enterLastName(String name) {
    lastName.sendKeys(name);
  }

  /**
   * Enter the home phone
   * 
   * @param phone
   */
  public void enterHomePhone(String phone) {
    homePhone.sendKeys(phone);
  }

  /**
   * Enter the mobile phone
   * 
   * @param phone
   */
  public void enterMobilePhone(String phone) {
    mobilePhone.sendKeys(phone);
  }

  /**
   * Clicks the send button and verifies that the page is closed
   * 
   * @return
   */
  public boolean send() {
    send.click();
    return Sync.wait(() -> isMissing(By.id("make-enquiry")));
  }

  /**
   * Verify the displayed property preview on top of the page
   * 
   * @param beds
   * @param type
   * @param address
   * @param shortDescription
   * @param monthlyPrice
   * @return
   */
  public boolean verifyDisplayedPropertyDetails(int beds, String type, String address, String shortDescription,
      double monthlyPrice) {
    if (getMonthlyPrice()!=monthlyPrice) {
      Log.messageRed("Invalid monthly price");
      return false;
    }
    if (!getPropertyShortDescription().equals(shortDescription)) {
      Log.messageRed("Invalid short description");
      return false;
    }
    if (!getPropertyAddressLine().equals(String.format("%d bed %s, %s", beds, type, address))) {
      Log.messageRed("Invalid address / title line");
      return false;
    }
    return true;
  }

  /**
   * Returns true if the pixel in the centre of the thumbnail displayed in the
   * property preview is of some expected color
   * 
   * @param expectedColour
   * @return
   * @throws Exception
   */
  public boolean verifyThumbnailColour(int expectedColour) throws Exception {
    return areColorsSimilar(expectedColour, getCentralPixelColor(propertyThumbnail));
  }

}
