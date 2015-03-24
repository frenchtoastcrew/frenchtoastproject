package com.nissan.tests.website.tests;

import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.nissan.tests.framework.Sync;
import com.nissan.tests.framework.WebsiteTestBase;

public class PagesAvailabilityTest extends WebsiteTestBase {

  @DataProvider
  public Object[][] pagesList() throws Exception {
    return excelDataProvider();
  }

  @Test(dataProvider = "pagesList")
  public void test1(String webBrowser, String webpageURL , String xpath) throws Exception {
    
    startTest("test");

    startStep("Go to page");
    wd.get(BASE_URL + webpageURL);
    endStep();

    startStep("Verify element exists on page");
    endStep(Sync.wait(() -> wd.findElement(By.xpath(xpath)).isDisplayed()));

    endTest();
    
  }
}
