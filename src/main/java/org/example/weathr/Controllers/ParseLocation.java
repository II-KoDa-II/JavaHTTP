package org.example.weathr.Controllers;


import org.example.weathr.Data.Exceptions.RetrivingException;
import org.example.weathr.Data.LocationData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ParseLocation {
    public List<LocationData> parseCityLocation(String sityName) throws IOException {
        String myKey = "355d1d5cfc0be4cf6878caa62163d130";
        String urlString = "http://api.openweathermap.org/geo/1.0/direct?q=" + sityName + "&limit=5&appid=" + myKey;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        List<LocationData> locations;
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(response.toString());
            locations = parseJsonArray(jsonArray);
        } else {
            throw new RetrivingException("Error while retrieving data: "+responseCode);
        }
        connection.disconnect();
        return locations;
    }

    private static List<LocationData> parseJsonArray(JSONArray jsonArray) {
        List<LocationData> locations = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            LocationData location = parseJsonObject(jsonObject);
            locations.add(location);
        }
        return locations;
    }

    private static LocationData parseJsonObject(JSONObject jsonObject) {
        String name = jsonObject.optString("name");
        double lat = jsonObject.optDouble("lat");
        double lon = jsonObject.optDouble("lon");
        String country = jsonObject.optString("country");
        String state = jsonObject.optString("state");

        return new LocationData(name, lat, lon, country, state);
    }
}
