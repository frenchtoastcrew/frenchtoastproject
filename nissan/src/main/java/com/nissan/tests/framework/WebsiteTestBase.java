package com.nissan.tests.framework;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.easyproperty.tests.pex.pages.ViewProperty;
import com.easyproperty.tests.utils.RandomPostcode;
import com.easyproperty.tests.utils.RandomPostcode.GeolocatedPostcode;
import com.easyproperty.tests.utils.RandomString;
import com.easyproperty.tests.website.pages.Home;
import com.easyproperty.tests.website.pages.Inbox;
import com.easyproperty.tests.website.pages.ListFavouriteProperties;
import com.easyproperty.tests.website.pages.Login;
import com.easyproperty.tests.website.pages.OwnedPropertyDetails;
import com.easyproperty.tests.website.pages.Profile;
import com.easyproperty.tests.website.pages.Search;
import com.easyproperty.tests.website.pages.SearchResults;

/**
 * A base class for all tests for the main site
 *
 * @author vlado a.
 *
 */
public class WebsiteTestBase extends TestBase {

	protected String baseUrl = getSystemProperty("WebsiteURL");
	protected String pexBaseUrl = getSystemProperty("PexURL");
  public final String GENERATED_USER_PASS = "Test1234";

	/**
	 * Navigate to the home page
	 *
	 * @return A Home page object
	 */
	public Home goToHomePage() {
		wd.get(baseUrl);
		return PageFactory.initElements(wd, Home.class);
	}

	/**
	 * Navigate to Login/Register page
	 *
	 * @return A Login/Register page object
	 */
	public Login goToLoginPage() {
		wd.get(baseUrl + "signin");
		return PageFactory.initElements(wd, Login.class);
	}

	/**
	 * Navigate to PEX MS Login page
	 *
	 * @return A Login page object
	 */
	public com.easyproperty.tests.pex.pages.Login goToPexMSLoginPage() {
		wd.get(pexBaseUrl + "signon/login.jsp");
		return PageFactory.initElements(wd,
				com.easyproperty.tests.pex.pages.Login.class);
	}

	/**
	 * Navigate to PEX MS Property View page
	 *
	 */
	public ViewProperty goToPexMSPropertyViewPage(String propertyId) {
		wd.get(pexBaseUrl + "Property/view.jsp?id=" + propertyId);
		return PageFactory.initElements(wd, ViewProperty.class);
	}

	/**
	 * Navigate to a search result page
	 *
	 * @param searchString
	 *            what to search for
	 * @return
	 */
	public SearchResults goToSearchResultsPage(String searchString) {
    return goToSearchPage().searchFor(searchString);
	}

	/**
	 * Navigate to the profile page for some user
	 * 
	 * @param user
	 * @param password
	 * @return
	 */
	public Profile goToProfilePage(String user, String password) {
		wd.get(baseUrl + "admin/updatepersondetails");
		PageFactory.initElements(wd, Login.class).loginUser(user, password);
		return PageFactory.initElements(wd, Profile.class);
	}

	/**
	 * Navigate to the profile page
	 * 
	 * @return
	 */
	public Profile goToProfilePage() {
		wd.get(baseUrl + "admin/updatepersondetails");
		return PageFactory.initElements(wd, Profile.class);
	}

	/**
	 * Navigate to the Search / Choose Location page
	 * 
	 * @return
	 */
	public Search goToSearchPage() {
		wd.get(baseUrl + "chooselocation");
		return PageFactory.initElements(wd, Search.class);
	}

  /**
   * Navigate to the Favourites page for specific user
   * 
   * @param username
   * @param password
   * @return
   */
  public ListFavouriteProperties goToFavouriteProperties(String username, String password) {
    goToLoginPage().loginUser(username, password);
    wd.get(baseUrl + "/admin/listfavouriteproperties");
    return PageFactory.initElements(wd, ListFavouriteProperties.class);
  }

	/**
	 * Navigate to the 'Owned Property Details' page for specific property id
	 * and user
	 * 
	 * @param propertyId
	 * @param username
	 * @param password
	 * @return
	 */
	public OwnedPropertyDetails goToOwnedPropertyDetails(String propertyId,
			String username, String password) {
    goToLoginPage().loginUser(username, password);
		wd.get(baseUrl + "/admin/ownedpropertydetails/" + propertyId);
		return PageFactory.initElements(wd, OwnedPropertyDetails.class);
	}

	/**
	 * Navigate to the 'Owned Property Details' page for specific property id
	 * created by the default test user
	 * 
	 * @param propertyId
	 * @return
	 */
	public OwnedPropertyDetails goToOwnedPropertyDetails(String propertyId) {
		return goToOwnedPropertyDetails(propertyId,
				getSystemProperty("DefaultUser"),
				getSystemProperty("DefaultUserPassword"));
	}

  /**
   * Navigate to the Inbox page for specific user
   * 
   * @param username
   * @param password
   * @return
   */
  public Inbox goToInbox(String username, String password) {
    goToLoginPage().loginUser(username, password);
    wd.get(baseUrl + "/messaging");
    return PageFactory.initElements(wd, Inbox.class);
  }

	/**
	 * Get unverified user verificationID
	 *
	 * @param email
	 *            (username)String
	 * 
	 * @return String verificationID
	 */
	public String getUserVerificationURL(String username) throws Exception {

		PexApi rs = new PexApi();
		String verId = rs.getUserVerificationId(username);
		@SuppressWarnings("deprecation")
		String verificationUrl = baseUrl + "verify/" + verId + "?email="
				+ URLEncoder.encode(username) + "&fe=true";
		return verificationUrl;

	}

	/**
   * Creates a user with a random username/email and password Test1234 and
   * verifies the user, leaving it logged in on the site
   * 
   * @param tenant
   * @return the generated username
   * @throws Exception
   */
	public String createUserVerifyAndLogin(boolean tenant) throws Exception {
    String userName = createUser(tenant);
    wd.get(getUserVerificationURL(userName));
    return userName;
	}
	
  /**
   * Creates a user with a random username/email and password Test1234 and
   * verifies the user, but does not log in on the site
   * 
   * @param tenant
   * @return the generated username
   * @throws Exception
   */
  public String createUserAndVerify(boolean tenant) throws Exception {
    String userName = createUser(tenant);
    PexApi pex = new PexApi();
    String verificationId = pex.getUserVerificationId(userName);
    String status = pex.verifyUser(userName, verificationId);
    Assert.assertEquals(status, "OK", "Error while verifying the user");
    return userName;
  }

  /**
   * Creates a user with a random username/email and password Test1234, but does
   * not verify the user's' email
   * 
   * 
   * @param tenant
   * @return the generated username
   * @throws Exception
   */
  public String createUser(boolean tenant) throws Exception {
    String userName = RandomString.generateEmail();
    String password = GENERATED_USER_PASS;
    PexApi pex = new PexApi();
    String status = pex.createUser(userName, password, tenant ? "EPT" : "EPLL", false, false);
    Assert.assertEquals(status, "OK", "Error while creating the user");
    Log.message(userName + "/" + password);

    return userName;
  }

  /**
   * Creates a random property for a user
   * 
   * @param userName
   * @return info about the created property
   * @throws Exception
   */
	public PropertyInfo createRandomProperty(String userName) throws Exception {
    Random r = new Random();
	  PropertyInfo info = new PropertyInfo();

    // Generate the random property info
	  GeolocatedPostcode gp = RandomPostcode.getGeolocatedPostcode();
	  info.latitude = gp.latitude;
	  info.longitude = gp.longitude;
	  info.postcode = gp.fullCode;
    info.partialPostCode = gp.partialCode;
	  
    info.numBeds = r.nextInt(11); // Max 10
    info.numBaths = r.nextInt(6); // Max 5

    info.address1 = "Addr1 " + RandomString.randomAlphanumeric(4, false);
    info.address2 = "Addr2 " + RandomString.randomAlphanumeric(4, false);
    info.address3 = "Addr3 " + RandomString.randomAlphanumeric(4, false);
    info.address4 = "Addr4 " + RandomString.randomAlphanumeric(4, false);
    
    info.monthlyRent = r.nextInt(2000);
    info.deposit = r.nextInt(5000);
    
    // Create the property via a PEX call
    String artirixAddress = "{'sub_building_name':'','number':'','building_name':'','main_or_only_street':'','post_town_title':'','city':'','postcode':'"
        + info.postcode
        + "','location':'"
        + info.latitude
        + ","
        + info.longitude
        + "','short_postcode':'"
        + info.partialPostCode
        + "','street_postcode':'','county_administrative':'','county_postal':'','county_traditional':'','borough':''}";
    PexApi pa = new PexApi();
    String userID = pa.getUserId(userName);
    info.propertyID = pa.propertyRegistration(userID, null, null, "PropertyName", artirixAddress,
        info.address1,
        info.address2, info.address3, info.address4, info.postcode, "GB",
        info.latitude, info.longitude, null, null, ""+info.monthlyRent, ""+info.deposit, null,
        PexApi.PRICE_FREQUENCY, ""+info.numBeds, ""+info.numBaths, null, true);

    // Add a property type
    String response = pa.addPropertyTag(userID, info.propertyID, "FLATS\\/APARTMENTS-GROUND FLOOR FLAT");
    Assert.assertEquals(response, "OK");

    // Add furnished status
    response = pa.addPropertyTag(userID, info.propertyID, "FURNISHED");
    Assert.assertEquals(response, "OK");

    // Add minimum tenancy
    response = pa.addPropertyTag(userID, info.propertyID, "4. 24MONTHS");
    Assert.assertEquals(response, "OK");

    // Add short and full description
    info.shortDescription = "Short decription " + RandomString.randomAlphanumeric(5, false);
    info.description = String.format(
        "This is automatically generated test property with ID = %s, located at latitude = %s and longitude = %s",
        info.propertyID, gp.latitude, gp.longitude);

    response = pa.createTextPropertyParticular(userID, info.propertyID, info.description, "ACC");
    Assert.assertEquals(response, "OK");
    response = pa.createTextPropertyParticular(userID, info.propertyID, info.shortDescription, "SUMMARY");
    Assert.assertEquals(response, "OK");
    
    // Add 3 Features
    info.features = new ArrayList<String>();
    for (int i = 1; i < 4; i++) {
      String feature = "Feature" + i + " " + RandomString.randomAlphabetic(5, false);
      info.features.add(feature);
      response = pa.createTextPropertyParticular(userID, info.propertyID, feature, "FEAT");
      Assert.assertEquals(response, "OK");
    }
    
    return info;
	}

  /**
   * Publishes and sets the status of a property to Accepted via the Pex MS
   * 
   * @param propertyID
   */
  public void publishPropertyInPexMS(String propertyID) {
    // Change the status of the property through the PEX MS UI
    com.easyproperty.tests.pex.pages.Login lo = goToPexMSLoginPage();
    lo.loginUser(getSystemProperty("PexUsername"), getSystemProperty("PexPassword"));
    ViewProperty vp = goToPexMSPropertyViewPage(propertyID);
    vp.clickModifyRecordButton();
    vp.selectSatus(PexApi.STATUS_ACCEPTED);
    vp.setPublished(true);
    vp.clickSaveButton();
  }
  
  /**
	 * Creates a random property for a user
	 * 
	 * @param userName
	 * @return info about the created property
	 * @throws Exception
	 */
	public PropertyInfo createRandomPropertyWithSpecificType(String userName,
			String typeOfProperty) throws Exception {
		Random r = new Random();
		PropertyInfo info = new PropertyInfo();

		// Generate the random property info
		GeolocatedPostcode gp = RandomPostcode.getGeolocatedPostcode();
		info.latitude = gp.latitude;
		info.longitude = gp.longitude;
		info.postcode = gp.fullCode;
		info.partialPostCode = gp.partialCode;

		info.numBeds = r.nextInt(11); // Max 10
		info.numBaths = r.nextInt(6); // Max 5

		info.address1 = "Addr1 " + RandomString.randomAlphanumeric(4, false);
		info.address2 = "Addr2 " + RandomString.randomAlphanumeric(4, false);
		info.address3 = "Addr3 " + RandomString.randomAlphanumeric(4, false);
		info.address4 = "Addr4 " + RandomString.randomAlphanumeric(4, false);

		info.monthlyRent = r.nextInt(2000);
		info.deposit = r.nextInt(5000);

		// Create the property via a PEX call
		String artirixAddress = "{'sub_building_name':'','number':'','building_name':'','main_or_only_street':'','post_town_title':'','city':'','postcode':'"
				+ info.postcode
				+ "','location':'"
				+ info.latitude
				+ ","
				+ info.longitude
				+ "','short_postcode':'"
				+ info.partialPostCode
				+ "','street_postcode':'','county_administrative':'','county_postal':'','county_traditional':'','borough':''}";
		PexApi pa = new PexApi();
		String userID = pa.getUserId(userName);
		info.propertyID = pa.propertyRegistrationV27(userID, null, null,
				"PropertyName", artirixAddress, info.address1, info.address2,
				info.address3, info.address4, info.postcode, "",
				info.latitude, info.longitude, null, null, ""
						+ info.monthlyRent, "" + info.deposit, null,
				PexApi.PRICE_FREQUENCY, "" + info.numBeds, "" + info.numBaths,
				null, "" + r.nextInt(2000), "" + r.nextInt(2000),
				"" + r.nextInt(2000), true);

		// Add a property type
		String response = pa.addPropertyTag(userID, info.propertyID,
				typeOfProperty);
		Assert.assertEquals(response, "OK");

		// Add furnished status
		response = pa.addPropertyTag(userID, info.propertyID, "FURNISHED");
		Assert.assertEquals(response, "OK");

		// Add minimum tenancy
		response = pa.addPropertyTag(userID, info.propertyID, "4. 24MONTHS");
		Assert.assertEquals(response, "OK");

		// Add short and full description
		info.shortDescription = "Short decription "
				+ RandomString.randomAlphanumeric(5, false);
		info.description = String
				.format("This is automatically generated test property with ID = %s, located at latitude = %s and longitude = %s",
						info.propertyID, gp.latitude, gp.longitude);

		response = pa.createTextPropertyParticular(userID, info.propertyID,
				info.description, "ACC");
		Assert.assertEquals(response, "OK");
		response = pa.createTextPropertyParticular(userID, info.propertyID,
				info.shortDescription, "SUMMARY");
		Assert.assertEquals(response, "OK");

		// Add 3 Features
		info.features = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			String feature = "Feature" + i + " "
					+ RandomString.randomAlphabetic(5, false);
			info.features.add(feature);
			response = pa.createTextPropertyParticular(userID, info.propertyID,
					feature, "FEAT");
			Assert.assertEquals(response, "OK");
		}

		return info;
	}
}
