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

public class TestDriveGTR extends PageBase {

	public TestDriveGTR(WebDriver wd) {
		super(wd);
		// Make sure we are on the "Test drive the New Nissan gt-r" page
		Assert.assertTrue(Sync.wait(() -> !isMissing(By
				.xpath("//span[@class = 'titleadj'][contains(text(), 'Test drive the New Nissan')]"))));
	}

	// Page web elements
	@FindBy(how = How.ID, using = "postcode")
	private WebElement postcodeField;

	@FindBy(how = How.ID, using = "submitlocation")
	private WebElement findButton;

	/**
	 * Enter PostCode
	 * 
	 */
	public void enterPotcode(String keysToSend) {
		postcodeField.sendKeys(keysToSend);
		;
	}

	/**
	 * Clicks on Find button
	 * 
	 */
	public SelectDealer findButtonClick() {
		findButton.click();
		return PageFactory.initElements(wd, SelectDealer.class);
	}
}
