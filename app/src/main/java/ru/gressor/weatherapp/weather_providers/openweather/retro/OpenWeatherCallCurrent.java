package ru.gressor.weatherapp.weather_providers.openweather.retro;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.gressor.weatherapp.weather_providers.openweather.dto_current_weather.CurrentWeather;

public interface OpenWeatherCallCurrent {
    @GET("data/2.5/weather")
    Call<CurrentWeather> loadCurrentWeather(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("appid") String apiKey,
            @Query("lang") String language);
}