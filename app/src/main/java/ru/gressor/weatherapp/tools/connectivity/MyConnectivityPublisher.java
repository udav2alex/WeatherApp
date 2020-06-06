package ru.gressor.weatherapp.tools.connectivity;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import ru.gressor.weatherapp.tools.battery.BatteryObserver;
import ru.gressor.weatherapp.tools.battery.BatteryPublisher;

public class MyConnectivityPublisher implements ConnectivityPublisher {
    private List<ConnectivityObserver> connectivityObservers = new ArrayList<>();

    @Override
    public void registerConnectivityObserver(ConnectivityObserver connectivityObserver) {
        connectivityObservers.add(connectivityObserver);
    }

    @Override
    public void connectivityUpdated(Intent intent) {
        for (int i = connectivityObservers.size() - 1; i >= 0; i--) {
            ConnectivityObserver connectivityObserver = connectivityObservers.get(i);
            if (connectivityObserver == null) {
                connectivityObservers.remove(i);
                continue;
            }
            connectivityObserver.connectivityUpdated(intent);
        }
    }
}
