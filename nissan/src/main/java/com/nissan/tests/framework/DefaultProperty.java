package com.nissan.tests.framework;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.nissan.tests.website.pages.SearchResults;
import com.nissan.tests.website.pages.elements.PropertyPreview;
import com.nissan.tests.website.pages.elements.SquareSearchResult;

/**
 * Test class describing the features of the default test property
 * 
 * @author vlado a.
 *
 */
public class DefaultProperty extends WdEx {

  public static final int BEDS = 3;
  public static final int BATHROOMS = 2;

  public static final String PROPERTY_TYPE = "House-Semi-Detached";
  public static final String PROPERTY_TYPE_MEDIUM = "Semi-detached";
  public static final String PROPERTY_TYPE_SHORT = "semidetached";
  
  public static final String ADDRESS = "Front Street, Newbiggin-By-The-Sea, NE64";
  public static final String ADDRESS2 = "63, Front Street, Newbiggin-By-The-Sea";
  public static final String ADDRESS3 = "63, Front Street, Newbiggin-By-The-Sea, Northumberland";

  public static final String POSTCODE = "NE64 6NJ";
  public static final String CITY = "Newbiggin-by-the-Sea";
  
  public static final String FURNISHING = "Furnished";
  public static final double DEPOSIT = 20000;
  public static final String AVAILABLE_FROM = "13th Feb 2015";
  public static final String MINIMUM_TENANCY = "24 Months";

  public static final boolean STUDENTS_ALLOWED = true;
  public static final boolean PETS_ALLOWED = true;
  public static final boolean DSS_ALLOWED = true;
  public static final boolean SMOKERS_ALLOWED = false;
  
  public static final double MONTHLY_PRICE = 152;

  public static final int MAIN_PHOTO_COLOR = -65280; // RED
  public static final int SECOND_PHOTO_COLOR = -16776962; // BLUE
  public static final int THIRD_PHOTO_COLOR = -16711935; // GREEN

  public static final int FIRST_FLOORPLAN_COLOR = -327936; // YELLOW
  public static final int SECOND_FLOORPLAN_COLOR = -16711681; // CYAN

  public static final String[] FEATURES = { "Property Feature 1", "Property Feature 2", "Property Feature 3",
      "Property Feature 4" };
  
  public static final String DESCRIPTION = "This is the full description of the test automation property.";
  public static final String SHORT_DESCRIPTION = "This is the short description of the test automation property.";

  // This is a highly specific search that is very unlikely to locate any other
  // properties
  private static String SEARCH_URL = "search/ne64/any-radius/HOUSES-SEMI-DETACHED/3-bedrooms/from-150/to-200/"
      + "page-0/per-page-20/sort-by-price-asc/";

  /**
   * Open a search results page containing only the test property
   * 
   * @param wd
   * @return
   */
  public static SearchResults getSearchResultsWithTheProperty(WebDriver wd) {
    wd.get(getSystemProperty("WebsiteURL") + SEARCH_URL);
    SearchResults results = PageFactory.initElements(wd, SearchResults.class);
    Assert.assertEquals(results.displayedResultsCount(), 1, "Exactly one property expected in the results");
    return results;
  }

  /**
   * Locate the test automation property in the search results
   * 
   * @param wd
   * @return
   */
  public static SquareSearchResult getSearchResult(WebDriver wd) {
    return getSearchResultsWithTheProperty(wd).getSearchResult(1);
  }
  
  /**
   * Locate the test automation property in the search results and then open its
   * preview
   * 
   * @param wd
   * @return
   */
  public static PropertyPreview getPreview(WebDriver wd) {
    return getSearchResult(wd).click();
  }
  
  /**
   * Gets the id of the default property
   * 
   * @return
   * @throws Exception
   */
  public static String getId() throws Exception {
    PexApi pex = new PexApi();
    ArrayList<String> properties = pex.getPropertyIds(DefaultUser.getId(), true);
    Assert.assertEquals(properties.size(), 1, "More than one property is published for the default user");
    return properties.get(0);
  }
}
