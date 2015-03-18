package com.nissan.tests.website.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

import com.nissan.tests.framework.PageBase;
import com.nissan.tests.framework.Sync;

public class SelectDealer extends PageBase {

	public SelectDealer(WebDriver wd) {
		super(wd);
		// Make sure we are on the Select Dealer page
		Assert.assertTrue(Sync.wait(() -> !isMissing(By
				.xpath("//h2[@class = 'strapline'][contains(text(), 'Please select the dealer of your choice')]"))));
	}

	// Page web elements
	@FindBy(how = How.ID, using = "postcode")
	private WebElement postcodeField;

	/**
	 * Enter PostCode
	 * 
	 */
	public void enterPotcode(String keysToSend) {
		postcodeField.sendKeys(keysToSend);

	}

}
