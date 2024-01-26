package com.luma.core.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class WebDriverConfig {
    private Config config;
    private String browser;
    private boolean headless;
    private boolean useGrid;
    private String gridUrl;

    public WebDriverConfig(Config config) {
        this.config = config;
        configWebDriver();
        setWebAppUrl();
    }

    public WebDriver createDriver() throws MalformedURLException {
        return useGrid ? createRemoteDriver() : createLocalDriver();
    }

    private WebDriver createRemoteDriver() throws MalformedURLException {
        WebDriver driver;
        switch (browser) {
            case "chrome":
                driver = new RemoteWebDriver(new URL(gridUrl), getChromeOptions());
                break;
            case "firefox":
                driver = new RemoteWebDriver(new URL(gridUrl), getFirefoxOptions());
                break;
            case "edge":
                driver = new RemoteWebDriver(new URL(gridUrl), getEdgeOptions());
                break;
            case "safari":
                driver = new RemoteWebDriver(new URL(gridUrl), null);
                break;
            default:
                throw new IllegalArgumentException("Browser [" + browser + "] is not supported when using Selenium Grid.");
        }
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().window().maximize();
        return driver;
    }

    private WebDriver createLocalDriver() {
        WebDriver driver;
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(getChromeOptions());
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(getFirefoxOptions());
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver(getEdgeOptions());
                break;
            case "safari":
                WebDriverManager.safaridriver().setup();
                driver = new SafariDriver();
                break;
            default:
                throw new IllegalArgumentException("Browser [" + browser + "] is not supported.");
        }

        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().window().maximize();
        return driver;
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");

        if (headless) {
            options.addArguments("--headless");
        }
        return options;
    }

    private FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--disable-gpu");

        if (headless) {
            options.addArguments("--headless");
        }
        return options;
    }

    private EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-gpu");

        if (headless) {
            options.addArguments("--headless");
        }
        return options;
    }

    private void configWebDriver() {
        browser = System.getProperty("browser", config.getConfigProperties().getProperty("browser").toLowerCase());
        System.setProperty("browser", browser);
        headless = Boolean.parseBoolean(System.getProperty("headless", config.getConfigProperties().getProperty("headless")));
        System.setProperty("headless", String.valueOf(headless));
        useGrid = Boolean.parseBoolean(System.getProperty("useGrid", config.getConfigProperties().getProperty("useGrid")));
        System.setProperty("useGrid", String.valueOf(useGrid));
        gridUrl = config.getConfigProperties().getProperty("gridUrl");
    }

    private void setWebAppUrl() {
        String envWebAppUrlKey = config.getEnv().toLowerCase() + ".webAppUrl";
        System.setProperty("webAppUrl", config.getConfigProperties().getProperty(envWebAppUrlKey));
    }
}
