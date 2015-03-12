package com.nissan.tests.website.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.nissan.tests.framework.WebsiteTestBase;
import com.nissan.tests.utils.RandomString;
import com.nissan.tests.website.pages.Login;

/**
 * Tests for registering new users through our website UI
 * 
 * @author ivarbanovv
 * 
 */
public class RegisterTest extends WebsiteTestBase {

  @DataProvider
  public Object[][] positiveRegister() throws Exception {
    return excelDataProvider();
  }

  @Test(dataProvider = "positiveRegister")
  public void positiveRegisterTest(String browserProfile, String username, String password, Double title, String fName,
      String lName, Double telephoneType, Double phoneNumber, Double howDidYouHear, boolean userType) throws Exception {
    String randomEmail = RandomString.generateEmail();

    String phoneNumberStr = String.valueOf(phoneNumber.intValue());

    startTest("Registering user  " + randomEmail, browserProfile);

    startStep("Open Registration page");
    Login loginPage = goToLoginPage();
    endStep();

    startStep("Fill out user data");
    loginPage.registerUser(userType, randomEmail, password, title.intValue(), fName, lName, telephoneType.intValue(),
        phoneNumberStr, howDidYouHear.intValue());
    endStep();

    startStep("Proceed with verification page");
    // Thread.sleep(4000); //DEBUG
    wd.get(getUserVerificationURL(randomEmail));
    endStep();

    startStep("Check if user is successfuly verified");
    endStep(wd.getCurrentUrl().endsWith("listownedproperties"));

    endTest();

  }

  @DataProvider
  public Object[][] negativeRegister() throws Exception {
    return excelDataProvider();
  }

  @Test(dataProvider = "negativeRegister", enabled=false)
  public void negativeRegisterTest(String browserProfile, String username, String password, Double title, String fName,
      String lName, Double telephoneType, Double phoneNumber, Double howDidYouHear, boolean userType) throws Exception {

    String phoneNumberStr = String.valueOf(phoneNumber.intValue());

    startTest("Registering user  " + username, browserProfile);

    startStep("Open Registration page");
    Login loginPage = goToLoginPage();
    endStep();

    startStep("Fill out user data");
    loginPage.registerUser(userType, username, password, title.intValue(), fName, lName, telephoneType.intValue(),
        phoneNumberStr, howDidYouHear.intValue());
    endStep();

    // TODO finish final verifications
    endTest();

  }

}
