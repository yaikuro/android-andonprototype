package com.example.andonprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.Dashboard.MainDashboard;

import java.io.IOError;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Survey extends AppCompatActivity {
    Button btnSubmit;
    EditText ans1,ans2,ans3,ans4,ans5,ans6,ans7;
    String Name,Job,Company,Email,Phone,Application,Plan;
    ConnectionClass connectionClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        connectionClass = new ConnectionClass();
        btnSubmit = findViewById(R.id.btnSubmit);
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);
        ans5 = findViewById(R.id.ans5);
        ans6 = findViewById(R.id.ans6);
        ans7 = findViewById(R.id.ans7);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                UploadData();
            }
        });
    }
    public void UploadData(){
        String msg = "";
        try {
            Connection con = connectionClass.CONN();
            String query = "Insert into survey (Name,JobDepartment,CompanyName,[E-mail]," +
                    "PhoneNumber,ApplicationOfInterest," +
                    "PlanToImplementSimilarApplication) values " +
                    "('" + Name + "','" + Job + "','" + Company + "','" + Email + "','"
                    + Phone + "','" + Application +
                    "','" + Plan + "')";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.execute();
            msg = "Inserted Successfully";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            LoadMainDashboard();
        }
        catch (IOError|Exception ex){
            msg = ex.getMessage();
            Log.d("hitesh",msg);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }
    public void getData(){
        Name = ans1.getText().toString();
        Job = ans2.getText().toString();
        Company = ans3.getText().toString();
        Email = ans4.getText().toString();
        Phone = ans5.getText().toString();
        Application = ans6.getText().toString();
        Plan = ans7.getText().toString();
    }
    public void LoadMainDashboard() {
        Intent i = new Intent(Survey.this, MainDashboard.class);
        startActivity(i);
    }
}
