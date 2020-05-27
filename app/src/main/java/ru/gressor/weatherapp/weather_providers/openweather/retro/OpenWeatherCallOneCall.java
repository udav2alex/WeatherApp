package ru.gressor.weatherapp.weather_providers.openweather.retro;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.OpenWeatherOneCall;

public interface OpenWeatherCallOneCall {
    @GET("data/2.5/onecall")
    Call<OpenWeatherOneCall> loadOpenWeatherOneCall(
            @Query("lon") float lon,
            @Query("lat") float lat,
            @Query("units") String units,
            @Query("appid") String apiKey,
            @Query("lang") String language);
}