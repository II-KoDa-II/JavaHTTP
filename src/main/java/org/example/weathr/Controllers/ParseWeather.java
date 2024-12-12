package org.example.weathr.Controllers;

import org.example.weathr.Data.*;
import org.example.weathr.Data.Exceptions.RetrivingException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ParseWeather {
    public List<WeatherData> getWeatherForecast(double lat, double lon) throws IOException {
        String myKey = "355d1d5cfc0be4cf6878caa62163d130";
        String urlString = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + myKey;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        List<WeatherData> weather;
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            weather = convertWeatherAsList(jsonObject);
        } else {
            throw new RetrivingException("Error while retrieving data: " + responseCode);
        }
        connection.disconnect();
        return weather;
    }

    public WeatherData getCurrentWeather(double lat, double lon) throws IOException {
        String myKey = "355d1d5cfc0be4cf6878caa62163d130";
        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat="+ lat + "&lon=" + lon + "&units=metric&appid=" + myKey;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        WeatherData weather;
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            weather = convertWeatherFromJson(jsonObject);
        } else {
            throw new RetrivingException("Error while retrieving data: " + responseCode);
        }
        connection.disconnect();
        return weather;
    }

    private static List<WeatherData> convertWeatherAsList(JSONObject jsonObject) {
        List<WeatherData> weathers = new ArrayList<>();
        JSONArray weatherArray = jsonObject.getJSONArray("list");

        for (int i = 0; i < weatherArray.length(); i++) {
            WeatherData weatherShort = convertWeatherFromJson(weatherArray.getJSONObject(i));
            try {
                int dateTime = Integer.parseInt(weatherShort.getDate().substring(11, 13));
                if(dateTime == 12){
                    weathers.add(weatherShort);
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        return weathers;
    }

    private static WeatherData convertWeatherFromJson(JSONObject jsonObject) {
        String date;
        if(jsonObject.has("dt_txt")){
            date = jsonObject.getString("dt_txt");
        } else {
            date = String.valueOf(jsonObject.getInt("dt"));
        }

        String main = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
        String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
        String icon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
        Weather weather = new Weather(main, description, icon);
        double temp = jsonObject.getJSONObject("main").getDouble("temp");
        double feelsLike = jsonObject.getJSONObject("main").getDouble("feels_like");
        double tempMin = jsonObject.getJSONObject("main").getDouble("temp_min");
        double tempMax = jsonObject.getJSONObject("main").getDouble("temp_max");
        double pressure = jsonObject.getJSONObject("main").getDouble("pressure");
        double humidity = jsonObject.getJSONObject("main").getDouble("humidity");
        Temperature temperature = new Temperature(temp, feelsLike, tempMin, tempMax, humidity, pressure);
        double speed = jsonObject.getJSONObject("wind").getDouble("speed");
        double deg = jsonObject.getJSONObject("wind").getDouble("deg");
        Wind wind = new Wind(speed, deg);

        double visibility = 0;
        if(jsonObject.has("visibility")){
            visibility = jsonObject.getDouble("visibility");
        }

        Rain rain = null;
        if(jsonObject.has("rain")){
            if(jsonObject.getJSONObject("rain").has("1h")){
                double h1 = jsonObject.getJSONObject("rain").getDouble("1h");
                rain = new Rain(h1);
            }
        }

        Clouds clouds = null;
        if(jsonObject.has("clouds")){
            if(jsonObject.getJSONObject("clouds").has("all")){
                double all = jsonObject.getJSONObject("clouds").getDouble("all");
                clouds = new Clouds(all);
            }
        }

        if(rain != null && clouds != null){
            return new WeatherData(date, weather, temperature, wind, visibility, rain, clouds);
        }

        if(rain != null){
            return new WeatherData(date, weather, temperature, wind, visibility, rain);
        }

        if(clouds != null){
            return new WeatherData(date, weather, temperature, wind, visibility, clouds);
        }

        return new WeatherData(date, weather, temperature, wind, visibility);
    }
}
