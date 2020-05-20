package ru.gressor.weatherapp.ui;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.data_types.HistoryItem;
import ru.gressor.weatherapp.data_types.HistoryStorage;
import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.data_types.WeatherState;
import ru.gressor.weatherapp.ui.fragments.forecast.FragmentForecast;
import ru.gressor.weatherapp.ui.fragments.FragmentWeatherToday;
import ru.gressor.weatherapp.weather_providers.DataController;

public class MainActivity extends AppCompatActivity {

    private WeatherState weatherState;
    private PositionPoint currentPosition;
    private DataController dataController;
    private HistoryStorage historyStorage;
    private TownDrawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        dataController = new DataController(this);

        Toolbar toolbar = initToolbar();
        drawer = new TownDrawer(this, toolbar, historyStorage);

        if (savedInstanceState == null) {
            setup();
        }
    }

    private void setup() {
        historyStorage = new HistoryStorage();
        currentPosition = new PositionPoint(
                getApplicationContext().getResources().getString(R.string.town),
                getApplicationContext().getResources().getString(R.string.site));
        historyStorage.push(new HistoryItem(null, currentPosition));
        drawer.updateDrawerMenu();
        dataController.refreshWeatherState(currentPosition);
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectTownActivity.GET_TOWN && resultCode == RESULT_OK && data != null) {
            currentPosition = data.getParcelableExtra(PositionPoint.CURRENT_POSITION);
            positionUpdated();
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

    private void positionUpdated() {
        positionUpdated(currentPosition);
    }

    public void positionUpdated(PositionPoint positionPoint) {
        historyStorage.push(new HistoryItem(null, currentPosition));
        dataController.refreshWeatherState(positionPoint);
    }

    public void weatherUpdated(WeatherState weatherState) {
        this.weatherState = weatherState;
        historyStorage.push(new HistoryItem(null, currentPosition));
        drawer.updateDrawerMenu();
        showActualData();
    }

    private void setFragmentWeatherToday() {
        FragmentWeatherToday fragment = (FragmentWeatherToday)
                getSupportFragmentManager().findFragmentById(R.id.fragmentWeatherToday);

        if (fragment == null || weatherState == null
                || !currentPosition.equals(fragment.getCurrentPosition())
                || !weatherState.equals(fragment.getWeatherState())) {
            fragment = FragmentWeatherToday.create(weatherState, currentPosition);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentWeatherToday, fragment).commit();
        }
    }

    private void setFragmentForecast() {
        FragmentForecast fragment = (FragmentForecast)
                getSupportFragmentManager().findFragmentById(R.id.fragmentForecast);

        if (fragment == null || weatherState == null
                || !currentPosition.equals(fragment.getCurrentPosition())
                || !weatherState.equals(fragment.getWeatherState())) {
            fragment = FragmentForecast.create(weatherState, currentPosition);

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
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showErrorMessage(String errorMessage) {
        Intent intent = new Intent(getApplicationContext(), ErrorMessageActivity.class);
        intent.putExtra(ErrorMessageActivity.ERROR_MESSAGE, errorMessage);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(PositionPoint.CURRENT_POSITION, currentPosition);
        outState.putParcelable(WeatherState.WEATHER_STATE, weatherState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        currentPosition = savedInstanceState.getParcelable(PositionPoint.CURRENT_POSITION);
        weatherState = savedInstanceState.getParcelable(WeatherState.WEATHER_STATE);
    }
}