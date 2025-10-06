package com.securitEase.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utility {

    public static Properties fetchApiDetails(String propertyFile) throws IOException {
        Properties prop = new Properties();
        try (InputStream input = Utility.class.getClassLoader().getResourceAsStream(propertyFile)) {
            if (input == null) {
                throw new IOException("Property file not found in classpath: " + propertyFile);
            }
            prop.load(input);
        }
        return prop;
    }
}
