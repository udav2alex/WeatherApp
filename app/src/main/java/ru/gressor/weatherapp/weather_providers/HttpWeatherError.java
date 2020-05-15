package ru.gressor.weatherapp.weather_providers;

import java.io.IOException;

public class HttpWeatherError extends IOException {
    private int code;

    public HttpWeatherError(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
