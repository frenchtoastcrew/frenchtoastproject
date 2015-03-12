package com.easyproperty.tests.website.tests;

import java.util.Date;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.easyproperty.tests.framework.BrowserProfilesDataProvider;
import com.easyproperty.tests.framework.DefaultProperty;
import com.easyproperty.tests.framework.DefaultUser;
import com.easyproperty.tests.framework.MessageChecker;
import com.easyproperty.tests.framework.Sync;
import com.easyproperty.tests.framework.WebsiteTestBase;
import com.easyproperty.tests.utils.RandomString;
import com.easyproperty.tests.website.pages.ListFavouriteProperties;
import com.easyproperty.tests.website.pages.Login;
import com.easyproperty.tests.website.pages.MakeAnOffer;
import com.easyproperty.tests.website.pages.OwnedPropertyDetails;
import com.easyproperty.tests.website.pages.SearchResults;
import com.easyproperty.tests.website.pages.YouveReceivedAnOffer;
import com.easyproperty.tests.website.pages.elements.FavouriteProperty;
import com.easyproperty.tests.website.pages.elements.Offer;
import com.easyproperty.tests.website.pages.elements.PropertyPreview;

public class MakeOfferTest extends WebsiteTestBase {

  @Test(dataProvider = "defaultBrowser", dataProviderClass = BrowserProfilesDataProvider.class)
  public void makeOfferLoggedInAndReject(String browserProfile) throws Exception
  {
    startTest("Make offer while logged in and reject it", browserProfile);

    startStep("Register a new user and log in with it on the site");
    String userName = createUserVerifyAndLogin(true);
    endStep();

    startStep("Search for the property created by the default test user and open its preview");
    SearchResults searchResults = DefaultProperty.getSearchResultsWithTheProperty(wd);
    PropertyPreview preview = searchResults.getSearchResult(1).click();
    String propertyId = preview.getId();
    endStep();

    startStep("Click the 'Make offer' button. The form that appears shows the correct data for the property");
    MakeAnOffer offerPage = preview.makeOfferLoggedIn();
    endStep(offerPage.verifyDisplayedPropertyDetails(DefaultProperty.BEDS, DefaultProperty.PROPERTY_TYPE_MEDIUM,
        DefaultProperty.ADDRESS, DefaultProperty.SHORT_DESCRIPTION, DefaultProperty.MONTHLY_PRICE));
    
    startStep("The tenancy start date should be pre-filled with tomorrow's date");
    Date todayAtMidnight = dateFormatter.parse(dateFormatter.format(new Date()));
    Date tomorrow = new Date(todayAtMidnight.getTime() + (1000 * 60 * 60 * 24));
    endStep(tomorrow.equals(offerPage.getTenancyStartDate()));

    startStep("The tenancy duration should be pre-filled with the ad's minimum tenancy");
    endStep(offerPage.getSelectedTenancyDuration().equals(DefaultProperty.MINIMUM_TENANCY));

    startStep("The deposit field should be pre-filled with the ad's deposit");
    endStep(offerPage.getEnteredDeposit() == DefaultProperty.DEPOSIT);

    startStep("The furnished field should show the ad's furnished status");
    endStep(offerPage.getFurnishedText().toUpperCase().equals(DefaultProperty.FURNISHING.toUpperCase()));

    startStep("The email field should also be correctly filled with the current user's email");
    endStep(offerPage.getEnteredEmail().equals(userName));

    startStep("Select 10 for number of occupants and 1 for number of occupants over 18");
    offerPage.selectNumberOfOccupants(10);
    offerPage.selectNumberOfOccupantsOver18(1);
    endStep();

    startStep("Enter some text in the additional comments field");
    String comment = RandomString.randomAlphabetic(10, false);
    offerPage.enterAdditionalComment(comment);
    endStep();

    startStep("Enter first and last names and a mobile phone");
    String firstName = RandomString.randomAlphabetic(6, true);
    String lastName = RandomString.randomAlphabetic(5, true);
    offerPage.enterFirstName(firstName);
    offerPage.enterLastName(lastName);
    offerPage.enterMobilePhone("123456");
    endStep();

    startStep("Click the send offer button. The 'Make offer' window is closed and "
        + "'Offer Successfully Sent' message appears briefly");
    endStep(offerPage.sendOffer() && searchResults.verifySuccessMessage("Offer Successfully Sent"));

    startStep("Click the 'Make offer' button for the property again. An 'Active Offer' error message appears");
    endStep(preview.makeOfferExpectingError("Active Offer"));

    startStep("Verify that the correct 'Your offer has been sent' message is received by the user");
    endStep(MessageChecker.verifyOfferSentMessage(userName, firstName, propertyId, DefaultProperty.ADDRESS2));
    
    startStep("Log out and then open the owned property details page for the test ad with the user that created it");
    searchResults.clickSignOutButton();
    OwnedPropertyDetails opd = goToOwnedPropertyDetails(propertyId);
    endStep();

    startStep("Verify that the correct 'You have received an offer' message is received by the user");
    String tenantNames = firstName + " " + lastName;
    endStep(MessageChecker.verifyOfferReceivedMessage(DefaultUser.USERNAME, DefaultUser.FIRST_NAME, tenantNames,
        DefaultProperty.ADDRESS2));

    startStep("Locate the offer by the user that just submitted it. It should show the correct information");
    Offer offer = opd.getOfferByTenantEmail(userName);
    endStep(offer.verifyDisplayedDetails(DefaultProperty.MONTHLY_PRICE, todayAtMidnight, tenantNames));

    startStep("Click the view button on the offer. The 'You've received an offer' popup should appear");
    YouveReceivedAnOffer receivedOfferPage = offer.view();
    endStep();
    
    startStep("The offer status is 'Please check this offer carefully, you may accept or reject this offer'");
    endStep(receivedOfferPage
        .verifyOfferStatus("Please check this offer carefully, you may accept or reject this offer"));

    startStep("The correct informatioin about the property is displayed on top of the popup");
    endStep(receivedOfferPage.verifyDisplayedPropertyDetails(DefaultProperty.BEDS, DefaultProperty.PROPERTY_TYPE_MEDIUM,
        DefaultProperty.ADDRESS, DefaultProperty.SHORT_DESCRIPTION, DefaultProperty.MONTHLY_PRICE));

    startStep("All the detils about the offer displayed below it are also correct");
    endStep(receivedOfferPage.verifyDisplayedOfferDetails(todayAtMidnight, tenantNames, "123456",
        DefaultProperty.MONTHLY_PRICE, tomorrow, DefaultProperty.MINIMUM_TENANCY, DefaultProperty.DEPOSIT, DefaultProperty.FURNISHING,
        10, 1, comment));

    startStep("Enter a comment with reason for the rejection, then reject the offer");
    String rejectionComment = String.format("Dear %s, there are far too many teenagers in your household.", firstName);
    receivedOfferPage.enterComment(rejectionComment);
    endStep(receivedOfferPage.rejectOffer(true));

    startStep("The new status displayed for the offer becomes 'You have rejected this offer'");
    endStep(Sync.wait(() -> receivedOfferPage.verifyOfferStatus("You have rejected this offer")));
    
    startStep("Click the CANCEL button to close the You've received an offer! popup");
    endStep(receivedOfferPage.cancel());

    startStep("Refresh the page and locate the offer on the page again. It should be marked as 'Rejected'");
    wd.navigate().refresh();
    offer = opd.getOfferByTenantEmail(userName);
    endStep(offer.isRejected());

    startStep("Log in with the tenant again and go to the favourites page. There should be one favourite");
    opd.clickSignOutButton();
    ListFavouriteProperties favouritesPage = goToFavouriteProperties(userName, GENERATED_USER_PASS);
    endStep(favouritesPage.getNumberOfFavourites() == 1);

    startStep("The favourite shown is the default property");
    FavouriteProperty favourite = favouritesPage.getFavouriteProperty(1); 
    endStep(favourite.getProprtyId().equals(propertyId));

    startStep("It was added as a favorite today");
    endStep(favourite.getFavouritedDate().equals(todayAtMidnight));

    startStep("2 messages related to this favourite property have been received");
    endStep(favourite.getMessageCount() == 2);

    startStep("Verify that the correct 'Your offer has been rejected' message is received by the user");
    endStep(MessageChecker.verifyOfferRejectedMessage(userName, firstName, propertyId, DefaultProperty.ADDRESS2));

    endTest();
  }

  @Test(dataProvider = "defaultBrowser", dataProviderClass = BrowserProfilesDataProvider.class)
  public void makeOfferNotLoggedIn(String browserProfile) throws Exception {

    startTest("Make offer while not logged in", browserProfile);

    startStep("Register a new user and verify it, but do not log in");
    String userName = createUserAndVerify(true);
    endStep();

    startStep("Search for the default test property and open its preview");
    SearchResults searchResults = DefaultProperty.getSearchResultsWithTheProperty(wd);
    PropertyPreview preview = searchResults.getSearchResult(1).click();
    endStep();

    startStep("Click the 'Make offer' button. The login/register form appears in a pop-up");
    Login loginPage = preview.makeOfferLoggedOut();
    endStep();

    startStep("Sign in with the registered user. The 'Make an offer' popup should appear");
    loginPage.loginUser(userName,GENERATED_USER_PASS);
    MakeAnOffer offerPage = PageFactory.initElements(wd, MakeAnOffer.class);
    endStep();

    startStep("The thumbnail displayed for the property inside the form is showing the correct image");
    endStep(offerPage.verifyThumbnailColour(DefaultProperty.MAIN_PHOTO_COLOR));

    startStep("Select 1 for number of occupants and 1 for number of occupants over 18");
    offerPage.selectNumberOfOccupants(1);
    offerPage.selectNumberOfOccupantsOver18(1);
    endStep();

    startStep("Enter first and last names and a mobile phone");
    String firstName = RandomString.randomAlphabetic(6, true);
    String lastName = RandomString.randomAlphabetic(5, true);
    offerPage.enterFirstName(firstName);
    offerPage.enterLastName(lastName);
    offerPage.enterMobilePhone("123456");
    endStep();

    startStep("Click the send offer button. The 'Make offer' window is closed and "
        + "'Offer Successfully Sent' message appears briefly");
    endStep(offerPage.sendOffer() && searchResults.verifySuccessMessage("Offer Successfully Sent"));

    startStep("Verify that the correct 'You have received an offer' message is received by the landlord");
    String tenantNames = firstName + " " + lastName;
    endStep(MessageChecker.verifyOfferReceivedMessage(DefaultUser.USERNAME, DefaultUser.FIRST_NAME, tenantNames,
        DefaultProperty.ADDRESS2));

    endTest();
  }

  @Test(dataProvider = "defaultBrowser", dataProviderClass = BrowserProfilesDataProvider.class)
  public void makeOfferNotLoggedInRegister(String browserProfile) throws Exception {

    startTest("Make offer while not logged in with new user registration", browserProfile);

    startStep("Search for the default test property and open its preview");
    SearchResults searchResults = DefaultProperty.getSearchResultsWithTheProperty(wd);
    PropertyPreview preview = searchResults.getSearchResult(1).click();
    endStep();

    startStep("Click the 'Make offer' button. The login/register form appears in a pop-up");
    Login loginPage = preview.makeOfferLoggedOut();
    endStep();

    startStep("Fill in all fields in the user registration form and hit create an account. "
        + "Dismiss the 'We've sent you an email' message that appears");
    String userName = RandomString.generateEmail();
    String firstName = RandomString.randomAlphabetic(6, true);
    String lastName = RandomString.randomAlphabetic(5, true);
    loginPage.registerUser(false, userName, GENERATED_USER_PASS, 1, firstName, lastName, 1, "00123456789056466", 4);
    endStep(searchResults.verifyMessageAndDismiss("ve sent you an email"));

    startStep("Click the 'Make offer' button again. The login/register form appears again");
    preview.makeOfferLoggedOut();
    endStep();

    startStep("The user has received a 'Verify your account' message");
    endStep(MessageChecker.verifyNewUser(userName));

    // Once the user has completed the email verification this scenario becomes
    // identical to the making offer while logged-in

    endTest();
  }

}
