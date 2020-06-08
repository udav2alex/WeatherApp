package ru.gressor.weatherapp.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.data_types.HistoryItem;
import ru.gressor.weatherapp.data_types.HistoryStorage;
import ru.gressor.weatherapp.tools.location.LocationProvider;

public class TownDrawer
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int PERMISSION_REQUEST_CODE = 10;
    private static final int TOWN_LIST_OFFSET = 10000;

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private MainActivity activity;
    private HistoryStorage historyStorage;
    private Menu drawerMenu;
    private LocationProvider locationProvider;

    public TownDrawer(MainActivity activity, Toolbar toolbar, HistoryStorage historyStorage) {
        this.drawer = activity.findViewById(R.id.drawer_layout);

        this.activity = activity;
        this.toolbar = toolbar;
        this.historyStorage = historyStorage;
        this.init();
    }

    public void init() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = activity.findViewById(R.id.drawer_navigation);
        navigationView.setNavigationItemSelectedListener(this);
        drawerMenu = navigationView.getMenu();
        updateDrawerMenu();
    }

    private void updateFirstDrawerMenuHistoryItem(HistoryItem historyItem) {
        MenuItem menuItem = drawerMenu.findItem(R.id.menu_drawer_add_favorite);

        if (historyItem.getPositionPoint().getTown() == null) {
            menuItem.setTitle(R.string.current_location);
            menuItem.setIcon(null);
        } else {
            menuItem.setTitle((historyItem.isFavorite() ?
                    activity.getResources().getString(R.string.remove_favorite)
                    : activity.getResources().getString(R.string.add_favorite))
                    + " : " + historyItem.getPositionPoint().getTown());
            menuItem.setIcon(getHistoryIcon(historyItem.isFavorite()));
        }
    }

    public void updateDrawerMenu() {
        List<HistoryItem> historyItems = historyStorage.getItemsList();
        updateFirstDrawerMenuHistoryItem(historyItems.get(0));

        for (int i = 1; i < historyItems.size(); i++) {
            MenuItem menuItem = drawerMenu.findItem(TOWN_LIST_OFFSET + i);

            if (menuItem != null) {
                menuItem.setTitle(historyItems.get(i).getPositionPoint().getTown());
                menuItem.setVisible(true);
            } else {
                drawerMenu.add(R.id.menu_group_history, TOWN_LIST_OFFSET + i, i * 10,
                        historyItems.get(i).getPositionPoint().getTown());

                menuItem = drawerMenu.findItem(TOWN_LIST_OFFSET + i);
            }

            menuItem.setIcon(getHistoryIcon(historyItems.get(i).isFavorite()));
        }

        clearDrawer();
    }

    private void clearDrawer() {
        int size = historyStorage.size();
        for (int i = size; i < HistoryStorage.MAX_SIZE; i++) {
            MenuItem menuItem = drawerMenu.findItem(TOWN_LIST_OFFSET + i);
            if (menuItem != null) {
                menuItem.setVisible(false);
            } else {
                break;
            }
        }
    }

    private int getHistoryIcon(boolean isFavorite) {
        return isFavorite ? android.R.drawable.star_big_on : android.R.drawable.star_off;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_drawer_about) {
            Intent intent = new Intent(activity, AboutActivity.class);
            activity.startActivity(intent);

        } else if (id == R.id.menu_drawer_current_location) {
            getCurrentLocation();

        } else if (id == R.id.menu_drawer_clear) {
            callClearDialog();

        } else if (id == R.id.menu_drawer_select) {
            Intent intent = new Intent(activity, SelectTownActivity.class);
            activity.startActivityForResult(intent, SelectTownActivity.GET_TOWN);

        } else if (item.getItemId() == R.id.menu_drawer_add_favorite) {
            List<HistoryItem> historyItems = historyStorage.getItemsList();
            historyItems.get(0).setFavorite(!historyItems.get(0).isFavorite());
            historyStorage.invalidate();
            updateDrawerMenu();
            return true;

        } else if (item.getGroupId() == R.id.menu_group_history) {
            List<HistoryItem> historyItems = historyStorage.getItemsList();
            activity.positionUpdated(historyItems.get(item.getItemId() - 10000).getPositionPoint());
            return true;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getCurrentLocation() {
        if (locationProvider == null) {
            locationProvider = new LocationProvider(activity);
        }

        locationProvider.requestCurrentLocation();
    }

    public void permissionsGranted() {
        getCurrentLocation();
    }

    private void callClearDialog() {
        List<HistoryItem> historyItems = historyStorage.getItemsList();
        if (historyItems.size() == 1) {
            showSnackbar(R.string.clear_dialog_skip);
            return;
        }

        final String[] towns = new String[historyItems.size() - 1];
        for (int i = 1; i < historyItems.size(); i++) {
            towns[i - 1] = historyItems.get(i).getPositionPoint().getTown();
        }

        final boolean[] chosen = new boolean[towns.length];

        new AlertDialog.Builder(activity)
                .setTitle(R.string.clear_dialog_title)
                .setMultiChoiceItems(towns, chosen, (dialogInterface, i, b) -> chosen[i] = b)
                .setNegativeButton(android.R.string.cancel,
                        (dialogInterface, i) -> showSnackbar(R.string.clear_dialog_cancel_message))
                .setPositiveButton(R.string.clear_dialog_clear_selected, (dialogInterface, i) -> {
                    for (int index = 0; index < chosen.length; index++) {
                        if (!chosen[index]) towns[index] = null;
                    }
                    historyStorage.clear(towns);
                    updateDrawerMenu();
                })
                .setNeutralButton(R.string.clear_dialog_clear_all, (dialogInterface, i) -> {
                    historyStorage.clear(towns);
                    updateDrawerMenu();
                }).create().show();
    }

    private void showSnackbar(int textId) {
        Snackbar.make(activity.findViewById(R.id.fragmentForecast), textId, Snackbar.LENGTH_SHORT).show();
    }

    public boolean isDrawerOpen(int gravity) {
        return drawer.isDrawerOpen(gravity);
    }

    public void closeDrawer(int gravity) {
        drawer.closeDrawer(gravity);
    }
}