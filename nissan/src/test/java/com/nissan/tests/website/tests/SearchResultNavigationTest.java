package com.easyproperty.tests.website.tests;

import java.util.List;

import org.testng.annotations.Test;

import com.easyproperty.tests.framework.BrowserProfilesDataProvider;
import com.easyproperty.tests.framework.DefaultProperty;
import com.easyproperty.tests.framework.PageBase.ResponsiveSiteVersion;
import com.easyproperty.tests.framework.WebsiteTestBase;
import com.easyproperty.tests.website.pages.SearchResults;
import com.easyproperty.tests.website.pages.elements.PropertyPreview;
import com.easyproperty.tests.website.pages.elements.SquareSearchResult;

/**
 * Tests for navigating between the search results and the property preview
 *
 * @author vlado a.
 *
 */
public class SearchResultNavigationTest extends WebsiteTestBase {

  @Test(dataProvider = "browserProfiles", dataProviderClass = BrowserProfilesDataProvider.class)
  public void searchResultListNavigation(String browserProfile) throws Exception {

    startTest("Test navigating in the search results list", browserProfile);

    startStep("Search for London. There should be results for this test");
    SearchResults resultsPage = goToSearchResultsPage("London");
    endStep(resultsPage.displayedResultsCount() > 0);

    startStep("Click on the first result. The property preview appears");
    SquareSearchResult result1 = resultsPage.getSearchResult(1);
    PropertyPreview preview = result1.click();
    endStep();

    if (resultsPage.getCurrentResponsiveSiteVersion() == ResponsiveSiteVersion.WEB) {
      // Only on the 'web' version of the site the results list remain visible
      // when preview is opened
      startStep("All the search results except the first are grayed-out");
      List<SquareSearchResult> results = resultsPage.getAllSearchResults();
      results.remove(0);
      endStep(!result1.isGrayedOut() && results.stream().allMatch((result) -> result.isGrayedOut()));
    } else {
      startStep("Close the preview to go back to the results");
      endStep(preview.close());
    }

    startStep("The data in the preview corresponds to the data in the search result");
    endStep(preview.isShowing(result1));

    startStep("Click on the second result");
    SquareSearchResult result2 = resultsPage.getSearchResult(2);
    preview = result2.click();
    endStep();

    if (resultsPage.getCurrentResponsiveSiteVersion() == ResponsiveSiteVersion.WEB) {
      startStep("All the search results except the second are grayed-out");
      List<SquareSearchResult> results = resultsPage.getAllSearchResults();
      results.remove(1);
      endStep(!result2.isGrayedOut() && results.stream().allMatch((result) -> result.isGrayedOut()));
    }

    startStep("The preview now shows the second search result");
    endStep(preview.isShowing(result2));

    startStep("Click the close button in the preview. It should close");
    endStep(preview.close());

    startStep(String.format("If there are more than %d results scroll to the bottom to load more",
        SearchResults.RESULTS_PAGE_SIZE));
    resultsPage.tryToLoadMoreResults();
    endStep();

    startStep("Click on the last loaded result. The preview that opens shows the correct data");
    SquareSearchResult lastResult = resultsPage.getSearchResult(resultsPage.loadedResultsCount());
    preview = lastResult.click();
    endStep(preview.isShowing(lastResult));

    endTest();

  }
  
  @Test(dataProvider = "defaultBrowser", dataProviderClass = BrowserProfilesDataProvider.class)
  public void thumbnailInSearchResults(String browserProfile) throws Exception {

    startTest("Verify the image thumbnail in the search results list", browserProfile);

    startStep("Locate the default test property in the search results list");
    SquareSearchResult property = DefaultProperty.getSearchResult(wd);
    endStep();

    startStep("The thumbnail of the property is with the correct color (red)");
    endStep(property.verifyThumbnailColour(DefaultProperty.MAIN_PHOTO_COLOR));

    endTest();

  }
  
  @Test(dataProvider = "defaultBrowser", dataProviderClass = BrowserProfilesDataProvider.class)
  public void photosRotationInPropertyPreview(String browserProfile) throws Exception {

    startTest("Verify the automatic rotation of photos in property preview", browserProfile);

    startStep("Open the preview of the test property. The photo should be of the correct color (red)");
    PropertyPreview preview = DefaultProperty.getPreview(wd);
    endStep(preview.verifyCurrentGalleryImageColor(DefaultProperty.MAIN_PHOTO_COLOR));

    startStep(String.format("Wait %d seconds. The second photo should be displayed (blue)",
        preview.GALLERY_ROTATION_INTERVAL));
    sleep(preview.GALLERY_ROTATION_INTERVAL);
    endStep(preview.verifyCurrentGalleryImageColor(DefaultProperty.SECOND_PHOTO_COLOR));

    startStep(String.format("Wait %d seconds. The third photo should be displayed (green)",
        preview.GALLERY_ROTATION_INTERVAL));
    sleep(preview.GALLERY_ROTATION_INTERVAL);
    endStep(preview.verifyCurrentGalleryImageColor(DefaultProperty.THIRD_PHOTO_COLOR));

    startStep(String.format("Wait %d seconds. The main photo should be displayed again (red)",
        preview.GALLERY_ROTATION_INTERVAL));
    sleep(preview.GALLERY_ROTATION_INTERVAL);
    endStep(preview.verifyCurrentGalleryImageColor(DefaultProperty.MAIN_PHOTO_COLOR));

    endTest();
  }

  @Test(dataProvider = "defaultBrowser", dataProviderClass = BrowserProfilesDataProvider.class)
  public void floorplansRotationInPropertyPreview(String browserProfile) throws Exception {

    startTest("Verify the automatic rotation of floorplan images in property preview", browserProfile);

    startStep("Open the preview of the test property and click switch to floorplan. "
        + "The displayed image should be of the correct color (yellow)");
    PropertyPreview preview = DefaultProperty.getPreview(wd);
    preview.switchToFloorplan();
    endStep(preview.verifyCurrentGalleryImageColor(DefaultProperty.FIRST_FLOORPLAN_COLOR));

    startStep(String.format("Wait %d seconds. The second floor plan image should be displayed (cyan)",
        preview.GALLERY_ROTATION_INTERVAL));
    sleep(preview.GALLERY_ROTATION_INTERVAL);
    endStep(preview.verifyCurrentGalleryImageColor(DefaultProperty.SECOND_FLOORPLAN_COLOR));

    startStep(String.format("Wait %d seconds. The first floor plan should be displayed again (yellow)",
        preview.GALLERY_ROTATION_INTERVAL));
    sleep(preview.GALLERY_ROTATION_INTERVAL);
    endStep(preview.verifyCurrentGalleryImageColor(DefaultProperty.FIRST_FLOORPLAN_COLOR));

    endTest();
  }
}
