package ru.gressor.weatherapp.data_types.openweather_one_call;

public class Daily {
    private long dt;
    private long sunrise;
    private long sunset;
    private Temp temp;
    private FeelsLike feels_like;
    private float pressure;
    private float humidity;
    private float dew_point;
    private float uvi;
    private float clouds;
    private float visibility;
    private float wind_speed;
    private float wind_deg;
    Weather[] weather;


    // Getter Methods

    public long getDt() {
        return dt;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public Temp getTemp() {
        return temp;
    }

    public FeelsLike getFeels_like() {
        return feels_like;
    }

    public float getPressure() {
        return pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getDew_point() {
        return dew_point;
    }

    public float getUvi() {
        return uvi;
    }

    public float getClouds() {
        return clouds;
    }

    public float getVisibility() {
        return visibility;
    }

    public float getWind_speed() {
        return wind_speed;
    }

    public float getWind_deg() {
        return wind_deg;
    }

    public Weather[] getWeather() {
        return weather;
    }

    // Setter Methods


    public void setDt(long dt) {
        this.dt = dt;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public void setFeels_like(FeelsLike feels_like) {
        this.feels_like = feels_like;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public void setDew_point(float dew_point) {
        this.dew_point = dew_point;
    }

    public void setUvi(float uvi) {
        this.uvi = uvi;
    }

    public void setClouds(float clouds) {
        this.clouds = clouds;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public void setWind_speed(float wind_speed) {
        this.wind_speed = wind_speed;
    }

    public void setWind_deg(float wind_deg) {
        this.wind_deg = wind_deg;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }
}
