package tests.uiTests;

import com.luma.core.helpers.EmailFactory;
import com.luma.core.helpers.HexColorToString;
import com.luma.testUtils.UiTestBase;
import com.luma.testUtils.pages.*;
import com.luma.testUtils.testObjects.Item;
import com.luma.testUtils.testObjects.Shipping;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
public class OrderTests extends UiTestBase {
    private HomePage homePage;
    private WhatsNewPage whatsNewPage;
    private ItemsPage itemsPage;
    private ShippingPage shippingPage;
    private CartPage cartPage;
    private ReviewPaymentsPage reviewPaymentsPage;

    private Item item;
    private Shipping shipping;
    private final String longInvalidString = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    private EmailFactory emailUtils;
    private String cartSubtotal;

    @BeforeMethod
    private void init() {
        homePage = new HomePage(driverThreadLocal.get());
        whatsNewPage = new WhatsNewPage(driverThreadLocal.get());
        itemsPage = new ItemsPage(driverThreadLocal.get());
        shippingPage = new ShippingPage(driverThreadLocal.get());
        cartPage = new CartPage(driverThreadLocal.get());
        reviewPaymentsPage = new ReviewPaymentsPage(driverThreadLocal.get());
    }

    @Test(testName = "The validations of the fields in the Shipping section work correctly", groups = "Orders")
    public void checkShippingFieldValidationsTest() {
        //1. Open the application
            openApplication();
        //2. Add an item to cart and proceed to the Order page
            addAnItemToCartAndProceedToOrderPage(0, 1, 1);
        //3. Click on the Next button
            shippingPage.clickOnNextButton();

        //Expected Result: An error message is displayed regarding shipping methods
            Assert.assertTrue(shippingPage.checkShippingMethodErrorMessage("The shipping method is missing. Select the shipping method and try again."));

        //4. Select a shipping method
            shippingPage.selectShippingMethod(0);
        //5. Click on the Next button
            shippingPage.clickOnNextButton();

        //Expected Result: Error messages are displayed for all the mandatory fields
            Assert.assertTrue(shippingPage.checkEmailFieldErrorMessage("This is a required field."));
            Assert.assertTrue(shippingPage.checkFirstNameFieldErrorMessage("This is a required field."));
            Assert.assertTrue(shippingPage.checkLastNameFieldErrorMessage("This is a required field."));
            Assert.assertTrue(shippingPage.checkFirstAddressFieldErrorMessage("This is a required field."));
            Assert.assertTrue(shippingPage.checkCityFieldErrorMessage("This is a required field."));
            Assert.assertTrue(shippingPage.checkStateProvinceDropdownErrorMessage("This is a required field."));
            Assert.assertTrue(shippingPage.checkZipPostalCodeFieldErrorMessage("This is a required field."));
            Assert.assertTrue(shippingPage.checkPhoneNumberFieldErrorMessage("This is a required field."));

        //6. Add invalid input in all the rest of the fields
            shippingPage.enterEmail("invalidEmail");
            shippingPage.enterFirstName(longInvalidString);
            shippingPage.enterLastName(longInvalidString);
            shippingPage.enterCompany(longInvalidString);
            shippingPage.enterFirstAddress(longInvalidString);
            shippingPage.enterSecondAddress(longInvalidString);
            shippingPage.enterThirdAddress(longInvalidString);
            shippingPage.enterCity(longInvalidString);
            shippingPage.enterZipPostalCode("asdf");
            /*
            The Country dropdown is marked as mandatory but an empty value can be selected, a bug in my opinion. Also,
            when changing countries, the State/Province dropdown becomes a field and the shipping methods change. Not sure if that is supposed to happen
            */
            shippingPage.enterPhoneNumber(longInvalidString);

        //Expected Result: Error messages are displayed for the invalid input in the corresponding fields
            Assert.assertTrue(shippingPage.checkEmailFieldErrorMessage("Please enter a valid email address (Ex: johndoe@domain.com)."));
            Assert.assertTrue(shippingPage.checkFirstNameFieldErrorMessage("Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkLastNameFieldErrorMessage("Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkCompanyFieldErrorMessage("Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkFirstAddressFieldErrorMessage("Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkSecondAddressFieldErrorMessage("Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkThirdAddressFieldErrorMessage("Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkCityFieldErrorMessage("Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkZipPostalCodeFieldErrorMessage("Provided Zip/Postal Code seems to be invalid. Example: 12345-6789; 12345. If you believe it is the right one you can ignore this notice."));
            Assert.assertTrue(shippingPage.checkPhoneNumberFieldErrorMessage("Please enter less or equal than 255 symbols."));
    }

    @Test(testName = "The tooltips of the Email Address and Phone Number fields display additional information when clicked", groups = "Orders")
    public void checkTooltipFunctionalityTest() {
        //1. Open the application
            openApplication();
        //2. Add an item to cart and proceed to the Order page
            addAnItemToCartAndProceedToOrderPage(0, 1, 1);
        //3. Click on the Email Address tooltip
            shippingPage.clickOnTooltip(0);

        //Expected Result: The Email Address tooltip is displayed
            Assert.assertTrue(shippingPage.checkIfTooltipIsDisplayed(0, "We'll send your order confirmation here."));

        //4. Click on the Phone Number tooltip
            shippingPage.clickOnTooltip(1);

        //Expected Result: The Phone Number tooltip is displayed
            Assert.assertTrue(shippingPage.checkIfTooltipIsDisplayed(1, "For delivery questions."));
    }

    @Test(testName = "The correct item is displayed in the Order Summary section", groups = "Orders")
    public void checkOrderSummaryTest() {
        //1. Open the application
            openApplication();
        //2. Add an item to cart and proceed to the Order page
            addAnItemToCartAndProceedToOrderPage(0, 1, 1);

        //Expected Result: The correct amount of items is displayed in the Order Summary section
            Assert.assertEquals(shippingPage.getNumberOfItems(), item.getItemQuantity());

        //3. Click on Item in Cart button
            shippingPage.clickOnItemInCartButton();
            shippingPage.clickOnViewDetailsButton(0);

        //Expected Result: The item's information is correctly displayed
            Assert.assertEquals(shippingPage.getItemInformation(0).findElement(By.xpath("div/strong")).getText(), item.getItemTitle());
            Assert.assertEquals(shippingPage.getItemInformation(0).findElement(By.xpath("div/div/span[2]")).getText(), item.getItemQuantity());
            Assert.assertEquals(shippingPage.getItemInformation(0).findElement(By.xpath("div[2]/span/span/span")).getText(), item.getItemPrice());
            Assert.assertEquals(shippingPage.getItemInformation(0).findElement(By.xpath("following-sibling::div/div/dl/dd[1]")).getText(), item.getItemSize());
            Assert.assertEquals(shippingPage.getItemInformation(0).findElement(By.xpath("following-sibling::div/div/dl/dd[2]")).getText(), item.getItemColor());
    }

    @Test(testName = "The Discount Code field's validations work correctly", groups = "Orders")
    public void checkDiscountFieldValidationsTest() {
        //1. Open the application
            openApplication();
        //2. Add an item to cart and proceed to the Order page
            addAnItemToCartAndProceedToOrderPage(0, 1, 1);
        //3. Add valid input into the fields
            shippingPage.enterEmail("test@email.com");
            shippingPage.enterFirstName("Jon");
            shippingPage.enterLastName("Snow");
            shippingPage.enterFirstAddress("Flea Bottom");
            shippingPage.enterCity("King's Landing");
            shippingPage.selectStateProvince("Florida");
            shippingPage.enterZipPostalCode("55555");
            shippingPage.enterPhoneNumber("345345");
       //4. Select a shipping method
            shippingPage.selectShippingMethod(0);
       //5. Click on the Next button
            shippingPage.clickOnNextButton();

       //Expected Result: The Discount Code field and Apply Discount button are not displayed
            Assert.assertFalse(reviewPaymentsPage.checkThatTheDiscountCodeFieldIsDisplayed());
            Assert.assertFalse(reviewPaymentsPage.checkThatTheApplyDiscountButtonIsDisplayed());

       //6. Click on the Apply Discount Code button
            reviewPaymentsPage.clickOnApplyDiscountCodeButton();

       //Expected Result: The Discount Code field and Apply Discount button are displayed
            Assert.assertTrue(reviewPaymentsPage.checkThatTheDiscountCodeFieldIsDisplayed());
            Assert.assertTrue(reviewPaymentsPage.checkThatTheApplyDiscountButtonIsDisplayed());

       //7. Click on the Apply Discount button with the Discount Code field empty
            reviewPaymentsPage.clickOnApplyDiscountButton();

       //Expected Result: An error message is displayed informing the field is mandatory
            Assert.assertTrue(reviewPaymentsPage.checkDiscountCodeFieldValidationMessage("This is a required field."));

       //8. Enter an incorrect discount code and click on Apply Discount
            reviewPaymentsPage.enterDiscountCode("incorrectCode");
            reviewPaymentsPage.clickOnApplyDiscountButton();

       //Expected Result: An error message is displayed
            Assert.assertTrue(reviewPaymentsPage.checkDiscountCodeFieldErrorMessage("The coupon code isn't valid. Verify the code and try again."));
    }

    @Test(testName = "A Discount Code can be successfully added", groups = "Orders")
    public void applyDiscountCodeTest() {
        //Not implemented, I do not know a valid discount code
    }

    @Test(testName = "The order's details are correctly displayed in the Order Summary section", groups = "Orders")
    public void checkTheOrderSummaryDetailsTest() {
        //1. Open the application
            openApplication();
        //2. Add an item to cart and proceed to the Order page
            addAnItemToCartAndProceedToOrderPage(0, 1, 1);
        //3. Add valid input into the fields
            shippingPage.enterEmail("test@email.com");
            shippingPage.enterFirstName("Jon");
            shippingPage.enterLastName("Snow");
            shippingPage.enterFirstAddress("Flea Bottom");
            shippingPage.enterCity("King's Landing");
            shippingPage.selectStateProvince("Florida");
            shippingPage.enterZipPostalCode("55555");
            shippingPage.enterPhoneNumber("345345");
        //4. Select a shipping method
            shipping = shippingPage.selectShippingMethod(1);
        //5. Click on the Next button
            shippingPage.clickOnNextButton();

        //Expected Result: The Order Summary section contains the correct order details
            Assert.assertTrue(reviewPaymentsPage.checkShippingDetails(shipping.getPrice() + " " + shipping.getCarrier() + " - " + shipping.getMethod()));
            Assert.assertTrue(reviewPaymentsPage.checkCartSubtotal(cartSubtotal));
            Assert.assertTrue(reviewPaymentsPage.checkOrderTotal("$" + (Double.parseDouble(shipping.getPrice().substring(1)) + Double.parseDouble(item.getItemPrice().substring(1)))));
    }

    private void addAnItemToCartAndProceedToOrderPage(int itemIndex, int sizeIndex, int colorIndex) {
        item = new Item();

        homePage.clickOnWhatsNew();

        whatsNewPage.clickOnJackets();

        item.setItemTitle(itemsPage.getItemTitle(itemIndex));
        item.setItemPrice(itemsPage.getItemPrice(itemIndex));
        item.setItemSize(itemsPage.getItemSize(itemIndex, sizeIndex - 1));
        item.setItemColor(HexColorToString.getColorName(itemsPage.getItemColor(itemIndex, colorIndex)));

        itemsPage.selectSizeAndColorForAnItem(itemIndex, sizeIndex, colorIndex);
        itemsPage.clickOnAddToCartButton(itemIndex);

        homePage.waitForTheCartCounterToBeUpdated();
        homePage.clickOnCartButton();

        cartSubtotal = cartPage.getCartSubTotal();
        item.setItemQuantity(cartPage.getItemQuantity(itemIndex));
        cartPage.clickOnProceedToCheckoutButton();
    }

    private String getAValidEmailAddress() {
        emailUtils = new EmailFactory();
        return emailUtils.getEmail();
    }
}
