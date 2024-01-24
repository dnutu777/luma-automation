package com.luma.core.config;

import com.luma.core.customExceptions.ConfigException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String CREDENTIALS_FILE_PREFIX = "_credentials.properties";

    @Getter
    private String env;
    @Getter
    private final Properties configProperties = new Properties();
    @Getter
    private final Properties credentials = new Properties();
    private final Logger logger = LoggerFactory.getLogger(Config.class);

    public Config() {
        loadConfigProperties();
        this.env = determineEnvironment();
    }

    private String determineEnvironment() {
        String environment = System.getProperty("env", configProperties.getProperty("env")).toLowerCase();
        System.setProperty("env", environment);
        logger.info("Environment set to: {}", environment);
        return environment;
    }

    private void loadConfigProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            if (input == null) {
                throw new ConfigException("Configuration properties file not found: " + CONFIG_FILE_NAME);
            }
            configProperties.load(input);
            logger.info("Configuration properties loaded successfully from {}", CONFIG_FILE_NAME);
        } catch (IOException ex) {
            throw new ConfigException("Unable to load configuration properties file: " + CONFIG_FILE_NAME, ex);
        }
    }
}
