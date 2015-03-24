package com.nissan.tests.website.pages.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.nissan.tests.framework.Sync;
import com.nissan.tests.framework.WdEx;
import com.nissan.tests.website.pages.TestDriveGTRCalendar;

public class TestDriveGTRDealer extends WdEx {

  private WebElement container;
  private int index;

  public TestDriveGTRDealer(WebDriver wd, WebElement container, int index) {
    this.wd = wd;
    this.container = container;
    this.index = index;
    Sync.wait(() -> container.isDisplayed());
  }

  /**
   * Clicks the select button to open go to the next page - date select
   *
   * @return OwnedPropertyDetails
   */
  public TestDriveGTRCalendar clickSelectButton() {
    
    container.click();
    wd.findElement(By.xpath(".//*[@id='pos_" + index + "']/ul/li/div[2]/span[2]")).click();

    return PageFactory.initElements(wd, TestDriveGTRCalendar.class);
  }

}
