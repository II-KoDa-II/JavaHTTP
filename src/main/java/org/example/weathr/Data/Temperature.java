package org.example.weathr.Data;

public class Temperature{
    private final double temp;
    private final double feels_like;
    private final double temp_min;
    private final double temp_max;
    private final double humidity;
    private final double pressure;

    public Temperature(double temp, double feelsLike, double tempMin, double tempMax, double humidity, double pressure) {
        this.temp = temp;
        feels_like = feelsLike;
        temp_min = tempMin;
        temp_max = tempMax;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public double getTemp() {
        return temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }
}
