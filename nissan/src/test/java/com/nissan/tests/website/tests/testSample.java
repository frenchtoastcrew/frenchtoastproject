package com.nissan.tests.website.tests;

import org.testng.annotations.Test;

import com.nissan.tests.framework.WebsiteTestBase;
import com.nissan.tests.utils.RandomPostcode;
import com.nissan.tests.website.pages.Home;
import com.nissan.tests.website.pages.SelectDealerGTR;
import com.nissan.tests.website.pages.TestDrive;
import com.nissan.tests.website.pages.TestDriveGTR;
import com.nissan.tests.website.pages.TestDriveGTRCalendar;
import com.nissan.tests.website.pages.elements.TestDriveGTRDealer;

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

		startStep("Enter postcode");
		tdgtr.enterPotcode(RandomPostcode.getPostcode());
		endStep();

		startStep("Click on the find");
		SelectDealerGTR selDealer = tdgtr.findButtonClick();
		endStep();

		startStep("Select dealer");
		TestDriveGTRDealer dealer = selDealer.getDealer(1);
		endStep();
		
		startStep("Submit dealer choice");
     TestDriveGTRCalendar calendar = dealer.clickSelectButton();
    endStep();

    startStep("Submit date");
    calendar.selectDateTime();
    calendar.submitButtonClick();
   endStep();
    
		endTest();

	}

}
