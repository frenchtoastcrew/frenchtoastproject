package com.nissan.tests.website.pages;

import java.util.Set;

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
 * Page object for the home page
 *
 * @author ivo v
 *
 */
public class Home extends PageBase {

	public Home(WebDriver wd) {
		super(wd);
		// Verify that we are on the home page
		Assert.assertTrue(
				Sync.wait(() -> wd.getCurrentUrl().equals(
						getSystemProperty("WebsiteURL"))),
				"We are not on the home page");
	}

	// Page web elements
	@FindBy(how = How.XPATH, using = "//a[@class = 'btn btn-icon-left btn-primary']")
	private WebElement bookATestDriveButton;

	@FindBy(how = How.ID, using = "psyma_button_2_2")
	private WebElement psymaNoButton;

	/**
	 * True if the psyma pop up is displayed
	 *
	 * @return
	 * @throws InterruptedException
	 */
	public boolean isPopUpDispalyed() throws InterruptedException {
		Thread.sleep(1000);
		return !isMissing(By.id("psyma_button_2_2"));
	}

	/**
	 * Clicks the NO psyma button
	 * 
	 */
	public void closePsymaPopup() throws InterruptedException {
		psymaNoButton.click();
	}

	/**
	 * Clicks the Book a test drive button
	 * 
	 * @throws InterruptedException
	 * 
	 */
	public TestDrive bookATestDriveButtonClick() throws InterruptedException {

		if (isPopUpDispalyed()) {
			closePsymaPopup();
			Thread.sleep(500);
		}

		bookATestDriveButton.click();
		Thread.sleep(500);
		Set<String> AllWindowHandles = wd.getWindowHandles();
		String window2 = (String) AllWindowHandles.toArray()[1];
		wd.switchTo().window(window2);
		return PageFactory.initElements(wd, TestDrive.class);
	}

}
