package ru.gressor.weatherapp.data_types.openweather_current_weather;

public class Sys {
    private float type;
    private float id;
    private String country;
    private float sunrise;
    private float sunset;

    public float getType() {
        return type;
    }

    public void setType(float type) {
        this.type = type;
    }

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public float getSunrise() {
        return sunrise;
    }

    public void setSunrise(float sunrise) {
        this.sunrise = sunrise;
    }

    public float getSunset() {
        return sunset;
    }

    public void setSunset(float sunset) {
        this.sunset = sunset;
    }
}
