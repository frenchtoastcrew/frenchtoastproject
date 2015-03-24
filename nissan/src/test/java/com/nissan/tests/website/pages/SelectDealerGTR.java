package com.nissan.tests.website.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

import com.nissan.tests.framework.PageBase;
import com.nissan.tests.framework.Sync;
import com.nissan.tests.website.pages.elements.TestDriveGTRDealer;

/**
 * Page object for select dealer page - GTR selected.
 *
 * @author Vladimir Mihov
 *
 */
public class SelectDealerGTR extends PageBase {

  public SelectDealerGTR(WebDriver wd) {
    super(wd);
    // Make sure we are on the Select Dealer GTR page
    Assert.assertTrue(Sync.wait(() -> !isMissing(By
        .xpath("//h2[@class = 'strapline'][contains(text(), 'Please select the dealer of your choice')]"))));
  }

  // Page web elements
  @FindBy(how = How.XPATH, using = "//div[contains(@id,'maps_container')]")
  private WebElement dealerListContainer;

  @FindBy(how = How.XPATH, using = ".//*[@id='pos_0']/ul/li/div[2]/span[2]")
  private WebElement submitButton;

  /**
   * Returns the dealer object for the n'th result
   *
   * @param n
   *          1-based index
   * @return
   */
  public TestDriveGTRDealer getDealer(int n) {
    // Locate the container of the dealer and create the object
    return new TestDriveGTRDealer(wd, dealerListContainer.findElement(By.xpath(".//*[@id='dealer_" + n + "']")), n);
  }

}
