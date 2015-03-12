package com.nissan.tests.website.tests;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.Page;
import com.nissan.tests.framework.PageBase;
import com.nissan.tests.framework.PexApi;
import com.nissan.tests.framework.PropertyInfo;
import com.nissan.tests.framework.WebsiteTestBase;
import com.nissan.tests.pex.pages.ViewProperty;
import com.nissan.tests.utils.RandomPostcode;
import com.nissan.tests.utils.RandomString;
import com.nissan.tests.utils.RandomPostcode.GeolocatedPostcode;
import com.nissan.tests.website.pages.ListOwnedProperties;
import com.nissan.tests.website.pages.Login;
import com.nissan.tests.website.pages.elements.OwnedProperties;

public class testVladko extends WebsiteTestBase {

	protected String baseUrl = getSystemProperty("WebsiteURL");

	@DataProvider
	public Object[][] searchLocationData() throws Exception {
		return excelDataProvider();
	}

	@Test()
	public void basicTest() throws Exception {

		startTest("Test start");

		Login login = goToLoginPage();

		login.loginUser("test.eprop@gmail.com", "test.eprop@gmail.com");
		ListOwnedProperties lop = PageFactory.initElements(wd,
				ListOwnedProperties.class);
		lop.addYourPropertyButtonClick();

		endStep();

		Thread.sleep(10000);

		endTest();

	}

	@Test()
	public void reg() throws Exception {

		startTest("Start user creation tets");
		String usr = "test.vladkoautotot14@gmail.com";

		startStep("test");
		PexApi pa = new PexApi();
		String res = pa.createUser(usr, usr, "EPLL", true, true);
		System.out.println(res);
		Thread.sleep(5000);
		String vrfUrl = getUserVerificationURL(usr);
		String userId = pa.getUserId(usr);
		wd.get(vrfUrl);
		GeolocatedPostcode location = RandomPostcode.getGeolocatedPostcode();
		int latitudePointIndex = location.latitude.indexOf(".");
		int longtitudePointIndex = location.longitude.indexOf(".");
		pa.propertyRegistration(userId, PexApi.PROPERTY_TYPE_HOUSES_DETACHED,
				null, "PropertyName", "", "address1", "address2", "address3",
				"address4", location.fullCode, "GB",
				location.latitude.substring(0, latitudePointIndex + 5),
				location.longitude.substring(0, longtitudePointIndex + 5), "",
				"", "2000", "1000", "", PexApi.PRICE_FREQUENCY, "3", "2", "",
				true);
		Thread.sleep(10000);
		wd.get("https://qa.e-propuk.com/admin/listownedproperties");
		ArrayList<String> propID = pa.getPropertyIds(userId);
		System.out.println("Property ID = " + propID.get(0));
		pa.setPropertyStatus(userId, propID.get(0).toString(),
				pa.STATUS_NOT_REVIEWED);
		pa.setPropertyStatus(userId, propID.get(0).toString(),
				pa.STATUS_WAITING_REVIEW);
		pa.setPropertyStatus(userId, propID.get(0).toString(),
				pa.STATUS_ACCEPTED);
		Thread.sleep(3000);
		wd.get("https://qa.e-propuk.com/admin/listownedproperties");

		ListOwnedProperties lop = new ListOwnedProperties(wd);
		List<OwnedProperties> listOwnedProp = lop.getAllOwnedProperties();

		endStep();
		endTest();

	}

	@Test()
	public void regDve() throws Exception {

		PexApi pa = new PexApi();
		ArrayList<String> propertyIDs = pa
				.getPropertyIds("-9158455461209899352");
		System.out.println(propertyIDs.get(1));
		System.out.println(propertyIDs.get(2));
		System.out.println(propertyIDs.get(3));
		System.out.println(propertyIDs.get(4));
		System.out.println(propertyIDs.get(5));

	}

	@Test()
	public void regTri() throws Exception {

		startTest("Start user creation tets");
		startStep("test");

		Login lo = goToLoginPage();

		lo.loginUser("test.eprop@gmail.com", "test.eprop@gmail.com");
		ListOwnedProperties lop = PageFactory.initElements(wd,
				ListOwnedProperties.class);
		List<OwnedProperties> listOwned = lop.getAllOwnedProperties();

		OwnedProperties propertySixteen = listOwned.get(14);
		System.out.println(propertySixteen.getAddressText());
		System.out.println(propertySixteen.getPropertyRefNumber());
		System.out.println(propertySixteen.getPropertyOffersNumber());
		System.out.println(propertySixteen.getPropertyMessagesNumber());

		endStep();
		endTest();

	}

	@Test()
	public void regChetiri() throws Exception {

		startTest("test");
		startStep("test");

		com.nissan.tests.pex.pages.Login lo = goToPexMSLoginPage();

		lo.loginUser(getSystemProperty("PexUsername"),
				getSystemProperty("PexPassword"));
		Thread.sleep(15000);
		ViewProperty vp = goToPexMSPropertyViewPage("-1415716553086518910");
		vp.clickModifyRecordButton();
		vp.selectSatus(PexApi.STATUS_ACCEPTED);
		vp.setPublished(false);
		vp.clickSaveButton();

		Thread.sleep(10000);
		endStep();
		endTest();

	}

	@Test()
	public void regPet() throws Exception {

		startTest("test");
		startStep("test");

		com.nissan.tests.pex.pages.Login lo = goToPexMSLoginPage();

		lo.loginUser(getSystemProperty("PexUsername"),
				getSystemProperty("PexPassword"));
		Thread.sleep(15000);
		ViewProperty vp = goToPexMSPropertyViewPage("-1415716553086518910");
		vp.clickModifyRecordButton();
		vp.selectSatus(PexApi.STATUS_ACCEPTED);
		vp.setPublished(false);
		vp.clickSaveButton();

		Thread.sleep(10000);
		endStep();
		endTest();

	}

	@Test()
	public void regMiUserSProperty() throws Exception {

		startTest("test");
		startStep("test");
		String userName = createUserVerifyAndLogin(false);
		PropertyInfo propertyInfo = createRandomPropertyWithSpecificType(
				userName, "FLATS/APARTMENTS-GROUND FLOOR FLAT");
		publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-GARAGE");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-LEISURE FACILITY");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-CONVENIENCE STORE");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-PLACE OF WORSHIP");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-RETAIL PROPERTY (OUT OF TOWN)");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-CHILDCARE FACILITY");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-RETAIL PROPERTY (HIGH STREET)");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-SCIENCE PARK");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-SERVICED OFFICE");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-RESEARCH & DEVELOPMENT FACILITY");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-BUSINESS PARK");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-HOSPITALITY");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-OFFICE");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-GUEST HOUSE");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-SHOP");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-BAR / NIGHTCLUB");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-HEALTHCARE FACILITY");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-MILL");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-FARM");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-CAFE");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-MIXED USE");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-RESTAURANT");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-MARINE PROPERTY");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-RESIDENTIAL DEVELOPMENT");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-INDUSTRIAL DEVELOPMENT");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-DATA CENTRE");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-COMMERCIAL PROPERTY");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-SHOWROOM");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-WAREHOUSE");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-LAND");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-TRADE COUNTER");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-COMMERCIAL DEVELOPMENT");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-HEAVY INDUSTRIAL");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-INDUSTRIAL PARK");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-LIGHT INDUSTRIAL");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-STORAGE");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-PUB");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-WORKSHOP");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-DISTRIBUTION WAREHOUSE");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-FACTORY");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-HAIRDRESSER/BARBER SHOP");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-HOTEL");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-PETROL STATION");
		// publishPropertyInPexMS(propertyInfo.propertyID);
		// propertyInfo = createRandomPropertyWithSpecificType(userName,
		// "COMMERCIAL PROPERTY-POST OFFICE");
		// publishPropertyInPexMS(propertyInfo.propertyID);

		endStep();
		endTest();

	}

	@Test()
	public void regMiUserSName() throws Exception {

		startTest("test");
		startStep("test");
		String userName = createUser(false);

		PexApi pa = new PexApi();

		String status = pa.updateUserDetails(userName, "42314312412",
				"4234123432", PexApi.DR, "Neiko", "Automationa");
		System.out.println("Status " + status);
		String verId = pa.getUserVerificationId(userName);
		pa.verifyUser(userName, verId);

		endStep();
		endTest();

	}
}
