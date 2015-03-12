package com.easyproperty.tests.website.pages.elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.easyproperty.tests.framework.Sync;
import com.easyproperty.tests.framework.WdEx;

/**
 * Class representing a message summary in the inbox page
 * 
 * @author vlado a.
 *
 */
public class MessageSummary extends WdEx {

  private WebElement container;

  public MessageSummary(WebDriver wd, WebElement container) {
    this.wd = wd;
    this.container = container;
    Sync.wait(() -> container.isDisplayed());
  }

  /**
   * Gets the sender of the message (i.e. 'easyProperty Website')
   * 
   * @return
   */
  public String getSender() {
    return container.getText().replace(getDateText(), "").split(getHeader())[0].trim();
  }

  /**
   * Gets the header of the message (i.e. 'VERIFY YOUR EASYPROPERTY ACCOUNT')
   * 
   * @return
   */
  public String getHeader() {
    return container.findElement(By.xpath(".//strong")).getText().trim();
  }

  /**
   * Gets the subheader of the message (usually the address of the property)
   * This is not always present
   * 
   * @return
   */
  public String getSubHeader() {
    return container.getText().replace(getDateText(), "").split(getHeader())[1].trim();
  }

  /**
   * The date / time in string format
   * 
   * @return
   */
  public String getDateText() {
    return container.findElement(By.className("date")).getText().trim();
  }
  
  /**
   * Returns true if time is shown (which means that the message is from today)
   * 
   * @return
   */
  public boolean isTimeShown() {
    SimpleDateFormat dateFormatter = new SimpleDateFormat("H:mm");
    try {
      dateFormatter.parse(getDateText());
    }
    catch (ParseException e) {
      return false;
    }
    return true;
  }

  /**
   * Returns true if the message is unread
   * 
   * @return
   */
  public boolean isUnread() {
    return container.getAttribute("class").contains("unread");
  }

  /**
   * Returns true if the message is currently selected
   * 
   * @return
   */
  public boolean isSelected() {
    return container.getAttribute("class").contains("selected");
  }

  /**
   * Clicks the message to select it
   */
  public void click() {
    container.click();
    Sync.wait(() -> this.isSelected());
  }
}
