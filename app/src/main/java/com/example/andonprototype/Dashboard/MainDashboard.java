package com.example.andonprototype.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.Configuration.Query;
import com.example.andonprototype.MyService;
import com.example.andonprototype.R;
import com.example.andonprototype.ReportActivity;
import com.example.andonprototype.SaveSharedPreference;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.andonprototype.App.CHANNEL_1_ID;
import static com.example.andonprototype.SaveSharedPreference.clearUserName;
import static com.example.andonprototype.SaveSharedPreference.getID;
import static com.example.andonprototype.SaveSharedPreference.getUserName;

public class MainDashboard extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    public String MachineID,MachineIDprev;
    public String Status;
    public SaveSharedPreference saveSharedPreference;
    public String notifikasi;
    Connection connect;
    String ConnectionResult = "";
    public String pic;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        createNotificationChannels();
        saveSharedPreference = new SaveSharedPreference();

        notificationManager = NotificationManagerCompat.from(this);
        TextView welcomeText = findViewById(R.id.welcomeText);
        pic = saveSharedPreference.getID(this);
        welcomeText.setText("Welcome " + pic); //You logged in on " + user.getDate() + ", your session will expire on " + user.getSessionExpiryDate());
        /*+ user.getUSERID());*/
        Button logoutBtn = findViewById(R.id.btnLogout);
        Button btnV = findViewById(R.id.btnView);
        Button btnReportActivity = findViewById(R.id.btnReportActivity);
        Button btnProblemWaitingList = findViewById(R.id.btn_waiting_list);
        Button btnStartService = findViewById(R.id.StartService);
        Button btnStopService = findViewById(R.id.StopService);

        notifikasi = welcomeText.getText().toString();

        btnV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainDashboard.this, MachineDashboard.class);
                startActivity(a);
//                finish();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUserName(MainDashboard.this);
                Intent i = new Intent(MainDashboard.this, LoginActivity.class);
                notifikasi = "Welcome ";
                startActivity(i);
                Toast.makeText(MainDashboard.this, "Logged Out", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btnReportActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainDashboard.this, ReportActivity.class);
                i.putExtra("PIC", pic);
                startActivity(i);
            }
        });

        btnProblemWaitingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainDashboard.this, ProblemWaitingList.class);
                i.putExtra("PIC", pic);
                startActivity(i);
            }
        });

        btnStartService.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent startServiceIntent = new Intent(MainDashboard.this, MyService.class);
                startService(startServiceIntent);
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent stopServiceIntent = new Intent(MainDashboard.this, MyService.class);
                stopService(stopServiceIntent);
            }
        });
        content(); //Get Status for Notification
    }

    private  void content(){
        getStatus();
        //Toast.makeText(MainDashboard.this, Status, Toast.LENGTH_SHORT).show();
        if (!notifikasi.equals("Welcome ")&&Status.equals("2"))
        {
            sendOnChannel1();
        }
        else
        {
        }
        refresh(3000);
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

    public void getStatus()
    {
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
                    "Channel PENTING",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
    public void sendOnChannel1() {
        String title = "ALERT";
        String message = "Machine Problem Detected";
        PendingIntent notifyPIntent = PendingIntent.getActivity(getApplicationContext(),0,new Intent(),0);

        Intent activityIntent = new Intent(this, ProblemWaitingList.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,activityIntent,0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
// .bigText(emailObject.getSubjectAndSnippet()))
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                //.setContentIntent(notifyPIntent)
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


}
