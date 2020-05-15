package ru.gressor.weatherapp.weather_providers;

public class HttpWeatherError extends Exception {
    private int httpCode;

    public HttpWeatherError(String message, int httpCode) {
        super(message);
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
