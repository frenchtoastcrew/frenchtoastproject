package com.nissan.tests.website.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.nissan.tests.framework.Sync;

public class Search extends SearchPageBase {

	public Search(WebDriver wd) {
		super(wd);
		// Verify that we are on the right page
		Assert.assertTrue(
				Sync.wait(() -> wd.getCurrentUrl().endsWith("chooselocation")),
				"We are not on the search / choose location page");
	}

	@FindBy(how = How.ID, using = "search_location")
	public WebElement searchFiled;

	@FindBy(how = How.NAME, using = "submit")
	public WebElement searchButton;

	// TODO: Add the specifics for the page not covered in the search base
	// class

	/**
	 * Clicks the Search for Property button
	 */
	public SearchResults searchButtonSubmit() {
		searchButton.click();
		return PageFactory.initElements(wd, SearchResults.class);
	}

}
