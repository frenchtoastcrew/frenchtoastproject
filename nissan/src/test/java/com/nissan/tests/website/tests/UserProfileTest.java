package com.easyproperty.tests.website.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.easyproperty.tests.framework.WebsiteTestBase;
import com.easyproperty.tests.website.pages.Profile;

/**
 * Test coverage for the user profile section page
 * 
 * @author ivo varbanov
 * 
 */
public class UserProfileTest extends WebsiteTestBase {

  @DataProvider
  public Object[][] positiveProfileEdit() throws Exception {
    return excelDataProvider();
  }

  @Test(dataProvider = "positiveProfileEdit")
  public void positiveProfileEditTest(String browserProfile, Double title, String fName, String lName, boolean gender,
      Double landline, Double mobile, String dateOfBirth, String postcode, String house, String street, String town,
      String county, boolean landlord, boolean epMarketing, boolean thirdPartyMarketing) throws Exception {

    String landlineNumber = String.valueOf(landline.intValue());
    String mobileNumber = String.valueOf(mobile.intValue());

    startTest("Positive scenario for edit on user profile page", browserProfile);

    startStep("Register a new user and log in on the site");
    String userName = createUserVerifyAndLogin(true);
    endStep();

    startStep("Open user profile page");
    Profile profilePage = goToProfilePage();
    endStep();

    startStep("Update and submit changes to user profile page");
    profilePage.updateProfileDetails(title.intValue(), fName, lName, gender, landlineNumber, mobileNumber, dateOfBirth,
        false);
    profilePage.updateProfileAddress(postcode, house, street, town, county, false);
    profilePage.updateProfilePreferences(landlord, epMarketing, thirdPartyMarketing, true);
    endStep();

    startStep("Verify that changes have been saved successfully");
    endStep(new String(profilePage.updateMessageHeader()).equals("Success"));

    endTest();

  }

  @DataProvider
  public Object[][] negativeProfileEdit() throws Exception {
    return excelDataProvider();
  }

  // TODO finish negative tests
  @Test(dataProvider = "negativeProfileEdit")
  public void negativeProfileEditTest(String browserProfile, Double title, String fName, String lName, boolean gender,
      Double landline, Double mobile, String dateOfBirth, String postcode, String house, String street, String town,
      String county, boolean landlord, boolean epMarketing, boolean thirdPartyMarketing) throws Exception {

    String landlineNumber = String.valueOf(landline.intValue());
    String mobileNumber = String.valueOf(mobile.intValue());

    startTest("Positive scenario for edit on user profile page", browserProfile);

    startStep("Register a new user and log in on the site");
    String userName = createUserVerifyAndLogin(true);
    endStep();

    startStep("Open user profile page");
    Profile profilePage = goToProfilePage();
    endStep();

    startStep("Update and submit changes to user profile page");
    profilePage.updateProfileDetails(title.intValue(), fName, lName, gender, landlineNumber, mobileNumber, dateOfBirth,
        false);
    profilePage.updateProfileAddress(postcode, house, street, town, county, false);
    profilePage.updateProfilePreferences(landlord, epMarketing, thirdPartyMarketing, true);
    endStep();

    startStep("Verify that changes have been saved successfully");
    endStep(new String(profilePage.updateMessageHeader()).equals("Error"));

    endTest();

  }

}
