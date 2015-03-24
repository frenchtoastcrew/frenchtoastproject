package com.nissan.tests.website.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.nissan.tests.framework.PageBase;
import com.nissan.tests.framework.Sync;

/**
 * Page object for the user details page from the test drive booking journey
 *
 * @author Ivo Varbanov
 *
 */

public class TestDriveDetails extends PageBase {

  public TestDriveDetails(WebDriver wd) {
    super(wd);

    // Make sure we are on the Select Dealer GTR page
    Assert.assertTrue(Sync.wait(() -> !isMissing(By
        .xpath("//h2[@class = 'strapline'][contains(text(), 'Please enter your details below')]"))));
  }

  // Page web elements
  @FindBy(how = How.XPATH, using = ".//*[@id='title']")
  private Select dealerListContainer;

  @FindBy(how = How.XPATH, using = ".//*[@id='firstname']")
  private WebElement fName;

  @FindBy(how = How.XPATH, using = ".//*[@id='lastname']")
  private WebElement lName;
  
  @FindBy(how = How.XPATH, using = ".//*[@id='user_email']")
  private WebElement email;
  
  @FindBy(how = How.XPATH, using = ".//*[@id='postcode']")
  private WebElement postCode;
  
  @FindBy(how = How.XPATH, using = ".//*[@id='address1']")
  private WebElement addr1;
  
  @FindBy(how = How.XPATH, using = ".//*[@id='address2']")
  private WebElement addr2;
  
  @FindBy(how = How.XPATH, using = ".//*[@id='city']")
  private WebElement city;
  
  @FindBy(how = How.XPATH, using = ".//*[@id='phone2']")
  private WebElement mobile;
  
  @FindBy(how = How.XPATH, using = ".//*[@id='recive']")
  private WebElement contactMail;
  
  @FindBy(how = How.XPATH, using = ".//*[@id='recive2']")
  private WebElement contactSMS;
  
  @FindBy(how = How.XPATH, using = ".//*[@id='recive3']")
  private WebElement contactPhoneMail;
  
  /**
   * Returns the dealer object for the n'th result
   *
   * @param n
   *          1-based index
   * @return
   */
  public void fillOutDetailsForm(String title, String fName, String lName, String email, String postCode, String addr1,
      String addr2, String city) {
    dealerListContainer.selectByValue(title);
    this.fName.sendKeys(fName);
    this.lName.sendKeys(lName);
    this.email.sendKeys(email);
    this.postCode.sendKeys(postCode);
    this.addr1.sendKeys(addr1);
    this.addr2.sendKeys(addr2);
    this.city.sendKeys(city);
    
    //TODO finish the setter method or write separate setters instead
    
    
    
  }

}
