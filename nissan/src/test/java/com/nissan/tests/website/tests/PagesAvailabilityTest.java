package com.nissan.tests.website.tests;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.testng.Assert;
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
    Cookie psyma = new Cookie("psyma_participation", "2");
    wd.manage().addCookie(psyma);
    wd.get(BASE_URL + webpageURL);
    endStep();

    startStep("Verify element exists on page");
    endStep(Sync.wait(() -> wd.findElement(By.xpath(xpath)).isDisplayed()));


    endTest();
    
  }
}
