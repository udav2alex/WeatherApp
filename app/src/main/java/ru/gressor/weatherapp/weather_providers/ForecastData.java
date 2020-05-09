package ru.gressor.weatherapp.weather_providers;

import java.util.Calendar;

import ru.gressor.weatherapp.data_types.WeatherState;

public class ForecastData {
    private static final int FORECAST_PERIOD = 10;

    private int[][] stub = {
            {10, 5},
            {11, 6},
            {11, 7},
            {13, 9},
            {10, 6},
            {9, 5},
            {13, 8},
            {17, 12},
            {18, 13},
            {18, 14}
    };

    public int getCount() {
        return FORECAST_PERIOD;
    }

    public WeatherState getForecastFor(int day) {
        Calendar actualAt = Calendar.getInstance();
        actualAt.add(Calendar.DATE, day);

        return new WeatherState(stub[day][0], stub[day][1], actualAt);
    }
}
