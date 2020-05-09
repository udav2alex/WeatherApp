package ru.gressor.weatherapp.weather_providers;

import android.os.Handler;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import ru.gressor.weatherapp.activities.MainActivity;
import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.data_types.WeatherState;
import ru.gressor.weatherapp.data_types.weather_today.WeatherToday;

public class OpenWeatherDataProvider implements DataProvider {
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 10000;
    private static final String API_CONNECTION_URL =
            "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s";
    // TODO extract API_KEY
    private static final String API_KEY =
            "c4b46b269a484630f9a27a8c115c5e86";

    private MainActivity activity;

    public OpenWeatherDataProvider(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void refreshCurrentWeather(PositionPoint position) {
        try {
            final URL url = new URL(getApiURL(position));
            final Handler handler = new Handler();

            new Thread(() -> {
                HttpsURLConnection connection = null;
                try {
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(READ_TIMEOUT);
                    connection.setConnectTimeout(CONNECT_TIMEOUT);

                    if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String string = readLines(in);

                        Gson gson = new Gson();
                        final WeatherToday weatherToday = gson.fromJson(string, WeatherToday.class);

                        handler.post(() ->
                                activity.weatherUpdated(WeatherState.create(weatherToday)));
                    } else {
                        final String errorMessage = connection.getResponseMessage();
                        handler.post(() ->
                                activity.showErrorMessage(errorMessage));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getApiURL(PositionPoint position) {
        try {
            return String.format(API_CONNECTION_URL,
                URLEncoder.encode(position.getTown(), "utf-8"), API_KEY);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readLines(BufferedReader in) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null)
            sb.append(line).append("\n");

        return sb.toString();
    }
}
