package com.example.andonprototype.Dashboard;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
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
import com.example.andonprototype.drawer_ui.Help;
import com.example.andonprototype.drawer_ui.Settings;
import com.example.andonprototype.ui.MainDashboard.MainDashboardFragment;
import com.example.andonprototype.ui.ProblemList.ProblemListFragment;
import com.example.andonprototype.ui.ProblemList.ProblemWaitingList;
import com.example.andonprototype.ui.Report.ReportActivityFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.andonprototype.Background.SaveSharedPreference.clearUserName;
import static com.example.andonprototype.Background.SaveSharedPreference.getID;

public class MainDashboard extends AppCompatActivity {
    private static final String TAG = "MainDashboard";
    boolean doubleBackToExitPressedOnce = false;
    public static final String CHANNEL_1_ID = "channel1";
    public String MachineID;
    public String Status;
    public String Station;
    public String notifikasi = "Welcome ";
    public String send;
    public String hasil;
    Connection connect;
    String ConnectionResult = "";
    public String pic;
    private NotificationManagerCompat notificationManager;

    public String notfikasi;
    private AppBarConfiguration mAppBarConfiguration,appBarConfiguration;

    private BottomBar mBottomBar;
    private FragNavController fragNavController;


    private final int TAB_FIRST = FragNavController.TAB1;
    private final int TAB_SECOND = FragNavController.TAB2;
    private final int TAB_THIRD = FragNavController.TAB3;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        createNotificationChannels();

        notificationManager = NotificationManagerCompat.from(this);
        TextView txtnotif = findViewById(R.id.txtnotif);
        pic = getID(this);
        send = notifikasi + pic;
        txtnotif.setText(send);
        hasil = txtnotif.getText().toString();

        content();
        isStoragePermissionGranted();


        ///////////////////////////////////////////////////////////////////////////
        // Navigation Section
        ///////////////////////////////////////////////////////////////////////////
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_maindashboard, R.id.navigation_problemwaitinglist, R.id.navigation_reportactivity)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);



//        List<Fragment> fragments = new ArrayList<>(3);
//
//        //add fragments to list
//        fragments.add(MainDashboardFragment.newInstance(0));
//        fragments.add(ProblemListFragment.newInstance(0));
//        fragments.add(ReportActivityFragment.newInstance(0));
//
//
//        //link fragments to container
//        fragNavController = new FragNavController(getSupportFragmentManager(),R.id.container,fragments);
//        //End of FragNav
//
//        //BottomBar menu
//        mBottomBar = BottomBar.attach(this, savedInstanceState);
//        mBottomBar.setItems(R.menu.bottom_nav_menu);
//        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
//            @Override
//            public void onMenuTabSelected(@IdRes int menuItemId) {
//                //switch between tabs
//                switch (menuItemId) {
//                    case R.id.navigation_maindashboard:
//                        fragNavController.switchTab(TAB_FIRST);
//                        break;
//                    case R.id.navigation_problemwaitinglist:
//                        fragNavController.switchTab(TAB_SECOND);
//                        break;
//                    case R.id.navigation_reportactivity:
//                        fragNavController.switchTab(TAB_THIRD);
//                        break;
//                }
//            }
//
//            @Override
//            public void onMenuTabReSelected(@IdRes int menuItemId) {
//                if (menuItemId == R.id.navigation_maindashboard) {
//                    fragNavController.clearStack();
//                }
//            }
//        });
//        //End of BottomBar menu

        //Navigation drawer
        new DrawerBuilder().withActivity(this).build();

        //primary items
        PrimaryDrawerItem home = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.drawer_item_home)
                .withIcon(R.drawable.ic_home_black_24dp);
//        PrimaryDrawerItem primary_item1 = new PrimaryDrawerItem()
//                .withIdentifier(2)
//                .withName(R.string.drawer_item_option1)
//                .withIcon(R.drawable.ic_looks_one_black_24dp);
//        PrimaryDrawerItem primary_item2 = new PrimaryDrawerItem()
//                .withIdentifier(3)
//                .withName(R.string.drawer_item_option2)
//                .withIcon(R.drawable.ic_looks_two_black_24dp);
//        //secondary items
//        SecondaryDrawerItem secondary_item1 = (SecondaryDrawerItem) new SecondaryDrawerItem()
//                .withIdentifier(11)
//                .withName(R.string.drawer_item_option1)
//                .withIcon(R.drawable.ic_looks_one_black_24dp);
//        SecondaryDrawerItem secondary_item2 = (SecondaryDrawerItem) new SecondaryDrawerItem()
//                .withIdentifier(12)
//                .withName(R.string.drawer_item_option2)
//                .withIcon(R.drawable.ic_looks_two_black_24dp);
//        SecondaryDrawerItem secondary_item3 = (SecondaryDrawerItem) new SecondaryDrawerItem()
//                .withIdentifier(13)
//                .withName(R.string.drawer_item_option3)
//                .withIcon(R.drawable.ic_looks_3_black_24dp);
        //settings, help, contact items
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

        //Toolbar
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
//                        primary_item1,
//                        primary_item2,
//                        new SectionDrawerItem().withName("Categories"),
//                        secondary_item1,
//                        secondary_item2,
//                        secondary_item2,
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
//                            } else if (drawerItem.getIdentifier() == 2) {
//                                //intent = new Intent(MainActivity.this, Class.class);
//                            } else if (drawerItem.getIdentifier() == 3) {
//                                //intent = new Intent(MainActivity.this, Class.class);
//                            } else if (drawerItem.getIdentifier() == 11) {
//                                //intent = new Intent(MainActivity.this, Class.class);
//                            } else if (drawerItem.getIdentifier() == 12) {
//                                //intent = new Intent(MainActivity.this, Class.class);
//                            } else if (drawerItem.getIdentifier() == 13) {
//                                //intent = new Intent(MainActivity.this, Class.class);
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



    @Override
    public void onBackPressed() {
//        if (fragNavController.getCurrentStack().size() > 1) {
//            fragNavController.pop();
//        } else
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
        }, 3000);{
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
//        mBottomBar.onSaveInstanceState(outState);
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
                String query = "Select Station,Status from stationdashboard where Status = 2";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next())
                {
                    Station = rs.getString("Station");
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
        Intent activityIntent = new Intent(this, ProblemWaitingList.class);
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
