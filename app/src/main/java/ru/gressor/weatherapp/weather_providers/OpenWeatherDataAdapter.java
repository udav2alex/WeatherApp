package ru.gressor.weatherapp.weather_providers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import ru.gressor.weatherapp.data_types.ActualWeather;
import ru.gressor.weatherapp.data_types.DayForecast;
import ru.gressor.weatherapp.data_types.HourForecast;
import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.data_types.WeatherState;
import ru.gressor.weatherapp.weather_providers.openweather.OpenWeatherRetrofitDataProvider;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.Daily;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.Hourly;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.OpenWeatherOneCall;
import ru.gressor.weatherapp.weather_providers.openweather.OpenWeatherDataProvider;

public class OpenWeatherDataAdapter implements DataAdapter {

    @Override
    public WeatherState getWeatherState(PositionPoint position) throws IOException, HttpWeatherError {
        OpenWeatherDataProvider provider = new OpenWeatherDataProvider();

        OpenWeatherOneCall in = provider.getWeatherAndForecasts(position);

        // TODO Test call for Retrofit
        OpenWeatherRetrofitDataProvider provider1 = new OpenWeatherRetrofitDataProvider();
        provider1.getWeatherAndForecasts(position);
        // TODO Test call for Retrofit

        return getWeatherState(in);
    }

    private WeatherState getWeatherState(OpenWeatherOneCall in) {
        Calendar actualAt = Calendar.getInstance();
        actualAt.setTimeInMillis(in.getCurrent().getDt() * 1000);

        ActualWeather actualWeather = new ActualWeather(
                actualAt,
                Math.round(in.getCurrent().getTemp()),
                in.getCurrent().getWeather()[0].getIcon(),
                in.getCurrent().getWeather()[0].getDescription(),
                Math.round(in.getCurrent().getFeels_like()),
                Math.round(in.getCurrent().getClouds()),
                Math.round(in.getCurrent().getWind_speed()),
                Math.round(in.getCurrent().getWind_deg()),
                Math.round(0.750062f * in.getCurrent().getPressure()),
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
                    Math.round(daily.getTemp().getMax()),
                    daily.getWeather()[0].getIcon()
            ));
        }

        return new WeatherState(actualWeather, hourlyForecast, dailyForecast);
    }
}