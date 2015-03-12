package com.easyproperty.tests.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Generate random valid UK post codes
 * 
 * @author vlado a.
 *
 */
public class RandomPostcode {

  private static String FILE_LOCATION = "src/main/resources/postcodes.csv";
  private static int MAX_CHARS_TO_SKIP = 90000000;

  private static Random r = new Random();
  
  /**
   * Returns a random valid UK post code
   * 
   * @return
   * @throws IOException
   */
  public static String getPostcode() throws IOException {
    return getRandomPostcodeRecord().split(",")[1];
  }

  public static class GeolocatedPostcode {
    public String fullCode;
    public String partialCode;
    public String latitude;
    public String longitude;
  }

  /**
   * Returns a random valid UK post code with the geolocation coordinates
   * 
   * @return
   * @throws IOException
   */
  public static GeolocatedPostcode getGeolocatedPostcode() throws IOException {
    String record = getRandomPostcodeRecord();
    String[] elements = record.split(",");
    GeolocatedPostcode codeWithLocation = new GeolocatedPostcode();
    codeWithLocation.fullCode = elements[1];
    codeWithLocation.partialCode = getOutwardCodeFromFullCode(elements[1]);
    codeWithLocation.latitude = fixCoordinateForPEX(elements[2]);
    codeWithLocation.longitude = fixCoordinateForPEX(elements[3]);
    return codeWithLocation;
  }

  private static String getRandomPostcodeRecord() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader(FILE_LOCATION));
    String record = null;
    try {
      in.skip(r.nextInt(MAX_CHARS_TO_SKIP));
      in.readLine();
      record = in.readLine();
    }
    finally {
      in.close();
    }
    return record;
  }

  private static String getOutwardCodeFromFullCode(String fullCode) {
    return fullCode.substring(0, fullCode.length() - 3);
  }

  private static String fixCoordinateForPEX(String geoCoordinate) {
    // Add the leading zeros for coordinates with absolute value < 1.0
    if (geoCoordinate.startsWith(".")) {
      geoCoordinate = "0" + geoCoordinate;
    }
    if (geoCoordinate.startsWith("-.")) {
      geoCoordinate = geoCoordinate.replace("-.", "-0.");
    }
    // PEX seems to want exactly 4 digits after the decimal point
    int pointIndex = geoCoordinate.indexOf(".");
    return geoCoordinate.substring(0, pointIndex + 5);
  }
}
