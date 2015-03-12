package com.easyproperty.tests.website.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.easyproperty.tests.framework.BrowserProfilesDataProvider;
import com.easyproperty.tests.framework.Log;
import com.easyproperty.tests.framework.Sync;
import com.easyproperty.tests.framework.WebsiteTestBase;
import com.easyproperty.tests.website.pages.SearchResults;
import com.easyproperty.tests.website.pages.elements.SquareSearchResult;

/**
 * Tests for the search results filtering functionality
 *
 * @author vlado a.
 *
 */
public class SearchResultFilteringTest extends WebsiteTestBase {

  @Test(dataProvider = "browserProfiles", dataProviderClass = BrowserProfilesDataProvider.class)
  public void filteringByMinBeds(String browserProfile) throws Exception {

    startTest("Test filtering by minimum bedrooms", browserProfile);

    startStep("Search for London. There should be results for this test");
    SearchResults resultsPage = goToSearchResultsPage("London");
    endStep(resultsPage.displayedResultsCount() > 0);

    startStep("By default there is no filtering by Min Beds");
    int initialResultsCount = resultsPage.displayedResultsCount();
    endStep(resultsPage.selectedMinBeds().equals(SearchResults.MIN_BEDS_DEFAULT));

    startStep("Select 'Studio' for minimum bedrooms and search");
    resultsPage.selectMinBeds(0);
    resultsPage.search();
    endStep();

    startStep("The minimum bedrooms drop-down should be reverted back to the default value "
        + SearchResults.MIN_BEDS_DEFAULT);
    endStep(Sync.wait(() -> resultsPage.selectedMinBeds().equals(SearchResults.MIN_BEDS_DEFAULT)));

    startStep("The number of results remains the same");
    endStep(resultsPage.displayedResultsCount() == initialResultsCount);

    // Test all the number of bedrooms options from 1 to 5
    for (int n = 1; n <= 5; n++) {

      final int resCount = resultsPage.displayedResultsCount();

      startStep("Select " + n + " for minimum bedrooms and search");
      resultsPage.selectMinBeds(n);
      resultsPage.search();
      endStep();

      startStep("The number of results returned should be the same or smaller than the previous one");
      // Wait for the number of results to change for maximum of 15
      // seconds
      Sync.wait(() -> resCount != resultsPage.displayedResultsCount(), MEDIUM_WAIT);
      int newResCount = resultsPage.displayedResultsCount();
      endStep(resCount >= newResCount);

      startStep(String.format("If there are more than %d results scroll to the bottom to load more",
          SearchResults.RESULTS_PAGE_SIZE));
      resultsPage.tryToLoadMoreResults();
      endStep();

      startStep("All of the displayed results have at least " + n + " bedrooms");
      boolean wrongResult = false;
      for (SquareSearchResult res : resultsPage.getAllSearchResults()) {
        if (res.getNumberOfBedrooms() < n) {
          Log.messageRed(res.getPropertyTypeText());
          wrongResult = true;
          break;
        }
      }
      endStep(!wrongResult);
    }

    endTest();

  }

  @Test(dataProvider = "browserProfiles", dataProviderClass = BrowserProfilesDataProvider.class)
  public void filteringBySearchRadius(String browserProfile) throws Exception {

    startTest("Test filtering by seacrh radius", browserProfile);

    startStep("Search for London. There should be results for this test");
    SearchResults resultsPage = goToSearchResultsPage("London");
    endStep(resultsPage.displayedResultsCount() > 0);

    startStep("By default only properties from this area are displayed");
    endStep(resultsPage.selectedSearchRadius().equals(resultsPage.SEARCH_RADIUS_DEFAULT));

    // Test all the remaining options for the search radius
    for (int n = 1; n < resultsPage.searchRadiusFilteringOptions.length; n++) {

      final int resCount = resultsPage.displayedResultsCount();

      startStep("Select '" + resultsPage.searchRadiusFilteringOptions[n] + "' for search radius and search");
      resultsPage.selectSearchRadius(resultsPage.searchRadiusFilteringOptions[n]);
      resultsPage.search();
      endStep();

      startStep("The number of results returned should be the same or larger than the previous one");
      // Wait for the number of results to change for maximum of 15
      // seconds
      Sync.wait(() -> resCount != resultsPage.displayedResultsCount(), MEDIUM_WAIT);
      int newResCount = resultsPage.displayedResultsCount();
      endStep(resCount <= newResCount);
    }

    final int oldResCount = resultsPage.displayedResultsCount();

    startStep("Select '" + resultsPage.SEARCH_RADIUS_DEFAULT + "' for search radius and search again");
    resultsPage.selectSearchRadius(resultsPage.SEARCH_RADIUS_DEFAULT);
    resultsPage.search();
    endStep();

    startStep("The number of results returned should be smaller than the previous one");
    // Wait for the number of results to change for maximum of 15
    // seconds
    Sync.wait(() -> oldResCount != resultsPage.displayedResultsCount(), MEDIUM_WAIT);
    int newResCount = resultsPage.displayedResultsCount();
    endStep(oldResCount >= newResCount);

    endTest();

  }

  @DataProvider
  public Object[][] propertyTypeData() throws Exception {
    return excelDataProvider();
  }

  @Test(dataProvider = "propertyTypeData")
  public void filteringByPropertyType(String browserProfile, String propertyType, String resultsPropertyType)
      throws Exception {

    startTest(String.format("Test filtering by '%s' property type", propertyType), browserProfile);

    startStep("Search for London. There should be results for this test");
    SearchResults resultsPage = goToSearchResultsPage("London");
    endStep(resultsPage.displayedResultsCount() > 0);

    startStep("By default there is no filtering by property type");
    int initialResultsCount = resultsPage.displayedResultsCount();
    endStep(resultsPage.selectedPropertyType().equals(resultsPage.PROPERTY_TYPE_DEFAULT));

    startStep("Select '" + propertyType + "' for property type and search");
    resultsPage.selectPropertyType(propertyType);
    resultsPage.search();
    endStep();

    startStep("The number of results should decrease");
    endStep(Sync.wait(() -> resultsPage.displayedResultsCount() < initialResultsCount));

    startStep(String.format("If there are more than %d results scroll to the bottom to load more",
        SearchResults.RESULTS_PAGE_SIZE));
    resultsPage.tryToLoadMoreResults();
    endStep();

    startStep("The selected property type should remain " + propertyType);
    endStep(resultsPage.selectedPropertyType().equals(propertyType));

    startStep("All of the displayed results should be " + resultsPropertyType);
    boolean wrongResult = false;
    for (SquareSearchResult res : resultsPage.getAllSearchResults()) {
      if (!res.getPropertyType().equals(resultsPropertyType)) {
        Log.messageRed(res.getPropertyTypeText());
        wrongResult = true;
        break;
      }
    }
    endStep(!wrongResult);

    startStep("Turn off the filtering by property type and search again. The number of results shohuld increase");
    int filteredCount = resultsPage.displayedResultsCount();
    resultsPage.selectPropertyType(resultsPage.PROPERTY_TYPE_DEFAULT);
    resultsPage.search();
    endStep(Sync.wait(() -> resultsPage.displayedResultsCount() > filteredCount));

    endTest();

  }

  @DataProvider
  public Object[][] priceFilteringData() throws Exception {
    return excelDataProvider();
  }

  @Test(dataProvider = "priceFilteringData")
  public void filteringByPrice(String browserProfile, Double minPrice, Double maxPrice) throws Exception {

    String minPriceText = getMinPriceFilteringOption(minPrice.intValue());
    String maxPriceText = getMaxPriceFilteringOption(maxPrice.intValue());

    startTest(String.format("Test filtering by price (%s-%s)", minPriceText, maxPriceText), browserProfile);

    startStep("Search for London. There should be results for this test");
    SearchResults resultsPage = goToSearchResultsPage("London");
    endStep(resultsPage.displayedResultsCount() > 0);

    startStep("By default there is no filtering by min and max price");
    int initialResultsCount = resultsPage.displayedResultsCount();
    endStep(resultsPage.selectedMinPrice().equals(SearchResults.MIN_PRICE_DEFAULT)
        && resultsPage.selectedMaxPrice().equals(SearchResults.MAX_PRICE_DEFAULT));

    startStep(String.format("Select '%s' for minimum, '%s' for maximum price and search", minPriceText, maxPriceText));
    resultsPage.selectMinPrice(minPriceText);
    resultsPage.selectMaxPrice(maxPriceText);
    resultsPage.search();
    endStep();

    startStep("The number of results should be less or equal than the previous number");
    // Wait for the number of results to change
    Sync.wait(() -> initialResultsCount != resultsPage.displayedResultsCount(), MEDIUM_WAIT);
    endStep(resultsPage.displayedResultsCount() <= initialResultsCount);

    startStep(String.format("If there are more than %d results scroll to the bottom to load more",
        SearchResults.RESULTS_PAGE_SIZE));
    resultsPage.tryToLoadMoreResults();
    endStep();

    startStep("All of the displayed results should be in the correct price range");
    boolean wrongResult = false;
    for (SquareSearchResult res : resultsPage.getAllSearchResults()) {
      // Check the min price
      Double price = res.getMonthlyPrice();
      if (minPrice != 0) {
        if (price < minPrice) {
          Log.messageRed("Wrong (minimum) price for " + res.getPropertyTypeText());
          wrongResult = true;
          break;
        }
      }
      // Check the max price
      if (maxPrice != 0) {
        if (price > maxPrice) {
          Log.messageRed("Wrong (maximum) price for " + res.getPropertyTypeText());
          wrongResult = true;
          break;
        }
      }
    }
    endStep(!wrongResult);

    endTest();
  }

  private String getMinPriceFilteringOption(int price) {
    return price == 0 ? SearchResults.MIN_PRICE_DEFAULT : String.format("%s%d PCM", POUND, price);
  }

  private String getMaxPriceFilteringOption(int price) {
    return price == 0 ? SearchResults.MAX_PRICE_DEFAULT : String.format("%s%d PCM", POUND, price);
  }


}
