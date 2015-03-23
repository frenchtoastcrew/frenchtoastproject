package com.nissan.tests.website.tests;

import org.testng.annotations.Test;

import com.nissan.tests.framework.WebsiteTestBase;
import com.nissan.tests.website.pages.Home;
import com.nissan.tests.website.pages.SelectDealerGTR;
import com.nissan.tests.website.pages.TestDrive;
import com.nissan.tests.website.pages.TestDriveGTR;

public class testSample extends WebsiteTestBase {

	@Test()
	public void test1() throws Exception {

		startTest("test");

		startStep("Go to nissan homepage");
		Home home = goToHomePage();
		endStep();

		startStep("Click on book-test-drive button");
		TestDrive td = home.bookATestDriveButtonClick();
		endStep();

		startStep("Select the new gtr");
		TestDriveGTR tdgtr = td.newGtrButtonClick();
		endStep();

		// TODO - read the poscode from excel
		startStep("Enter postcode");
		tdgtr.enterPotcode("B7 5JY");
		endStep();

		startStep("Click on the find");
		SelectDealerGTR selDealer = tdgtr.findButtonClick();
		endStep();

		startStep("Select dealer");
		selDealer.getDealer(1);
		endStep();

		endTest();

	}

}
