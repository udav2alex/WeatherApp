package ru.gressor.weatherapp.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.data_types.HistoryItem;
import ru.gressor.weatherapp.data_types.HistoryStorage;

public class TownDrawer
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private MainActivity activity;
    private HistoryStorage historyStorage;
    private Menu drawerMenu;

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

        menuItem.setTitle((historyItem.isFavorite() ?
                activity.getResources().getString(R.string.remove_favorite)
                : activity.getResources().getString(R.string.add_favorite))
                + " : " + historyItem.getPositionPoint().getTown());
        menuItem.setIcon(getHistoryIcon(historyItem.isFavorite()));
    }

    public void updateDrawerMenu() {
        List<HistoryItem> historyItems = historyStorage.getItemsList();
        updateFirstDrawerMenuHistoryItem(historyItems.get(0));

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

    private int getHistoryIcon(boolean isFavorite) {
        return isFavorite ? android.R.drawable.star_big_on : android.R.drawable.star_off;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_drawer_about) {
            Intent intent = new Intent(activity, AboutActivity.class);
            activity.startActivity(intent);
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

    public boolean isDrawerOpen(int gravity) {
        return drawer.isDrawerOpen(gravity);
    }

    public void closeDrawer(int gravity) {
        drawer.closeDrawer(gravity);
    }
}