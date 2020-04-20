package ru.gressor.weatherapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_VERBOSE = "MY_LOG";
    private TScale tScale = TScale.CELSIUS;

    private TextView textViewTown;
    private TextView textViewSite;
    private TextView textViewCurrentTemperature;
    private TextView textViewFeelsLike;
    private TextView textViewWindSpeed;
    private TextView textViewPressureValue;
    private TextView textViewHumidityValue;
    private TextView textViewTempNow;
    private TextView textViewTempNext3H;
    private TextView textViewTempNext6H;

    WeatherState currentWeather;
    DestinationPoint currentDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logIt("protected void onCreate");

        if (savedInstanceState == null) {
            currentWeather = getCurrentWeather();
        }

        init();
    }

    private void init() {
        textViewTown = findViewById(R.id.textViewTown);
        textViewSite = findViewById(R.id.textViewSite);
        textViewCurrentTemperature = findViewById(R.id.currentTemperature);
        textViewFeelsLike = findViewById(R.id.feelsLike);
        textViewWindSpeed = findViewById(R.id.windSpeed);
        textViewPressureValue = findViewById(R.id.pressureValue);
        textViewHumidityValue = findViewById(R.id.humidityValue);
        textViewTempNow = findViewById(R.id.tempNow);
        textViewTempNext3H = findViewById(R.id.tempNext3H);
        textViewTempNext6H = findViewById(R.id.tempNext6H);

        textViewTown.setOnClickListener(textViewTownOnClickListener);
        findViewById(R.id.linkToSite).setOnClickListener(textLinkToSiteOnClickListener);

        currentDestination = new DestinationPoint(
                getApplicationContext().getString(R.string.town),
                getApplicationContext().getString(R.string.site));
    }

    private void populateNow() {
        Context context = getApplicationContext();

        textViewTown.setText(currentDestination.getTown());
        textViewSite.setText(currentDestination.getSite());

        textViewCurrentTemperature.setText(fromCelsius(
                currentWeather.getTemperature()));
        textViewFeelsLike.setText(context.getString(R.string.feels_like, fromCelsius(
                currentWeather.getTempFeelsLike())));
        textViewWindSpeed.setText(context.getString(R.string.windSpeed,
                currentWeather.getWindSpeed()));
        textViewPressureValue.setText(context.getString(R.string.pressureValue,
                currentWeather.getPressure()));
        textViewHumidityValue.setText(context.getString(R.string.humidityValue,
                currentWeather.getHumidity()));
        textViewTempNow.setText(fromCelsius(
                currentWeather.getTemperature()));
    }

    private void populateToday() {
        Context context = getApplicationContext();

        textViewTempNext3H.setText(fromCelsius(7));
        textViewTempNext6H.setText(fromCelsius(5));
    }

    private void populateForecast() {
        Context context = getApplicationContext();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SelectTown.GET_TOWN && resultCode == RESULT_OK && data != null) {
            currentDestination = data.getParcelableExtra("destinationPoint");
            townChanged();
        }
    }

    private View.OnClickListener textViewTownOnClickListener = (v) -> {
        Intent intent = new Intent(this, SelectTown.class);
        startActivityForResult(intent, SelectTown.GET_TOWN);
    };

    private View.OnClickListener textLinkToSiteOnClickListener = (v) -> {
//        ACTION_VIEW
        logIt("textLinkToSiteOnClickListener");

        String query = String.format("%s %s",
                getResources().getString(R.string.weather),
                currentDestination.getTown());
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);

        startActivity(intent);
    };

    private void townChanged() {
        textViewTown.setText(currentDestination.getTown());
        textViewSite.setText(currentDestination.getSite());
    }

    private WeatherState getCurrentWeather() {
        logIt("getCurrentWeather");
        return WeatherState.generateRandom();
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

        populateNow();
        populateToday();
        populateForecast();
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
        populateNow();
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
//        Toast.makeText(getApplicationContext(), logString, Toast.LENGTH_SHORT).show();
        Log.v(LOG_VERBOSE, logString);
    }

    private String fromCelsius(int value) {
        String result = tScale.fromCelsius(value);

        if (result == null) {
            return getApplicationContext().getString(R.string.error_unknown_scale);
        } else {
            return result;
        }
    }

    public enum TScale {
        CELSIUS, FAHRENHEIT;

        public String fromCelsius(int value) {
            if (this == CELSIUS) {
                return (value < 0 ? "–" : "+") + value + " ℃";
            }

            if (this == FAHRENHEIT) {
                int converted = (int)((value - 32)/1.8);
                return  (converted < 0 ? "–" : "+") + converted + " ℉";
            }

            return null;
        }
    }
}
