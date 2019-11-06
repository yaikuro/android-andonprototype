package com.example.andonprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    CheckBox checkBoxq61,checkBoxq62,checkBoxq63,checkBoxq64;
    RadioButton radioButtonQ71,radioButtonQ72;
    String Name,Job,Company,Email,Phone,Application,Plan;
    String q61,q62,q63,q64;
    boolean doubleBackToExitPressedOnce = false;
    ConnectionClass connectionClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        final RadioGroup radioGroup;
        connectionClass = new ConnectionClass();
        btnSubmit = findViewById(R.id.btnSubmit);
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);
        ans5 = findViewById(R.id.ans5);
        checkBoxq61 = findViewById(R.id.checkboxq61);
        checkBoxq62 = findViewById(R.id.checkboxq62);
        checkBoxq63 = findViewById(R.id.checkboxq63);
        checkBoxq64 = findViewById(R.id.checkboxq64);
        radioButtonQ71 = findViewById(R.id.radioButtonQ71);
        radioButtonQ72 = findViewById(R.id.radioButtonQ72);
        radioButtonQ71.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_yes();
            }
        });
        radioButtonQ72.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_no();
            }
        });
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
        getDataQ6();
        getDataQ7();
    }
    public void getDataQ6(){
        q61 = "";
        q62 = "";
        q63 = "";
        q64 = "";
        if (checkBoxq61.isChecked()){
            q61 = checkBoxq61.getText().toString() + ", ";
        }
        if (checkBoxq62.isChecked()){
            q62 = checkBoxq62.getText().toString() + ", ";
        }
        if (checkBoxq63.isChecked()){
            q63 = checkBoxq63.getText().toString() + ", ";
        }
        if (checkBoxq64.isChecked()){
            q64 = checkBoxq64.getText().toString();
        }
        Application = q61 + q62 + q63 + q64;
    }
    public void getDataQ7(){
        if (radioButtonQ71.isChecked()){
            Plan = radioButtonQ71.getText().toString();
        }
        else if (radioButtonQ72.isChecked()){
            Plan = radioButtonQ72.getText().toString();
        }
    }
    public void LoadMainDashboard() {
        Intent i = new Intent(Survey.this, MainDashboard.class);
        startActivity(i);
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
                doubleBackToExitPressedOnce=false;
            }
        }, 3000);
    }

    public void radio_yes()
    {
        // Note that I have unchecked  radiobuttons except the one
        // which is clicked/checked by user
        radioButtonQ72.setChecked(false);
    }

    public void radio_no()
    {
        // Note that I have unchecked  radiobuttons except the one
        // which is clicked/checked by user
        radioButtonQ71.setChecked(false);
    }
}
