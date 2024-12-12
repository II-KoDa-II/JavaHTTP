package org.example.weathr.Data;


import org.example.weathr.Data.Exceptions.WindDirectionException;

public class WeatherData {
    private final String date;
    private final Weather weather;
    private final Temperature temperature;
    private final Wind wind;
    private final double visibility;
    private Rain rain = null;
    private Clouds clouds = null;

    public WeatherData(String date, Weather weather, Temperature temperature, Wind wind, double visibility, Rain rain, Clouds clouds) {
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
        this.wind = wind;
        this.visibility = visibility;
        this.rain = rain;
        this.clouds = clouds;
    }

    public WeatherData(String date, Weather weather, Temperature temperature, Wind wind, double visibility, Rain rain) {
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
        this.wind = wind;
        this.visibility = visibility;
        this.rain = rain;
    }

    public WeatherData(String date, Weather weather, Temperature temperature, Wind wind, double visibility, Clouds clouds) {
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
        this.wind = wind;
        this.visibility = visibility;
        this.clouds = clouds;
    }

    public WeatherData(String date, Weather weather, Temperature temperature, Wind wind, double visibility) {
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
        this.wind = wind;
        this.visibility = visibility;
    }

    public double getTemp() {
        return temperature.getTemp();
    }

    public double getFeelsLike(){
        return temperature.getFeels_like();
    }

    public double getTempMin(){
        return temperature.getTemp_min();
    }

    public double getTempMax(){
        return temperature.getTemp_max();
    }

    public double getVisibility(){
        return visibility;
    }

    public double getHumidity(){
        return temperature.getHumidity();
    }

    public double getPressure(){
        return temperature.getPressure();
    }

    public double getWindSpeed(){
        return wind.getSpeed();
    }

    public String getStatus(){
        return weather.getMain();
    }

    public String getDeg(){
        double deg = wind.getDeg();
        if(deg <= 45 || deg > 315){
            return "Северный";
        }
        if(deg <= 135 && deg > 45){
            return "Восточный";
        }
        if(deg <= 225 && deg > 135){
            return "Южный";
        }
        if(deg <= 315 && deg > 225){
            return "Западный";
        }
        throw new WindDirectionException("failed to determine wind direction");
    }

    public Rain getRain() {
        return rain;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public String getDate(){
        return date;
    }

    public String getIcon(){
        return weather.getIcon();
    }
}

