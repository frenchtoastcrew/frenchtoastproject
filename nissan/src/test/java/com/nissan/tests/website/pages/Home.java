package com.nissan.tests.website.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

import com.nissan.tests.framework.PageBase;
import com.nissan.tests.framework.Sync;

/**
 * Page object for the home page
 *
 * @author ivo v
 *
 */
public class Home extends PageBase {

  public Home(WebDriver wd) {
    super(wd);
    // Verify that we are on the home page 
    Assert.assertTrue(Sync.wait(() -> wd.getCurrentUrl().equals(getSystemProperty("WebsiteURL"))),
        "We are not on the home page");
  }
  @FindBy(how = How.ID, using = "new")
  private WebElement container;



}
