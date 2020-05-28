package ru.gressor.weatherapp.weather_providers;

import java.net.HttpURLConnection;

import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.data_types.WeatherState;
import ru.gressor.weatherapp.ui.MainActivity;
import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.weather_providers.openweather.retro.OpenWeatherRetrofitDataProvider;

public class DataController {
    private MainActivity activity;
    private OpenWeatherRetrofitDataProvider provider;

    public DataController(MainActivity activity) {
        this.activity = activity;
        this.provider = new OpenWeatherRetrofitDataProvider(this);
    }

    public void refreshWeatherState(PositionPoint position) {
        provider.getWeatherAndForecasts(position);
    }

    public void updateWeather(WeatherState weatherState) {
        activity.weatherUpdated(weatherState);
    }

    public void updateWeatherFailure(Throwable throwable) {
        if (throwable instanceof HttpWeatherError) {
            HttpWeatherError e = (HttpWeatherError) throwable;

            if (e.getHttpCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                showMessage(
                        activity.getResources().getString(R.string.provider_message_prescription),
                        activity.getResources().getString(R.string.provider_message_not_found));
            } else if (e.getHttpCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                showMessage(
                        activity.getResources().getString(R.string.provider_message_prescription),
                        activity.getResources().getString(R.string.provider_message_unauthorized));
            } else {
                showMessage(
                        activity.getResources().getString(R.string.provider_message_prescription),
                        e.getMessage());
            }
        } else {
            throwable.printStackTrace();
            showMessage(
                    activity.getResources().getString(R.string.provider_message_internal_error),
                    throwable.getMessage());
        }
    }

    private void showMessage(String preface, String message) {
        final String errorMessage = preface + "\n\n" + message;
        activity.showErrorMessage(errorMessage);
    }
}