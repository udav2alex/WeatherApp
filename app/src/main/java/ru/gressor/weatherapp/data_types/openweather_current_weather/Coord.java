package ru.gressor.weatherapp.data_types.openweather_current_weather;

public class Coord {
    private float lon = -1000f;
    private float lat = -1000f;

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }
}
