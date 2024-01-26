package com.luma.core.reporting;

import com.aventstack.extentreports.ExtentTest;
import com.luma.testUtils.UiTestBase;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

public class Screenshot {
    private static final Logger logger = LoggerFactory.getLogger(Screenshot.class);

    public void logFailureScreenshot(ITestResult iTestResult, ExtentTest extentTest) {
        Object testClassInstance = iTestResult.getInstance();

        if (testClassInstance instanceof UiTestBase) {
            WebDriver driver = ((UiTestBase) testClassInstance).getDriverThreadLocal().get();
            if (driver != null) {
                try {
                    extentTest.addScreenCaptureFromBase64String(takeScreenshot(driver), "Failure Screenshot");
                } catch (Exception e) {
                    logger.error("An exception occurred while taking screenshot", e);
                    extentTest.fail("An exception occurred while taking screenshot: " + e.getMessage());
                }
            } else {
                logger.warn("Driver instance was null while trying to take screenshot.");
                extentTest.warning("Driver instance was null while trying to take screenshot.");
            }
        } else {
            logger.warn("Test class instance does not extend BaseTest. Cannot take screenshot.");
            extentTest.warning("Test class instance does not extend BaseTest. Cannot take screenshot.");
        }
    }

    private String takeScreenshot(WebDriver driver) {
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        return takesScreenshot.getScreenshotAs(OutputType.BASE64);
    }
}
