package com.easyproperty.tests.website.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.easyproperty.tests.framework.BrowserProfilesDataProvider;
import com.easyproperty.tests.framework.DefaultUser;
import com.easyproperty.tests.framework.Log;
import com.easyproperty.tests.framework.Sync;
import com.easyproperty.tests.framework.WebsiteTestBase;
import com.easyproperty.tests.utils.RandomString;
import com.easyproperty.tests.website.pages.Inbox;
import com.easyproperty.tests.website.pages.ListOwnedProperties;
import com.easyproperty.tests.website.pages.Search;
import com.easyproperty.tests.website.pages.elements.MessagePanel;
import com.easyproperty.tests.website.pages.elements.MessageSummary;

public class InboxTest extends WebsiteTestBase {

  @Test(dataProvider = "defaultBrowser", dataProviderClass = BrowserProfilesDataProvider.class)
  public void messageCountAndLayout(String browserProfile) throws Exception {

    final String header = "VERIFY YOUR EASYPROPERTY ACCOUNT";
    final String SENDER = "easyProperty website";

    startTest("Verify unread messsage indicators and page layout", browserProfile);

    startStep("Register a new user, verify the email and go to the search page. The indicator shows 1 unread message");
    String username = createUserVerifyAndLogin(true);
    Search searchPage = goToSearchPage();
    endStep(searchPage.getNumberOfUnreadMessages() == 1);

    startStep("Click on the Control Panel button to go to the properties page for the user. The indicator shows 1");
    ListOwnedProperties propertiesPage = searchPage.clickControlPanelButton();
    endStep(propertiesPage.getNumberOfUnreadMessages() == 1);

    startStep("Click on the Inbox button to go to the inbox page for the user. The indicator still shows 1");
    Inbox inboxPage = searchPage.clickInboxSubHeaderButton();
    endStep(inboxPage.getNumberOfUnreadMessages() == 1);
    
    startStep("The first message summary is from '" + SENDER + "' and is titled 'VERIFY YOUR EASYPROPERTY ACCOUNT'");
    MessageSummary summary = inboxPage.getVisibleMessageSummary(1);
    endStep(summary.getSender().equals(SENDER) && summary.getHeader().equals(header));

    startStep("The message summary shows only time (i.e. it is from today)");
    endStep(summary.isTimeShown());

    startStep("The message summary is selected");
    endStep(summary.isSelected());

    startStep("Replying is not available for this message, a text about phone/email enquiry is present instead");
    endStep(!inboxPage.isReplyAvailable() && inboxPage.isEnquireByPhoneAndEmailAvailable());
    
    startStep("There is only one message in the message thread");
    endStep(inboxPage.getMessagePanelsCount() == 1);

    startStep("The message is from '" + SENDER + "' and has the correct title");
    MessagePanel message = inboxPage.getMessagePanel(1);
    endStep(message.isFrom() && message.getSender().equals(SENDER) && message.getHeader().equals(header));
    
    startStep("The date in the message header shows the same time from the message summary");
    endStep(message.getDateText().contains(summary.getDateText()));
    
    startStep("The message contains a 'Verify your account to get going' link and a 'VERIFY ME' button");
    WebElement contents = message.getMessageContentsElement();
    WebElement verifyLink = contents.findElement(By.linkText("Verify your account to get going"));
    WebElement verifyButton = contents.findElement(By.linkText("VERIFY ME"));
    endStep(verifyButton.isDisplayed() && verifyLink.isDisplayed());

    startStep("Click on SEARCH to go to the search page. The indicator for unread messages disappears");
    searchPage = inboxPage.clickSearchButton();
    endStep(searchPage.getNumberOfUnreadMessages() == 0);

    startStep("Click on the Control Panel button to go to the properties page for the user. The indicator is still missing");
    propertiesPage = searchPage.clickControlPanelButton();
    endStep(propertiesPage.getNumberOfUnreadMessages() == 0);

    endTest();
  }
  
  @Test(dataProvider = "defaultBrowser", dataProviderClass = BrowserProfilesDataProvider.class)
  public void messageNavigation(String browserProfile) throws Exception {

    startTest("Verify the navigation between messages", browserProfile);

    startStep("Log in with the default test user and go to the inbox page");
    Inbox inboxPage = goToInbox(DefaultUser.USERNAME, DefaultUser.PASSWORD);
    endStep();

    startStep("The first message in the list of messages to the left should be selected");
    MessageSummary summary = inboxPage.getVisibleMessageSummary(1);
    endStep(summary.isSelected());

    startStep("The message preview to the right is showing the right message");
    MessagePanel message = inboxPage.getLastMessagePanel();
    endStep(summary.getHeader().contains(message.getHeader()) && message.getAddress().contains(summary.getSubHeader()));

    startStep("Select the second message. The message preview is again correct");
    MessageSummary newSummary = inboxPage.getVisibleMessageSummary(2);
    newSummary.click();
    endStep(waitForMessageToShowExpectedSummary(inboxPage, newSummary));

    int pagesNumber = inboxPage.getLastDisplayedPageNumber();
    if (pagesNumber > 1) {
      
      startStep("If there are more than one pages of messages, click the second page. It becomes active");
      Log.message("Visible pages: " + pagesNumber);
      inboxPage.selectPage(2);
      endStep(inboxPage.getActivePageNumber()==2);
      
      startStep("New messages are loaded");
      MessageSummary page2Summary = inboxPage.getVisibleMessageSummary(1);
      endStep(!summary.getDateText().equals(page2Summary.getDateText())
          || !summary.getHeader().equals(page2Summary.getHeader()));

      startStep("Select the first message. The preview is again showing the right message");
      page2Summary.click();
      endStep(waitForMessageToShowExpectedSummary(inboxPage, page2Summary));
    }
      
    endTest();
  }


  @Test(dataProvider = "defaultBrowser", dataProviderClass = BrowserProfilesDataProvider.class)
  public void replyToMessage(String browserProfile) throws Exception {

    startTest("Reply to a message", browserProfile);

    startStep("Log in with the default test user and go to the inbox page");
    Inbox inboxPage = goToInbox(DefaultUser.USERNAME, DefaultUser.PASSWORD);
    endStep();

    startStep("Open a message where replying is enabled");
    String originalMessageHeader = null;
    MessageSummary summary = null;
    for (int i = 1; i < inboxPage.getVisibleMessageSummariesCount() + 1; i++) {
      summary = inboxPage.getVisibleMessageSummary(i);
      summary.click();
      waitForMessageToShowExpectedSummary(inboxPage, summary);
      if (inboxPage.isReplyAvailable()) {
        originalMessageHeader = summary.getHeader();
        Log.message("Message header: " + originalMessageHeader);
        break;
      }
    }
    endStep(originalMessageHeader != null);

    startStep("Type some text in the reply field and click the send reply button. An additional message "
        + "panel appears in the email thread");
    int messagePanelsCount = inboxPage.getMessagePanelsCount();
    String reply = RandomString.randomAlphabetic(10, false);
    inboxPage.typeReply(reply);
    inboxPage.submitReply();
    endStep(Sync.wait(() -> inboxPage.getMessagePanelsCount() == messagePanelsCount + 1, LONG_WAIT));
    
    startStep("The first message panel contains the text of the reply");
    MessagePanel replyPanel = inboxPage.getMessagePanel(1);
    endStep(replyPanel.getMessageContentsElement().getAttribute("innerHTML").contains(reply));

    startStep("The message should be sent to the sender of the first message in the thread");
    String originalSender = inboxPage.getLastMessagePanel().getSender();
    endStep(!replyPanel.isFrom() && replyPanel.getRecipient().equals(originalSender));

    startStep("The message header of the reply should be 'RE: ' plus the original message header");
    String newHeader = "RE: " + originalMessageHeader;
    endStep(replyPanel.getHeader().equals(newHeader));
    
    endTest();
  }

  private boolean waitForMessageToShowExpectedSummary(Inbox inboxPage, MessageSummary expectedSummary) {
    return Sync.wait(() -> {
      // Wait for the message panels to update
        try {
          MessagePanel newPanel = inboxPage.getLastMessagePanel();
          return expectedSummary.getHeader().contains(newPanel.getHeader())
              && newPanel.getAddress().contains(expectedSummary.getSubHeader());
        }
        catch (StaleElementReferenceException e) {
          // This is thrown if the panel updates after we have the reference
          return false;
        }
      });
  }
}
