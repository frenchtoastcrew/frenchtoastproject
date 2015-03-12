package com.nissan.tests.framework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Class that handles verification of messages received by the users
 * 
 * @author vlado a.
 *
 */
public class MessageChecker extends WdEx {

  /**
   * Checks that a specific user has received the 'Verify your account' message
   * 
   * @param email
   * @return
   */
  public static boolean verifyNewUser(String email) {
    HashMap<String, String> macros = new HashMap<String, String>();
    macros.put("email", email.replace("@", "%40"));
    return MessageChecker.verifyLastMessage(email, "verify_your_account", macros);
  }

  /**
   * Verifies that a specific user has received an 'You've received a viewing
   * enquiry' message with the correct details
   * 
   * @param userName
   *          the user that should receive the message
   * @param tenantTitleAndNames
   *          i.e. 'Mr John Smith'
   * @param address
   * @return
   */
  public static boolean verifyViewingRequestMessage(String userName, String tenantTitleAndNames, String tenantPhone,
      String tenantMessage, String address) {
    HashMap<String, String> macros = new HashMap<String, String>();
    macros.put("tenantTitleAndNames", tenantTitleAndNames);
    macros.put("tenantPhone", tenantPhone);
    macros.put("tenantMessage", tenantMessage);
    macros.put("address", address);
    return MessageChecker.verifyLastMessage(userName, "viewing_request", macros);
  }
  
  /**
   * Verifies that a specific user has received an 'Your offer has been sent'
   * message with the correct details
   * 
   * @param userName
   *          the user that should receive the message
   * @param firstName
   *          the first name of the user that should receive the message
   * @param propertyId
   * @param address
   * @return
   */
  public static boolean verifyOfferSentMessage(String userName, String firstName, String propertyId, String address) {
    HashMap<String, String> macros = new HashMap<String, String>();
    macros.put("firstName", firstName);
    macros.put("propertyId", propertyId);
    macros.put("address", address);
    return MessageChecker.verifyLastMessage(userName, "offer_sent", macros);
  }

  /**
   * Verifies that a specific user has received an 'Your offer has been
   * rejected' message with the correct details
   * 
   * @param userName
   *          the user that should receive the message
   * @param firstName
   *          the first name of the user that should receive the message
   * @param propertyId
   * @param address
   * @return
   */
  public static boolean
      verifyOfferRejectedMessage(String userName, String firstName, String propertyId, String address) {
    HashMap<String, String> macros = new HashMap<String, String>();
    macros.put("firstName", firstName);
    macros.put("propertyId", propertyId);
    macros.put("address", address);
    return MessageChecker.verifyLastMessage(userName, "offer_rejected", macros);
  }

  /**
   * Verifies that a specific user has received an 'You have received an offer'
   * offer with the correct details
   * 
   * @param userName
   *          the user that should receive the message
   * @param firstName
   *          the first name of the user that should receive the message
   * @param tenantNames
   *          the two names of the tenant separated with space
   * @param propertyId
   * @param address
   * @return
   */
  public static boolean
      verifyOfferReceivedMessage(String userName, String firstName, String tenantNames, String address) {
    HashMap<String, String> macros = new HashMap<String, String>();
    macros.put("firstName", firstName);
    macros.put("tenantNames", tenantNames);
    macros.put("address", address);
    return MessageChecker.verifyLastMessage(userName, "offer_received", macros);
  }

  /**
   * Verify that the latest message received by the user corresponds to a
   * particular html template with variable data supplied in the macros hash map
   * 
   * @param user
   * @param template
   * @param macros
   * @return
   * @throws Exception
   */
  public static boolean verifyLastMessage(String user, String template, HashMap<String, String> macros) {
    // Potentially we have to give time for the message to be received
    boolean success = Sync.wait(() -> {
      String message = "";
      try {
        message = (new PexApi()).getLastMessage(user);
        String templateFileName = String.format("src/test/resources/messages/%s.html", template);
        try (BufferedReader br = new BufferedReader(new FileReader(templateFileName))) {
          for (String line; (line = br.readLine()) != null;) {
            String exactLine = replaceMacros(line, macros).trim();
            if (!message.contains(exactLine)) {
              System.out.println("Not found:" + exactLine);
              return false;
            }
          }
        }
        return true;
      }
      catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }, LONG_WAIT);
    if (!success) {
      Log.messageRed("The following message was incorrect");
      try {
        Log.message((new PexApi()).getLastMessage(user));
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    return success;
  }

  private static String replaceMacros(String line, HashMap<String, String> macros) {
    for (Entry<String, String> entry : macros.entrySet()) {
      String key = String.format("#%s#", entry.getKey());
      String value = entry.getValue();
      line = line.replace(key, value);
    }
    return line;
  }

}
