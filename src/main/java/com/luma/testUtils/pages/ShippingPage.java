package com.luma.testUtils.pages;

import com.luma.testUtils.testObjects.Shipping;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ShippingPage extends Page {
    @FindBy(xpath = "(//*[@id='customer-email'])[1]")
    private WebElement emailAddressField;
    @FindBy(name = "firstname")
    private WebElement firstNameField;
    @FindBy(name = "lastname")
    private WebElement lastNameField;
    @FindBy(name = "company")
    private WebElement companyField;
    @FindBy(name = "street[0]")
    private WebElement firstStreetAddressField;
    @FindBy(name = "street[1]")
    private WebElement secondStreetAddressField;
    @FindBy(name = "street[2]")
    private WebElement thirdStreetAddressField;
    @FindBy(name = "city")
    private WebElement cityField;
    @FindBy(name = "region_id")
    private WebElement stateProvinceDropdown;
    @FindBy(name = "postcode")
    private WebElement zipPostalCodeField;
    @FindBy(name = "country_id")
    private WebElement countryDropdown;
    @FindBy(name = "telephone")
    private WebElement phoneNumberField;
    @FindBy(xpath = "//span[@id='tooltip']")
    private List<WebElement> tooltipsList;
    @FindBy(xpath = "//div[@class='field-tooltip-content']")
    private List<WebElement> tooltipsContentList;
    @FindBy(css = "tr[data-bind='click: element.selectShippingMethod']")
    private List<WebElement> shippingMethodsList;
    @FindBy(className = "continue")
    private WebElement nextButton;
    @FindBy(className = "items-in-cart")
    private WebElement itemInCartButton;
    @FindBy(css = "span[data-bind='text: getCartSummaryItemsCount()']")
    private WebElement itemCounter;
    @FindBy(className = "product-item-inner")
    private List<WebElement> itemInformationList;
    @FindBy(className = "options")
    private List<WebElement> viewDetailsButtonsList;
    @FindBy(xpath = "//*[@id='co-shipping-method-form']/div[3]")
    private WebElement shippingMethodErrorMessage;

    public ShippingPage(WebDriver driver) {
        super(driver);
    }

    public boolean checkShippingMethodErrorMessage(String message) {
        return getTextFromElement(waitForVisibility(shippingMethodErrorMessage)).equals(message);
    }
    public boolean checkEmailFieldErrorMessage(String message) {
        return getTextFromElement(waitForVisibility(emailAddressField.findElement(By.xpath("following-sibling::div[1]")))).equals(message);
    }

    public boolean checkFirstNameFieldErrorMessage(String message) {
        return getTextFromElement(waitForVisibility(firstNameField.findElement(By.xpath("following-sibling::div")))).equals(message);
    }

    public boolean checkLastNameFieldErrorMessage(String message) {
        return getTextFromElement(waitForVisibility(lastNameField.findElement(By.xpath("following-sibling::div")))).equals(message);
    }

    public boolean checkCompanyFieldErrorMessage(String message) {
        return getTextFromElement(waitForVisibility(companyField.findElement(By.xpath("following-sibling::div")))).equals(message);
    }

    public boolean checkFirstAddressFieldErrorMessage(String message) {
        return getTextFromElement(waitForVisibility(firstStreetAddressField.findElement(By.xpath("following-sibling::div")))).equals(message);
    }

    public boolean checkSecondAddressFieldErrorMessage(String message) {
        return getTextFromElement(waitForVisibility(secondStreetAddressField.findElement(By.xpath("following-sibling::div")))).equals(message);
    }

    public boolean checkThirdAddressFieldErrorMessage(String message) {
        return getTextFromElement(waitForVisibility(secondStreetAddressField.findElement(By.xpath("following-sibling::div")))).equals(message);
    }

    public boolean checkCityFieldErrorMessage(String message) {
        return getTextFromElement(waitForVisibility(cityField.findElement(By.xpath("following-sibling::div")))).equals(message);
    }

    public boolean checkStateProvinceDropdownErrorMessage(String message) {
        return getTextFromElement(waitForVisibility(stateProvinceDropdown.findElement(By.xpath("following-sibling::div")))).equals(message);
    }

    public boolean checkZipPostalCodeFieldErrorMessage(String message) {
        return getTextFromElement(waitForVisibility(zipPostalCodeField.findElement(By.xpath("following-sibling::div")))).equals(message);
    }

    public boolean checkPhoneNumberFieldErrorMessage(String message) {
        return getTextFromElement(waitForVisibility(phoneNumberField.findElement(By.xpath("following-sibling::div[2]")))).equals(message);
    }

    public void clickOnNextButton() {
        clickOn(nextButton);
        waitForLoadingToFinish();
    }

    public Shipping selectShippingMethod(int shippingMethodIndex) {
        clickOn(waitForVisibilityOfAllElements(shippingMethodsList).get(shippingMethodIndex));
        Shipping shipping = new Shipping();
        shipping.setPrice(getTextFromElement(shippingMethodsList.get(shippingMethodIndex).findElement(By.xpath("td[2]/span/span"))));
        shipping.setMethod(getTextFromElement(shippingMethodsList.get(shippingMethodIndex).findElement(By.xpath("td[3]"))));
        shipping.setCarrier(getTextFromElement(shippingMethodsList.get(shippingMethodIndex).findElement(By.xpath("td[4]"))));

        return shipping;
    }

    public void enterEmail(String input) {
        enterInputInField(emailAddressField, input);
    }

    public void enterFirstName(String input) {
        enterInputInField(firstNameField, input);
    }

    public void enterLastName(String input) {
        enterInputInField(lastNameField, input);
    }

    public void enterCompany(String input) {
        enterInputInField(companyField, input);
    }

    public void enterFirstAddress(String input) {
        enterInputInField(firstStreetAddressField, input);
    }

    public void enterSecondAddress(String input) {
        enterInputInField(secondStreetAddressField, input);
    }

    public void enterThirdAddress(String input) {
        enterInputInField(thirdStreetAddressField, input);
    }

    public void enterCity(String input) {
        enterInputInField(cityField, input);
    }

    public void enterZipPostalCode(String input) {
        enterInputInField(zipPostalCodeField, input);
        waitForLoadingToFinish();
    }

    public void enterPhoneNumber(String input) {
        enterInputInField(phoneNumberField, input);
    }

    public void clickOnTooltip(int tooltipIndex) {
        clickOn(tooltipsList.get(tooltipIndex));
    }

    public boolean checkIfTooltipIsDisplayed(int tooltipIndex, String tooltipContent) {
        return getTextFromElement(tooltipsContentList.get(tooltipIndex)).equals(tooltipContent);
    }

    public void clickOnItemInCartButton() {
        clickOn(itemInCartButton);
    }

    public void clickOnViewDetailsButton(int itemIndex) {
        waitForVisibilityOfAllElements(viewDetailsButtonsList);
        clickOn(viewDetailsButtonsList.get(itemIndex));
    }

    public String getNumberOfItems() {
        return getTextFromElement(itemCounter);
    }

    public WebElement getItemInformation(int itemIndex) {
        waitForLoadingToFinish();
        return waitForVisibilityOfAllElements(itemInformationList).get(itemIndex);
    }

    public void selectStateProvince(String value) {
        new Select(waitForVisibility(stateProvinceDropdown)).selectByVisibleText(value);
    }
}
