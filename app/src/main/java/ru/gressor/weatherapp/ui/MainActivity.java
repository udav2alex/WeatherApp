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
import ru.gressor.weatherapp.ui.fragments.today.FragmentWeatherToday;
import ru.gressor.weatherapp.weather_providers.DataController;

public class MainActivity extends AppCompatActivity {

    private HistoryStorage historyStorage;
    private DataController dataController;
    private TownDrawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        dataController = new DataController(this);

        if (savedInstanceState == null) {
            setup();
        } else {
            historyStorage = savedInstanceState.getParcelable(HistoryStorage.HISTORY_STORAGE);
        }

        Toolbar toolbar = initToolbar();
        drawer = new TownDrawer(this, toolbar, historyStorage);
    }

    private void setup() {
        historyStorage = new HistoryStorage();
        dataController.refreshWeatherState(historyStorage.getCurrentPosition());
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == TownDrawer.PERMISSION_REQUEST_CODE) {
            drawer.permissionsGranted();
        }
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
            positionUpdated(data.getParcelableExtra(PositionPoint.CURRENT_POSITION));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showActualData();
    }

    private void showActualData() {
        drawer.updateDrawerMenu();
        setFragmentWeatherToday();
        setFragmentForecast();
    }

    public void positionUpdated(PositionPoint positionPoint) {
        historyStorage.push(new HistoryItem(null, positionPoint));
        dataController.refreshWeatherState(positionPoint);
        showActualData();
    }

    public void weatherUpdated(WeatherState weatherState) {
        historyStorage.push(new HistoryItem(weatherState, getCurrentPosition()));
        showActualData();
    }

    private void setFragmentWeatherToday() {
        FragmentWeatherToday fragment = (FragmentWeatherToday)
                getSupportFragmentManager().findFragmentById(R.id.fragmentWeatherToday);

        if (fragment == null || getCurrentWeather() == null
                || !getCurrentPosition().equals(fragment.getPositionPoint())
                || !getCurrentWeather().equals(fragment.getWeatherState())) {
            fragment = FragmentWeatherToday.create(getCurrentWeather(), getCurrentPosition());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentWeatherToday, fragment).commit();
        }
    }

    private void setFragmentForecast() {
        FragmentForecast fragment = (FragmentForecast)
                getSupportFragmentManager().findFragmentById(R.id.fragmentForecast);

        if (fragment == null || getCurrentWeather() == null
                || !getCurrentPosition().equals(fragment.getPositionPoint())
                || !getCurrentWeather().equals(fragment.getWeatherState())) {
            fragment = FragmentForecast.create(getCurrentWeather(), getCurrentPosition());

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

    public void showMessage(String preface, String message) {
        final String errorMessage = preface + "\n\n" + message;
        showErrorMessage(errorMessage);
    }

    public void showErrorMessage(String errorMessage) {
        Intent intent = new Intent(getApplicationContext(), ErrorMessageActivity.class);
        intent.putExtra(ErrorMessageActivity.ERROR_MESSAGE, errorMessage);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(HistoryStorage.HISTORY_STORAGE, historyStorage);
    }

    private WeatherState getCurrentWeather() {
        return historyStorage.getCurrentWeather();
    }

    private PositionPoint getCurrentPosition() {
        return historyStorage.getCurrentPosition();
    }
}