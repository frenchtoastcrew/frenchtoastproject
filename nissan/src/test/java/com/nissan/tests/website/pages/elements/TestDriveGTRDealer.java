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

	public TestDriveGTRDealer(WebDriver wd, WebElement container) {
		this.wd = wd;
		this.container = container;
		Sync.wait(() -> container.isDisplayed());
	}

	/**
	 * Clicks the select button to open go to the next page - date select
	 *
	 * @return OwnedPropertyDetails
	 */
	public TestDriveGTRCalendar clickSelectButton() {

		// Click
		container
				.findElement(
						By.xpath(".//span[@class='model_selection_find form_submit_online']"))
				.click();

		return PageFactory.initElements(wd, TestDriveGTRCalendar.class);
	}

}
