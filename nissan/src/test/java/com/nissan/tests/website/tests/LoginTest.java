package com.nissan.tests.website.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.nissan.tests.framework.BrowserProfilesDataProvider;
import com.nissan.tests.framework.PexApi;
import com.nissan.tests.framework.WebsiteTestBase;
import com.nissan.tests.utils.RandomString;
import com.nissan.tests.website.pages.Login;

/**
 * Tests for the login functionality through our WS UI
 * 
 * @author Ivo Varbanov
 * 
 */
public class LoginTest extends WebsiteTestBase {

	@DataProvider
	public Object[][] positiveLogin() throws Exception {
		return excelDataProvider();
	}

	@Test(dataProvider = "positiveLogin")
	public void positiveLoginTest(String browserProfile, String username,
			String password) throws Exception {

		startTest("Logging in user " + username, browserProfile);

		startStep("Open Login page");
		Login loginPage = goToLoginPage();
		endStep();

		startStep("Fill out user data");
		loginPage.loginUser(username, password);
		endStep();

		startStep("Check if user is successfuly logged in");
		endStep(wd.getCurrentUrl().endsWith("listownedproperties"));

		endTest();

	}

	@DataProvider
	public Object[][] negativeLogin() throws Exception {
		return excelDataProvider();
	}

	@Test(dataProvider = "negativeLogin")
	public void negativeLoginTest(String browserProfile, String username,
			String password, String alertError, String inputError,
			String errorType) throws Exception {

		startTest("Logging in user " + username, browserProfile);

		startStep("Open Login page");
		Login loginPage = goToLoginPage();
		endStep();

		startStep("Fill out user data");
		loginPage.loginUser(username, password);
		endStep();

		startStep("Check login error");
		String a = loginPage.alertErrorText(alertError); // DEBUG
		String s = loginPage.errorText(errorType); // DEBUG
		endStep(new String(s).equals(inputError)
				|| new String(a).equals(alertError));

		endTest();

	}

	@Test(dataProvider = "negativeLoginFields")
	public void negativeLoginFieldsTest(String browserProfile, String username,
			String password, String alertError, String inputError,
			String errorType) throws Exception {

		startTest("Logging in user " + username, browserProfile);

		startStep("Open Login page");
		Login loginPage = goToLoginPage();
		endStep();

		startStep("Fill out user data");
		loginPage.loginUserNoSubmit(username, password);
		endStep();

		startStep("Check login error");
		String s = loginPage.errorText(errorType); // DEBUG
		endStep(new String(s).equals(inputError));

		endTest();

	}

	/**
	 * According our rules user with secondary group EPLL(Landlord) should be
	 * redirected to /listownedproperties and user with secondary group
	 * EPT(Tenant) should be redirected to /listfavouriteproperties.
	 * 
	 * This test also verifies that user is redirected always to
	 * /listownedproperties after verification.
	 * 
	 */
	@Test(dataProvider = "defaultBrowser", dataProviderClass = BrowserProfilesDataProvider.class)
	public void loginRulesTest(String browserProfile) throws Exception {

		startTest("Login with LL and TE verify user is redirected to correct location");

		startStep("Register LL and verify he/she is redirected to /listownedproperties from the verification link");
		PexApi pa = new PexApi();
		String username = RandomString.generateEmail().toLowerCase();
		pa.createUser(username, getSystemProperty("DefaultUserPassword"),
				"EPLL", false, false);
		wd.get(getUserVerificationURL(username));
		endStep(wd.getCurrentUrl().contains("listownedproperties"));

		startStep("Login with the registered LL and verify he/she is redirected to /listownedproperties after login");
		Login login = goToLoginPage();
		login.loginUser(username, getSystemProperty("DefaultUserPassword"));
		endStep(wd.getCurrentUrl().contains("listownedproperties"));

		startStep("Register TE and verify he/she is redirected to /listownedproperties from the verification link");
		username = RandomString.generateEmail().toLowerCase();
		pa.createUser(username, getSystemProperty("DefaultUserPassword"),
				"EPT", false, false);
		wd.get(getUserVerificationURL(username));
		endStep(wd.getCurrentUrl().contains("listownedproperties"));

		startStep("Login with the registered TE and verify he/she is redirected to /listfavouriteproperties");
		login = goToLoginPage();
		login.loginUser(username, getSystemProperty("DefaultUserPassword"));
		endStep(wd.getCurrentUrl().contains("listfavouriteproperties"));

		endTest();

	}

}
