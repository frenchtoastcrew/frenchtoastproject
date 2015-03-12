package com.nissan.tests.pex.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

/**
 * Page object for the Property View of PEX MS
 * 
 * @author Vladimir Mihov
 * 
 */
public class ViewProperty {

	public static final String STATUS_INCOMPLETE = "1. Incomplete";
	public static final String STATUS_NOT_REVIEWED = "2. Not Reviewed";
	public static final String STATUS_WAITING_REVIEW = "3. Waiting Review";
	public static final String STATUS_IN_REVIEW = "4. In Review";
	public static final String STATUS_ACCEPTED = "5. Accepted";
	public static final String STATUS_REJECTED = "6. Rejected";
	public static final String STATUS_SOLD = "7. Sold";
	public static final String STATUS_ARCHIVE = "8. Archive";

	private WebDriver wd;

	@FindBy(how = How.XPATH, using = ".//img[@alt='Modify this record']")
	private WebElement modifyRecord;

	@FindBy(how = How.ID, using = "formSubmitButton_top")
	private WebElement saveButton;

	@FindBy(how = How.ID, using = "Property_status")
	private WebElement propertyStatusDropdown;

	@FindBy(how = How.ID, using = "Property_notShowOnWebsite")
	private WebElement unpublish;

	public ViewProperty(WebDriver wd) {
		this.wd = wd;
	}

	public boolean isUnpublished() {
		boolean isUnublished = unpublish.isSelected();
		if (isUnublished) {
			return true;
		}
		return false;
	}

	/**
	 * Click on Unpublish checkbox
	 * 
	 *  true - live
	 *  false - offline
	 * 
	 */
	public void setPublished(boolean condition) {
		boolean isUnpublished = isUnpublished();
		if (condition == isUnpublished) {
			unpublish.click();
		}

	}

	/**
	 * Select on Accepted Status
	 * 
	 */
	public void selectSatus(String status) {

		Select select = new Select(wd.findElement(By.id("Property_status")));
		select.selectByValue(status);

	}

	/**
	 * Click on modify property botton
	 * 
	 */
	public void clickModifyRecordButton() {

		modifyRecord.click();

	}

	/**
	 * Click on save property button
	 * 
	 */
	public void clickSaveButton() {

		saveButton.click();

	}

}
