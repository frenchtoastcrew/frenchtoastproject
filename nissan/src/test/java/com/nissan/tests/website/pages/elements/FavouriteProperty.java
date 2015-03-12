package com.nissan.tests.website.pages.elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.nissan.tests.framework.Sync;
import com.nissan.tests.framework.WdEx;
import com.nissan.tests.website.pages.ListFavourite;

/**
 * Class representing property object in /listfavouriteproperties page
 * 
 * @author vlado a.
 *
 */
public class FavouriteProperty extends WdEx {

  private WebElement container;

  public FavouriteProperty(WebDriver wd, WebElement container) {
    this.wd = wd;
    this.container = container;
    Sync.wait(() -> container.isDisplayed());
  }

  /**
   * Returns the date this property was favourited on
   * 
   * @return
   * @throws ParseException
   */
  public Date getFavouritedDate() throws ParseException {
    SimpleDateFormat df = new SimpleDateFormat("d/M/y");
    return df
        .parse(container.findElement(By.className("advert-status")).getText().trim()
        .replace("FAVOURITED ON ", ""));
  }

  /**
   * Returns the property Id
   * 
   * @return
   */
  public String getProprtyId() {
    return container.getAttribute("id").replace("ep", "");
  }

  /**
   * Returns the displayed number of messages related to this property
   * 
   * @return
   */
  public int getMessageCount() {
    return Integer
        .parseInt(container.findElement(By.xpath(".//span[contains(@class,'unread_items')]/label")).getText());
  }

  /**
   * Returns true if the pixel in the centre of the background image in the
   * favourite property is of some expected color
   * 
   * @param expectedColour
   * @return
   * @throws Exception
   */
  public boolean verifyImageColor(int expectedColour) throws Exception {
    return areColorsSimilar(expectedColour, getCentralPixelColor(container));
  }

  /**
   * Clicks the favourite to open its 'list favourite' page and returns the page
   * object
   * 
   * @return
   */
  public ListFavourite click() {
    container.findElement(By.className("property-select")).click();
    return PageFactory.initElements(wd, ListFavourite.class);
  }
}
