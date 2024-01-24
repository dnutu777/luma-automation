package com.luma.testUtils;

import com.luma.core.config.ApiConfig;
import com.luma.core.config.Config;
import com.luma.core.reporting.ExtentReportsListener;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
@Listeners(ExtentReportsListener.class)
public class ApiTestBase {
    private Config config;
    protected RequestSpecification requestSpecification;
    public ApiTestBase() {
        this.config = new Config();
    }

    public void setRequestSpecification(RequestSpecBuilder requestSpecBuilder) {
        requestSpecification = requestSpecBuilder.build();
    }

    @BeforeClass
    protected void init() {
        new ApiConfig(config);

        setRequestSpecification(new RequestSpecBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"));
    }

    @AfterClass
    protected void tearDown() {
        RestAssured.reset();
        this.requestSpecification = null;
    }
}
