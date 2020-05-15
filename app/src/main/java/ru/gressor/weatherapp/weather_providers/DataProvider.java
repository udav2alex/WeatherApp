package ru.gressor.weatherapp.weather_providers;

import ru.gressor.weatherapp.data_types.local_dto.PositionPoint;

public interface DataProvider {
    void refreshCurrentWeather(PositionPoint position);
}
