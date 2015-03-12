package com.nissan.tests.website.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.nissan.tests.framework.PageBase;
import com.nissan.tests.framework.Sync;

/**
 * Page object for the /admin/listfavourite/<id> page
 * 
 * @author vlado a.
 *
 */
public class ListFavourite extends PageBase {

  @FindBy(how = How.XPATH, using = "//dl[@class='tabs']//a[contains(text(),'Messages')]")
  private WebElement messagesHeaderButton;

  @FindBy(how = How.XPATH, using = "//dl[@class='tabs']//a[contains(text(),'Offers')]")
  private WebElement offersHeaderButton;

  public ListFavourite(WebDriver wd) {
    super(wd);
    Assert.assertTrue(Sync.wait(() -> wd.getCurrentUrl().contains("/admin/listfavourite/")),
        "We are not on the favourite property page");
  }

  /**
   * Opens the messages tab (if needed) and returns an Inbox page object for it
   * 
   * @return
   */
  public Inbox getInboxFromTheMessagesTab() {
    if (isMissing(By.xpath("//dd[@class='active']/a[contains(text(),'Messages')]"))) {
      messagesHeaderButton.click();
    }
    return PageFactory.initElements(wd, Inbox.class);
  }
}
