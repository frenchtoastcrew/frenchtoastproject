package com.nissan.tests.pex.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.nissan.tests.framework.Sync;

/**
 * Page object for the login page of PEX MS
 * 
 * @author Vladimir Mihov
 * 
 */
public class Login {

	private WebDriver wd;

	@FindBy(how = How.XPATH, using = ".//input[@class='button']")
	private WebElement logIn;

	@FindBy(how = How.NAME, using = "username")
	private WebElement usernameField;

	@FindBy(how = How.NAME, using = "password")
	private WebElement passwordField;

	public Login(WebDriver wd) {
		this.wd = wd;
	}

	public void loginUser(String username, String password) {

		usernameField.sendKeys(username);
		passwordField.sendKeys(password);
		logIn.click();
    // Wait to log in
    Sync.wait(() -> !wd.getCurrentUrl().contains("signon"));
	}

}
