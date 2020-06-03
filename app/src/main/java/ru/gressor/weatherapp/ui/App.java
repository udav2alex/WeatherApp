package ru.gressor.weatherapp.ui;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import ru.gressor.weatherapp.battery_tools.BatteryObserver;
import ru.gressor.weatherapp.battery_tools.BatteryPublisher;
import ru.gressor.weatherapp.battery_tools.BatteryPublisherProvider;
import ru.gressor.weatherapp.db.WeatherDao;
import ru.gressor.weatherapp.db.WeatherDatabase;
import ru.gressor.weatherapp.battery_tools.BatteryBroadcastReceiver;

public class App extends Application implements BatteryPublisher, BatteryPublisherProvider {
    private static App instance;
    private WeatherDatabase db;
    private List<BatteryObserver> batteryObservers = new ArrayList<>();

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        db = Room.databaseBuilder(getApplicationContext(),
                WeatherDatabase.class, "weatherDB")
                .allowMainThreadQueries()
                .build();

        registerBatteryBroadcastReceiver();
    }

    private void registerBatteryBroadcastReceiver() {
        registerBatteryBroadcastReceiver("android.intent.action.ACTION_POWER_CONNECTED");
        registerBatteryBroadcastReceiver("android.intent.action.ACTION_POWER_DISCONNECTED");
        registerBatteryBroadcastReceiver("android.intent.action.BATTERY_LOW");
        registerBatteryBroadcastReceiver("android.intent.action.BATTERY_OKAY");
    }

    private void registerBatteryBroadcastReceiver(String actionName) {
        IntentFilter intentFilter = new IntentFilter(actionName);
        this.registerReceiver(new BatteryBroadcastReceiver(), intentFilter);
    }

    public WeatherDao getWeatherDao() {
        return db.getWeatherDao();
    }

    @Override
    public void registerBatteryObserver(BatteryObserver batteryObserver) {
        batteryObservers.add(batteryObserver);
    }

    @Override
    public void batteryUpdated(Intent intent) {
        for (int i = batteryObservers.size() - 1; i >= 0; i--) {
            BatteryObserver batteryObserver = batteryObservers.get(i);
            if (batteryObserver == null) {
                batteryObservers.remove(i);
                continue;
            }
            batteryObserver.batteryUpdated(intent);
        }
    }

    @Override
    public BatteryPublisher getBatteryPublisher() {
        return this;
    }
}
