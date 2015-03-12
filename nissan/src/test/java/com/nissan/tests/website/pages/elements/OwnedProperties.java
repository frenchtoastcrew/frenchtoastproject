package com.easyproperty.tests.website.pages.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.easyproperty.tests.framework.Sync;
import com.easyproperty.tests.framework.WdEx;
import com.easyproperty.tests.website.pages.Login;
import com.easyproperty.tests.website.pages.OwnedPropertyDetails;

/**
 * Class representing property object in /listownedproperties page
 *
 * @author Vladimir Mihov
 *
 */
public class OwnedProperties extends WdEx {

	private WebElement container;

	public OwnedProperties(WebDriver wd, WebElement container) {
		this.wd = wd;
		this.container = container;
		Sync.wait(() -> container.isDisplayed());
	}

	/**
	 * The status of the property
	 *
	 * @return
	 */
	public String getPropertyStatus() {
		Boolean isCompleteButtonMissing = isMissingFromContainer(container,
				By.xpath(".//span[contains(@class,'advert-status-btn')]"));
		if (!isCompleteButtonMissing) {
			return "Incomplete";
		}
		return container
				.findElement(
						By.xpath(".//span[contains(@class,'advert-status')]"))
				.getText().trim();
	}

	/**
	 * The Tenancy status of the property
	 *
	 * @return
	 */
	public String getPropertyTenancyStatus() {
		Boolean isCompleteButtonMissing = isMissingFromContainer(container,
				By.xpath(".//span[contains(@class,'advert-status-btn')]"));

		if (!isCompleteButtonMissing) {
			return "Not Available - Incomplete proeprty";
		} else {
			Boolean isApprovedMissing = isMissingFromContainer(container,
					By.xpath(".//span[contains(text(),'Approved')]"));

			if (!isApprovedMissing) {

				Boolean isAvailableMissing = isMissingFromContainer(container,
						By.xpath(".//span[contains(@class,'available')]"));
				if (!isAvailableMissing) {
					return "Available";
				}
				return "Let Agreed";

			}
			return "Property in not Approved status - no tenancy status displayed";
		}

	}

	/**
	 * The Listing status of the property
	 *
	 * @return
	 */
	public String getPropertyListingStatus() {
		Boolean isCompleteButtonMissing = isMissingFromContainer(container,
				By.xpath(".//span[contains(@class,'advert-status-btn')]"));
		if (!isCompleteButtonMissing) {
			return "Incomplete";
		} else {

			Boolean isOfflineLabelMissing = isMissingFromContainer(container,
					By.xpath(".//span[contains(@class,'notonline')]"));
			if (!isOfflineLabelMissing) {

				return "OFFLINE";

			}
			return "LIVE";
		}

	}

	/**
	 * The address text displayed in the first line under the picture
	 *
	 * @return
	 */
	public String getAddressText() {
		return container
				.findElement(
						By.xpath(".//span[contains(@class,'property_name')]"))
				.getText().trim();
	}

	/**
	 * The property reference number, the text on the left from the second line
	 * under the picture
	 * 
	 * @return
	 */
	public String getPropertyRefNumber() {
		return container
				.findElement(By.xpath(".//label[contains(@class,'text-left')]"))
				.getText().trim();
	}

	/**
	 * Number of pending offers related to this property
	 * 
	 * @return
	 */
	public String getPropertyOffersNumber() {
		return container
				.findElement(
						By.xpath(".//span[contains(@class,'total_offers')]/label"))
				.getText().trim();
	}

	/**
	 * Number of unread messages related to this property
	 * 
	 * @return
	 */
	public String getPropertyMessagesNumber() {
		return container
				.findElement(
						By.xpath(".//span[contains(@class,'unread_items')]/label"))
				.getText().trim();
	}

	/**
	 * Clicks the property box to open the property details page
	 *
	 * @return OwnedPropertyDetails
	 */
	public OwnedPropertyDetails click() {

		// Click
		container.findElement(By.xpath(".//a[@class='property-select']"))
				.click();

		return PageFactory.initElements(wd, OwnedPropertyDetails.class);
	}

}
