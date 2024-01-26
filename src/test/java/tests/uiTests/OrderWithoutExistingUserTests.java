package tests.uiTests;

import com.luma.core.helpers.EmailFactory;
import com.luma.core.helpers.HexColorToString;
import com.luma.testUtils.UiTestBase;
import com.luma.testUtils.pages.*;
import com.luma.testUtils.testObjects.ItemDetails;
import com.luma.testUtils.testObjects.AddressDetails;
import com.luma.testUtils.testObjects.ShippingDetails;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
public class OrderWithoutExistingUserTests extends UiTestBase {
    private HomePage homePage;
    private WhatsNewPage whatsNewPage;
    private ItemsPage itemsPage;
    private ShippingPage shippingPage;
    private CartPage cartPage;
    private ReviewPaymentsPage reviewPaymentsPage;
    private OrderSuccessfulPage orderSuccessfulPage;
    private CreateAccountPage createAccountPage;

    private ItemDetails itemDetails;
    private ShippingDetails shippingDetails;

    private final String longInvalidString = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    private String cartSubtotal;

    private EmailFactory emailUtils;

    @BeforeMethod
    private void init() {
        homePage = new HomePage(driverThreadLocal.get());
        whatsNewPage = new WhatsNewPage(driverThreadLocal.get());
        itemsPage = new ItemsPage(driverThreadLocal.get());
        shippingPage = new ShippingPage(driverThreadLocal.get());
        cartPage = new CartPage(driverThreadLocal.get());
        reviewPaymentsPage = new ReviewPaymentsPage(driverThreadLocal.get());
        orderSuccessfulPage = new OrderSuccessfulPage(driverThreadLocal.get());
        createAccountPage = new CreateAccountPage(driverThreadLocal.get());
    }

    @Test(testName = "The validations of the fields in the Shipping section work correctly", groups = "Orders")
    public void checkShippingFieldValidationsTest() {
        //1. Open the application
            openApplication();
        //2. Add an item to cart and proceed to the Order page
            addAnItemToCartAndProceedToOrderPage(0, 1, 1);
        //3. Click on the Next button
            shippingPage.clickOnButton("Next");

        //Expected Result: An error message is displayed regarding shipping methods
            Assert.assertTrue(shippingPage.checkErrorMessage("Shipping Method","The shipping method is missing. Select the shipping method and try again."));

        //4. Select a shipping method
            shippingPage.selectShippingMethod(0);
        //5. Click on the Next button
            shippingPage.clickOnButton("Next");

        //Expected Result: Error messages are displayed for all the mandatory fields
            Assert.assertTrue(shippingPage.checkErrorMessage("Email","This is a required field."));
            Assert.assertTrue(shippingPage.checkErrorMessage("First Name","This is a required field."));
            Assert.assertTrue(shippingPage.checkErrorMessage("Last Name","This is a required field."));
            Assert.assertTrue(shippingPage.checkErrorMessage("First Address","This is a required field."));
            Assert.assertTrue(shippingPage.checkErrorMessage("City","This is a required field."));
            Assert.assertTrue(shippingPage.checkErrorMessage("State/Province","This is a required field."));
            Assert.assertTrue(shippingPage.checkErrorMessage("Zip/Postal Code","This is a required field."));
            Assert.assertTrue(shippingPage.checkErrorMessage("Phone Number","This is a required field."));

        //6. Add invalid input in all the rest of the fields and click on Next
            shippingPage.enterInputInField("Email", "invalidEmail");
            shippingPage.enterInputInField("First Name", longInvalidString);
            shippingPage.enterInputInField("Last Name", longInvalidString);
            shippingPage.enterInputInField("Company", longInvalidString);
            shippingPage.enterInputInField("First Address", longInvalidString);
            shippingPage.enterInputInField("Second Address", longInvalidString);
            shippingPage.enterInputInField("Third Address", longInvalidString);
            shippingPage.enterInputInField("City", longInvalidString);
            shippingPage.enterInputInField("Zip/Postal Code", "asdf");
            /*
            The Country dropdown is marked as mandatory but an empty value can be selected, a bug in my opinion. Also,
            when changing countries, the State/Province dropdown becomes a field and the shipping methods change. Not sure if that is supposed to happen
            */
            shippingPage.enterInputInField("Phone Number", longInvalidString);
            shippingPage.clickOnButton("Next");

        //Expected Result: Error messages are displayed for the invalid input in the corresponding fields
            Assert.assertTrue(shippingPage.checkErrorMessage("Email","Please enter a valid email address (Ex: johndoe@domain.com)."));
            Assert.assertTrue(shippingPage.checkErrorMessage("First Name","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkErrorMessage("Last Name","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkErrorMessage("Company","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkErrorMessage("First Address","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkErrorMessage("Second Address","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkErrorMessage("Third Address","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkErrorMessage("City","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(shippingPage.checkErrorMessage("Zip/Postal Code","Provided Zip/Postal Code seems to be invalid. Example: 12345-6789; 12345. If you believe it is the right one you can ignore this notice."));
            Assert.assertTrue(shippingPage.checkErrorMessage("Phone Number","Please enter less or equal than 255 symbols."));
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
            Assert.assertEquals(shippingPage.getNumberOfItems(), itemDetails.getItemQuantity());

        //3. Click on Item in Cart button
            shippingPage.clickOnButton("Item in Cart");
            shippingPage.clickOnViewDetailsButton(0);

        //Expected Result: The item's information is correctly displayed
            Assert.assertEquals(shippingPage.getItemInformation(0).findElement(By.xpath("div/strong")).getText(), itemDetails.getItemTitle());
            Assert.assertEquals(shippingPage.getItemInformation(0).findElement(By.xpath("div/div/span[2]")).getText(), itemDetails.getItemQuantity());
            Assert.assertEquals(shippingPage.getItemInformation(0).findElement(By.xpath("div[2]/span/span/span")).getText(), itemDetails.getItemPrice());
            Assert.assertEquals(shippingPage.getItemInformation(0).findElement(By.xpath("following-sibling::div/div/dl/dd[1]")).getText(), itemDetails.getItemSize());
            Assert.assertEquals(shippingPage.getItemInformation(0).findElement(By.xpath("following-sibling::div/div/dl/dd[2]")).getText(), itemDetails.getItemColor());
    }

    @Test(testName = "The Discount Code field's validations work correctly", groups = "Orders")
    public void checkDiscountFieldValidationsTest() {
        //1. Open the application
            openApplication();
        //2. Add an item to cart and proceed to the Order page
            addAnItemToCartAndProceedToOrderPage(0, 1, 1);
        //3. Add valid input into the fields
            shippingPage.enterInputInField("Email", "test@email.com");
            shippingPage.enterInputInField("First Name", "Jon");
            shippingPage.enterInputInField("Last Name", "Snow");
            shippingPage.enterInputInField("First Address", "Flea Bottom");
            shippingPage.enterInputInField("City", "King's Landing");
            shippingPage.selectStateProvince("Florida");
            shippingPage.enterInputInField("Zip/Postal Code", "55555");
            shippingPage.enterInputInField("Phone Number", "345345");
       //4. Select a shipping method
            shippingPage.selectShippingMethod(0);
       //5. Click on the Next button
            shippingPage.clickOnButton("Next");
       //6. Click on the Apply Discount Code button
            reviewPaymentsPage.clickOnElement("Apply Discount Code");

       //Expected Result: The Discount Code field and Apply Discount button are displayed
            Assert.assertTrue(reviewPaymentsPage.checkElementIsDisplayed("Discount Code Field"));
            Assert.assertTrue(reviewPaymentsPage.checkElementIsDisplayed("Apply Discount Button"));

       //7. Click on the Apply Discount button with the Discount Code field empty
            reviewPaymentsPage.clickOnElement("Apply Discount");

       //Expected Result: An error message is displayed informing the field is mandatory
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("Discount Validation","This is a required field."));

       //8. Enter an incorrect discount code and click on Apply Discount
            reviewPaymentsPage.enterInputInField("Discount Code", "incorrectCode");
            reviewPaymentsPage.clickOnElement("Apply Discount");

       //Expected Result: An error message is displayed
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("Discount Error", "The coupon code isn't valid. Verify the code and try again."));
    }

    @Test(testName = "A Discount Code can be successfully added", groups = "Orders")
    public void applyDiscountCodeTest() {
        //Not implemented, I do not know a valid discount code
    }

    @Test(testName = "The order's details in the Order Summary section and the Payment Method are correctly displayed", groups = "Orders")
    public void checkTheOrderSummaryDetailsTest() {
        //1. Open the application
            openApplication();
        //2. Add an item to cart and proceed to the Order page
            addAnItemToCartAndProceedToOrderPage(0, 1, 1);
        //3. Add valid input into the fields
            shippingPage.enterInputInField("Email", "test@email.com");
            shippingPage.enterInputInField("First Name", "Jon");
            shippingPage.enterInputInField("Last Name", "Snow");
            shippingPage.enterInputInField("First Address", "Flea Bottom");
            shippingPage.enterInputInField("City", "King's Landing");
            shippingPage.selectStateProvince("Florida");
            shippingPage.enterInputInField("Zip/Postal Code", "55555");
            shippingPage.enterInputInField("Phone Number", "345345");
        //4. Select a shipping method
            shippingDetails = shippingPage.selectShippingMethod(0);
        //5. Click on the Next button
            shippingPage.clickOnButton("Next");

        //Expected Result: The Payment Method is correct and Order Summary section contains the correct order details
            Assert.assertTrue(reviewPaymentsPage.checkPaymentMethod("Check / Money order"));
            Assert.assertTrue(reviewPaymentsPage.checkShippingDetails(shippingDetails.getPrice() + " " + shippingDetails.getCarrier() + " - " + shippingDetails.getMethod()));
            Assert.assertTrue(reviewPaymentsPage.checkCartSubtotal(cartSubtotal));
            Assert.assertTrue(reviewPaymentsPage.checkOrderTotal("$" + (Double.parseDouble(shippingDetails.getPrice().substring(1)) + Double.parseDouble(itemDetails.getItemPrice().substring(1)))));
    }

    @Test(testName = "The shipping details can be successfully edited", groups = "Orders")
    public void editTheShippingDetailsTest() {
        //1. Open the application
            openApplication();
        //2. Add an item to cart and proceed to the Order page
            addAnItemToCartAndProceedToOrderPage(0, 1, 1);
        //3. Add valid input into the fields
            AddressDetails shippingAddressDetails = createAddress("test@email.com", "Jon", "Snow", "Google", new String[]{"Flea Bottom", "11", "B"}, "King's Landing", "Florida", "12345", "United States", "6666666");
            shippingPage.enterInputInField("Email", shippingAddressDetails.getEmail());
            shippingPage.enterInputInField("First Name", shippingAddressDetails.getFirstName());
            shippingPage.enterInputInField("Last Name", shippingAddressDetails.getLastName());
            shippingPage.enterInputInField("First Address", shippingAddressDetails.getStreetAddress()[0]);
            shippingPage.enterInputInField("Second Address", shippingAddressDetails.getStreetAddress()[1]);
            shippingPage.enterInputInField("Third Address", shippingAddressDetails.getStreetAddress()[2]);
            shippingPage.enterInputInField("City", shippingAddressDetails.getCity());
            shippingPage.selectStateProvince(shippingAddressDetails.getStateProvince());
            shippingPage.enterInputInField("Zip/Postal Code", shippingAddressDetails.getZipPostalCode());
            shippingPage.enterInputInField("Phone Number", shippingAddressDetails.getPhoneNumber());
        //4. Select a shipping method
            shippingDetails = shippingPage.selectShippingMethod(1);
        //5. Click on the Next button
            shippingPage.clickOnButton("Next");

        //Expected Result: The Shipping details are correctly displayed
            Assert.assertTrue(reviewPaymentsPage.checkShippingAddressDetails(shippingAddressDetails.toString()));
            Assert.assertTrue(reviewPaymentsPage.checkShippingMethodDetails(shippingDetails.getCarrier() + " - " + shippingDetails.getMethod()));

        //6. Click on the Shipping Address Edit button
            reviewPaymentsPage.clickOnElement("Shipping Address Edit");
        //7. Edit the Shipping Address details and click on the Next button
            shippingPage.clearField("Email");
            shippingPage.clearField("First Name");
            shippingPage.clearField("Last Name");

            shippingAddressDetails.setEmail("editedEmail@email.com");
            shippingAddressDetails.setFirstName("editedFirstName");
            shippingAddressDetails.setLastName("editedLastName");
            shippingPage.enterInputInField("Email", shippingAddressDetails.getEmail());
            shippingPage.enterInputInField("First Name", shippingAddressDetails.getFirstName());
            shippingPage.enterInputInField("Last Name", shippingAddressDetails.getLastName());

            shippingPage.clickOnButton("Next");

        //Expected Result: The Shipping Address details are successfully edited
            Assert.assertTrue(reviewPaymentsPage.checkShippingAddressDetails(shippingAddressDetails.toString()));

        //8. Click on Shipping Method Edit button
            reviewPaymentsPage.clickOnElement("Shipping Method Edit");
        //9. Edit the Shipping method and click on Next
            shippingDetails = shippingPage.selectShippingMethod(0);
            shippingPage.clickOnButton("Next");

        //Expected Result: The Shipping Method details are successfully edited
            Assert.assertTrue(reviewPaymentsPage.checkShippingMethodDetails(shippingDetails.getCarrier() + " - " + shippingDetails.getMethod()));
    }

    @Test(testName = "The billing address can be successfully edited", groups = "Orders")
    public void editTheBillingAddressDetailsTest() {
        //1. Open the application
            openApplication();
        //2. Add an item to cart and proceed to the Order page
            addAnItemToCartAndProceedToOrderPage(0, 1, 1);
        //3. Add valid input into the fields
            AddressDetails shippingAddressDetails = createAddress("test@email.com", "Jon", "Snow", "Google", new String[]{"Flea Bottom", "11", "B"}, "King's Landing", "Florida", "12345", "United States", "6666666");
            shippingPage.enterInputInField("Email", shippingAddressDetails.getEmail());
            shippingPage.enterInputInField("First Name", shippingAddressDetails.getFirstName());
            shippingPage.enterInputInField("Last Name", shippingAddressDetails.getLastName());
            shippingPage.enterInputInField("Company", shippingAddressDetails.getCompany());
            shippingPage.enterInputInField("First Address", shippingAddressDetails.getStreetAddress()[0]);
            shippingPage.enterInputInField("Second Address", shippingAddressDetails.getStreetAddress()[1]);
            shippingPage.enterInputInField("Third Address", shippingAddressDetails.getStreetAddress()[2]);
            shippingPage.enterInputInField("City", shippingAddressDetails.getCity());
            shippingPage.selectStateProvince(shippingAddressDetails.getStateProvince());
            shippingPage.enterInputInField("Zip/Postal Code", shippingAddressDetails.getZipPostalCode());
            shippingPage.enterInputInField("Phone Number", shippingAddressDetails.getPhoneNumber());
        //4. Select a shipping method
            shippingDetails = shippingPage.selectShippingMethod(1);
        //5. Click on the Next button
            shippingPage.clickOnButton("Next");
        //Expected Result: The Billing and Shipping address checkbox is checked and the addresses are the same
            Assert.assertTrue(reviewPaymentsPage.checkElementIsDisplayed("Billing Address"));
            Assert.assertTrue(reviewPaymentsPage.checkBillingAddressDetails(shippingAddressDetails.toString()));

        //6. Deselect the Billing and Shipping address checkbox
            reviewPaymentsPage.clickOnElement("Billing/Shipping Checkbox");
        //7. Enter the billing address details
            AddressDetails billingAddressDetails = createAddress(null, "Bill", "Andersen", "Amazon", new String[]{"Black", "77", "C"}, "New Jersey", "Alabama", "22222", "United States", "7777777");
            reviewPaymentsPage.enterInputInField("First Name", billingAddressDetails.getFirstName());
            reviewPaymentsPage.enterInputInField("Last Name", billingAddressDetails.getLastName());
            reviewPaymentsPage.enterInputInField("Company", billingAddressDetails.getCompany());
            reviewPaymentsPage.enterInputInField("First Address", billingAddressDetails.getStreetAddress()[0]);
            reviewPaymentsPage.enterInputInField("Second Address", billingAddressDetails.getStreetAddress()[1]);
            reviewPaymentsPage.enterInputInField("Third Address", billingAddressDetails.getStreetAddress()[2]);
            reviewPaymentsPage.enterInputInField("City", billingAddressDetails.getCity());
            reviewPaymentsPage.selectStateProvince(billingAddressDetails.getStateProvince());
            reviewPaymentsPage.enterInputInField("Zip/Postal Code", billingAddressDetails.getZipPostalCode());
            reviewPaymentsPage.enterInputInField("Phone Number", billingAddressDetails.getPhoneNumber());
        //8. Click on Cancel button
            reviewPaymentsPage.clickOnElement("Cancel");

        //Expected Result: The billing address remains the same as the shipping address
            Assert.assertTrue(reviewPaymentsPage.checkElementIsDisplayed("Billing Address"));
            Assert.assertTrue(reviewPaymentsPage.checkBillingAddressDetails(shippingAddressDetails.toString()));

        //9. Deselect the Billing and Shipping address checkbox
            reviewPaymentsPage.clickOnElement("Billing/Shipping Checkbox");
        //10. Click on Update button
            reviewPaymentsPage.clickOnElement("Update");
        //11. Click on the Edit Button
            reviewPaymentsPage.clickOnElement("Edit");

        //Expected Result: The edit billing address section is displayed and the Place Order button is displayed
            Assert.assertTrue(reviewPaymentsPage.checkIfEditBillingSectionAndPlaceOrderButtonIsDisabled());
    }

    @Test(testName = "The validations of the fields in the Billing section work correctly", groups = "Orders")
    public void checkBillingFieldValidationsTest() {
        //1. Open the application
            openApplication();
        //2. Add an item to cart and proceed to the Order page
            addAnItemToCartAndProceedToOrderPage(0, 1, 1);
        //3. Add valid input into the fields
            shippingPage.enterInputInField("Email", "test@email.com");
            shippingPage.enterInputInField("First Name", "Jon");
            shippingPage.enterInputInField("Last Name", "Snow");
            shippingPage.enterInputInField("First Address", "Flea Bottom");
            shippingPage.enterInputInField("City", "King's Landing");
            shippingPage.selectStateProvince("Florida");
            shippingPage.enterInputInField("Zip/Postal Code", "55555");
            shippingPage.enterInputInField("Phone Number", "345345");
        //4. Select a shipping method
            shippingPage.selectShippingMethod(0);
        //5. Click on the Next button
            shippingPage.clickOnButton("Next");
        //6. Deselect the Billing and Shipping address checkbox and click on Update
            reviewPaymentsPage.clickOnElement("Billing/Shipping Checkbox");
            reviewPaymentsPage.clickOnElement("Update");

        //Expected Result: Error messages are displayed for all the mandatory fields
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("First Name","This is a required field."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("Last Name","This is a required field."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("First Address","This is a required field."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("City","This is a required field."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("State/Province","This is a required field."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("Zip/Postal Code","This is a required field."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("Phone Number","This is a required field."));

        //7. Add invalid input in all the rest of the fields
            reviewPaymentsPage.enterInputInField("First Name", longInvalidString);
            reviewPaymentsPage.enterInputInField("Last Name", longInvalidString);
            reviewPaymentsPage.enterInputInField("Company", longInvalidString);
            reviewPaymentsPage.enterInputInField("First Address", longInvalidString);
            reviewPaymentsPage.enterInputInField("Second Address", longInvalidString);
            reviewPaymentsPage.enterInputInField("Third Address", longInvalidString);
            reviewPaymentsPage.enterInputInField("City", longInvalidString);
            reviewPaymentsPage.enterInputInField("Zip/Postal Code", "asdf");
            /*
            The Country dropdown is marked as mandatory but an empty value can be selected, a bug in my opinion. Also,
            when changing countries, the State/Province dropdown becomes a field and the shipping methods change. Not sure if that is supposed to happen
            */
            reviewPaymentsPage.enterInputInField("Phone Number", longInvalidString);

        //Expected Result: Error messages are displayed for the invalid input in the corresponding fields
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("First Name","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("Last Name","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("Company","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("First Address","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("Second Address","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("Third Address","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("City","Please enter less or equal than 255 symbols."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("Zip/Postal Code","Provided Zip/Postal Code seems to be invalid. Example: 12345-6789; 12345. If you believe it is the right one you can ignore this notice."));
            Assert.assertTrue(reviewPaymentsPage.checkErrorMessage("Phone Number","Please enter less or equal than 255 symbols."));
    }

    @Test(testName = "The user can successfully place an order", groups = "Orders")
    public void theUserCanSuccessfullyPlaceAnOrderTest() {
        //1. Open the application
            openApplication();
        //2. Add an item to cart and proceed to the Order page
            addAnItemToCartAndProceedToOrderPage(0, 1, 1);
        //3. Add valid input into the fields
            AddressDetails shippingAddressDetails = createAddress(getAValidEmailAddress(), "Jon", "Snow", "Google", new String[]{"Flea Bottom", "11", "B"}, "King's Landing", "Florida", "12345", "United States", "6666666");
            shippingPage.enterInputInField("Email", shippingAddressDetails.getEmail());
            shippingPage.enterInputInField("First Name", shippingAddressDetails.getFirstName());
            shippingPage.enterInputInField("Last Name", shippingAddressDetails.getLastName());
            shippingPage.enterInputInField("Company", shippingAddressDetails.getCompany());
            shippingPage.enterInputInField("First Address", shippingAddressDetails.getStreetAddress()[0]);
            shippingPage.enterInputInField("Second Address", shippingAddressDetails.getStreetAddress()[1]);
            shippingPage.enterInputInField("Third Address", shippingAddressDetails.getStreetAddress()[2]);
            shippingPage.enterInputInField("City", shippingAddressDetails.getCity());
            shippingPage.selectStateProvince(shippingAddressDetails.getStateProvince());
            shippingPage.enterInputInField("Zip/Postal Code", shippingAddressDetails.getZipPostalCode());
            shippingPage.enterInputInField("Phone Number", shippingAddressDetails.getPhoneNumber());
        //4. Select a shipping method
            shippingDetails = shippingPage.selectShippingMethod(1);
        //5. Click on the Next button
            shippingPage.clickOnButton("Next");
        //6. Click on Place Order
            reviewPaymentsPage.clickOnElement("Place Order");

        //Expected Result: The order is successfully created and a confirmation email is received
            Assert.assertTrue(orderSuccessfulPage.checkOrderIdIsDisplayed());
            Assert.assertTrue(orderSuccessfulPage.checkThatTheCorrectEmailIsDisplayed(shippingAddressDetails.getEmail()));

            String emailContent = Jsoup.parse(emailUtils.getEmailContent().body().jsonPath().get("mail_body").toString()).text();
            Assert.assertTrue(emailContent.contains(shippingAddressDetails.getFirstName()));
            Assert.assertTrue(emailContent.contains(shippingAddressDetails.getLastName()));
            Assert.assertTrue(emailContent.contains(shippingAddressDetails.getCompany()));
            Assert.assertTrue(emailContent.contains(shippingAddressDetails.getStreetAddress()[0]));
            Assert.assertTrue(emailContent.contains(shippingAddressDetails.getStreetAddress()[1]));
            Assert.assertTrue(emailContent.contains(shippingAddressDetails.getStreetAddress()[2]));
            Assert.assertTrue(emailContent.contains(shippingAddressDetails.getCity()));
            Assert.assertTrue(emailContent.contains(shippingAddressDetails.getStateProvince()));
            Assert.assertTrue(emailContent.contains(shippingAddressDetails.getZipPostalCode()));
            Assert.assertTrue(emailContent.contains(shippingAddressDetails.getPhoneNumber()));

            Assert.assertTrue(emailContent.contains(itemDetails.getItemTitle()));
            Assert.assertTrue(emailContent.contains(itemDetails.getItemQuantity()));
            Assert.assertTrue(emailContent.contains(itemDetails.getItemColor()));
            Assert.assertTrue(emailContent.contains(itemDetails.getItemSize()));
            Assert.assertTrue(emailContent.contains(itemDetails.getItemPrice()));

            Assert.assertTrue(emailContent.contains(shippingDetails.getPrice()));
            Assert.assertTrue(emailContent.contains(shippingDetails.getCarrier()));
            Assert.assertTrue(emailContent.contains(shippingDetails.getMethod()));

            Assert.assertTrue(emailContent.contains(orderSuccessfulPage.getOrderId()));

            Assert.assertTrue(emailContent.contains("$" + (Double.parseDouble(shippingDetails.getPrice().substring(1)) + Double.parseDouble(itemDetails.getItemPrice().substring(1)))));
    }

    @Test(testName = "The Continue Shopping button on the Order Successful page works correctly", groups = "Orders")
    public void theContinueShoppingButtonOnOrderSuccessfulPageRedirectsSuccessfullyTest() {
        //1. Open the application
        openApplication();
        //2. Add an item to cart and proceed to the Order page
        addAnItemToCartAndProceedToOrderPage(0, 1, 1);
        //3. Add valid input into the fields
            shippingPage.enterInputInField("Email", getAValidEmailAddress());
            shippingPage.enterInputInField("First Name", "Jon");
            shippingPage.enterInputInField("Last Name", "Snow");
            shippingPage.enterInputInField("First Address", "Flea Bottom");
            shippingPage.enterInputInField("City", "King's Landing");
            shippingPage.selectStateProvince("Florida");
            shippingPage.enterInputInField("Zip/Postal Code", "55555");
            shippingPage.enterInputInField("Phone Number", "345345");
        //4. Select a shipping method
            shippingDetails = shippingPage.selectShippingMethod(1);
        //5. Click on the Next button
            shippingPage.clickOnButton("Next");
        //6. Click on Place Order
            reviewPaymentsPage.clickOnElement("Place Order");
        //7. Click on Continue Shopping
            orderSuccessfulPage.clickOnButton("Continue Shopping");

        //Expected Result: The user is redirected to the Home Page
            Assert.assertTrue(homePage.checkWhatsNewButtonIsDisplayed());
    }

    @Test(testName = "The Create an Account button on the Order Successful page works correctly", groups = "Orders")
    public void theCreateAnAccountButtonOnOrderSuccessfulPageRedirectsSuccessfullyTest() {
        //1. Open the application
            openApplication();
        //2. Add an item to cart and proceed to the Order page
            addAnItemToCartAndProceedToOrderPage(0, 1, 1);
        //3. Add valid input into the fields
            shippingPage.enterInputInField("Email", getAValidEmailAddress());
            shippingPage.enterInputInField("First Name", "Jon");
            shippingPage.enterInputInField("Last Name", "Snow");
            shippingPage.enterInputInField("First Address", "Flea Bottom");
            shippingPage.enterInputInField("City", "King's Landing");
            shippingPage.selectStateProvince("Florida");
            shippingPage.enterInputInField("Zip/Postal Code", "55555");
            shippingPage.enterInputInField("Phone Number", "345345");
        //4. Select a shipping method
            shippingDetails = shippingPage.selectShippingMethod(1);
        //5. Click on the Next button
            shippingPage.clickOnButton("Next");
        //6. Click on Place Order
            reviewPaymentsPage.clickOnElement("Place Order");
        //7. Click on Continue Shopping
            orderSuccessfulPage.clickOnButton("Create an Account");

        //Expected Result: The user is redirected to the Create Account page
            Assert.assertTrue(createAccountPage.checkTitleIsDisplayed());
    }

    private void addAnItemToCartAndProceedToOrderPage(int itemIndex, int sizeIndex, int colorIndex) {
        itemDetails = new ItemDetails();

        homePage.clickOnButton("What's New");

        whatsNewPage.clickOnJackets();

        itemDetails.setItemTitle(itemsPage.getItemTitle(itemIndex));
        itemDetails.setItemPrice(itemsPage.getItemPrice(itemIndex));
        itemDetails.setItemSize(itemsPage.getItemSize(itemIndex, sizeIndex - 1));
        itemDetails.setItemColor(HexColorToString.getColorName(itemsPage.getItemColor(itemIndex, colorIndex)));

        itemsPage.selectSizeAndColorForAnItem(itemIndex, sizeIndex, colorIndex);
        itemsPage.clickOnAddToCartButton(itemIndex);

        homePage.waitForTheCartCounterToBeUpdated("1");
        homePage.clickOnButton("Cart");

        cartSubtotal = cartPage.getCartSubtotal();
        itemDetails.setItemQuantity(cartPage.getItemQuantity(itemIndex));
        cartPage.clickOnProceedToCheckoutButton();
    }

    private AddressDetails createAddress(String email, String firstName, String lastName, String company, String[] streetAddress, String city, String stateProvince, String zipPostalCode, String country, String phoneNumber) {
        return new AddressDetails(email, firstName, lastName, company, streetAddress, city, stateProvince, zipPostalCode, country, phoneNumber);
    }

    private String getAValidEmailAddress() {
        emailUtils = new EmailFactory();
        return emailUtils.getEmail();
    }
}
