package com.nissan.tests.website.pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

import com.easyproperty.tests.framework.Log;
import com.easyproperty.tests.framework.PageBase;
import com.easyproperty.tests.framework.Sync;

public class YouveReceivedAnOffer extends PageBase {

  public YouveReceivedAnOffer(WebDriver wd) {
    super(wd);
    // Verify that the 'You've received an offer!' popup is open
    Assert.assertTrue(
        Sync.wait(() -> wd.findElement(By.xpath("//h1[contains(text(),'received an offer!')]")).isDisplayed()),
        "We are not at the 'You've received an offer!' page");
  }

  @FindBy(how = How.ID, using = "view-offer")
  private WebElement container;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//h2")
  private WebElement offerStatusText;
  
  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//span[@class='location_left']/strong")
  private WebElement propertyAddressLine;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//span[@class='location_left']/span")
  private WebElement propertyShortDescription;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//span[@class='info_right']/span[contains(@class,'price')]")
  private WebElement propertyMonhlyPrice;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//table[@class='offer_information']//"
      + "td[text()='Date of offer']/following-sibling::td")
  private WebElement dateOfOffer;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//table[@class='offer_information']//"
      + "td[text()='Name']/following-sibling::td")
  private WebElement tenantName;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//table[@class='offer_information']//"
      + "td[text()='Mobile']/following-sibling::td")
  private WebElement mobile;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//table[@class='offer_information']//"
      + "td[text()='Rent(per calendar month)']/following-sibling::td")
  private WebElement monthlyRent;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//table[@class='offer_information']//"
      + "td[text()='Tenancy start date']/following-sibling::td")
  private WebElement tenancyStartDate;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//table[@class='offer_information']//"
      + "td[text()='Length of tenancy']/following-sibling::td")
  private WebElement lengthOfTenancy;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//table[@class='offer_information']//"
      + "td[text()='Deposit']/following-sibling::td")
  private WebElement deposit;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//table[@class='offer_information']//"
      + "td[text()='Furnished status']/following-sibling::td")
  private WebElement furnished;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//table[@class='offer_information']//"
      + "td[text()='Number of occupants']/following-sibling::td")
  private WebElement numberOfOccupants;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//table[@class='offer_information']//"
      + "td[text()='Occupants over 18']/following-sibling::td")
  private WebElement numberOfOccupantsOver18;

  @FindBy(how = How.XPATH, using = "//form[@id='view-offer']//table[@class='offer_information']//"
      + "td[text()='Comments']/following-sibling::td")
  private WebElement comments;

  @FindBy(how = How.ID, using = "comments")
  private WebElement additionalComments;
  
  @FindBy(how = How.LINK_TEXT, using = "REJECT OFFER")
  private WebElement rejectOffer;

  @FindBy(how = How.LINK_TEXT, using = "CANCEL")
  private WebElement cancelButton;

  /**
   * Checks that the offer is with the expected status (i.e. 'You have rejected
   * this offer')
   * 
   * @param expectedStatusMessage
   * @return
   */
  public boolean verifyOfferStatus(String expectedStatusMessage) {
    return offerStatusText.getText().contains(expectedStatusMessage);
  }
  
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
    if (getMonthlyPrice() != monthlyPrice) {
      Log.messageRed("Invalid monthly price");
      return false;
    }
    if (!getPropertyShortDescription().equals(shortDescription)) {
      Log.messageRed("Invalid short description");
      return false;
    }
    // A cosmetic bug here https://easyproperty.atlassian.net/browse/WEB-1950
    if (!getPropertyAddressLine().equals(String.format("%d bed %s%s", beds, type, address))) {
      Log.messageRed("Invalid address / title line");
      return false;
    }
    return true;
  }

  /**
   * Gets the displayed offer date
   * 
   * @return
   * @throws ParseException
   */
  public Date getOfferDate() throws ParseException {
    // Bug about the date format inconsistency
    // https://easyproperty.atlassian.net/browse/WEB-1947
    SimpleDateFormat dateFormatter = new SimpleDateFormat("d/M/y");
    return dateFormatter.parse(dateOfOffer.getText());
  }

  /**
   * Gets the offered monthly rent
   * 
   * @return
   */
  public double getMonthlyRent() {
    return parsePoundPrice(monthlyRent.getText());
  }

  /**
   * Gets the displayed tenancy start date
   * 
   * @return
   * @throws ParseException
   */
  public Date getTenancyStartDate() throws ParseException {
    return parseDate(tenancyStartDate.getText());
  }

  /**
   * Gets the displayed tenancy start date
   * 
   * @return
   * @throws ParseException
   */
  public double getDeposit() {
    return parsePoundPrice(deposit.getText());
  }


  /**
   * Types a comment into the 'Type your aditional comments here...' field
   * 
   * @param additionalComments
   */
  public void enterComment(String additionalComments) {
    this.additionalComments.sendKeys(additionalComments);
  }

  /**
   * Click the REJECT OFFER button and handle the confirmation message that
   * appears
   * 
   * @param confirm
   *          whether we should confirm or cancel on the the confirmation
   *          message
   */
  public boolean rejectOffer(boolean confirm) {
    rejectOffer.click();
    return respondToConfirmationMessage("Are you sure?", confirm);
  }

  /**
   * Clicks the cancel button and verifies that the 'You've received an offer!' popup is closed
   * @return
   */
  public boolean cancel() {
    cancelButton.click();
    return Sync.wait(() -> isMissing(By.id("view-offer")));
  }
  
  /**
   * Verify the displayed offer details
   * 
   * @param offerDate
   * @param tenantNames
   * @param mobile
   * @param monthlyRent
   * @param tenancyStartDate
   * @param tenancyLength
   * @param deposit
   * @param furnishing
   * @param ocupants
   * @param ocupantsOver18
   * @param comment
   * @return
   * @throws ParseException
   */
  public boolean verifyDisplayedOfferDetails(Date offerDate, String tenantNames, String mobile, double monthlyRent,
      Date tenancyStartDate, String tenancyLength, double deposit, String furnishing, int ocupants, int ocupantsOver18,
      String comment) throws ParseException {
    if (!getOfferDate().equals(offerDate)) {
      Log.messageRed("Invalid offer date");
      return false;
    }
    if (!this.tenantName.getText().equals(tenantNames)) {
      Log.messageRed("Invalid tenant names");
      return false;
    }
    if (!this.mobile.getText().equals(mobile)) {
      Log.messageRed("Invalid mobile number");
      return false;
    }
    if (getMonthlyRent() != monthlyRent) {
      Log.messageRed("Invalid monthly rent");
      return false;
    }
    if (!getTenancyStartDate().equals(tenancyStartDate)) {
      Log.messageRed("Invalid tenancy start date");
      return false;
    }
    if (!this.lengthOfTenancy.getText().equals(tenancyLength)) {
      Log.messageRed("Invalid tenancy length");
      return false;
    }
    if (getDeposit() != deposit) {
      Log.messageRed("Invalid deposit");
      return false;
    }
    if (!this.furnished.getText().equals(furnishing)) {
      Log.messageRed("Invalid furnished status");
      return false;
    }
    if (!this.numberOfOccupants.getText().equals(Integer.toString(ocupants))) {
      Log.messageRed("Invalid number of ocupants");
      return false;
    }
    if (!this.numberOfOccupantsOver18.getText().equals(Integer.toString(ocupantsOver18))) {
      Log.messageRed("Invalid number of ocupants over 18");
      return false;
    }
    // Optional values
    if (comment != null) {
      if (!this.comments.getText().equals(comment)) {
        Log.messageRed("Invalid comment");
        return false;
      }
    }
    return true;
  }

}
