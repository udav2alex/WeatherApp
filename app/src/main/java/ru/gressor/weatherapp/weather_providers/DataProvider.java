package ru.gressor.weatherapp.weather_providers;

import ru.gressor.weatherapp.data_types.PositionPoint;

public interface DataProvider {
    void refreshCurrentWeather(PositionPoint position);
}
