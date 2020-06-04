package ru.gressor.weatherapp.tools.battery;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class MyBatteryPublisher implements BatteryPublisher {
    private List<BatteryObserver> batteryObservers = new ArrayList<>();

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
}
