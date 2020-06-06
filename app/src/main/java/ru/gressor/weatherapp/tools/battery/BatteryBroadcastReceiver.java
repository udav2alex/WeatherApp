package ru.gressor.weatherapp.tools.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.gressor.weatherapp.ui.App;

public class BatteryBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ((BatteryPublisherProvider)App.getInstance()).getBatteryPublisher().batteryUpdated(intent);
    }
}
