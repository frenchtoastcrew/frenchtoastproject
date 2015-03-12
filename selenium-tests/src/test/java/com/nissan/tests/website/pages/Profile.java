package com.nissan.tests.website.pages;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

import com.nissan.tests.framework.PageBase;

/**
 * Page object for the Profile page
 *
 * @author kpopova
 */
public class Profile extends PageBase {

  public Profile(WebDriver wd) {
    super(wd);
  }

  // User profile details section

  @FindBy(how = How.NAME, using = "title")
  public WebElement pUserTitle;

  @FindBy(how = How.NAME, using = "firstName")
  public WebElement pFirstName;

  @FindBy(how = How.NAME, using = "lastName")
  public WebElement pLastName;

  @FindBy(how = How.NAME, using = "email")
  public WebElement pEmail;

  @FindBy(how = How.NAME, using = "telephone")
  public WebElement pTelephone;

  @FindBy(how = How.NAME, using = "mobile")
  public WebElement pMobile;

  @FindBy(how = How.ID, using = "gender-male")
  public WebElement pGenderMale;

  @FindBy(how = How.ID, using = "gender-female")
  public WebElement pGenderFemale;

  @FindBy(how = How.XPATH, using = ".//*[@id='dateofbirthpicker']")
  public WebElement pDateOfBirth;

  @FindBy(how = How.XPATH, using = ".//*[@id='ui-datepicker-div']")
  public WebElement calendar;

  // User profile address section

  @FindBy(how = How.NAME, using = "postcode")
  public WebElement pPostcode;

  @FindBy(how = How.CLASS_NAME, using = "postcode_lookup_button")
  public WebElement pPostcodeLookupButton;

  @FindBy(how = How.NAME, using = "address1")
  public WebElement pHouse;

  @FindBy(how = How.NAME, using = "address2")
  public WebElement pStreet;

  @FindBy(how = How.NAME, using = "address3")
  public WebElement pTown;

  @FindBy(how = How.NAME, using = "address4")
  public WebElement pCounty;

  // User profile preferences section

  @FindBy(how = How.XPATH, using = ".//*[@id='tenant-group']")
  public WebElement pLandlord;

  @FindBy(how = How.XPATH, using = ".//*[@id='tenant-group']")
  public WebElement pTenant;

  @FindBy(how = How.NAME, using = "both")
  public WebElement pBoth;

  @FindBy(how = How.NAME, using = "marketing")
  public WebElement pMarketing;

  @FindBy(how = How.NAME, using = "partnerMarketing")
  public WebElement pPartnerMarketing;

  @FindBy(how = How.ID, using = "submitbutton")
  private WebElement pSaveChangesButton;

  @FindBy(how = How.CLASS_NAME, using = "change_password")
  private WebElement chqngePasswordButton;

  @FindBy(how = How.CLASS_NAME, using = "change-email")
  private WebElement changeEmailAddressButton;

  @FindBy(how = How.XPATH, using = "html/body/div[1]/div/div/h2")
  private WebElement pHeaderMessage;

  @FindBy(how = How.XPATH, using = ".//*[@id='userform']/fieldset/div[1]/div/div/div[1]/div[2]/div/div/ul/li")
  private WebElement pfNameErrMessage;

  @FindBy(how = How.XPATH, using = ".//*[@id='userform']/fieldset/div[1]/div/div/div[1]/div[3]/div/div/ul/li")
  private WebElement plNameErrMessage;
  
  @FindBy(how = How.XPATH, using = ".//*[@id='userform']/fieldset/div[1]/div/div/div[3]/div[1]/div/div/ul/li")
  private WebElement pLandLineErrMessage;

  @FindBy(how = How.XPATH, using = ".//*[@id='userform']/fieldset/div[1]/div/div/div[3]/div[2]/div/div/ul/li")
  private WebElement pMobileErrMessage;

  @FindBy(how = How.XPATH, using = ".//*[@id='userform']/fieldset/div[1]/div/div/div[6]/div/div[1]/ul/li")
  private WebElement pPostcodeErrMessage;
  
  /**
   * Selects the Title value
   *
   * @param title
   * 
   */
  public void selectTitle(String text) {
    Select titleSelect = new Select(pUserTitle);
    titleSelect.selectByVisibleText(text);
  }

  /**
   * Clicks the Change Password Button
   */
  public void clickChangePassword() {
    chqngePasswordButton.click();
  }

  /**
   * Clicks the Change Email Address Button
   */
  public void clickChangeEmailAddress() {
    changeEmailAddressButton.click();
  }

  /**
   * Clicks the Lookup postcode
   */
  public void lookupPostcode() {
    pPostcodeLookupButton.click();
  }

  /**
   * Clicks the Save changes button
   */
  public void clickSaveChanges() {
    pSaveChangesButton.click();
  }

  /**
   * Selects a predefined date from calendar widget on user profile page
   * 
   * @author ivo varbanov
   *
   */
  public void updateCalendar() {
    List<WebElement> columns = calendar.findElements(By.tagName("td"));

    for (WebElement cell : columns) {
      // Select 13th Date
      if (cell.getText().equals("1")) {
        cell.findElement(By.linkText("1")).click();
        break;
      }
    }
  }

  /**
   * Selects a date from calendar widget on user profile page using a String
   * parameter
   * 
   * @author ivo varbanov
   * @param date
   *          {format ddmmyyyy}
   */
  public void updateCalendar(String date) {
    pDateOfBirth.click();
    DateTimeFormatter formatter = DateTimeFormat.forPattern("ddmmyyyy");
    DateTime dt = formatter.parseDateTime(date);
    String day = String.valueOf(dt.getDayOfMonth());
    String month = String.valueOf(dt.getMonthOfYear());
    String year = String.valueOf(dt.getYear());

    // Select Year
    WebElement yearObject = calendar.findElement(By.className("ui-datepicker-year"));
    Select select = new Select(yearObject);
    select.selectByValue(year);

    // Select month
    WebElement monthObject = calendar.findElement(By.className("ui-datepicker-month"));
    select = new Select(monthObject);
    select.selectByValue(month);

    // Select day
    List<WebElement> columns = calendar.findElements(By.tagName("td"));
    for (WebElement cell : columns) {
      if (cell.getText().equals(day)) {
        cell.findElement(By.linkText(day)).click();
        break;
      }
    }

  }

  /**
   * Updates user profile page details
   * 
   * @author ivo varbanov
   *
   */
  public void updateProfileDetails(int title, String fName, String lName, boolean gender, String landline,
      String mobile, String dob, boolean submit) {

    Select select = new Select(pUserTitle);
    select.selectByIndex(title);

    pFirstName.sendKeys(fName);
    pLastName.sendKeys(lName);

    if (gender) {
      pGenderMale.click();
    } else {
      pGenderFemale.click();
    }
    pTelephone.sendKeys(landline);
    pMobile.sendKeys(mobile);
    updateCalendar(dob);

    if (submit) {
      pSaveChangesButton.click();
    }
  }

  /**
   * Updates user profile page address
   * 
   * @author ivo varbanov
   *
   */
  public void updateProfileAddress(String postcode, String house, String street, String town, String county,
      boolean submit) {
    pPostcode.sendKeys(postcode);
    pHouse.sendKeys(house);
    pStreet.sendKeys(street);
    pTown.sendKeys(town);
    pCounty.sendKeys(county);

    if (submit) {
      pSaveChangesButton.click();
    }
  }

  /**
   * Updates user profile page preferences
   * 
   * @author ivo varbanov
   *
   */
  public void updateProfilePreferences(boolean landlord, boolean epMarketing, boolean thirdPartyMarketing,
      boolean submit) {

    if (landlord) {
      pLandlord.click();
    } else {
      pTenant.click();
    }

    if (epMarketing != pMarketing.isSelected()) {
      pMarketing.click();
    }
    if (thirdPartyMarketing != pPartnerMarketing.isSelected()) {
      pPartnerMarketing.click();
    }

    if (submit) {
      pSaveChangesButton.click();
    }
  }

  public String updateMessageHeader() {
    return pHeaderMessage.getText();
  }

  public String fNameErrMessage() {
    return pfNameErrMessage.getText();
  }

  public String lNameErrMessage() {
    return plNameErrMessage.getText();
  }
  
  public String landLineErrMessage() {
    return pLandLineErrMessage.getText();
  }
  
  public String mobileErrMessage() {
    return pMobile.getText();
  }
  
  public String postcodeErrMessage() {
    return pPostcode.getText();
  }
}
