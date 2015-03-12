package com.nissan.tests.website.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.easyproperty.tests.framework.Log;
import com.easyproperty.tests.framework.Sync;
import com.easyproperty.tests.website.pages.elements.SquareSearchResult;

/**
 * Page object for the search results page
 *
 * @author vlado a.
 */
public class SearchResults extends SearchPageBase {

	// Default values for the filtering options
	public static final String MIN_BEDS_DEFAULT = "Min Beds";
	public static final String SEARCH_RADIUS_DEFAULT = "This area only";
	public static final String PROPERTY_TYPE_DEFAULT = "Property type";
	public static final String MIN_PRICE_DEFAULT = "No Min";
	public static final String MAX_PRICE_DEFAULT = "No Max";

	public static final String[] searchRadiusFilteringOptions = {
			SEARCH_RADIUS_DEFAULT, "Within quarter of a mile",
			"Within half a mile", "Within 1 mile", "Within 3 miles",
			"Within 5 miles", "Within 10 miles", "Within 20 miles" };

	public static final int RESULTS_PAGE_SIZE = 20;

	public SearchResults(WebDriver wd) {
		super(wd);
		// Verify that we are on the right page
		Assert.assertTrue(
				Sync.wait(() -> wd.getCurrentUrl().contains("/search/")),
				"We are not on the search results page");
		// If we are not in the web responsive design, wait for the filters
		// to close (i.e. wait for the page to become fully loaded). This is
		// needed on Chrome mostly.
		if (getCurrentResponsiveSiteVersion() != ResponsiveSiteVersion.WEB) {
			Sync.wait(() -> !wd.findElement(By.id("submitbutton"))
					.isDisplayed());
		}
	}

	@FindBy(how = How.XPATH, using = "//div[contains(@class,'propcount')]/span")
	private WebElement propertiesCountHeader;

	@FindBy(how = How.XPATH, using = "//ul[contains(@class,'properties_list')]")
	private WebElement propertiesListContainer;

	@FindBy(how = How.ID, using = "infscr-loading")
	private WebElement propertiesLoadingIndicator;

	// Filtering
	@FindBy(how = How.NAME, using = "search_radius")
	private WebElement searchRadius;

	@FindBy(how = How.NAME, using = "search_beds_min")
	private WebElement minBeds;

	@FindBy(how = How.NAME, using = "search_price_min")
	private WebElement minPrice;

	@FindBy(how = How.NAME, using = "search_price_max")
	private WebElement maxPrice;

	@FindBy(how = How.NAME, using = "search_property_type")
	private WebElement propertyType;

	@FindBy(how = How.ID, using = "search_location")
	private WebElement searchLocation;

	@FindBy(how = How.ID, using = "submitbutton")
	public WebElement searchButton;

	@FindBy(how = How.XPATH, using = "//span[contains(@class,'mobile_filter_control')]")
	public WebElement showFiltersButton;

	// TODO: Add the rest of the elements on the page

	/**
	 * Gets the number displayed in the 'NN properties found' header
	 *
	 * @return
	 */
	public int displayedResultsCount() {
		if (areNoResultsDispalyed()) {
			Log.comment("No results");
			return 0;
		}
		int count = Integer.parseInt(propertiesCountHeader.getText().replace(
				" properties found", ""));
		Log.comment("Search results displayed: " + count);
		return count;
	}

	/**
	 * Gets the number of the results actually loaded in the page (more results
	 * are dynamically loaded as you scroll to the bottom of the list)
	 *
	 * @return
	 */
	public int loadedResultsCount() {
		if (areNoResultsDispalyed()) {
			Log.comment("No results");
			return 0;
		}
		return propertiesListContainer.findElements(By.xpath("./li")).size();
	}

	/**
	 * Returns the current search location
	 * 
	 * @return
	 */
	public String currentSearchLocation() {
		openFiltersIfNeeded();
		return searchLocation.getAttribute("value");
	}

	/**
	 * Returns the selected Min Beds value
	 *
	 * @return
	 */
	public String selectedMinBeds() {
		openFiltersIfNeeded();
		Select minBedsSelect = new Select(minBeds);
		return minBedsSelect.getFirstSelectedOption().getText();
	}

	/**
	 * Selects the Min Beds value
	 *
	 * @param beds
	 *            minimum number of bedrooms (Studio==0)
	 */
	public void selectMinBeds(int beds) {
		openFiltersIfNeeded();
		Select minBedsSelect = new Select(minBeds);
		minBedsSelect.selectByValue(String.valueOf(beds));
	}

	/**
	 * Returns the selected Search Radius value
	 *
	 * @return
	 */
	public String selectedSearchRadius() {
		openFiltersIfNeeded();
		Select select = new Select(searchRadius);
		return select.getFirstSelectedOption().getText();
	}

	/**
	 * Selects the Search Radius value
	 *
	 * @param searchRadiusText
	 */
	public void selectSearchRadius(String searchRadiusText) {
		openFiltersIfNeeded();
		Select select = new Select(searchRadius);
		select.selectByVisibleText(searchRadiusText);
	}

	/**
	 * Selects the Property Type value
	 *
	 * @param type
	 *
	 */
	public void selectPropertyType(String type) {
		openFiltersIfNeeded();
		Select select = new Select(propertyType);
		select.selectByVisibleText(type);
	}

	/**
	 * Returns the selected Property Type value
	 *
	 * @return
	 */
	public String selectedPropertyType() {
		openFiltersIfNeeded();
		Select select = new Select(propertyType);
		return select.getFirstSelectedOption().getText();
	}

	/**
	 * Selects the minimum price value
	 *
	 * @param type
	 *
	 */
	public void selectMinPrice(String value) {
		openFiltersIfNeeded();
		Select select = new Select(minPrice);
		select.selectByVisibleText(value);
	}

	/**
	 * Returns the selected minimum price value
	 *
	 * @return
	 */
	public String selectedMinPrice() {
		openFiltersIfNeeded();
		Select select = new Select(minPrice);
		return select.getFirstSelectedOption().getText();
	}

	/**
	 * Selects the maximum price value
	 *
	 * @param type
	 *
	 */
	public void selectMaxPrice(String value) {
		openFiltersIfNeeded();
		Select select = new Select(maxPrice);
		select.selectByVisibleText(value);
	}

	/**
	 * Returns the selected maximum price value
	 *
	 * @return
	 */
	public String selectedMaxPrice() {
		openFiltersIfNeeded();
		Select select = new Select(maxPrice);
		return select.getFirstSelectedOption().getText();
	}

	/**
	 * Returns the search result object for the n'th result
	 *
	 * @param n
	 *            1-based index
	 * @return
	 */
	public SquareSearchResult getSearchResult(int n) {
		// Locate the container of the search results and create the object
		return new SquareSearchResult(
				wd,
				propertiesListContainer.findElement(By.xpath("./li[" + n + "]")));
	}

	/**
	 * Returns all search results currently displayed on the page
	 *
	 * @return
	 */
	public List<SquareSearchResult> getAllSearchResults() {
		ArrayList<SquareSearchResult> results = new ArrayList<SquareSearchResult>();
		if (!areNoResultsDispalyed()) {
			for (WebElement container : propertiesListContainer.findElements(By
					.xpath("./li"))) {
				results.add(new SquareSearchResult(wd, container));
			}
		}
		return results;
	}

	/**
	 * Clicks the search button in the filters bar
	 */
	public void search() {
		openFiltersIfNeeded();
		searchButton.click();
	}

	/**
	 * True if the page shows no results
	 *
	 * @return
	 */
	public boolean areNoResultsDispalyed() {
		return !isMissing(By.xpath("//div[contains(@class,'no_results')]"));
	}

	/**
	 * In case there are enough results for the current search (more than
	 * RESULTS_PAGE_SIZE), load more by scrolling down to bottom of the list
	 * 
	 * N.B. This does not work with the SafariDriver
	 *
	 * @throws Exception
	 */
	public void tryToLoadMoreResults() throws Exception {
		if (displayedResultsCount() > RESULTS_PAGE_SIZE) {
			// WD actions which are unsupported by Safari
			if (!(wd instanceof SafariDriver)) {
				// Find the last result on the page and scroll it into view
				(new Actions(wd)).moveToElement(
						propertiesListContainer.findElement(By
								.xpath("./li[last()]"))).perform();
				// Wait for the loading indicator to appear
				Sync.wait(() -> propertiesLoadingIndicator.isDisplayed(),
						VERY_SHORT_WAIT);
				// Wait for the loading indicator to disappear
				Sync.wait(() -> !propertiesLoadingIndicator.isDisplayed(),
						LONG_WAIT);
				Log.message("More results loaded");
			} else {
				Log.message("Can not load more results on Safari");
			}
		} else {
			Log.message("Not enough results");
		}
	}

	/**
	 * Opens the filters in case they are not opened in the current responsive
	 * design layout of the site
	 */
	public void openFiltersIfNeeded() {
		if (!searchButton.isDisplayed()) {
			if (getCurrentResponsiveSiteVersion() != ResponsiveSiteVersion.WEB) {
				showFiltersButton.click();
				// Wait the 'reveal' animation to complete
				Sync.wait(() -> searchButton.isDisplayed());
			} else {
				throw new IllegalStateException(
						"The filters should be always visible in the web version "
								+ "of the reponsive design");
			}
		}
	}

	// SearchPageBase overrides to handle the fact that on the search results
	// page
	// the search field and the submit button can be hidden in responsive
	// designs

	/**
	 * Type a search text into the search field and submit the search
	 * 
	 * @param searchString
	 * @return SearchResults page
	 */
	@Override
	public SearchResults searchFor(String searchString) {
		openFiltersIfNeeded();
		return super.searchFor(searchString);
	}

	/**
	 * Submit the search (equivalent of hitting enter in the search field)
	 * 
	 * @return SearchResults page
	 */
	@Override
	public SearchResults submitSearch() {
		openFiltersIfNeeded();
		return super.submitSearch();
	}

	/**
	 * Types some text in the search box
	 * 
	 * @param searchText
	 */
	@Override
	public void typeSearchText(String searchText) {
		openFiltersIfNeeded();
		searchField.clear();
		openFiltersIfNeeded();
		searchField.sendKeys(searchText);
	}

	/**
	 * Gets the currently entered text in the search box
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getSearchText() throws Exception {
		openFiltersIfNeeded();
		return super.getSearchText();
	}

	/**
	 * Search for property in search results page by propertyId
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isPropertyMissingInSearchresults(String propertyId) {
		return isMissing(By.xpath("//li[contains(@data-id,'ep" + propertyId
				+ "')]"));
	}

}
