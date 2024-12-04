package com.example.ruletauwu;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyServiceNotification extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        NotificationManager nManager = (NotificationManager)  getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getBaseContext())
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("MyService")
                .setContentText("Terminó el servicio!");

        //.setWhen(System.currentTimeMillis());//afegir l'hora a la notificació
        Intent targetIntent = new Intent(getApplicationContext(), BaterryLow.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                targetIntent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(contentIntent);

        builder.setAutoCancel(true);
        nManager.notify(12345, builder.build());

        this.stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
}
