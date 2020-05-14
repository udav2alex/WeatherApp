package ru.gressor.weatherapp.weather_providers;

import android.os.Handler;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import ru.gressor.weatherapp.activities.MainActivity;
import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.data_types.WeatherState;
import ru.gressor.weatherapp.data_types.openweather_current_weather.CurrentWeather;

import ru.gressor.weatherapp.R;

public class OpenWeatherDataProvider implements DataProvider {
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 10000;
    private static final String API_CONNECTION_URL =
            "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s&lang="
            + Locale.getDefault().getLanguage();
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
                    connection = createConnection(url);

                    if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                        final CurrentWeather currentWeather = getWeather(connection);
                        handler.post(() ->
                                activity.weatherUpdated(WeatherState.create(currentWeather)));
                    } else if (connection.getResponseCode() == HttpsURLConnection.HTTP_NOT_FOUND) {
                        showMessage(handler,
                                activity.getResources().getString(R.string.provider_message_prescription),
                                activity.getResources().getString(R.string.provider_message_not_found));
                    } else {
                        showMessage(handler,
                                activity.getResources().getString(R.string.provider_message_prescription),
                                connection.getResponseMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    showMessage(handler,
                            activity.getResources().getString(R.string.message_connection_error),
                            e.getMessage());
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

    private HttpsURLConnection createConnection(URL url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        return connection;
    }

    private CurrentWeather getWeather(HttpsURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String string = readLines(in);
        Gson gson = new Gson();

        return gson.fromJson(string, CurrentWeather.class);
    }

    private void showMessage(Handler handler, String preface, String message) {
        final String errorMessage = preface + "\n\n" + message;
        handler.post(() -> activity.showErrorMessage(errorMessage));
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
