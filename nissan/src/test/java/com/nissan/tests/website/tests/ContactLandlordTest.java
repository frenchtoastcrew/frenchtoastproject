package com.easyproperty.tests.website.tests;

import org.testng.annotations.Test;

import com.easyproperty.tests.framework.BrowserProfilesDataProvider;
import com.easyproperty.tests.framework.DefaultProperty;
import com.easyproperty.tests.framework.DefaultUser;
import com.easyproperty.tests.framework.MessageChecker;
import com.easyproperty.tests.framework.Sync;
import com.easyproperty.tests.framework.WebsiteTestBase;
import com.easyproperty.tests.utils.RandomString;
import com.easyproperty.tests.website.pages.ContactLandlord;
import com.easyproperty.tests.website.pages.Inbox;
import com.easyproperty.tests.website.pages.ListFavouriteProperties;
import com.easyproperty.tests.website.pages.SearchResults;
import com.easyproperty.tests.website.pages.elements.FavouriteProperty;
import com.easyproperty.tests.website.pages.elements.MessagePanel;
import com.easyproperty.tests.website.pages.elements.MessageSummary;
import com.easyproperty.tests.website.pages.elements.PropertyPreview;

public class ContactLandlordTest extends WebsiteTestBase {

  @Test(dataProvider = "defaultBrowser", dataProviderClass = BrowserProfilesDataProvider.class)
  public void arrangeViewingLoggedIn(String browserProfile) throws Exception
  {
    startTest("Arrange a viewing while logged in", browserProfile);

    startStep("Register a new user and log in with it on the site");
    String userName = createUserVerifyAndLogin(true);
    endStep();

    startStep("Search for the property created by the default test user and open its preview");
    SearchResults searchResults = DefaultProperty.getSearchResultsWithTheProperty(wd);
    PropertyPreview preview = searchResults.getSearchResult(1).click();
    String propertyId = preview.getId();
    endStep();

    startStep("Click the 'Contact landlord' button. The form that appears shows the correct data for the property");
    ContactLandlord contactPage = preview.contactLandlordLoggedIn();
    endStep(contactPage.verifyDisplayedPropertyDetails(DefaultProperty.BEDS, DefaultProperty.PROPERTY_TYPE_MEDIUM,
        DefaultProperty.ADDRESS, DefaultProperty.SHORT_DESCRIPTION, DefaultProperty.MONTHLY_PRICE));
    
    startStep("The thumbnail displayed for the property inside the form is showing the correct image");
    endStep(contactPage.verifyThumbnailColour(DefaultProperty.MAIN_PHOTO_COLOR));

    startStep("The email field should be correctly filled with the current user's email");
    endStep(contactPage.getEnteredEmail().equals(userName));

    startStep("Select that you would like to arrange a viewing");
    contactPage.selectArrangeViewing();
    endStep();

    startStep("Enter first and last name, home and mobile phone");
    String firstName = RandomString.randomAlphabetic(6, true);
    String lastName = RandomString.randomAlphabetic(5, true);
    contactPage.enterFirstName(firstName);
    contactPage.enterLastName(lastName);
    // Not much validation is currently present for phone numbers
    contactPage.enterHomePhone("1");
    contactPage.enterMobilePhone("0");
    endStep();

    startStep("Enter some text in the message field");
    String message = RandomString.randomAlphabetic(1, false);
    contactPage.enterMessage(message);
    endStep();

    startStep("Click the send offer button. The contact form is closed and a 'Success' message appears briefly");
    endStep(contactPage.send() && searchResults.verifySuccessMessage("Success"));

    startStep("Click the 'Contact landlord' button for the property again. The contact form appears again");
    preview.contactLandlordLoggedIn();
    endStep();

    startStep("The user has in his inbox the same 'Your offer has been sent' message sent to the landlord");
    String tenantTitleAndNames = "Mr " + firstName + " " + lastName;
    endStep(MessageChecker.verifyViewingRequestMessage(userName, tenantTitleAndNames, "0", message,
        DefaultProperty.ADDRESS3));

    startStep("Log out and then open the inbox page for the landlord");
    searchResults.clickSignOutButton();
    Inbox inbox = goToInbox(DefaultUser.USERNAME, DefaultUser.PASSWORD);
    endStep();

    startStep("Click the summary of the last message. It is from the correct user, and is a 'VIEWING ENQUIRY' "
        + "message with the correct property address");
    MessageSummary summary = inbox.getVisibleMessageSummary(1);
    summary.click();
    MessagePanel msg = inbox.getLastMessagePanel();
    endStep(msg.isFrom() && msg.getSender().equals(firstName + " " + lastName)
        && msg.getHeader().equals("VIEWING ENQUIRY") && msg.getAddress().contains(DefaultProperty.ADDRESS2));

    startStep("The contents of the last message are correct and the same as the message in the tenant's inboxs");
    endStep(MessageChecker.verifyViewingRequestMessage(userName, tenantTitleAndNames, "0", message,
        DefaultProperty.ADDRESS3));
    
    startStep("Type some reply in the reply box and send it. The reply message appears on top of the message thread");
    String reply = RandomString.randomAlphabetic(10, false);
    inbox.typeReply(reply);
    inbox.submitReply();
    Sync.wait(() -> inbox.getMessagePanelsCount() == 2, LONG_WAIT);
    endStep(inbox.getMessagePanel(1).getMessageContentsElement().getAttribute("innerHTML").contains(reply));

    startStep("Log in with the tenant again and go to the favourites page. There should be one favourite");
    inbox.clickSignOutButton();
    ListFavouriteProperties favouritesPage = goToFavouriteProperties(userName, GENERATED_USER_PASS);
    endStep(favouritesPage.getNumberOfFavourites() == 1);

    startStep("The favourite shown is the default property");
    FavouriteProperty favourite = favouritesPage.getFavouriteProperty(1); 
    endStep(favourite.getProprtyId().equals(propertyId) && favourite.verifyImageColor(DefaultProperty.MAIN_PHOTO_COLOR));

    startStep("2 messages related to this favourite property have been received");
    endStep(favourite.getMessageCount() == 2);

    startStep("Click the favorite to open the 'list favourite' page and the messages tab. There is only one message thread");
    Inbox favouriteMessages = favourite.click().getInboxFromTheMessagesTab();
    endStep(favouriteMessages.getVisibleMessageSummariesCount() == 1);

    startStep("Inside the message thread there are 2 message panels");
    endStep(Sync.wait(() -> favouriteMessages.getMessagePanelsCount() == 2));
    
    startStep("The top message is from the landlord, its title is 'RE: VIEWING ENQUIRY' and it contains the correct reply");
    msg = favouriteMessages.getMessagePanel(1);
    endStep(msg.isFrom() && msg.getSender().equals(DefaultUser.FIRST_NAME + " " + DefaultUser.LAST_NAME)
        && msg.getHeader().equals("RE: VIEWING ENQUIRY")
        && msg.getMessageContentsElement().getAttribute("innerHTML").contains(reply));

    endTest();
  }
}
