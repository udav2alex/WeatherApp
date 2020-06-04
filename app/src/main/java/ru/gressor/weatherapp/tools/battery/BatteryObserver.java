package ru.gressor.weatherapp.tools.battery;

import android.content.Intent;

public interface BatteryObserver {
    void batteryUpdated(Intent intent);
}
