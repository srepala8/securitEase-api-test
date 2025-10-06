package com.securitEase.api.test;


import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.io.IOException;
import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.securitEase.api.constant.Constant;
import com.securitEase.api.util.Utility;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ApiSchemaValidationTest {
	
	
	
	Properties properties;
    public static ExtentTest test;

    ExtentReports extent = com.securitEase.api.utils.ReportManager.getInstance();

    @BeforeTest
    public void setUp() {

        try {
            properties = Utility.fetchApiDetails(Constant.API_PROPERTY_FILE_PATH);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
	
	

    @Test
    public void validateCountrySchema() {
    	
    	RestAssured.baseURI = properties.getProperty("baseURL");
        // Step 1: Call the API
        Response response = 
            given()
                    
            .when()
                .get()
            .then()
                .extract().response();

        // Step 2: Log response in console + TestNG report
        String responseBody = response.getBody().asPrettyString();
        Reporter.log("<b>API Response:</b><br><pre>" + responseBody + "</pre>", true);
        System.out.println("API Response: \n" + responseBody);

        // Step 3: Validate schema
        try {
            response.then().assertThat()
                    .body(matchesJsonSchemaInClasspath("schemas/countrySchema.json"));

            Reporter.log("<b>Schema Validation:</b> ✅ PASSED", true);
            System.out.println("Schema Validation: ✅ PASSED");

        } catch (AssertionError e) {
            Reporter.log("<b>Schema Validation:</b> ❌ FAILED<br><pre>" + e.getMessage() + "</pre>", true);
            System.err.println("Schema Validation: ❌ FAILED\n" + e.getMessage());
            Assert.fail("Schema validation failed: " + e.getMessage());
        }
    }
}

