package com.example.andonprototype;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.Configuration.Query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.andonprototype.App.CHANNEL_1_ID;

public class MyService extends Service {
    public String Status,MachineID;
    private NotificationManagerCompat notificationManager;
    Connection connect;
    public String ConnectionResult = "";
        public MyService() {
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onCreate(){
            Toast.makeText(this, "Invoke background service onCreate method.", Toast.LENGTH_LONG).show();
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            Toast.makeText(this, "Invoke background service onStartCommand method.", Toast.LENGTH_LONG).show();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Toast.makeText(this, "Invoke background service onDestroy method.", Toast.LENGTH_LONG).show();
        }
    }
