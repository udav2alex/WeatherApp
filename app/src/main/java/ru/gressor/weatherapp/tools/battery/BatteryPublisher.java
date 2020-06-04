package ru.gressor.weatherapp.tools.battery;

import android.content.Intent;

public interface BatteryPublisher {
    void registerBatteryObserver(BatteryObserver batteryObserver);
    void batteryUpdated(Intent intent);
}
