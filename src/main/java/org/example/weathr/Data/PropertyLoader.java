package org.example.weathr.Data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
    private final String PROPERTIES_FILE_PATH = "src/main/resources/application.properties";
    private final Properties properties;

    public PropertyLoader() throws IOException {
        this.properties = new Properties();
        loadProperties();
    }

    private void loadProperties() throws IOException {
        try (FileInputStream inputStream = new FileInputStream(PROPERTIES_FILE_PATH)) {
            properties.load(inputStream);
        }
    }

    private void saveProperties() throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(PROPERTIES_FILE_PATH)) {
            properties.store(outputStream, null);
        }
    }

    public String getSityName() {
        return properties.getProperty("sityName");
    }

    public String getCountry() {
        return properties.getProperty("country");
    }

    public String getLat() {
        return properties.getProperty("lat");
    }

    public String getLon() {
        return properties.getProperty("lon");
    }

    public void setNewValue(String newName, String newCountry, String newLat, String newLon) throws IOException {
        properties.setProperty("sityName", newName);
        properties.setProperty("country", newCountry);
        properties.setProperty("lat", newLat);
        properties.setProperty("lon", newLon);
        saveProperties();
    }
}