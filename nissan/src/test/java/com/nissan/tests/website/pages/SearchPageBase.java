package com.nissan.tests.website.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.nissan.tests.framework.PageBase;

/**
 * Base class for all pages where you can search for location
 * 
 * @author vlado a.
 *
 */
public class SearchPageBase extends PageBase {

  @FindBy(how = How.ID, using = "search_location")
  protected WebElement searchField;
  @FindBy(how = How.NAME, using = "submit")
  protected WebElement searchButton;

  public SearchPageBase(WebDriver wd) {
    super(wd);
  }

  /**
   * Type a search text into the search field and submit the search
   * 
   * @param searchString
   * @return SearchResults page
   */
  public SearchResults searchFor(String searchString) {
    typeSearchText(searchString);
    return submitSearch();
  }

  /**
   * Submit the search (equivalent of hitting enter in the search field)
   * 
   * @return SearchResults page
   */
  public SearchResults submitSearch() {
    searchButton.submit();
    return PageFactory.initElements(wd, SearchResults.class);
  }

  /** 
   * Types some text in the search box
   * @param searchText
   */
  public void typeSearchText(String searchText) {
    searchField.clear();
    searchField.sendKeys(searchText);
  }

  /**
   * Gets the currently entered text in the search box
   * 
   * @return
   * @throws Exception
   */
  public String getSearchText() throws Exception {
    return searchField.getAttribute("value");
  }

  /**
   * Returns true if autocomplete results are open
   * 
   * @return
   */
  public boolean autocompleteIsOpen() {
    List<WebElement> autocompleteResults = wd.findElements(By.xpath("//div[@class='autocompleteResults']/ul/li/a"));
    return !autocompleteResults.isEmpty() && autocompleteResults.get(0).isDisplayed();
  }

  /**
   * Gets the presented autocomplete suggestions
   * 
   * @return
   */
  public List<String> getAutocompleteSuggestions() {
    return wd.findElements(By.xpath("//div[@class='autocompleteResults']/ul/li/a")).stream()
        .map(WebElement::getText).collect(Collectors.toList());
  }

  /**
   * Clicks on some autocomplete text
   * 
   * @param text
   */
  public void clickOnAutocompleteSuggestion(String text) {
    wd.findElement(By.xpath(String.format("//div[@class='autocompleteResults']/ul/li/a[text()='%s']", text))).click();
  }
}
