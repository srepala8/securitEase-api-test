package com.securitEase.api.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utility {
	
	public static Properties fetchApiDetails(String propertyFile) throws IOException {

        FileInputStream file = new FileInputStream(propertyFile);
        Properties prop = new Properties();
        prop.load(file);
        return prop;
    }

}
