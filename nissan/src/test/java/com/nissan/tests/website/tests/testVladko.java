package com.nissan.tests.website.tests;

import org.testng.annotations.Test;

import com.nissan.tests.framework.WebsiteTestBase;
import com.nissan.tests.website.pages.Home;
import com.nissan.tests.website.pages.TestDrive;
import com.nissan.tests.website.pages.TestDriveGTR;

public class testVladko extends WebsiteTestBase {

	@Test()
	public void test() throws Exception {

		startTest("test");
		startStep("test");

		Home home = goToHomePage();
		TestDrive td = home.bookATestDriveButtonClick();
		TestDriveGTR tdgtr = td.newGtrButtonClick();
		tdgtr.enterPotcode("B7 5JY");
		tdgtr.findButtonClick();

		endStep();
		endTest();

	}

}
