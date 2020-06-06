package ru.gressor.weatherapp.tools.connectivity;

import android.content.Intent;

public interface ConnectivityPublisher {
    void registerConnectivityObserver(ConnectivityObserver connectivityObserver);
    void connectivityUpdated(Intent intent);
}
