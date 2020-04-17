package ru.gressor.weatherapp;

public class WeatherState {
    private int temperature;
    private int tempFeelsLike;
    private int clouds;
    private int windSpeed;
    private int windDirection;
    private int pressure = -1;
    private int humidity = -1;
    private int conditions = -1;

    public WeatherState() {
    }

    public WeatherState(int temperature) {
        this.temperature = temperature;
    }

    public static WeatherState generateRandom() {
        WeatherState weatherState = new WeatherState();

        weatherState.temperature = (int)(8 + Math.random() * 10);
        weatherState.tempFeelsLike = (int)(8 + Math.random() * 10);
        weatherState.clouds = 0;
        weatherState.windSpeed = (int)(Math.random() * 7);
        weatherState.windDirection = 0;
        weatherState.pressure = (int)(730 + Math.random() * 50);
        weatherState.humidity = (int)(40 + Math.random() * 61);
        weatherState.conditions = 0;

        return weatherState;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getTempFeelsLike() {
        return tempFeelsLike;
    }

    public int getClouds() {
        return clouds;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getConditions() {
        return conditions;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setTempFeelsLike(int tempFeelsLike) {
        this.tempFeelsLike = tempFeelsLike;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setConditions(int conditions) {
        this.conditions = conditions;
    }
}
