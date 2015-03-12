package com.nissan.tests.website.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

import com.easyproperty.tests.framework.PageBase;
import com.easyproperty.tests.framework.Sync;
import com.easyproperty.tests.website.pages.elements.OwnedProperties;

/**
 * Page object for the /listownedproperties page
 * 
 * @author Vladimir Mihov
 * 
 */

public class ListOwnedProperties extends PageBase {

	public ListOwnedProperties(WebDriver wd) {
		super(wd);
		Assert.assertTrue(
				Sync.wait(() -> wd.getCurrentUrl().contains(
						"/admin/listownedproperties")),
				"We are not on the owned properties page");
	}

	@FindBy(how = How.XPATH, using = "//ul[contains(@class,'properties__list')]")
	private WebElement propertiesListContainer;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'REFERENCE TENANTS')]")
	private WebElement referenceTenantsButton;

	// buttons when user does not have added properties
	@FindBy(how = How.XPATH, using = "//a[contains(text(),'ADD YOUR PROPERTY')]")
	private WebElement addPropertyButton;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'SEARCH FOR PROPERTY')]")
	private WebElement searchForPropertyButton;

	/**
	 * Clicks the referenceTenants button
	 */
	public void referenceTenantsButtonClick() {
		referenceTenantsButton.click();
		// TODO add return declaration for reference tenants page
	}

	/**
	 * Clicks the Add Your Property button
	 */
	public void addYourPropertyButtonClick() {
		addPropertyButton.click();
		// TODO add return declaration for add property page
	}

	/**
	 * Clicks the Search for Property button
	 */
	public void searchForPropertyButtonClick() {
		searchForPropertyButton.click();
		// TODO add return declaration for search property page
	}

	/**
	 * True if the page shows no properties
	 *
	 * @return
	 */
	public boolean areNoPropertiesDispalyed() {
		return !isMissing(By.xpath("//a[contains(text(),'ADD YOUR PROPERTY')]"));
	}

	/**
	 * Returns the owned property object for the n'th result
	 *
	 * @param n
	 *            1-based index
	 * @return
	 */
	public OwnedProperties getOwnedProperty(int n) {
		// Locate the container of the owned properties and create the object
		return new OwnedProperties(wd, propertiesListContainer.findElement(By
				.xpath("./li[" + n + "]")));
	}

	/**
	 * Returns all properties displayed on the page
	 *
	 * @return
	 */
	public List<OwnedProperties> getAllOwnedProperties() {
		ArrayList<OwnedProperties> results = new ArrayList<OwnedProperties>();
		if (!areNoPropertiesDispalyed()) {
			for (WebElement container : propertiesListContainer.findElements(By
					.xpath("./li"))) {
				results.add(new OwnedProperties(wd, container));
			}
		}
		return results;
	}

}
