package com.nissan.tests.website.tests;

import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.nissan.tests.framework.Sync;
import com.nissan.tests.framework.WebsiteTestBase;

public class PagesAvailabilityTestMultyBrowser extends WebsiteTestBase {

	@DataProvider
	public Object[][] pagesList() throws Exception {
		return excelDataProvider();
	}

	@Test(dataProvider = "pagesList")
	public void test1(String webBrowser, String webpageURL, String xpath)
			throws Exception {

		startTest("test", webBrowser);

		startStep("Go to page and close psyma popup if needed");
		wd.get(BASE_URL + webpageURL);
		if (Sync.wait(() -> wd.findElement(By.id("psyma_button_2_2"))
				.isDisplayed())) {
			wd.findElement(By.id("psyma_button_2_2")).click();
		}
		endStep();

		startStep("Verify element exists on page");
		endStep(Sync.wait(() -> wd.findElement(By.xpath(xpath)).isDisplayed()));

		endTest();

	}
}
