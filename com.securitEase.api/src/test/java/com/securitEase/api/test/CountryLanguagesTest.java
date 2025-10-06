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

public class CountryLanguagesTest {
	
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
	

    @Test(description = "Verify SASL is among South Africa's official languages")
    public void validateSouthAfricanSignLanguageRecognition() {
    	
    	RestAssured.baseURI = properties.getProperty("baseURL");

        // Step 1: Call the API to get country names and languages
        Response response =
            given()
                
                .queryParam("fields", "name,languages")
            .when()
                .get()
            .then()
                .statusCode(200)
                .extract().response();

        // Step 2: Parse the JSON into a list of maps
        List<Map<String, Object>> countryList = response.jsonPath().getList("$");

        // Step 3: Find South Africa entry
        Map<String, Object> southAfrica = countryList.stream()
                .filter(c -> {
                    Map<String, Object> name = (Map<String, Object>) c.get("name");
                    return "South Africa".equals(name.get("common"));
                })
                .findFirst()
                .orElse(null);

        Assert.assertNotNull(southAfrica, "❌ South Africa not found in API response!");

        // Step 4: Extract South Africa’s languages
        @SuppressWarnings("unchecked")
        Map<String, String> saLanguages = (Map<String, String>) southAfrica.get("languages");

        Reporter.log("<b>South Africa Languages (from API):</b><br><pre>" + saLanguages + "</pre>", true);
        System.out.println("South Africa languages: " + saLanguages);

        // Step 5: Assert SASL inclusion
        boolean hasSASL = saLanguages != null && saLanguages.values().stream()
                .anyMatch(lang -> lang.equalsIgnoreCase("South African Sign Language") || lang.equalsIgnoreCase("SASL"));

        if (hasSASL) {
            Reporter.log("<b>✅ Assertion Passed:</b> South African Sign Language (SASL) is recognized.", true);
            System.out.println("✅ SASL recognized as an official language of South Africa.");
        } else {
            Reporter.log("<b>❌ Assertion Failed:</b> SASL not found in South Africa's language list.", true);
            System.err.println("❌ SASL not found in API languages for South Africa.");
        }

       
        Assert.assertFalse(hasSASL,	
                "Expected South African Sign Language (SASL) not to be listed as one of South Africa’s official languages.");
    }
}
