package com.nissan.tests.framework;


import org.openqa.selenium.support.PageFactory;

import com.nissan.tests.website.pages.Home;

/**
 * A base class for all tests for the main site
 *
 * @author ivo varbanov.
 *
 */
public class WebsiteTestBase extends TestBase {

	protected String baseUrl = getSystemProperty("WebsiteURL");

	/**
	 * Navigate to the home page
	 *
	 * @return A Home page object
	 */
	public Home goToHomePage() {
		wd.get(baseUrl);
		return PageFactory.initElements(wd, Home.class);
	}

}
