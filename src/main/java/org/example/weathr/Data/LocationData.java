package org.example.weathr.Data;

public class LocationData {
    private final String name;
    private final double latitude;
    private final double longitude;
    private final String country;
    private final String state;

    public LocationData(String name, double latitude, double longitude, String country, String state) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public double getLat(){
        return latitude;
    }

    public double getLon(){
        return longitude;
    }

    @Override
    public String toString() {
        return "LocationData{name="+name+", latitude="+latitude+", longitude="+longitude+", country=" + country +", state=" + state+"}";
    }
}