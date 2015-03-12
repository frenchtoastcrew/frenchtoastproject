package com.nissan.tests.framework;

import org.testng.annotations.DataProvider;

public class BrowserProfilesDataProvider {

	@DataProvider(name = "browserProfiles")
	public static Object[][] getBrowserProfiles() throws Exception {
		String filePath = "src/test/resources/datasheets/BrowserProfiles.xlsx";
		return ExcelReader.getDataFromExcel(filePath, "browserProfiles");
	}

	@DataProvider(name = "defaultBrowser")
	public static Object[][] getDefaultBrowsers() throws Exception {
		return new Object[][] { { "Default" } };
	}

}
