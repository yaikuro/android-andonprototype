package com.app.andonprototype.ui.Dashboard;

import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.R;
import com.app.andonprototype.SwipeProblem;
import com.app.andonprototype.drawer_ui.Help;
import com.app.andonprototype.drawer_ui.Settings.Settings;
import com.app.andonprototype.ui.MachineDashboard.MachineDashboard;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.app.andonprototype.Background.SaveSharedPreference.clearUserName;
import static com.app.andonprototype.Background.SaveSharedPreference.getID;
import static com.app.andonprototype.Background.SaveSharedPreference.getNama;

public class MainDashboard extends AppCompatActivity {
    private static final String TAG = "MainDashboard";
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    public static boolean running = false;
    public static boolean validate = false;
    boolean doubleBackToExitPressedOnce = false;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    public String currentDate, MachineID, Status, MachineName, Station, send, pic, ID;
    String notifikasi = "Welcome ";
    Connection connect;
    String ConnectionResult = "";
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        // Minta ijin untuk mengakses kamera
        isCameraPermissionGranted();
        // Minta ijin untuk mengakses storage
        isStoragePermissionGranted();

        // Ambil ID dan Nama dari SaveSharedPreference
        ID = getID(this); // Npk
        pic = getNama(this); // Nama

        // Diperlukan untuk mengirim notifikasi
        if (!running) {
            running = true;
        }
        if (!validate) {
            validate = true;
        }
        createNotificationChannels();
        notificationManager = NotificationManagerCompat.from(this);
        TextView txtnotif = findViewById(R.id.txtnotif);
        send = notifikasi + pic;
        txtnotif.setText(send);
        content();

        ///////////////////////////////////////////////////////////////////////////
        // Bottom Navigation Section
        ///////////////////////////////////////////////////////////////////////////
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_maindashboard, R.id.navigation_problemwaitinglist, R.id.navigation_reportactivity, R.id.navigation_machinereport, R.id.navigation_assetmanagement)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        //Navigation Drawer Section
        new DrawerBuilder().withActivity(this).build();

        // Primary items
        PrimaryDrawerItem home = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.drawer_item_home)
                .withIcon(R.drawable.ic_home_black_24dp);
        SecondaryDrawerItem settings = (SecondaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(97)
                .withName(R.string.drawer_item_settings)
                .withIcon(R.drawable.ic_settings_black_24dp);
        SecondaryDrawerItem help = (SecondaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(98)
                .withName(R.string.drawer_item_help)
                .withIcon(R.drawable.help);
        SecondaryDrawerItem contact = (SecondaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(99)
                .withName(R.string.drawer_item_logout)
                .withIcon(R.drawable.ic_contact_mail_black_24dp);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentStatusBar(false)
                .withFullscreen(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        new DividerDrawerItem(),
                        home,
                        new DividerDrawerItem(),
                        settings,
                        help,
                        contact
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(MainDashboard.this, MainDashboard.class);
                                finish();
                            } else if (drawerItem.getIdentifier() == 97) {
                                intent = new Intent(MainDashboard.this, Settings.class);
                            } else if (drawerItem.getIdentifier() == 98) {
                                intent = new Intent(MainDashboard.this, Help.class);
                            } else if (drawerItem.getIdentifier() == 99) {
//                                intent = new Intent(MainDashboard.this, Contact.class);
                                logout();
                            }

                            if (intent != null) {
                                MainDashboard.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .build();
        //End of Navigation drawer

    } // End of onCreate


    // Tekan back 2x untuk keluar dari activity
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
//        mBottomBar.onSaveInstanceState(outState);
    }

    // Log out, hapus semua data yang tersimpan di SaveSharedPreference
    public void logout() {
        clearUserName(this);
        send = "";
        notifikasi = "";
        running = false;
        Intent i = new Intent(MainDashboard.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    // Logic ketika mengirim notifikasi
    private void content() {

        // Cek status mesin di database
        getStatus();
        getDate();

        // Kalau salah satu mesin rusak (Status = 2), kirim notifikasi
        if (running && !send.equals(notifikasi) && Status.equals("2") && validate) {
            sendOnChannel1();
            Intent i = new Intent(MainDashboard.this, SwipeProblem.class);
            startActivity(i);
        }
        if (!MachineName.isEmpty()) {
            sendOnChannel2();
        }
        refresh(1000);
    }

    // Refresh
    private void refresh(int milliseconds) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }

    // Cek status mesin di database
    public void getStatus() {
        Status = "1";
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select Station,Status from stationdashboard where Status = 2";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    Station = rs.getString("Station");
                    Status = rs.getString("Status");
                }
                ConnectionResult = "Successfull";
                connect.close();
            }
        } catch (Exception ex) {
            ConnectionResult = ex.getMessage();
        }
    }

    // Cek status part di database
    public void getDate() {
        currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select Machine_Name, DATEDIFF(second, '" + currentDate + "', Due_Date) from machinelist " +
                        "where DATEDIFF(second, '" + currentDate + "', Due_Date) < 300";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next())
                    MachineName = rs.getString("Machine_Name");
                ConnectionResult = "Successfull";
                connect.close();
            }
        } catch (Exception ex) {
            ConnectionResult = ex.getMessage();
        }
    }

    // Buat channel untuk mengirim notifikasi
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Machine Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Part Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Problem Channel");
            channel2.setDescription("Part Replacement Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }

    // Notifikasi ketika terjadi kerusakan pada salah satu mesin
    public void sendOnChannel1() {
        String title = "ALERT";
        String message = "Machine Problem Detected";
        Intent activityIntent = new Intent(this, SwipeProblem.class);
        activityIntent.putExtra("Response_Time_Start", currentDate);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setFullScreenIntent(contentIntent, true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Repair", contentIntent)
                .build();
        notificationManager.notify(1, notification);
    }

    // Notifikasi ketika diperlukan pergantian part pada salah satu mesin
    public void sendOnChannel2() {
        String title = "ALERT PART";
        String message = "Part Replacement Check";
        Intent activityIntent = new Intent(this, Change_Part_List.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setFullScreenIntent(contentIntent, true)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .setContentIntent(contentIntent)
                .build();
        notificationManager.notify(2, notification);
    }

    public void isStoragePermissionGranted() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission is granted");
        } else {

            Log.v(TAG, "Permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void isCameraPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        }
    }

    // List menu pada ProblemWaitingList
    public void listMenuButton(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View CustomView = inflater.inflate(R.layout.activity_pop_dialog, null);

        new MaterialAlertDialogBuilder(this)
                .setView(CustomView)
                .show();
    }

    // Tombol Location pada list menu di atas
    public void btnLocation(View view) {
        Intent i = new Intent(this, MachineDashboard.class);
        startActivity(i);
    }

    // Tombol More Info pada list menu di atas
    public void btnMoreInfo(View view) {
        Intent i = new Intent(this, Settings.class);
        startActivity(i);
    }

}
