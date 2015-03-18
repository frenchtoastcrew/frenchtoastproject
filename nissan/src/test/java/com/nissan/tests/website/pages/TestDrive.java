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

public class TestDrive extends PageBase {

	public TestDrive(WebDriver wd) {
		super(wd);
		// Make sure the test drive pop-up is opened
		Assert.assertTrue(Sync.wait(() -> !isMissing(By
				.xpath("//h1[@class = 'title-1'][contains(text(), 'Book a test drive')]"))));
	}

	// Page web elements
	@FindBy(how = How.XPATH, using = "//a[contains(@data-tracking, 'selectnewgtrpackshot')]")
	private WebElement newGtr;

	/**
	 * Clicks the newGtr car
	 * 
	 */
	public TestDriveGTR newGtrButtonClick() {
		newGtr.click();
		return PageFactory.initElements(wd, TestDriveGTR.class);
	}

}
