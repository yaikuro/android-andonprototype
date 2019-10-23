package com.example.andonprototype.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.Background.GetData;
import com.example.andonprototype.Notification;
import com.example.andonprototype.R;
import com.example.andonprototype.ReportActivity;
import com.example.andonprototype.Useless.SessionHandler;
import com.example.andonprototype.Useless.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainDashboard extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    public String MachineID;
    public String Status;
    private SessionHandler session;
    ConnectionClass connectionClass;
    public String pic;
    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        List<Map<String,String>> Notif = null;
        notificationManager = NotificationManagerCompat.from(this);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        TextView welcomeText = findViewById(R.id.welcomeText);
        pic = getIntent().getStringExtra("ID");
        welcomeText.setText("Welcome " + getIntent().getStringExtra("ID")); //You logged in on " + user.getDate() + ", your session will expire on " + user.getSessionExpiryDate());
        /*+ user.getUSERID());*/
        Button logoutBtn = findViewById(R.id.btnLogout);
        Button btnV = findViewById(R.id.btnView);
        Button btnReportActivity = findViewById(R.id.btnReportActivity);
        Button btnProblemWaitingList = findViewById(R.id.btn_waiting_list);

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
                session.logoutUser();
                Intent i = new Intent(MainDashboard.this, LoginActivity.class);
                startActivity(i);
                Toast.makeText(MainDashboard.this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
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
