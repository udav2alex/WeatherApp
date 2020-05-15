package ru.gressor.weatherapp.weather_providers;

import java.io.IOException;

import ru.gressor.weatherapp.data_types.local_dto.PositionPoint;
import ru.gressor.weatherapp.data_types.local_dto.WeatherState;

public interface DataAdapter {
    WeatherState getWeatherState(PositionPoint position) throws IOException;
}
