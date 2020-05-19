package ru.gressor.weatherapp.ui;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.data_types.HistoryItem;
import ru.gressor.weatherapp.data_types.HistoryStorage;
import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.data_types.WeatherState;
import ru.gressor.weatherapp.ui.fragments.forecast.FragmentForecast;
import ru.gressor.weatherapp.ui.fragments.FragmentWeatherToday;
import ru.gressor.weatherapp.weather_providers.DataController;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private WeatherState weatherState;
    private PositionPoint currentPosition;
    private DataController dataController;
    private HistoryStorage historyStorage;
    private Menu drawerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        dataController = new DataController(this);
        historyStorage = new HistoryStorage();

        if (savedInstanceState == null) {
            currentPosition = new PositionPoint(
                    getApplicationContext().getResources().getString(R.string.town),
                    getApplicationContext().getResources().getString(R.string.site));
            historyStorage.push(new HistoryItem(null, currentPosition));
            setupDrawerMenu();
            dataController.refreshWeatherState(currentPosition);
        }
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.drawer_navigation);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        drawerMenu = navigationView.getMenu();
    }

    private void setupDrawerMenu() {
        List<HistoryItem> historyItems = historyStorage.getItemsList();
        setupFirstDrawerMenuHistoryItem(historyItems.get(0));

        for (int i = 1; i < historyItems.size(); i++) {
            MenuItem menuItem;
            menuItem = drawerMenu.findItem(10000 + i);

            if (menuItem != null) {
                menuItem.setTitle(historyItems.get(i).getPositionPoint().getTown());
            } else {
                drawerMenu.add(R.id.menu_group_history, 10000 + i, i * 10,
                        historyItems.get(i).getPositionPoint().getTown());

                menuItem = drawerMenu.findItem(10000 + i);
            }

            menuItem.setIcon(getHistoryIcon(historyItems.get(i).isFavorite()));
        }
    }

    private void setupFirstDrawerMenuHistoryItem(HistoryItem historyItem) {
        MenuItem menuItem = drawerMenu.findItem(R.id.menu_drawer_add_favorite);

        menuItem.setTitle((historyItem.isFavorite() ?
                getResources().getString(R.string.remove_favorite)
                : getResources().getString(R.string.add_favorite))
                + " : " + historyItem.getPositionPoint().getTown());
        menuItem.setIcon(getHistoryIcon(historyItem.isFavorite()));
    }

    private int getHistoryIcon(boolean isFavorite) {
        return isFavorite ? android.R.drawable.star_big_on : android.R.drawable.star_off;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_drawer_about) {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_drawer_select) {
            Intent intent = new Intent(getApplicationContext(), SelectTownActivity.class);
            startActivityForResult(intent, SelectTownActivity.GET_TOWN);
        } else if (item.getItemId() == R.id.menu_drawer_add_favorite) {
            List<HistoryItem> historyItems = historyStorage.getItemsList();
            historyItems.get(0).setFavorite(!historyItems.get(0).isFavorite());
            setupDrawerMenu();
            return true;
        } else if (item.getGroupId() == R.id.menu_group_history) {
            List<HistoryItem> historyItems = historyStorage.getItemsList();
            currentPosition = historyItems.get(item.getItemId() - 10000).getPositionPoint();
            positionUpdated();
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        historyStorage.push(new HistoryItem(null, currentPosition));
        dataController.refreshWeatherState(currentPosition);
    }

    public void weatherUpdated(WeatherState weatherState) {
        this.weatherState = weatherState;
        historyStorage.push(new HistoryItem(null, currentPosition));
        setupDrawerMenu();
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