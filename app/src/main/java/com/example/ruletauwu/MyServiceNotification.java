package com.example.ruletauwu;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyServiceNotification extends Service {

    private static final String CHANNEL_ID = "my_service_channel";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        createNotificationChannel(nManager);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)

                .setContentTitle("MyService")
                .setContentText("Terminó el servicio!")
                .setAutoCancel(true);

        Intent targetIntent = new Intent(this, BaterryLow.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        nManager.notify(12345, builder.build());
//metodo para detener este servicio
        stopSelf();

        return START_NOT_STICKY;
    }

    private void createNotificationChannel(NotificationManager nManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "My Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Canal para MyServiceNotification");
            nManager.createNotificationChannel(channel);
        }
    }
}
