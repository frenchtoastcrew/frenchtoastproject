package com.easyproperty.tests.website.pages.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.easyproperty.tests.framework.Sync;
import com.easyproperty.tests.framework.WdEx;

/**
 * Class representing a single message or reply in the inbox page
 * 
 * @author vlado a.
 *
 */
public class MessagePanel extends WdEx {

  private WebElement container;

  public MessagePanel(WebDriver wd, WebElement container) {
    this.wd = wd;
    this.container = container;
    Sync.wait(() -> container.isDisplayed());
  }

  /**
   * Returns true if this is a From: message as opposed to To: message
   * 
   * @return
   */
  public boolean isFrom() {
    return container.findElement(By.className("message_sender")).getText().contains("From:");
  }

  /**
   * Gets the sender of the message displayed in the header
   * 
   * @return
   */
  public String getSender() {
    return container.findElement(By.className("sender_name")).getText();
  }

  /**
   * Gets the recipient of the message displayed in the header
   * 
   * @return
   */
  public String getRecipient() {
    return container.findElement(By.className("recipient_name")).getText();
  }

  /**
   * Gets the title (header) of the message, not including the address part if
   * it is present
   * 
   * @return
   */
  public String getHeader() {
    String address = this.getAddress();
    String header = container.findElement(By.className("message_title")).getText().trim();
    return address == null ? header : header.replace(address, "").trim();
  }

  /**
   * Gets the address displayed in the the message header. The address is
   * optional, if missing this will return null
   * 
   * @return
   */
  public String getAddress() {
    String address = null;
    if (!isMissingFromContainer(container, By.className("property_address"))) {
      address = container.findElement(By.className("property_address")).getText();
    }
    return address;
  }

  /**
   * Gets the displayed date of the message represented as string
   * 
   * @return
   */
  public String getDateText() {
    return container.findElement(By.className("message_date")).getText();
  }

  /**
   * Gets the web element that contains the actual message contents
   * 
   * @return
   */
  public WebElement getMessageContentsElement() {
    return container.findElement(By.xpath(".//div[@class='row']"));
  }
}
