package com.nissan.tests.website.pages;

import java.text.ParseException;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.easyproperty.tests.framework.Log;
import com.easyproperty.tests.framework.PageBase;
import com.easyproperty.tests.framework.Sync;

public class MakeAnOffer extends PageBase {

  public MakeAnOffer(WebDriver wd) {
    super(wd);
    // Make sure the make offer popup is opened
    Assert.assertTrue(Sync.wait(() -> wd.findElement(By.id("make-offer")).isDisplayed()));
  }

  @FindBy(how = How.ID, using = "make-offer")
  private WebElement container;

  @FindBy(how = How.XPATH, using = "//form[@id='make-offer']//span[@class='location_left']/strong")
  private WebElement propertyAddressLine;

  @FindBy(how = How.XPATH, using = "//form[@id='make-offer']//span[@class='location_left']/span")
  private WebElement propertyShortDescription;

  @FindBy(how = How.XPATH, using = "//form[@id='make-offer']//span[@class='info_right']/span[contains(@class,'price')]")
  private WebElement propertyMonhlyPrice;
  
  @FindBy(how = How.XPATH, using = "//form[@id='make-offer']//div[@class='offer_property_description']/img")
  private WebElement propertyThumbnail;

  @FindBy(how = How.ID, using = "moveInDate")
  private WebElement moveInDate;

  @FindBy(how = How.NAME, using = "tenancyDuration")
  private WebElement tenancyDuration;

  @FindBy(how = How.NAME, using = "offerAmount")
  private WebElement offerAmount;

  @FindBy(how = How.NAME, using = "totalDeposit")
  private WebElement deposit;

  @FindBy(how = How.NAME, using = "furnished")
  private WebElement furnished;

  @FindBy(how = How.NAME, using = "noOccs")
  private WebElement numberOfOccumpants;

  @FindBy(how = How.NAME, using = "noOccs18")
  private WebElement numberOfOccumpantsOver18;

  @FindBy(how = How.NAME, using = "relevantInformation")
  private WebElement additionalComments;

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

  @FindBy(how = How.XPATH, using = "//form[@id='make-offer']//input[@id='submitbutton']")
  private WebElement sendOffer;

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
   * Returns the tenancy start date displayed in the form
   * 
   * @return a java Date object
   * @throws ParseException
   */
  public Date getTenancyStartDate() throws ParseException {
    String dateText = moveInDate.getAttribute("value");
    return parseDate(dateText);
  }

  /**
   * Returns the selected tenancy duration
   * 
   * @return
   */
  public String getSelectedTenancyDuration() {
    Select select = new Select(tenancyDuration);
    return select.getFirstSelectedOption().getText();
  }

  /**
   * Returns the entered deposit
   * 
   * @return
   */
  public double getEnteredDeposit() {
    String depositText = deposit.getAttribute("value");
    return Double.parseDouble(depositText);
  }

  public String getEnteredEmail() {
    return email.getAttribute("value");
  }

  /**
   * Returns the furnished status value displayed
   * 
   * @return
   */
  public String getFurnishedText() {
    return furnished.getAttribute("value");
  }

  /**
   * Selects the number of occupants
   * 
   * @param i
   */
  public void selectNumberOfOccupants(int i) {
    Select select = new Select(numberOfOccumpants);
    select.selectByIndex(i);
  }
  
  /**
   * Selects the number of occupants over 18
   * 
   * @param i
   */
  public void selectNumberOfOccupantsOver18(int i) {
    Select select = new Select(numberOfOccumpantsOver18);
    select.selectByIndex(i);
  }

  /**
   * Enter some comment in the additional comments field
   * 
   * @param comment
   */
  public void enterAdditionalComment(String comment) {
    additionalComments.sendKeys(comment);
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
   * Enter the mobile phone
   * 
   * @param phone
   */
  public void enterMobilePhone(String phone) {
    mobilePhone.sendKeys(phone);
  }

  /**
   * Click the send offer button and verifies that the page is closed
   * 
   * @return
   */
  public boolean sendOffer() {
    sendOffer.click();
    return Sync.wait(() -> isMissing(By.id("make-offer")));
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
