package ru.gressor.weatherapp.weather_providers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import ru.gressor.weatherapp.data_types.CurrentWeather;
import ru.gressor.weatherapp.data_types.DayForecast;
import ru.gressor.weatherapp.data_types.HourForecast;
import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.data_types.WeatherState;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.Daily;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.Hourly;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.OpenWeatherOneCall;
import ru.gressor.weatherapp.weather_providers.openweather.OpenWeatherDataProvider;

public class OpenWeatherDataAdapter implements DataAdapter {

    @Override
    public WeatherState getWeatherState(PositionPoint position) throws IOException, HttpWeatherError {
        OpenWeatherDataProvider provider = new OpenWeatherDataProvider();

        OpenWeatherOneCall in = provider.getWeatherAndForecasts(position);

        Calendar actualAt = Calendar.getInstance();
        actualAt.setTimeInMillis(in.getCurrent().getDt() * 1000);

        CurrentWeather currentWeather = new CurrentWeather(
                actualAt,
                Math.round(in.getCurrent().getTemp()),
                in.getCurrent().getWeather()[0].getIcon(),
                in.getCurrent().getWeather()[0].getDescription(),
                Math.round(in.getCurrent().getFeels_like()),
                Math.round(in.getCurrent().getClouds()),
                Math.round(in.getCurrent().getWind_speed()),
                Math.round(in.getCurrent().getWind_deg()),
                Math.round(in.getCurrent().getPressure()),
                Math.round(in.getCurrent().getHumidity())
        );

        ArrayList<HourForecast> hourlyForecast = new ArrayList<>();
        for (Hourly hourly : in.getHourlies()) {
            Calendar actual = Calendar.getInstance();
            actual.setTimeInMillis(hourly.getDt() * 1000);

            hourlyForecast.add(new HourForecast(
                    actual,
                    Math.round(hourly.getTemp()),
                    hourly.getWeather()[0].getIcon()
            ));
        }

        ArrayList<DayForecast> dailyForecast = new ArrayList<>();
        for (Daily daily : in.getDailies()) {
            Calendar actual = Calendar.getInstance();
            actual.setTimeInMillis(daily.getDt() * 1000);

            dailyForecast.add(new DayForecast(
                    actual,
                    Math.round(daily.getTemp().getMin()),
                    Math.round(daily.getTemp().getMin()),
                    daily.getWeather()[0].getIcon()
            ));
        }

        return new WeatherState(currentWeather, hourlyForecast, dailyForecast);
    }
}