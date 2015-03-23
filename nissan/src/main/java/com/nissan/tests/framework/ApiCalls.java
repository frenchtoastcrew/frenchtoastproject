package com.nissan.tests.framework;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class where PEX API call will be developed using HttpURLConnection
 *
 * @author Vladimir Mihov.
 *
 */

public class ApiCalls extends WdEx {

	protected String baseUrl = getSystemProperty("WebsiteURL");
	protected String pexUrl = getSystemProperty("PexURL");
	protected String pexVersion = getSystemProperty("PexVersion");

	// API endpoints
	String apiCheckUserStatusEndpoint = pexUrl + pexVersion
			+ "rs/UserService/checkUserStatus";
	String apiListUserMessagesEndpoint = pexUrl + pexVersion
			+ "rs/MessageService/listMessages";
	String apiCreateUserEndPoint = pexUrl + pexVersion
			+ "rs/UserService/userRegistration";
	String apiPropertyRegistrationEndPoint = pexUrl + pexVersion
			+ "rs/PropertyService/propertyRegistration";
	String apiListPropertiesEndpoint = pexUrl + pexVersion
			+ "rs/PropertyService/listProperties";
	String apiUpdatePropertyStatusEndPoint = pexUrl + pexVersion
			+ "rs/PropertyService/updatePropertyStatus";
	String apiUpdatePropertyTags = pexUrl + pexVersion
			+ "rs/PropertyService/addPropertyTags";
  String apiCreatePropertyParticulars = pexUrl + pexVersion + "rs/PropertyService/createPropertyParticulars";
  String apiConfirmUserRegistration = pexUrl + pexVersion + "rs/UserService/confirmUserRegistration";

	// Assuming that this codes will never change as all DB updates will be copy
	// of the production DB

	// Pex codes for property status
	public static final String STATUS_INCOMPLETE = "-3617699720207651338";
	public static final String STATUS_NOT_REVIEWED = "7476605700517277309";
	public static final String STATUS_REJECTED = "-6427730383407796407";
	public static final String STATUS_ARCHIVE = "-4169566863999108428";
	public static final String STATUS_WAITING_REVIEW = "-85597780814451020";
	public static final String STATUS_SOLD = "1232606709543310624";
	public static final String STATUS_ACCEPTED = "-4184084547954135270";
	public static final String STATUS_IN_REVIEW = "4324195912520707810";

	// Pex codes for property type
	public static final String PROPERTY_TYPE_HOUSES_SEMI_DETACHED = "-3617699720207651338";
	public static final String PROPERTY_TYPE_HOUSES_DETACHED = "-3158442525285679761";
	// TODO add the rest codes

	// Pex codes for frequency of property price
	public static final String PRICE_FREQUENCY = "8412782589104139675";

	// Creates HTTP POST request to targetURL with json URL parameters
	public String excutePost(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	/**
	 * Returns the raw html text of the latest message for a user
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public String getLastMessage(String username) throws Exception {

		JSONObject obj = new JSONObject();

		// Get userID using username
		String userId = getUserId(username);

		// Get user message using userID
		String jsonParameterRequestListMessages = "{\"apiUser\": \"apiuser\",\"apiPassword\": \"password\",\"userID\": \""
				+ userId + "\"}";
		String listMessagesRespose = excutePost(apiListUserMessagesEndpoint,
				jsonParameterRequestListMessages);
		obj = new JSONObject(listMessagesRespose);
		String content = null;
		JSONArray arr = obj.getJSONArray("messageList");
		// The messages in the JSON array do not seem to always follow a
		// particular
		// order, so try to find the latest:
		long latestTime = 0;
		for (int i = 0; i < arr.length(); i++) {
			JSONObject element = arr.getJSONObject(i);
			long time = element.getLong("date");
			if (time > latestTime) {
				latestTime = time;
				content = arr.getJSONObject(i).getString("content");
			}
		}
		return content;
	}

	/**
	 * Get user verification ID
	 *
	 * @return String user verification ID
	 */
	public String getUserVerificationId(String username) throws Exception {

		String content = getLastMessage(username);

		// Get verificaton ID from user message content
		final Pattern pattern = Pattern.compile("<strong>(\\S+)</strong>");
		final Matcher matcher = pattern.matcher(content);
		matcher.find();

		return matcher.group(1);
	}

  /**
   * Confirms (verifies) the user registration
   * 
   * @param username
   * @param verificationId
   * @return
   */
  public String verifyUser(String username, String verificationId) {
    String request = "{\"apiUser\": \"apiuser\", \"apiPassword\": \"password\",\"username\":\"" + username
        + "\",\"verificationId\":\"" + verificationId + "\",\"user\":{\"title\":null}}";
    String response = excutePost(apiConfirmUserRegistration, request);
    JSONObject obj = new JSONObject(response);

    return obj.getString("status");
  }

	/**
	 * Get user ID
	 *
	 * @return String user ID
	 */
	public String getUserId(String username) throws Exception {

		// Get userID using username
		String jsonParameterRequestCheckUserStatus = "{\"apiUser\": \"apiuser\", \"apiPassword\": \"password\",\"username\":\""
				+ username + "\"}";
    String checkUserStatusResponse = excutePost(apiCheckUserStatusEndpoint,
						jsonParameterRequestCheckUserStatus);
		JSONObject obj = new JSONObject(checkUserStatusResponse);

		return obj.getString("userID");
	}

	/**
	 * Set property status
	 */
	public String setPropertyStatus(String userId, String propertyId,
			String status) throws Exception {

		String jsonParameterRequestUpdatePropertyStatus = "{\"apiUser\": \"apiuser\",\"apiPassword\": \"password\",\"propertyID\": \""
				+ propertyId
				+ "\",\"userID\": \""
				+ userId
				+ "\",\"statusID\": \"" + status + "\"}";

		// Set property status using userId, propertyId and status
    String updatePropertyStatusResponse = excutePost(
				apiUpdatePropertyStatusEndPoint,
				jsonParameterRequestUpdatePropertyStatus);
		JSONObject obj = new JSONObject(updatePropertyStatusResponse);
		return obj.getString("status");
	}

	/**
	 * Create user
	 * 
	 * @param userGroup
	 *            - pass EPT to register tenant or EPLL to register landlord
	 * 
	 * @return String status
	 */
	public String createUser(String username, String password,
			String userGroup, boolean marketing, boolean partnerMarketing)
			throws Exception {

		String jsonParameterRequestCreateUser = "{\"apiUser\":\"apiuser\",\"apiPassword\":\"password\",\"username\":\""
				+ username
				+ "\",\"password\":\""
				+ password
				+ "\",\"withVerificationEmail\":true,\"salt\":\"H3ll05ulT4t\",\"group\":\"EPTE\",\"secondaryGroups\":[\""
				+ userGroup
				+ "\"],\"marketing\": \""
				+ marketing
				+ "\",\"partnerMarketing\": \"" + partnerMarketing + "\"}";

    String checkCreateuserResponse = excutePost(apiCreateUserEndPoint,
				jsonParameterRequestCreateUser);
		JSONObject obj = new JSONObject(checkCreateuserResponse);
		return obj.getString("status");
	}

	/**
	 * Property registration
	 * 
	 * @return String status
	 */
	public String propertyRegistration(String userId, String type, String area,
			String propertyName, String artrixAddress, String address1,
			String address2, String address3, String address4, String postcode,
			String country, String latitude, String longitude,
			String nextAvailabilityDate, String councilTax, String askingRent,
			String deposit, String holdingDeposit, String frequency,
			String numberOfBedrooms, String numberOfBathrooms, String floor,
			boolean landlord) throws Exception {

		String jsonParameterRequestPropertyRegistration = "{\"apiUser\":\"apiuser\",\"apiPassword\":\"password\",\"property\":{\"name\":\""
				+ propertyName
				+ "\",\"managingAgent\":\""
				+ userId
				+ "\",\"type\":\"6056001477067526710\",\"area\":\""
				+ area
				+ "\",\"artirixAddress\":\""
				+ artrixAddress
				+ "\",\"address1\":\""
				+ address1
				+ "\",\"address2\":\""
				+ address2
				+ "\",\"address3\":\""
				+ address3
				+ "\",\"address4\":\""
				+ address4
				+ "\",\"postcode\":\""
				+ postcode
				+ "\",\"country\":\""
				+ country
				+ "\",\"latitude\":\""
				+ latitude
				+ "\",\"longitude\":\""
				+ longitude
				+ "\",\"nextAvailabilityDate\":"
				+ nextAvailabilityDate
				+ ",\"councilTax\":"
				+ councilTax
				+ ",\"askingRent\":"
				+ askingRent
				+ ","
				+ "\"frequency\":\""
				+ frequency
				+ "\",\"deposit\":\""
				+ deposit
				+ "\",\"holdingDeposit\":"
				+ holdingDeposit
				+ ",\"noOfBedrooms\":\""
				+ numberOfBedrooms
				+ "\",\"numberOfBathrooms\":\""
				+ numberOfBathrooms
				+ "\",\"floor\":" + floor + "},\"landlord\":" + landlord + "}";

    String checkCreatePropertyResponse = excutePost(
				apiPropertyRegistrationEndPoint,
				jsonParameterRequestPropertyRegistration);
		JSONObject obj = new JSONObject(checkCreatePropertyResponse);
		return obj.getString("propertyID");
	}

	// TODO ListProperties

  /**
   * Get property IDs
   *
   * @return ArrayList pripertyIDs
   */
	public ArrayList<String> getPropertyIds(String userId) throws Exception {
    return getPropertyIds(userId, false);
	}

  /**
   * Gets a list of property IDs for a user (optionally getting only published
   * properties)
   * 
   * @param userId
   * @param onlyPublished
   * @return
   * @throws Exception
   */
  public ArrayList<String> getPropertyIds(String userId, boolean onlyPublished) throws Exception {

    JSONObject obj = new JSONObject();

    String jsonParameterRequestListProperties = "{\"apiUser\": \"apiuser\",\"apiPassword\": \"password\",\"userID\": \""
        + userId + "\"}";
    String listPropertiesRespose = excutePost(apiListPropertiesEndpoint, jsonParameterRequestListProperties);
    obj = new JSONObject(listPropertiesRespose);
    ArrayList<String> arrlist = new ArrayList<String>();
    JSONArray arr = obj.getJSONArray("ownedUnits");
    for (int i = 0; i < arr.length(); i++) {
      JSONObject property = arr.getJSONObject(i); 
      if (!onlyPublished || onlyPublished && property.getBoolean("published")) {
        arrlist.add(property.getString("id"));
      }

    }
    return arrlist;
  }

  /**
   * Adds a tag for a property
   * 
   * @param userId
   * @param propertyId
   * @param propertyTag
   * @return
   */
  public String addPropertyTag(String userId, String propertyId,
			String propertyTag) {
		JSONObject obj = new JSONObject();

		String jsonAddPropertyTagRequest = "{\"apiUser\":\"apiuser\",\"apiPassword\":\"password\",\"userID\":\""
				+ userId
				+ "\",\"propertyID\":\""
				+ propertyId
				+ "\",\"tags\":[{\"label\":\"" + propertyTag + "\"}]}";

    String addPropertyTagResponse = excutePost(apiUpdatePropertyTags,
				jsonAddPropertyTagRequest);
		obj = new JSONObject(addPropertyTagResponse);
		return obj.getString("status");
	}

  /**
   * Creates a text property particular
   * 
   * @param userId
   * @param propertyId
   * @param text
   * @param type
   * @return
   */
  public String createTextPropertyParticular(String userId, String propertyId, String text, String type) {
    String request = "{\"apiUser\":\"apiuser\",\"apiPassword\":\"password\",\"userID\":\"" + userId
        + "\",\"propertyID\":\"" + propertyId + "\",\"particulars\":[{\"text\":\"" + text + "\",\"type\":\"" + type
        + "\"}]}";

    String response = excutePost(apiCreatePropertyParticulars, request);
    JSONObject obj = new JSONObject(response);
    return obj.getString("status");
  }
  
	/**
	 * Property registration
	 * 
	 * @return String status
	 */
	public String propertyRegistrationV27(String userId, String type, String area,
			String propertyName, String artrixAddress, String address1,
			String address2, String address3, String address4, String postcode,
			String country, String latitude, String longitude,
			String nextAvailabilityDate, String councilTax, String askingRent,
			String deposit, String holdingDeposit, String frequency,
			String numberOfBedrooms, String numberOfBathrooms, String floor,
			String unitPrice, String unitmaxsize, String unitminsize,
			boolean landlord) throws Exception {

		String jsonParameterRequestPropertyRegistration = "{\"apiUser\":\"apiuser\",\"apiPassword\":\"password\",\"property\":{\"name\":\""
				+ propertyName
				+ "\",\"managingAgent\":\""
				+ userId
				+ "\",\"type\":\"\",\"area\":\""
				+ area
				+ "\",\"artirixAddress\":\""
				+ artrixAddress
				+ "\",\"address1\":\""
				+ address1
				+ "\",\"address2\":\""
				+ address2
				+ "\",\"address3\":\""
				+ address3
				+ "\",\"address4\":\""
				+ address4
				+ "\",\"postcode\":\""
				+ postcode
				+ "\",\"country\":\""
				+ country
				+ "\",\"latitude\":\""
				+ latitude
				+ "\",\"longitude\":\""
				+ longitude
				+ "\",\"nextAvailabilityDate\":"
				+ nextAvailabilityDate
				+ ",\"councilTax\":"
				+ councilTax
				+ ",\"askingRent\":"
				+ askingRent
				+ ","
				+ "\"frequency\":\""
				+ frequency
				+ "\",\"deposit\":\""
				+ deposit
				+ "\",\"holdingDeposit\":"
				+ holdingDeposit
				+ ",\"noOfBedrooms\":\""
				+ numberOfBedrooms
				+ "\",\"numberOfBathrooms\":\""
				+ numberOfBathrooms
				+ "\",\"floor\":"
				+ floor
				+ ",\"unitPrice\":\""
				+ unitPrice
				+ "\",\"unitmaxsize\":\""
				+ unitmaxsize
				+ "\",\"unitminsize\":\""
				+ unitminsize
				+ "\"},\"landlord\":"
				+ landlord + "}";

		ApiCalls rs = new ApiCalls();
		String checkCreatePropertyResponse = rs.excutePost(
				apiPropertyRegistrationEndPoint,
				jsonParameterRequestPropertyRegistration);
		JSONObject obj = new JSONObject(checkCreatePropertyResponse);
		return obj.getString("propertyID");
	}
}
