package ru.gressor.weatherapp.tools.connectivity;

import android.content.Intent;

public interface ConnectivityObserver {
    void connectivityUpdated(Intent intent);
}
