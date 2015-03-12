package com.nissan.tests.framework;

/**
 * Test class describing the features of the default test user
 * 
 * @author vlado a.
 *
 */
public class DefaultUser extends WdEx {
  public static final String USERNAME = getSystemProperty("DefaultUser");
  public static final String PASSWORD = getSystemProperty("DefaultUserPassword");
  public static final String FIRST_NAME = "Test";
  public static final String LAST_NAME = "Automation";

  /**
   * Gets the Id of the default user
   * 
   * @return
   * @throws Exception
   */
  public static String getId() throws Exception {
    PexApi pex = new PexApi();
    return pex.getUserId(USERNAME);
  }

}
