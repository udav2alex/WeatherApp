package ru.gressor.weatherapp.tools.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.gressor.weatherapp.ui.App;

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
        ((ConnectivityPublisherProvider) App.getInstance())
                .getConnectivityPublisher().connectivityUpdated(intent);
    }
}
