package ru.gressor.weatherapp.weather_providers.openweather.dto_one_call;

public class OpenWeatherOneCall {
    private float lat;
    private float lon;
    private String timezone;
    Current current;
    Hourly[] hourly;
    Daily[] daily;


    // Getter Methods

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public Current getCurrent() {
        return current;
    }

    public Hourly[] getHourlies() {
        return hourly;
    }

    public Daily[] getDailies() {
        return daily;
    }

    // Setter Methods

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setCurrent(Current currentObject) {
        this.current = currentObject;
    }

    public void setHourly(Hourly[] hourly) {
        this.hourly = hourly;
    }

    public void setDaily(Daily[] daily) {
        this.daily = daily;
    }
}
