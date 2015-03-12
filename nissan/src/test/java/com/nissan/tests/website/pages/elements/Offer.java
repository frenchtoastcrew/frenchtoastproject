package com.easyproperty.tests.website.pages.elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.easyproperty.tests.framework.Log;
import com.easyproperty.tests.framework.WdEx;
import com.easyproperty.tests.website.pages.YouveReceivedAnOffer;

/**
 * An offer for a property as displayed in the owned property details page
 * 
 * @author vlado a.
 *
 */
public class Offer extends WdEx {

  private WebElement container;

  public Offer(WebDriver wd, WebElement container) {
    this.container = container;
    this.wd = wd;
  }

  /**
   * Returns the displayed monthly price
   * 
   * @return
   */
  public double getMonthlyPrice() {
    String priceText = container.findElement(By.xpath("./span[@class='heading']")).getText();
    return parsePoundPrice(priceText);
  }

  /**
   * Returns the displayed offer date
   * 
   * @return
   * @throws ParseException
   */
  public Date getDate() throws ParseException {
    String dateText = container.findElement(By.xpath("./span[@class='description']")).getText();
    // Bug about the date format inconsistency
    // https://easyproperty.atlassian.net/browse/WEB-1947
    SimpleDateFormat dateFormatter = new SimpleDateFormat("d/M/y");
    return dateFormatter.parse(dateText);
  }

  /**
   * Returns the displayed names of the tenant
   * 
   * @return
   */
  public String getTenantNames() {
    return container.findElement(By.xpath("./span[@class='tenant-details']")).getText();
  }

  /**
   * Clicks the VIEW button for the property offer and returns the You've
   * received an offer! page that opens
   * 
   * @return
   */
  public YouveReceivedAnOffer view() {
    container.findElement(By.className("offer_view_button")).click();
    return PageFactory.initElements(wd, YouveReceivedAnOffer.class);
  }

  /**
   * Verifies that the displayed details in the offer are correct
   * 
   * @param price
   * @param submitDate
   * @param userName
   * @return
   * @throws ParseException
   */
  public boolean verifyDisplayedDetails(double price, Date submitDate, String userName) throws ParseException {
    if (price!=getMonthlyPrice()) {
      Log.messageRed("Displayed monthly price is incorrect");
      return false;
    }
    if (!submitDate.equals(getDate())) {
      Log.messageRed("Displayed submit date is incorrect");
      return false;      
    }
    if (!userName.equals(getTenantNames())) {
      Log.messageRed("Displayed tenant names are incorrect");
      return false;
    }
    return true;
  }

  /**
   * Returns true if 'Rejected' is displayed for the offer
   * 
   * @return
   */
  public boolean isRejected() {
    return !isMissingFromContainer(container,
        By.xpath("./div[@class='offer-status']/span[contains(text(),'Rejected')]"));
  }
  
}
