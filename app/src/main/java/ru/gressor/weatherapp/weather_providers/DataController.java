package ru.gressor.weatherapp.weather_providers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
        if (isOnline(activity)) {
            provider.getWeatherAndForecasts(position);
        }
    }

    public void updateWeather(WeatherState weatherState) {
        activity.weatherUpdated(weatherState);
    }

    public void updateWeatherFailure(Throwable throwable) {
        if (throwable instanceof HttpWeatherError) {
            HttpWeatherError e = (HttpWeatherError) throwable;

            if (e.getHttpCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                activity.showMessage(
                        activity.getResources().getString(R.string.provider_message_prescription),
                        activity.getResources().getString(R.string.provider_message_not_found));
            } else if (e.getHttpCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                activity.showMessage(
                        activity.getResources().getString(R.string.provider_message_prescription),
                        activity.getResources().getString(R.string.provider_message_unauthorized));
            } else {
                activity.showMessage(
                        activity.getResources().getString(R.string.provider_message_prescription),
                        e.getMessage());
            }
        } else {
            throwable.printStackTrace();
            activity.showMessage(
                    activity.getResources().getString(R.string.provider_message_internal_error),
                    throwable.getMessage());
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}