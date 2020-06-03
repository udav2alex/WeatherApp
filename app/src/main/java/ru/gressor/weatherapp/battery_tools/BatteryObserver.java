package ru.gressor.weatherapp.battery_tools;

import android.content.Intent;

public interface BatteryObserver {
    void batteryUpdated(Intent intent);
}
