package com.nissan.tests.website.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

import com.easyproperty.tests.framework.PageBase;
import com.easyproperty.tests.framework.Sync;
import com.easyproperty.tests.website.pages.elements.MessagePanel;
import com.easyproperty.tests.website.pages.elements.MessageSummary;

/**
 * Class for the inbox/messaging page and the messages tab on the favourites
 * page
 * 
 * @author vlado a.
 *
 */
public class Inbox extends PageBase {

  @FindBy(how = How.CLASS_NAME, using = "property_message_list")
  private WebElement messagesListContainer;

  @FindBy(how = How.ID, using = "message-threads")
  private WebElement messageThreadsContainer;

  @FindBy(how = How.ID, using = "content")
  private WebElement replyTextArea;

  @FindBy(how = How.ID, using = "submitbutton")
  private WebElement sendReplyButton;

  public Inbox(WebDriver wd) {
    super(wd);
    Assert.assertTrue(Sync.wait(() -> !isMissing(By.className("property_message_list"))),
        "We are not on the inbox page");
  }

  /**
   * Gets the n'th message summary from all the visible ones on the page
   * 
   * @param n
   *          1-based index
   * @return
   */
  public MessageSummary getVisibleMessageSummary(int n) {
    return new MessageSummary(wd, messagesListContainer.findElement(By.xpath(String.format(
        "./a[not(contains(@style,'none'))][%d]", n))));
  }

  /**
   * Gets the number of message summaries visible on the page
   * 
   * @return
   */
  public int getVisibleMessageSummariesCount() {
    return messagesListContainer.findElements(By.xpath("./a[not(contains(@style,'none'))]")).size();
  }

  /**
   * Returns the number of message panels (i.e. messages & replies) in the
   * currently open mail thread
   * 
   * @return
   */
  public int getMessagePanelsCount() {
    return messageThreadsContainer.findElements(By.xpath("./div[contains(@class,'message_panel')]")).size();
  }

  /**
   * Gets the n-th message panel (i.e. message or reply) in the currently open
   * mail thread
   * 
   * @param n
   *          1-based index
   * @return
   */
  public MessagePanel getMessagePanel(int n) {
    return new MessagePanel(wd, messageThreadsContainer.findElement(By.xpath(String.format(
        "./div[contains(@class,'message_panel')][%d]", n))));
  }

  /**
   * Gets the last message panel in the currently open mail thread which should
   * contain the original message
   * 
   * @return
   */
  public MessagePanel getLastMessagePanel() {
    return new MessagePanel(wd, messageThreadsContainer.findElement(By
        .xpath("./div[contains(@class,'message_panel')][last()]")));
  }

  /**
   * Returns true if replying is available for this message
   * 
   * @return
   */
  public boolean isReplyAvailable() {
    return !isMissing(By.id("content"));
  }

  /**
   * Enters a reply into the reply field
   * 
   * @param reply
   */
  public void typeReply(String reply) {
    replyTextArea.sendKeys(reply);
  }

  /**
   * Clicks the SEND REPLY button and waits for the 'Please wait...' message to
   * disappear
   */
  public void submitReply() {
    sendReplyButton.click();
    Sync.wait(() -> !isMissing(By.id("submitbutton")), LONG_WAIT);
  }

  /**
   * Returns true if the message shows the 'Please email us here or phone us on
   * 020 3096 5445 to enquire about this email'
   * 
   * @return
   */
  public boolean isEnquireByPhoneAndEmailAvailable() {
    return !isMissing(By.xpath("//p[contains(text(),'Please')]/a[text()='email us here']"));
  }

  /**
   * Gets the last displayed message page number
   * 
   * @return
   */
  public int getLastDisplayedPageNumber() {
    // Using getAttribute("innerHTML") instead of getText() as a workaround for
    // a Firefox WebDriver bug
    return Integer.parseInt(wd.findElement(
        By.xpath("//a[contains(@class,'page_link')][not(contains(@style,'none'))][last()]")).getAttribute("innerHTML"));
  }

  /**
   * Gets the active (selected) message page number
   * 
   * @return
   */
  public int getActivePageNumber() {
    // Using getAttribute("innerHTML") instead of getText() as a workaround for
    // a Firefox WebDriver bug
    return Integer.parseInt(wd.findElement(
        By.xpath("//a[contains(@class,'page_link')][contains(@class,'active_page')]")).getAttribute("innerHTML"));
  }

  /**
   * Selects a page from the message results
   * 
   * @param pageNumber
   */
  public void selectPage(int pageNumber) {
    wd.findElement(
        By.xpath(String.format("//a[contains(@class,'page_link')][not(contains(@style,'none'))][%d]", pageNumber)))
        .click();
  }

}
