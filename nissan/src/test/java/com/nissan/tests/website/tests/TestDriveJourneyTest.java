package com.nissan.tests.website.tests;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.nissan.tests.framework.BrowserProfilesDataProvider;
import com.nissan.tests.framework.WebsiteTestBase;
import com.nissan.tests.utils.RandomPostcode;
import com.nissan.tests.utils.RandomString;
import com.nissan.tests.website.pages.Home;
import com.nissan.tests.website.pages.SelectDealerGTR;
import com.nissan.tests.website.pages.TestDrive;
import com.nissan.tests.website.pages.TestDriveDetails;
import com.nissan.tests.website.pages.TestDriveGTR;
import com.nissan.tests.website.pages.TestDriveGTRCalendar;
import com.nissan.tests.website.pages.elements.TestDriveGTRDealer;

public class TestDriveJourneyTest extends WebsiteTestBase {

	@Test(dataProvider = "browserProfiles", dataProviderClass = BrowserProfilesDataProvider.class)
	public void test1(String browserProfile) throws Exception {

		startTest("Book a test drive journey test ", browserProfile);

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
		TestDriveDetails detailsPage = calendar.submitButtonClick();
		endStep();

		startStep("Fill out user data");
		detailsPage.fillOutDetailsForm("Doctor", "John", "Doe",
				RandomString.generateEmail(), RandomPostcode.getPostcode(),
				"Addr1", "Addr2", "Guildford", "", true, true, true);

		endStep(wd.findElement(By.id("submit")).isDisplayed());

		endTest();

	}

}
