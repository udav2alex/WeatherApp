package ru.gressor.weatherapp.weather_providers.openweather;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.weather_providers.openweather.dto_current_weather.CurrentWeather;
import ru.gressor.weatherapp.weather_providers.openweather.dto_one_call.OpenWeatherOneCall;
import ru.gressor.weatherapp.weather_providers.HttpWeatherError;

public class OpenWeatherDataProvider {
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 10000;
    private static final String API_URL_CURRENT_WEATHER =
            "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s&lang=%s";
    private static final String API_URL_ONE_CALL =
            "https://api.openweathermap.org/data/2.5/onecall?lon=%f&lat=%f&exclude=minutely&units=metric&appid=%s&lang=%s";
    // TODO extract API_KEY
    private static final String API_KEY =
            "c4b46b269a484630f9a27a8c115c5e86";

    public OpenWeatherOneCall getWeatherAndForecasts(PositionPoint position) throws IOException, HttpWeatherError {
        HttpsURLConnection connection = null;

        try {
            if (position.getLat() <= -1000 || position.getLon() <= -1000) {
                connection = createConnection(new URL(getApiUrlCurrentWeather(position)));

                if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                    String responseMessage = connection.getResponseMessage();
                    int responseCode = connection.getResponseCode();
                    throw new HttpWeatherError(responseMessage, responseCode);
                }

                CurrentWeather currentWeather = getWeather(connection);
                position.setLat(currentWeather.getCoord().getLat());
                position.setLon(currentWeather.getCoord().getLon());
            }

            connection = createConnection(new URL(getApiUrlOneCall(position)));

            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                return getOneCallWeather(connection);
            } else {
                throw new HttpWeatherError(connection.getResponseMessage(), connection.getResponseCode());
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private HttpsURLConnection createConnection(URL url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        return connection;
    }

    private OpenWeatherOneCall getOneCallWeather(HttpsURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String string = readLines(in);
        Gson gson = new Gson();

        return gson.fromJson(string, OpenWeatherOneCall.class);
    }

    private CurrentWeather getWeather(HttpsURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String string = readLines(in);
        Gson gson = new Gson();

        return gson.fromJson(string, CurrentWeather.class);
    }

    private String getApiUrlCurrentWeather(PositionPoint position) throws UnsupportedEncodingException {
        return String.format(Locale.ENGLISH, API_URL_CURRENT_WEATHER,
                URLEncoder.encode(position.getTown(), "utf-8"),
                API_KEY, Locale.getDefault().getLanguage());
    }

    private String getApiUrlOneCall(PositionPoint position) {
        // TODO Select correct parameters String.format() lon=37.62&lat=55.75
        return String.format(Locale.ENGLISH, API_URL_ONE_CALL, position.getLon(), position.getLat(),
                API_KEY, Locale.getDefault().getLanguage());
    }

    private String readLines(BufferedReader in) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null)
            sb.append(line).append("\n");

        return sb.toString();
    }
}
