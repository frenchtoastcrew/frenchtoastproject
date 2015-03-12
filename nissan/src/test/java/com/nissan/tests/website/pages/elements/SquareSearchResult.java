package com.easyproperty.tests.website.pages.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.easyproperty.tests.framework.Log;
import com.easyproperty.tests.framework.Sync;
import com.easyproperty.tests.framework.WdEx;

/**
 * Class representing a single search result using the default square view
 *
 * @author vlado a.
 *
 */
public class SquareSearchResult extends WdEx {

	private WebElement container;

	public SquareSearchResult(WebDriver wd, WebElement container) {
		this.wd = wd;
		this.container = container;
		Sync.wait(() -> container.isDisplayed());
	}

	/**
	 * The property type text displayed in the first line under the picture,
	 * i.e. '3 bed ground floor flat'
	 * 
	 * @return
	 */
	public String getPropertyTypeText() {
		return container
				.findElement(By.xpath(".//div[contains(@class,'type')]"))
				.getText().trim();
	}

	/**
	 * The address text displayed in the second line under the picture
	 *
	 * @return
	 */
	public String getAddressText() {
		return container
				.findElement(By.xpath(".//div[contains(@class,'address')]"))
				.getText().trim();
	}

	/**
	 * Returns the number of bedrooms for the search result (Studio == 0)
	 *
	 * @return
	 */
	public int getNumberOfBedrooms() {
		String text = getPropertyTypeText();
		if (!text.contains(" bed") && text.contains("studio")) {
			return 0;
		}
		return Integer.parseInt(text.split(" bed")[0]);
	}

	/**
	 * Returns the displayed property type (i.e. flat, ground floor flat, etc)
	 *
	 * @return
	 */
	public String getPropertyType() {
		String text = getPropertyTypeText();
		if (!text.contains(" bed ") && text.contains("studio")) {
			return "studio";
		}
		String[] parts = text.split(" bed ");
		if (parts.length == 2) {
			// The normal case
			return parts[1];
		} else {
			// This is actually an illegal property that does not have property
			// type
			Log.messageRed("Property without property type located in the results");
			return "";
		}

	}

	/**
	 * Returns the monthly price
	 *
	 * @return
	 */
	public Double getMonthlyPrice() {
		String priceText = container
				.findElement(
						By.xpath(".//div[contains(@class,'info_monthly')]/div[contains(@class,'price')]"))
				.getText().trim();
		return parsePoundPrice(priceText);
	}

	/**
	 * Returns true if the search result is grayed out
	 *
	 * @return
	 */
	public boolean isGrayedOut() {
		WebElement el = container.findElement(By.xpath("./div"));
		return el.getAttribute("style").contains("opacity: 0.5");
	}

  /**
   * Returns true if the pixel in the centre of the thumbnail displayed in the
   * search result is of some expected color
   * 
   * @param expectedColour
   * @return
   * @throws Exception
   */
  public boolean verifyThumbnailColour(int expectedColour) throws Exception {
    Assert.assertFalse(isGrayedOut(), "We can not verify the color of grayed-out search results");
    WebElement img = container.findElement(By.xpath("./div/a"));
    return areColorsSimilar(expectedColour, getCentralPixelColor(img));
  }

	/**
	 * Clicks the search result to open the preview
	 *
	 * @return
	 */
	public PropertyPreview click() {
		// Find out if another preview is already opened
		By containerLocator = By
				.xpath("//section[@id='properties-search']/div[contains(@class,'property_preview_cont')]");
		boolean previewAlreadyOpened = wd.findElement(containerLocator)
				.isDisplayed();

		// Click
		container.findElement(By.xpath(".//a[@class='property-select']"))
				.click();

		if (previewAlreadyOpened) {
			// Wait a little to give the old preview time to close
			sleep(VERY_SHORT_WAIT);
		}

		WebElement container = wd
				.findElement(By
						.xpath("//section[@id='properties-search']/div[contains(@class,'property_preview_cont')]"));
		return new PropertyPreview(wd, container);
	}

	public void clickPictureIcon() {
		// TODO
	}

	/**
	 * Is let agreed label missing on property square result
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isLetAgreedMissing(String propertyId) {
		return isMissing(By.xpath("//div[@class ='property ep" + propertyId
				+ "']/span[contains(@class,'let_agreed_label')]"));
	}

}
