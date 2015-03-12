package com.nissan.tests.website.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

import com.nissan.tests.framework.PageBase;
import com.nissan.tests.framework.Sync;
import com.nissan.tests.website.pages.elements.FavouriteProperty;

public class ListFavouriteProperties extends PageBase {

  public ListFavouriteProperties(WebDriver wd) {
    super(wd);
    Assert.assertTrue(Sync.wait(() -> wd.getCurrentUrl().contains("/admin/listfavouriteproperties")),
        "We are not on the favourite properties page");
  }

  @FindBy(how = How.XPATH, using = "//ul[contains(@class,'properties__list')]")
  private WebElement propertiesListContainer;

  /**
   * Returns the number of favourites present on the page
   * 
   * @return
   */
  public int getNumberOfFavourites() {
    return propertiesListContainer.findElements(By.xpath("./li")).size();
  }

  /**
   * Returns the favorite property object for the n'th result
   *
   * @param n
   *          1-based index
   * @return
   */
  public FavouriteProperty getFavouriteProperty(int n) {
    // Locate the container of the owned properties and create the object
    return new FavouriteProperty(wd, propertiesListContainer.findElement(By.xpath("./li[" + n + "]")));
  }

}
