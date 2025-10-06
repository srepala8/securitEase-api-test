package com.securitEase.api.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.securitEase.api.constant.Constant;
import com.securitEase.api.util.Utility;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.*;

public class CountryCountTest {
	
	
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
	

    @Test(description = "As a map builder I need information about the number of countries in the world so that my maps are correct")
    public void validateNumberOfCountries() {
    	
    	RestAssured.baseURI = properties.getProperty("baseURL");

        // Step 1: Call the REST Countries API
        Response response = 
            given()
                
                .queryParam("fields", "name")
            .when()
                .get()
            .then()
                .statusCode(200)
                .extract().response();

        // Step 2: Parse JSON response
        List<Map<String, Object>> countryList = response.jsonPath().getList("$");

        // Step 3: Count total countries
        int totalCountries = countryList.size();

        // Step 4: Extract all country names
        List<String> countryNames = response.jsonPath().getList("name.common");

        // Step 5: Print results in TestNG report and console
        Reporter.log("<b>API Endpoint:</b> https://restcountries.com/v3.1/all?fields=name", true);
        Reporter.log("<b>Status Code:</b> " + response.getStatusCode(), true);
        Reporter.log("<b>Total Countries:</b> " + totalCountries, true);
        Reporter.log("<b>Country Names:</b><br><pre>" + String.join(", ", countryNames) + "</pre>", true);

        System.out.println("==========================================");
        System.out.println("Total number of countries: " + totalCountries);
        System.out.println("Countries: " + countryNames);
        System.out.println("==========================================");

        // Step 6: Assertion
        Assert.assertTrue(totalCountries > 190, "Expected more than 190 countries");
    }
}
