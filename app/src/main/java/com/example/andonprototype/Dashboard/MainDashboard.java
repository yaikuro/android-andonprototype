package com.example.andonprototype.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andonprototype.Background.GetData;
import com.example.andonprototype.R;
import com.example.andonprototype.ReportActivity;
import com.example.andonprototype.Useless.SessionHandler;
import com.example.andonprototype.Useless.User;

import java.util.List;
import java.util.Map;

public class MainDashboard extends AppCompatActivity {
    private SessionHandler session;
    public String pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

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
                String PICR = getIntent().getStringExtra("PIC");
                Intent i = new Intent(MainDashboard.this, ReportActivity.class);
                i.putExtra("PICR",PICR);
                startActivity(i);
            }
        });

        btnProblemWaitingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainDashboard.this, ProblemWaitingList.class);
                i.putExtra("PIC",pic);
                startActivity(i);
            }
        });
    }
}
