package com.nissan.tests.website.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

import com.nissan.tests.framework.PageBase;
import com.nissan.tests.framework.Sync;
import com.nissan.tests.website.pages.elements.Offer;

/**
 * A class for the landlord's owned property details page
 * 
 * @author vlado a.
 *
 */
public class OwnedPropertyDetails extends PageBase {

	public OwnedPropertyDetails(WebDriver wd) {
		super(wd);
		Assert.assertTrue(
				Sync.wait(() -> wd.getCurrentUrl().contains(
						"/ownedpropertydetails/")),
				"We are not on the owned property details page");
	}

	@FindBy(how = How.XPATH, using = "//a[contains(@class, 'property_button current_unpublish')]")
	private WebElement liveButton;

	@FindBy(how = How.XPATH, using = "//a[contains(@class, 'property_button current_publish')]")
	private WebElement offlineButton;

	@FindBy(how = How.XPATH, using = "//a[contains(@class, 'property_button current_letagreed')]")
	private WebElement availableButton;

	@FindBy(how = How.XPATH, using = "//a[contains(@class, 'property_button current_available')]")
	private WebElement letAgreedButton;

	/**
	 * Gets an offer page element by the email of the tenant that made the offer
	 * 
	 * @param tenantEmail
	 * @return
	 */
	public Offer getOfferByTenantEmail(String tenantEmail) {
		WebElement container = wd.findElement(By.xpath(String.format(
				"//span[@title='%s']/..", tenantEmail)));
		return new Offer(wd, container);
	}

	/**
	 * Click live button
	 */
	public void clickLiveButton() {
		liveButton.click();
	}

	/**
	 * Click offline button
	 */
	public void clickOfflineButton() {
		offlineButton.click();
	}

	/**
	 * Click available button
	 */
	public void clickAvailableButton() {
		availableButton.click();
	}

	/**
	 * Click let agreed button
	 */
	public void clickLetAgreedButton() {
		letAgreedButton.click();
	}

	// TODO: Complete the page

}
