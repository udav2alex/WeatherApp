package ru.gressor.weatherapp.weather_providers.openweather.retro;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.gressor.weatherapp.data_types.ActualWeather;
import ru.gressor.weatherapp.data_types.Coord;
import ru.gressor.weatherapp.data_types.DayForecast;
import ru.gressor.weatherapp.data_types.HourForecast;
import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.data_types.WeatherState;
import ru.gressor.weatherapp.weather_providers.DataController;
import ru.gressor.weatherapp.weather_providers.HttpWeatherError;
import ru.gressor.weatherapp.weather_providers.openweather.dto_current_weather.CurrentWeather;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.Daily;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.Hourly;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.OpenWeatherOneCall;

public class OpenWeatherRetrofitDataProvider {
    public static final String BASE_URL = "https://api.openweathermap.org/";
    public static final String CURRENT_WEATHER_PATH = "data/2.5/weather";
    public static final String ONE_CALL_PATH = "data/2.5/onecall";
    public static final String UNITS = "metric";
    private static final String API_KEY = "c4b46b269a484630f9a27a8c115c5e86";

    private DataController dataController;

    private Retrofit retrofit;
    private OpenWeatherCallCurrent callCurrent;
    private OpenWeatherCallOneCall callOneCall;

    private PositionPoint positionPoint;
    private String cityName;

    public OpenWeatherRetrofitDataProvider(DataController dataController) {
        this.dataController = dataController;
    }

    public void getWeatherAndForecasts(PositionPoint position) {
        initRetrofit();
        positionPoint = position;
        cityName = position.getTown();

        callOneCall = retrofit.create(OpenWeatherCallOneCall.class);

        if (position.getCoord() == null) {
            callCurrent = retrofit.create(OpenWeatherCallCurrent.class);
            getCoordForCityName();
        } else {
            getWeatherForPositionWithCoord();
        }
    }

    private void initRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    private void getCoordForCityName() {
        callCurrent
                .loadCurrentWeather(
                        cityName,
                        UNITS, API_KEY, Locale.getDefault().getLanguage())
                .enqueue(new WeatherRequestCallBack<>(
                        rawData -> {
                            positionPoint = getNewPopulatedPosition(cityName, rawData);
                            coordsGot();
                        }));
    }

    private void coordsGot() {
        getWeatherForPositionWithCoord();
    }

    private void getWeatherForPositionWithCoord() {
        callOneCall
                .loadOpenWeatherOneCall(
                        positionPoint.getCoord().getLon(), positionPoint.getCoord().getLat(),
                        UNITS, API_KEY, Locale.getDefault().getLanguage())
                .enqueue(new WeatherRequestCallBack<>(
                        rawData ->
                                dataController.updateWeather(createWeatherState(rawData))));
    }

    private interface Runner<T> {
        void processIt(T rawData);
    }

    private class WeatherRequestCallBack<T> implements Callback<T> {
        private Runner<T> runner;

        public WeatherRequestCallBack(Runner<T> runner) {
            this.runner = runner;
        }

        @Override
        public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
            if (response.body() != null && response.isSuccessful()) {
                T rawData = response.body();
                runner.processIt(rawData);
            } else {
                dataController.updateWeatherFailure(
                        new HttpWeatherError(response.message(), response.code()));
            }
        }

        @Override
        public void onFailure(@NonNull Call<T> call, @NonNull Throwable throwable) {
            dataController.updateWeatherFailure(throwable);
        }
    }

    private PositionPoint getNewPopulatedPosition(String cityName, CurrentWeather currentWeather) {
        PositionPoint positionPoint = new PositionPoint(cityName, null);
        positionPoint.setServiceTown(currentWeather.getName());
        positionPoint.setCoord(new Coord(
                currentWeather.getCoord().getLon(), currentWeather.getCoord().getLat()));
        return positionPoint;
    }

    private WeatherState createWeatherState(OpenWeatherOneCall in) {
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
