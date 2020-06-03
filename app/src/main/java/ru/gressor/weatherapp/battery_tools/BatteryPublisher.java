package ru.gressor.weatherapp.battery_tools;

import android.content.Intent;

public interface BatteryPublisher {
    void registerBatteryObserver(BatteryObserver batteryObserver);
    void batteryUpdated(Intent intent);
}
