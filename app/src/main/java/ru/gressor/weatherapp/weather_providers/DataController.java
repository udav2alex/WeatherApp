package ru.gressor.weatherapp.weather_providers;

import android.os.Handler;

import java.io.IOException;
import java.net.HttpURLConnection;

import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.activities.MainActivity;
import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.data_types.WeatherState;

public class DataController {
    private MainActivity activity;
    private DataAdapter dataAdapter = new OpenWeatherDataAdapter();

    public DataController(MainActivity activity) {
        this.activity = activity;
    }

    public void refreshWeatherState(PositionPoint position) {
        Handler handler = new Handler();

        new Thread(() -> {
            try {
                final WeatherState weatherState = dataAdapter.getWeatherState(position);
                handler.post(() -> activity.weatherUpdated(weatherState));

            } catch (HttpWeatherError e) {
                if (e.getHttpCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                    showMessage(handler,
                            activity.getResources().getString(R.string.provider_message_prescription),
                            activity.getResources().getString(R.string.provider_message_not_found));
                } else {
                    showMessage(handler,
                            activity.getResources().getString(R.string.provider_message_prescription),
                            e.getMessage());
                }
            } catch (IOException e) {
                showMessage(handler,
                        activity.getResources().getString(R.string.message_connection_error),
                        e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private void showMessage(Handler handler, String preface, String message) {
        final String errorMessage = preface + "\n\n" + message;
        handler.post(() -> activity.showErrorMessage(errorMessage));
    }
}