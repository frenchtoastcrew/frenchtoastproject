package com.nissan.tests.website.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 * Page object for the verify page
 * 
 * @author Vladimri Mihov
 * 
 */

public class Verify {

	// We have sent you email text used to verify that verification email is
	// successfully sent
	@FindBy(how = How.XPATH, using = "//h1[contains(text(),'We’ve sent you an email')]")
	private WebElement weHaveSentYouAnEmail;

}
