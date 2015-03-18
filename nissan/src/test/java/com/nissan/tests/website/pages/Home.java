package com.nissan.tests.website.pages;

import java.util.Set;

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

	/**
	 * Clicks the Book a test drive button
	 * 
	 */
	public TestDrive bookATestDriveButtonClick() {
		bookATestDriveButton.click();
		Set<String> AllWindowHandles = wd.getWindowHandles();
		String window2 = (String) AllWindowHandles.toArray()[1];
		wd.switchTo().window(window2);
		return PageFactory.initElements(wd, TestDrive.class);
	}

}
