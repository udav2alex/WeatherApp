package ru.gressor.weatherapp.weather_providers.openweather;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.gressor.weatherapp.data_types.ActualWeather;
import ru.gressor.weatherapp.data_types.DayForecast;
import ru.gressor.weatherapp.data_types.HourForecast;
import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.data_types.WeatherState;
import ru.gressor.weatherapp.weather_providers.openweather.dto_current_weather.CurrentWeather;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.Daily;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.Hourly;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.OpenWeatherOneCall;
import ru.gressor.weatherapp.weather_providers.openweather.retro.OpenWeatherCallCurrent;
import ru.gressor.weatherapp.weather_providers.openweather.retro.OpenWeatherCallOneCall;

public class OpenWeatherRetrofitDataProvider {
    public static final String BASE_URL = "https://api.openweathermap.org/";
    public static final String CURRENT_WEATHER_PATH = "data/2.5/weather";
    public static final String ONE_CALL_PATH = "data/2.5/onecall";
    public static final String UNITS = "metric";
    private static final String API_KEY = "c4b46b269a484630f9a27a8c115c5e86";

    private Retrofit retrofit;
    private OpenWeatherCallCurrent callCurrent;
    private OpenWeatherCallOneCall callOneCall;


    public void getWeatherAndForecasts(PositionPoint position) {
        initRetrofit();
        enqueueWeatherByCityName("Moscow");
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        callCurrent = retrofit.create(OpenWeatherCallCurrent.class);
        callOneCall = retrofit.create(OpenWeatherCallOneCall.class);
    }

    private void enqueueWeatherByCityName(String cityName) {
        callCurrent
                .loadCurrentWeather(cityName, UNITS, API_KEY, Locale.getDefault().getLanguage())
                .enqueue(new Callback<CurrentWeather>() {
                    @Override
                    public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                        if (response.body() != null) {
                            CurrentWeather currentWeather = response.body();
                            PositionPoint positionPoint = getNewPopulatedPosition(cityName, currentWeather);

                            // TODO testing
                            Log.v("CurrentWeatherLON", Double.toString(currentWeather.getCoord().getLon()));
                            enqueueWeatherByCoords(positionPoint);
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeather> call, Throwable t) {

                    }
                });
    }

    private void enqueueWeatherByCoords(PositionPoint positionPoint) {
        callOneCall
                .loadOpenWeatherOneCall(
                positionPoint.getLon(), positionPoint.getLat(),
                UNITS, API_KEY, Locale.getDefault().getLanguage())
                .enqueue(new Callback<OpenWeatherOneCall>() {
                    @Override
                    public void onResponse(
                            Call<OpenWeatherOneCall> call, Response<OpenWeatherOneCall> response) {
                        if(response.body() != null) {
                            OpenWeatherOneCall weatherRaw = response.body();
                            WeatherState weatherState = getWeatherState(weatherRaw);

                            // TODO testing
                            Log.v("CurrentWeather", weatherState.getActualWeather().getIconFileName());
                        }
                    }

                    @Override
                    public void onFailure(Call<OpenWeatherOneCall> call, Throwable t) {

                    }
                });
    }

    private PositionPoint getNewPopulatedPosition(String cityName, CurrentWeather currentWeather) {
        PositionPoint positionPoint = new PositionPoint(cityName, null);
        positionPoint.setLon(currentWeather.getCoord().getLon());
        positionPoint.setLat(currentWeather.getCoord().getLat());
        positionPoint.setServiceTown(currentWeather.getName());
        return positionPoint;
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
