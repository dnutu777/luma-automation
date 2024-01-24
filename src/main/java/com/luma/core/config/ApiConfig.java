package com.luma.core.config;

import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiConfig {
    private static final Logger logger = LoggerFactory.getLogger(ApiConfig.class);
    private Config config;

    public ApiConfig(Config config) {
        this.config = config;
        try {
            setApiUri();
        } catch (MalformedURLException e) {
            logger.error("Malformed API URL: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid API URL configuration.", e);
        }
    }

    private void setApiUri() throws MalformedURLException {
        String envUriKey = config.getEnv().toLowerCase() + ".apiUri";
        String envApiPath = config.getEnv().toLowerCase() + ".apiPath";
        String apiUri = config.getConfigProperties().getProperty(envUriKey) + config.getConfigProperties().getProperty(envApiPath);

        new URL(apiUri);
        RestAssured.baseURI = apiUri;
        logger.info("API Base URI set to: {}", apiUri);
    }
}
