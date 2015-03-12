package com.nissan.tests.website.tests;

import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.nissan.tests.framework.BrowserProfilesDataProvider;
import com.nissan.tests.framework.PropertyInfo;
import com.nissan.tests.framework.WebsiteTestBase;
import com.nissan.tests.website.pages.ListOwnedProperties;
import com.nissan.tests.website.pages.Login;
import com.nissan.tests.website.pages.OwnedPropertyDetails;
import com.nissan.tests.website.pages.Search;
import com.nissan.tests.website.pages.SearchResults;
import com.nissan.tests.website.pages.elements.OwnedProperties;
import com.nissan.tests.website.pages.elements.SquareSearchResult;

public class StatusChangePropertyTest extends WebsiteTestBase {

	@Test(dataProvider = "defaultBrowser", dataProviderClass = BrowserProfilesDataProvider.class)
	public void verifyPropertyStatusChanges(String browserProfile)
			throws Exception {

		startTest(
				"Change proeprty status from LIVE to OFFLINE and back to LIVE and from AVAILABLE to LET AGREED and back to AVAILABLE test",
				browserProfile);

		startStep("Register user and proeprty using PEX API");
		String userName = createUserVerifyAndLogin(false);
		endStep();

		startStep("RegisterProeprty");
		PropertyInfo propInfo = createRandomProperty(userName);
		endStep();

		startStep("Login and search for property postcode to ensure that the proeprty is not displayed in search results page");
		Login login = goToLoginPage();
		login.loginUser(userName, getSystemProperty("DefaultUserPassword"));
		ListOwnedProperties lop = PageFactory.initElements(wd,
				ListOwnedProperties.class);
		Search searchPage = lop.clickSearchButton();
		searchPage.searchFiled.sendKeys(propInfo.partialPostCode);
		SearchResults searchResults = searchPage.searchButtonSubmit();
		if (searchResults.areNoResultsDispalyed()) {
			endStep();
		} else {
			endStep(searchResults
					.isPropertyMissingInSearchresults(propInfo.propertyID));
		}

		startStep("Set property status to Accepted and published from PEX MS");
		publishPropertyInPexMS(propInfo.propertyID);
		endStep();

		startStep("Login with regisered user who owns listed property on EP website");
		login = goToLoginPage();
		login.loginUser(userName, getSystemProperty("DefaultUserPassword"));
		lop = PageFactory.initElements(wd, ListOwnedProperties.class);

		List<OwnedProperties> listOwned = lop.getAllOwnedProperties();
		OwnedProperties property = listOwned.get(0);
		endStep(property.getPropertyListingStatus().contains("LIVE"));

		startStep("Verify that the property is with status Approved");
		endStep(property.getPropertyStatus().equals("APPROVED"));

		startStep("Search that the property is available in the search results sinice it has been put online");
		searchPage = lop.clickSearchButton();
		searchPage.searchFiled.sendKeys(propInfo.partialPostCode);
		searchResults = searchPage.searchButtonSubmit();
		endStep(!searchResults
				.isPropertyMissingInSearchresults(propInfo.propertyID));

		startStep("Click on the owned proeprty to open property details page");
		lop.clickControlPanelButton();
		lop.clickPropertiesSubHeaderButton();
		listOwned = lop.getAllOwnedProperties();
		property = listOwned.get(0);
		OwnedPropertyDetails opd = property.click();
		endStep();

		startStep("Click on OFFLINE button and verify that the proeprty is displayed as offline in owned properties page");
		opd.clickOfflineButton();
		opd.clickPropertiesSubHeaderButton();
		listOwned = lop.getAllOwnedProperties();
		property = listOwned.get(0);
		endStep(property.getPropertyListingStatus().contains("OFFLINE"));

		startStep("Search that the property is missing from the search results sinice it has been taken offline");
		searchPage = lop.clickSearchButton();
		searchPage.searchFiled.sendKeys(propInfo.partialPostCode);
		searchResults = searchPage.searchButtonSubmit();
		if (searchResults.areNoResultsDispalyed()) {
			endStep();
		} else {
			endStep(searchResults
					.isPropertyMissingInSearchresults(propInfo.propertyID));
		}

		startStep("Click on the owned proeprty to open property details page");
		lop.clickControlPanelButton();
		lop.clickPropertiesSubHeaderButton();
		listOwned = lop.getAllOwnedProperties();
		property = listOwned.get(0);
		opd = property.click();
		endStep();

		startStep("Click on LIVE button and verify that the proeprty is displayed as online in owned properties page");
		opd.clickLiveButton();
		lop = opd.clickPropertiesSubHeaderButton();
		listOwned = lop.getAllOwnedProperties();
		property = listOwned.get(0);
		endStep(property.getPropertyListingStatus().contains("LIVE"));

		startStep("Search that the property is available in the search results sinice it has been put online");
		searchPage = lop.clickSearchButton();
		searchPage.searchFiled.sendKeys(propInfo.partialPostCode);
		searchResults = searchPage.searchButtonSubmit();
		endStep(!searchResults
				.isPropertyMissingInSearchresults(propInfo.propertyID));

		startStep("Click on the owned proeprty to open property details page");
		lop.clickControlPanelButton();
		lop.clickPropertiesSubHeaderButton();
		listOwned = lop.getAllOwnedProperties();
		property = listOwned.get(0);
		opd = property.click();
		endStep();

		startStep("Click on LET AGREED button and verify that the proeprty is displayed as Let Agreed in owned properties page");
		opd.clickLetAgreedButton();
		opd.clickPropertiesSubHeaderButton();
		listOwned = lop.getAllOwnedProperties();
		property = listOwned.get(0);
		endStep(property.getPropertyTenancyStatus().contains("Let Agreed"));

		startStep("Search that the property is available in the search results sinice it has been put online and have let agreed status since the status has been changed in the previous step");
		searchPage = lop.clickSearchButton();
		searchPage.searchFiled.sendKeys(propInfo.partialPostCode);
		searchResults = searchPage.searchButtonSubmit();
		SquareSearchResult prop = searchResults.getSearchResult(1);
		endStep(!prop.isLetAgreedMissing(propInfo.propertyID));

		startStep("Click on the owned proeprty to open property details page");
		lop.clickControlPanelButton();
		lop.clickPropertiesSubHeaderButton();
		listOwned = lop.getAllOwnedProperties();
		property = listOwned.get(0);
		opd = property.click();
		endStep();

		startStep("Click on AVAILABLE button and verify that the proeprty is displayed as AVAILABLE in owned properties page");
		opd.clickAvailableButton();
		opd.clickPropertiesSubHeaderButton();
		listOwned = lop.getAllOwnedProperties();
		property = listOwned.get(0);
		endStep(property.getPropertyTenancyStatus().contains("Available"));

		startStep("Search that the property is available in the search results sinice it has been put online and it does not have let agreed status since the status has been changed in the previous step");
		searchPage = lop.clickSearchButton();
		searchPage.searchFiled.sendKeys(propInfo.partialPostCode);
		searchResults = searchPage.searchButtonSubmit();
		prop = searchResults.getSearchResult(1);
		endStep(prop.isLetAgreedMissing(propInfo.propertyID));

		endTest();
	}
}
