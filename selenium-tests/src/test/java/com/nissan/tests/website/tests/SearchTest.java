package com.nissan.tests.website.tests;

import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.nissan.tests.framework.Sync;
import com.nissan.tests.framework.WebsiteTestBase;
import com.nissan.tests.website.pages.SearchPageBase;
import com.nissan.tests.website.pages.SearchResults;

/**
 * Tests for the search functionality
 *
 * @author vlado a.
 *
 */
public class SearchTest extends WebsiteTestBase {

  @DataProvider
  public Object[][] searchAutoComplete() throws Exception {
    return excelDataProvider();
  }

  @Test(dataProvider = "searchAutoComplete")
  public void searchAutocompleteTest(String browserProfile, String pageType, String searchText,
      Double expectedNumberOfResults, String resultToSelect) throws Exception {

    startTest(String.format("Test autocomplete for '%s' on the %s page", searchText, pageType), browserProfile);

    startStep(String.format("Go to the %s page", pageType));
    SearchPageBase page = goToPageWithSearch(pageType);
    endStep();

    boolean expectingResults = expectedNumberOfResults>0;
    startStep(String.format("Search for '%s'. The autocomplete list should %sappear", searchText, expectingResults ? ""
        : "not "));
    page.typeSearchText(searchText);
    endStep(expectingResults ? Sync.wait(() -> page.autocompleteIsOpen()) : !Sync.wait(() -> page.autocompleteIsOpen(),
        SHORT_WAIT));

    if (expectingResults) {
      List<String> results = page.getAutocompleteSuggestions();

      startStep(String.format("There should be %d autocomplete results", expectedNumberOfResults.intValue()));
      endStep(results.size() == expectedNumberOfResults.intValue());

      startStep(String.format("Click on the '%s' result. The autocomplete list disappears.", resultToSelect));
      page.clickOnAutocompleteSuggestion(resultToSelect);
      endStep(Sync.wait(() -> !page.autocompleteIsOpen()));

      startStep("The text in the search box is replaced with the selected result");
      endStep(page.getSearchText().equals(resultToSelect));

      startStep("Click search. The search results page opens showing properties from " + resultToSelect);
      SearchResults searchResultsPage = page.submitSearch();
      endStep(searchResultsPage.currentSearchLocation().equals(resultToSelect));
    }

    endTest();
  }
  
  private SearchPageBase goToPageWithSearch(String pageType) {
    switch(pageType) {
      case "Search":
        return goToSearchPage();
      case "Search Results":
        return goToSearchResultsPage("London");
      default:
        throw new IllegalArgumentException("Unknown page " + pageType);
    }
  }
}
