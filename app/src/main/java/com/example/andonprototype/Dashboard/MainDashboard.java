package com.example.andonprototype.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.andonprototype.Background.SaveSharedPreference.clearUserName;
import static com.example.andonprototype.Background.SaveSharedPreference.getID;

public class MainDashboard extends AppCompatActivity {
    private static final String TAG = "MainDashboard";
    boolean doubleBackToExitPressedOnce = false;
    public static final String CHANNEL_1_ID = "channel1";
    public String MachineID;
    public String Status;
    public String notifikasi = "Welcome ";
    public String send;
    public String hasil;
    Connection connect;
    String ConnectionResult = "";
    public String pic;
    private NotificationManagerCompat notificationManager;

    public String notfikasi;
    private AppBarConfiguration mAppBarConfiguration,appBarConfiguration;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        createNotificationChannels();

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        notificationManager = NotificationManagerCompat.from(this);
        TextView txtnotif = findViewById(R.id.txtnotif);
        pic = getID(this);
        send = notifikasi + pic;
        txtnotif.setText(send);
        hasil = txtnotif.getText().toString();

        content();
        isStoragePermissionGranted();

        FloatingActionButton fab        = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                logout();
            }
        });

        DrawerLayout drawer             = findViewById(R.id.drawer_layout);
        NavigationView navigationView   = findViewById(R.id.nav_view_drawer);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navControllerDrawer = Navigation.findNavController(this, R.id.nav_host_fragment_drawer);
//        NavigationUI.setupActionBarWithNavController(this, navControllerDrawer, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navControllerDrawer);
        ////////////////////////////////////////////////End of Drawer//////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////Bottom Navigation Part////////////////////////////////////////////////////////////////////////////////
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_maindashboard, R.id.navigation_reportactivity, R.id.navigation_problemwaitinglist)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        ////////////////////////////////////////////////End of Bottom Navigation//////////////////////////////////////////////////////////////////////////////
    }


    public void logout() {
        clearUserName(this);
        send = "";
        notifikasi = "";
        Intent i = new Intent(MainDashboard.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
    private  void content(){
        getStatus();
        if (!send.equals(notifikasi)&&Status.equals("2"))
        {
            sendOnChannel1();
        }
        refresh(1000);
    }

    private void refresh(int milliseconds){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable,milliseconds);
    }

    public void getStatus() {
        Status = "1";
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect==null)
            {
                ConnectionResult = "Check your Internet Connection";
            }
            else{
                String query = "Select * from machinedashboard where Status = 2";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next())
                {
                    MachineID = rs.getString("MachineID");
                    Status = rs.getString("Status");
                }
                ConnectionResult = "Successfull";
                connect.close();
            }
        }
        catch (Exception ex)
        {
            ConnectionResult=ex.getMessage();
        }
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Machine Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Problem Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }

    public void sendOnChannel1() {
        String title = "ALERT";
        String message = "Machine Problem Detected";
        Intent activityIntent = new Intent(this, MainDashboard.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,activityIntent,0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher,"Repair",contentIntent)
                .build();
        notificationManager.notify(1, notification);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
    }

    public void isStoragePermissionGranted() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission is granted");
        } else {

            Log.v(TAG,"Permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}
