package ru.gressor.weatherapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_VERBOSE = "MY_LOG";

    WeatherState currentWeather;
    DestinationPoint currentDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            currentWeather = WeatherState.generateRandom();
            currentDestination = new DestinationPoint(
                    getApplicationContext().getResources().getString(R.string.town),
                    getApplicationContext().getResources().getString(R.string.site));
        }

        logIt("protected void onCreate");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SelectTown.GET_TOWN && resultCode == RESULT_OK && data != null) {
            currentDestination = data.getParcelableExtra("destinationPoint");
            logIt("onActivityResult, requestCode: " + requestCode);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        logIt("protected void onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        logIt("protected void onResume");

        setFragmentWeatherToday();
        setFragmentForecast();
    }

    private void setFragmentWeatherToday() {
        FragmentWeatherToday fragment = (FragmentWeatherToday)
                getSupportFragmentManager().findFragmentById(R.id.fragmentWeatherToday);

        if (fragment == null
                || !currentDestination.equals(fragment.getCurrentDestination())
                || !currentWeather.equals(fragment.getCurrentWeather())) {
            fragment = FragmentWeatherToday.create(currentWeather, currentDestination);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentWeatherToday, fragment).commit();
        }
    }

    private void setFragmentForecast() {
        FragmentForecast fragment = (FragmentForecast)
                getSupportFragmentManager().findFragmentById(R.id.fragmentForecast);

        if (fragment == null || !currentDestination.equals(fragment.getCurrentDestination())) {
            fragment = FragmentForecast.create(currentDestination);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentForecast, fragment).commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        logIt("protected void onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        logIt("protected void onSaveInstanceState");

        outState.putParcelable("currentDestination", currentDestination);
        outState.putParcelable("currentWeather", currentWeather);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        logIt("protected void onRestoreInstanceState");

        currentDestination = savedInstanceState.getParcelable("currentDestination");
        currentWeather = savedInstanceState.getParcelable("currentWeather");
    }

    @Override
    protected void onStop() {
        super.onStop();

        logIt("protected void onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        logIt("protected void onDestroy");
    }

    private void logIt(String logString) {
        Log.v(LOG_VERBOSE, logString);
    }
}