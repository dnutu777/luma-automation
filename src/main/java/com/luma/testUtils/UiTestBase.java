package com.luma.testUtils;

import com.luma.core.config.Config;
import com.luma.core.config.WebDriverConfig;
import com.luma.core.reporting.ExtentReportsListener;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.net.MalformedURLException;
import java.util.Properties;

@Listeners(ExtentReportsListener.class)
public class UiTestBase {
    @Getter
    protected ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    protected Properties credentials;
    private WebDriverConfig webDriverConfig;

    public UiTestBase() {
        Config config = new Config();
        this.credentials = config.getCredentials();
        this.webDriverConfig = new WebDriverConfig(config);
    }

    @BeforeMethod
    protected void setUp() throws MalformedURLException {
        driverThreadLocal.set(webDriverConfig.createDriver());
    }

    @AfterMethod
    protected void tearDown() {
        if (driverThreadLocal.get() != null) {
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
        }
    }

    protected void openApplication() {
        driverThreadLocal.get().navigate().to(System.getProperty("webAppUrl"));
    }
}
