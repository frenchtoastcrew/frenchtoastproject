package com.nissan.tests.website.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.nissan.tests.framework.Log;
import com.nissan.tests.framework.PageBase;
import com.nissan.tests.framework.Sync;

/**
 * Page object for the login page
 * 
 * @author ivarbanovv
 * 
 */
public class Login extends PageBase {

  public Login(WebDriver wd) {
    super(wd);
    // Verify that we are on the login page / wait for the login popup to open
    Assert.assertTrue(Sync.wait(() -> !isMissing(By.className("show-register"))), "We are not on the login page");
  }

  @FindBy(how = How.CLASS_NAME, using = "show-register")
  private WebElement registerTab;

  @FindBy(how = How.CLASS_NAME, using = "show-signin")
  private WebElement signinTab;

  // Sign in tab elements

  @FindBy(how = How.NAME, using = "username")
  private WebElement username_existing;

  @FindBy(how = How.NAME, using = "password")
  private WebElement password_existing;

  @FindBy(how = How.CLASS_NAME, using = "forgotten_password_link")
  private WebElement forgottenPassword;

  @FindBy(how = How.NAME, using = "submit-login")
  private WebElement submitButton;

  // Create account tab elements

  @FindBy(how = How.XPATH, using = ".//*[@id='panel-register']/div[1]/ul/li/label[1]")
  private WebElement landlordTab;

  @FindBy(how = How.XPATH, using = ".//*[@id='panel-register']/div[1]/ul/li/label[2]")
  private WebElement tenantTab;

  @FindBy(how = How.NAME, using = "username1")
  private WebElement username_new;

  @FindBy(how = How.NAME, using = "password1")
  private WebElement password_new;

  @FindBy(how = How.NAME, using = "firstName")
  private WebElement firstName;

  @FindBy(how = How.NAME, using = "lastName")
  private WebElement lastName;

  @FindBy(how = How.NAME, using = "title")
  private WebElement userTitle;

  @FindBy(how = How.NAME, using = "phone_type")
  private WebElement phoneType;

  @FindBy(how = How.NAME, using = "telephone")
  private WebElement telephone;

  @FindBy(how = How.NAME, using = "source")
  private WebElement source;

  @FindBy(how = How.NAME, using = "tcagree")
  private WebElement termsAgree;

  @FindBy(how = How.NAME, using = "messages")
  private WebElement spamMessages;

  @FindBy(how = How.NAME, using = "submit-register")
  private WebElement submitRegister;

  @FindBy(how = How.XPATH, using = ".//*[@id='form-validation']/fieldset/div[1]/h2")
  private WebElement alertMessageError;

  @FindBy(how = How.XPATH, using = "//ul[contains(@class,'appearance-input-error')]")
  private WebElement apperanceInputError;

  @FindBy(how = How.XPATH, using = "//label[contains(@id,'username-error')]")
  private WebElement usernameError;


  public void loginUser(String username, String password) {
    signinTab.click();
    username_existing.sendKeys(username);
    password_existing.sendKeys(password);
    submitButton.click();
    Log.comment("User login information submitted.");
    // Wait for the new page to load
    Sync.wait(() -> !wd.getCurrentUrl().contains("signin"));
  }

  public void loginUserNoSubmit(String username, String password) {
    signinTab.click();
    username_existing.sendKeys(username);
    password_existing.sendKeys(password);
  }

  public String errorText(String element) {

    switch (element) {

      case "usernameError":

        return (usernameError.getText());

      case "apperanceInputError":

        return (apperanceInputError.getText());

      default:
        Log.messageRed("Error message was not found");
        return null;
    }

  }

  public String alertErrorText(String element) {

    if (element != null) {
      return (alertMessageError.getText());
    }

    else
      Log.message("Skipping alert message check");
    return null;
  }

  public String alertErrorText() {

    return (alertMessageError.getText());
  }

  public Verify registerUser(Boolean landlord, String username, String password, int title, String fName, String lName,
      int telephoneType, String phoneNumber, int howDidYouHear) {
    registerTab.click();
    if (landlord) {
      landlordTab.click();
    } else
      tenantTab.click();
    username_new.sendKeys(username);
    password_new.sendKeys(password);

    // select value from title drop down
    Select select = new Select(userTitle);
    select.selectByIndex(title);

    // fill in FName and LName
    firstName.sendKeys(fName);
    lastName.sendKeys(fName);

    // select phone type
    select = new Select(phoneType);
    select.selectByIndex(telephoneType);

    // fill in telephone number
    telephone.sendKeys(phoneNumber);

    // select source type - how did you hear about us drop down
    select = new Select(source);
    select.selectByIndex(howDidYouHear);

    // accept terms & conditions
    termsAgree.click();

    submitRegister.click();
    Log.comment("User register information submitted.");

    // TODO
    return PageFactory.initElements(wd, Verify.class);

  }

}
