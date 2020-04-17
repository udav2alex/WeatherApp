package ru.gressor.weatherapp;

public final class MainPresenter {
    private static MainPresenter instance = null;
//    private static final Object monitor = new Object();

    private WeatherState weatherState;

    private MainPresenter() {
        weatherState = WeatherState.generateRandom();
    }

    public static MainPresenter getInstance() {
        if (instance == null) {
            instance = new MainPresenter();
        }
        return instance;
    }

    public WeatherState getWeatherState() {
        return weatherState;
    }

    public void setWeatherState(WeatherState weatherState) {
        this.weatherState = weatherState;
    }
}
