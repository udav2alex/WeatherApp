package ru.gressor.weatherapp.tools.firebase;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ru.gressor.weatherapp.R;

public class FirebaseService extends FirebaseMessagingService {
    public static final String CHANNEL_ID = "2";
    private static final String TAG = "MyPushToken";
    private int messageId = 0;

    public FirebaseService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String body = null;
        if (remoteMessage.getNotification() != null) {
            body = remoteMessage.getNotification().getBody();
        }
        if (body == null) {
            body = "Message body";
        }
        Log.d("PushMessage", body);

        String title = remoteMessage.getNotification().getTitle();
        if (title == null){
            title = "Push Message";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(messageId++, builder.build());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
    }
}