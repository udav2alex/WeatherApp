package ru.gressor.weatherapp.ui;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import ru.gressor.weatherapp.tools.battery.BatteryPublisher;
import ru.gressor.weatherapp.tools.battery.BatteryPublisherProvider;
import ru.gressor.weatherapp.db.WeatherDao;
import ru.gressor.weatherapp.db.WeatherDatabase;
import ru.gressor.weatherapp.tools.battery.BatteryBroadcastReceiver;
import ru.gressor.weatherapp.tools.FirebaseService;
import ru.gressor.weatherapp.tools.battery.MyBatteryPublisher;
import ru.gressor.weatherapp.tools.connectivity.ConnectivityBroadcastReceiver;
import ru.gressor.weatherapp.tools.connectivity.ConnectivityPublisher;
import ru.gressor.weatherapp.tools.connectivity.ConnectivityPublisherProvider;
import ru.gressor.weatherapp.tools.connectivity.MyConnectivityPublisher;

public class App extends Application
        implements BatteryPublisherProvider, ConnectivityPublisherProvider {
    private static App instance;
    private WeatherDatabase db;
    private BatteryPublisher batteryPublisher = new MyBatteryPublisher();
    private ConnectivityPublisher connectivityPublisher = new MyConnectivityPublisher();

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        db = Room.databaseBuilder(getApplicationContext(),
                WeatherDatabase.class, "weatherDB")
                .allowMainThreadQueries()
                .build();

        initNotificationChannel();
        registerBatteryBroadcastReceiver();
        registerConnectivityBroadcastReceiver();
//        writeFirebaseTokenToLog();
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(
                    FirebaseService.CHANNEL_ID, "name",
                    NotificationManager.IMPORTANCE_LOW);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void registerBatteryBroadcastReceiver() {
        registerBatteryBroadcastReceiver("android.intent.action.ACTION_POWER_CONNECTED");
        registerBatteryBroadcastReceiver("android.intent.action.ACTION_POWER_DISCONNECTED");
        registerBatteryBroadcastReceiver("android.intent.action.BATTERY_LOW");
        registerBatteryBroadcastReceiver("android.intent.action.BATTERY_OKAY");
    }

    private void registerBatteryBroadcastReceiver(String actionName) {
        IntentFilter intentFilter = new IntentFilter(actionName);
        this.registerReceiver(new BatteryBroadcastReceiver(), intentFilter);
    }

    private void registerConnectivityBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(new ConnectivityBroadcastReceiver(), intentFilter);
    }

    public WeatherDao getWeatherDao() {
        return db.getWeatherDao();
    }

    @Override
    public BatteryPublisher getBatteryPublisher() {
        return batteryPublisher;
    }

    @Override
    public ConnectivityPublisher getConnectivityPublisher() {
        return connectivityPublisher;
    }

    private void writeFirebaseTokenToLog() {
        String TAG = "MyPushToken";
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    if (task.getResult() == null) {
                        Log.w(TAG, "getInstanceId result is null");
                        return;
                    }

                    String token = task.getResult().getToken();
                    Log.d(TAG, "Token: " + token);
                });
    }

}
