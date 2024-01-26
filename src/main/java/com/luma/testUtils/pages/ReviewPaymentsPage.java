package com.luma.testUtils.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.function.Function;

public class ReviewPaymentsPage extends Page {
    @FindBy(id = "block-discount-heading")
    private WebElement applyDiscountCodeButton;
    @FindBy(id = "discount-code")
    private WebElement discountCodeField;
    @FindBy(className = "action-apply")
    private WebElement applyDiscountButton;
    @FindBy(className = "message-error")
    private WebElement discountCodeErrorMessage;
    @FindBy(css = "span[data-th='Shipping']")
    private WebElement shippingPrice;
    @FindBy(css = "span[data-bind='text: getShippingMethodTitle()']")
    private WebElement shippingMethodAndCarrier;
    @FindBy(css = "span[data-th='Cart Subtotal']")
    private WebElement cartSubtotal;
    @FindBy(xpath = "(//span[@class='price'])[7]")
    private WebElement orderTotal;
    @FindBy(className = "shipping-information-content")
    private List<WebElement> shippingInformationList;
    @FindBy(css = "button[data-bind='click: back']")
    private WebElement shippingAddressEditButton;
    @FindBy(css = "button[data-bind='click: backToShippingMethod']")
    private WebElement shippingMethodEditButton;
    @FindBy(css = "span[data-bind='text: getTitle()']")
    private WebElement paymentMethod;
    @FindBy(css = "input[type='checkbox']")
    private WebElement billingAndShippingAddressAreTheSameCheckbox;
    @FindBy(className = "billing-address-details")
    private WebElement billingAddressDetails;
    @FindBy(xpath = "//div[@name='billingAddresscheckmo.firstname']//input")
    private WebElement firstNameField;
    @FindBy(xpath = "//div[@name='billingAddresscheckmo.lastname']//input")
    private WebElement lastNameField;
    @FindBy(xpath = "//div[@name='billingAddresscheckmo.company']//input")
    private WebElement companyField;
    @FindBy(xpath = "//div[@name='billingAddresscheckmo.street.0']//input")
    private WebElement firstStreetAddressField;
    @FindBy(xpath = "//div[@name='billingAddresscheckmo.street.1']//input")
    private WebElement secondStreetAddressField;
    @FindBy(xpath = "//div[@name='billingAddresscheckmo.street.2']//input")
    private WebElement thirdStreetAddressField;
    @FindBy(xpath = "//div[@name='billingAddresscheckmo.city']//input")
    private WebElement cityField;
    @FindBy(xpath = "//div[@name='billingAddresscheckmo.region_id']/label/following-sibling::div/select")
    private WebElement stateProvinceDropdown;
    @FindBy(xpath = "//*[@id='checkout-payment-method-load']/div/div/div[2]/div[2]/div[2]/div/fieldset/div[2]/div/form/fieldset/div[7]/div/input")
    private WebElement zipPostalCodeField;
    @FindBy(xpath = "//div[@name='billingAddresscheckmo.country_id']/label/following-sibling::div/select")
    private WebElement countryDropdown;
    @FindBy(xpath = "//div[@name='billingAddresscheckmo.telephone']//input")
    private WebElement phoneNumberField;
    @FindBy(css = "button[data-bind='click: updateAddress']")
    private WebElement updateButton;
    @FindBy(className = "action-edit-address")
    private WebElement editBillingAddressButton;
    @FindBy(xpath = "//*[@id='checkout-payment-method-load']/div/div/div[2]/div[2]/div[4]/div/button")
    private WebElement placeOrderButton;

    public ReviewPaymentsPage(WebDriver driver) {
        super(driver);
    }

    public boolean checkElementIsDisplayed(String element) {
        switch (element) {
            case "Discount Code Field":
                return waitForVisibility(discountCodeField).isDisplayed();
            case "Billing Address":
                return waitForVisibility(billingAddressDetails).isDisplayed();
            default:
                return applyDiscountButton.isDisplayed();
        }
    }

    public boolean checkShippingDetails(String details) {
        return details.equals(getTextFromElement(shippingPrice) + " " + getTextFromElement(shippingMethodAndCarrier));
    }

    public boolean checkCartSubtotal(String subtotal) {
        return getTextFromElement(cartSubtotal).equals(subtotal);
    }

    public boolean checkOrderTotal(String total) {
        return getTextFromElement(orderTotal).equals(total + "0");
    }

    public boolean checkShippingAddressDetails(String shippingAddressDetails) {
        return getTextFromElement(shippingInformationList.get(0)).equals(shippingAddressDetails);
    }

    public boolean checkShippingMethodDetails(String shippingMethodDetails) {
        return getTextFromElement(shippingInformationList.get(1)).equals(shippingMethodDetails);
    }

    public boolean checkPaymentMethod(String method) {
        return getTextFromElement(paymentMethod).equals(method);
    }

    public boolean checkBillingAddressDetails(String billingDetails) {
        return getTextFromElement(billingAddressDetails).split("Edit")[0].trim().equals(billingDetails);
    }

    public void enterInputInField(String field, String input) {
        switch (field) {
            case "Discount Code":
                enterInputInField(discountCodeField, input);
                break;
            case "First Name":
                enterInputInField(firstNameField, input);
                break;
            case "Last Name":
                enterInputInField(lastNameField, input);
                break;
            case "Company":
                enterInputInField(companyField, input);
                break;
            case "First Address":
                enterInputInField(firstStreetAddressField, input);
                break;
            case "Second Address":
                enterInputInField(secondStreetAddressField, input);
                break;
            case "Third Address":
                enterInputInField(thirdStreetAddressField, input);
                break;
            case "Zip/Postal Code":
                enterInputInField(zipPostalCodeField, input);
                break;
            case "City":
                enterInputInField(cityField, input);
                break;
            default:
                enterInputInField(phoneNumberField, input);
        }
    }

    public void selectStateProvince(String value) {
        new Select(waitForVisibility(stateProvinceDropdown)).selectByVisibleText(value);
    }

    public boolean checkIfEditBillingSectionAndPlaceOrderButtonIsDisabled() {
        return waitForVisibility(firstNameField).isDisplayed() &&
                waitForVisibility(lastNameField).isDisplayed() &&
                waitForVisibility(placeOrderButton).getAttribute("class").contains("disabled");
    }

    public boolean checkErrorMessage(String element, String message) {
        switch (element) {
            case "Discount Validation":
                return getTextFromElement(discountCodeField.findElement(By.xpath("following-sibling::div"))).equals(message);
            case "Discount Error":
                return getTextFromElement(discountCodeErrorMessage).equals(message);
            case "First Name":
                return getTextFromElement(waitForVisibility(firstNameField.findElement(By.xpath("following-sibling::div/span")))).equals(message);
            case "Last Name":
                return getTextFromElement(waitForVisibility(lastNameField.findElement(By.xpath("following-sibling::div/span")))).equals(message);
            case "Company":
                return getTextFromElement(waitForVisibility(companyField.findElement(By.xpath("following-sibling::div/span")))).equals(message);
            case "First Address":
                return getTextFromElement(waitForVisibility(firstStreetAddressField.findElement(By.xpath("following-sibling::div/span")))).equals(message);
            case "Second Address":
                return getTextFromElement(waitForVisibility(secondStreetAddressField.findElement(By.xpath("following-sibling::div/span")))).equals(message);
            case "Third Address":
                return getTextFromElement(waitForVisibility(thirdStreetAddressField.findElement(By.xpath("following-sibling::div/span")))).equals(message);
            case "City":
                return getTextFromElement(waitForVisibility(cityField.findElement(By.xpath("following-sibling::div/span")))).equals(message);
            case "State/Province":
                return getTextFromElement(waitForVisibility(stateProvinceDropdown.findElement(By.xpath("following-sibling::div/span")))).equals(message);
            case "Zip/Postal Code":
                return getTextFromElement(fluentWait.until(driver -> zipPostalCodeField.findElement(By.xpath("following-sibling::div/span")))).equals(message);
            default:
                return getTextFromElement(waitForVisibility(phoneNumberField.findElement(By.xpath("following-sibling::div[2]/span")))).equals(message);
        }
    }

    public void clickOnElement(String element) {
        switch (element) {
            case "Shipping Address Edit":
                clickOn(shippingAddressEditButton);
                break;
            case "Shipping Method Edit":
                clickOn(shippingMethodEditButton);
                break;
            case "Billing/Shipping Checkbox":
                clickOn(billingAndShippingAddressAreTheSameCheckbox);
                break;
            case "Apply Discount Code":
                clickOn(applyDiscountCodeButton);
                break;
            case "Apply Discount":
                clickOn(applyDiscountButton);
                waitForLoadingToFinish();
                break;
            case "Place Order":
                clickOn(placeOrderButton);
                waitForLoadingToFinish();
                break;
            case "Cancel":
                clickOn(paymentMethod);
                waitForLoadingToFinish();
                break;
            case "Update":
                clickOn(updateButton);
                break;
            default:
                clickOn(editBillingAddressButton);
        }
    }
}
