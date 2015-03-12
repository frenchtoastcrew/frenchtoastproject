package com.nissan.tests.website.pages;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.easyproperty.tests.framework.PageBase;
import com.easyproperty.tests.framework.Sync;

/**
 * Page object for the home page
 *
 * @author vlado a
 *
 */
public class Home extends PageBase {

  public Home(WebDriver wd) {
    super(wd);
    // Verify that we are on the home page 
    Assert.assertTrue(Sync.wait(() -> wd.getCurrentUrl().equals(getSystemProperty("WebsiteURL"))),
        "We are not on the home page");
  }

  // TODO: Add the specifics for the page

}
