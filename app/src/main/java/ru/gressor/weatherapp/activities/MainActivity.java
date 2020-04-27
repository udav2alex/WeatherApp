package ru.gressor.weatherapp.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.data_types.WeatherState;
import ru.gressor.weatherapp.fragments.forecast.FragmentForecast;
import ru.gressor.weatherapp.fragments.FragmentWeatherToday;

public class MainActivity extends AppCompatActivity {
    WeatherState currentWeather;
    PositionPoint currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            currentWeather = WeatherState.generateRandom();
            currentPosition = new PositionPoint(
                    getApplicationContext().getResources().getString(R.string.town),
                    getApplicationContext().getResources().getString(R.string.site));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SelectTownActivity.GET_TOWN && resultCode == RESULT_OK && data != null) {
            currentPosition = data.getParcelableExtra(PositionPoint.CURRENT_POSITION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setFragmentWeatherToday();
        setFragmentForecast();
    }

    private void setFragmentWeatherToday() {
        FragmentWeatherToday fragment = (FragmentWeatherToday)
                getSupportFragmentManager().findFragmentById(R.id.fragmentWeatherToday);

        if (fragment == null
                || !currentPosition.equals(fragment.getCurrentPosition())
                || !currentWeather.equals(fragment.getCurrentWeather())) {
            fragment = FragmentWeatherToday.create(currentWeather, currentPosition);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentWeatherToday, fragment).commit();
        }
    }

    private void setFragmentForecast() {
        FragmentForecast fragment = (FragmentForecast)
                getSupportFragmentManager().findFragmentById(R.id.fragmentForecast);

        if (fragment == null || !currentPosition.equals(fragment.getCurrentPosition())) {
            fragment = FragmentForecast.create(currentPosition);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentForecast, fragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(PositionPoint.CURRENT_POSITION, currentPosition);
        outState.putParcelable(WeatherState.CURRENT_WEATHER, currentWeather);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        currentPosition = savedInstanceState.getParcelable(PositionPoint.CURRENT_POSITION);
        currentWeather = savedInstanceState.getParcelable(WeatherState.CURRENT_WEATHER);
    }
}