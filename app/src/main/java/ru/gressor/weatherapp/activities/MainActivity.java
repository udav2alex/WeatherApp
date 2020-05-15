package ru.gressor.weatherapp.activities;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.data_types.local_dto.PositionPoint;
import ru.gressor.weatherapp.data_types.local_dto.CurrentWeather;
import ru.gressor.weatherapp.fragments.forecast.FragmentForecast;
import ru.gressor.weatherapp.fragments.FragmentWeatherToday;
import ru.gressor.weatherapp.weather_providers.DataProvider;
import ru.gressor.weatherapp.weather_providers.OpenWeatherDataProvider;

public class MainActivity extends AppCompatActivity {
    CurrentWeather currentWeather;
    PositionPoint currentPosition;
    DataProvider dataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        dataProvider = new OpenWeatherDataProvider(this);

        if (savedInstanceState == null) {
            currentPosition = new PositionPoint(
                    getApplicationContext().getResources().getString(R.string.town),
                    getApplicationContext().getResources().getString(R.string.site));
            dataProvider.refreshCurrentWeather(currentPosition);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectTownActivity.GET_TOWN && resultCode == RESULT_OK && data != null) {
            currentPosition = data.getParcelableExtra(PositionPoint.CURRENT_POSITION);
            dataProvider.refreshCurrentWeather(currentPosition);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showActualData();
    }

    private void showActualData() {
        setFragmentWeatherToday();
        setFragmentForecast();
    }

    public void weatherUpdated(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
        showActualData();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showErrorMessage(String errorMessage) {
        Intent intent = new Intent(this, ErrorMessageActivity.class);
        intent.putExtra(ErrorMessageActivity.ERROR_MESSAGE, errorMessage);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(PositionPoint.CURRENT_POSITION, currentPosition);
        outState.putParcelable(CurrentWeather.CURRENT_WEATHER, currentWeather);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        currentPosition = savedInstanceState.getParcelable(PositionPoint.CURRENT_POSITION);
        currentWeather = savedInstanceState.getParcelable(CurrentWeather.CURRENT_WEATHER);
    }
}