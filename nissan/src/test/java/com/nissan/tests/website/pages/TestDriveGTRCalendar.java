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
 * Page object for calendar page from the test drive booking journey.
 *
 * @author Ivo Varbanov
 *
 */

public class TestDriveGTRCalendar extends PageBase {

  public TestDriveGTRCalendar(WebDriver wd) {
    super(wd);

    // Make sure we are on the "Calendar Nissan gt-r" page
    Assert.assertTrue(Sync.wait(() -> !isMissing(By
        .xpath("//h2[@class = 'strapline'][contains(text(), 'Choose a day and time for your Test Drive')]"))));
  }

  // Page web elements
  @FindBy(how = How.ID, using = "calendar_Thu")
  private WebElement thursdayColumn;

  @FindBy(how = How.ID, using = "submit")
  private WebElement submitButton;

  // Actions
  /**
   * Select date/time on a Thursday :)
   * 
   */
  public void selectDateTime() {
    thursdayColumn.findElement(By.xpath(".//div[2]/div[3]/a")).click();
  }

  /**
   * Clicks on Submit button
   * 
   */
  public TestDriveDetails submitButtonClick() {
    submitButton.click();
    return PageFactory.initElements(wd, TestDriveDetails.class);
  }
}
