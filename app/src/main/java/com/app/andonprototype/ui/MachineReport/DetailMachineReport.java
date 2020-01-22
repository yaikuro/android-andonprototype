package com.app.andonprototype.ui.MachineReport;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DetailMachineReport extends AppCompatActivity {
    private String mesin, pic, num, station, duration, line, Nomor, RepairTimeStart;
    private String RepairTimeFinish, Desc_Problem, Desc_Solution, ImageProblem, ImageSolution;
    private ImageView Image_Problem, Image_Solution;
    private TextView MachineID,Line,Station,PIC,Duration,RepairStart,RepairFinish,DescProblem,DescSolution;
    ProgressBar progressBar;
    Connection connect;
    String ConnectionResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report);
        progressBar = findViewById(R.id.pbbarReport);
        Nomor = getIntent().getStringExtra("No");
        MachineID = findViewById(R.id.MachineID);
        Line = findViewById(R.id.Line);
        Station = findViewById(R.id.Station);
        PIC = findViewById(R.id.PIC);
        Duration = findViewById(R.id.Duration);
        RepairStart = findViewById(R.id.RepairTimeStart);
        RepairFinish = findViewById(R.id.RepairTimeFinish);
        DescProblem = findViewById(R.id.DescImageProblem);
        DescSolution = findViewById(R.id.DescImageSolution);
        Image_Problem = findViewById(R.id.ImageProblem);
        Image_Solution = findViewById(R.id.ImageSolution);
        getProblem();
        setText();
        LoadData loadData = new LoadData();
        loadData.execute();
//        getProblem();
//        setPicture();
    }

    public void setText(){
        MachineID.setText(mesin);
        Line.setText(line);
        Station.setText(station);
        PIC.setText(pic);
        Duration.setText(duration);
        RepairStart.setText(RepairTimeStart);
        RepairFinish.setText(RepairTimeFinish);
        DescProblem.setText(Desc_Problem);
        DescSolution.setText(Desc_Solution);
    }

    public void getProblem() {
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select No,PIC,Problem_Desc," +
                        "Solution_Desc,Repair_Time_Start," +
                        "Repair_Time_Finish,MachineID,Line," +
                        "Station,Repair_Duration" +
                        " from machinestatustest where No='" + Nomor + "'";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    num = rs.getString("No");
                    pic = rs.getString("PIC");
                    Desc_Problem = rs.getString("Problem_Desc");
                    Desc_Solution = rs.getString("Solution_Desc");
                    RepairTimeStart = rs.getString("Repair_Time_Start");
                    RepairTimeFinish = rs.getString("Repair_Time_Finish");
                    mesin = rs.getString("MachineID");
                    line = rs.getString("Line");
                    station = rs.getString("Station");
                    duration = rs.getString("Repair_Duration");
                }
                ConnectionResult = "Successfull";
                connect.close();
            }
        } catch (Exception ex) {
            ConnectionResult = ex.getMessage();
        }
    }

    public void getPicture(){
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select Image_Problem, Image_Solution from machinestatustest where No='" + Nomor + "'";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    ImageProblem = rs.getString("Image_Problem");
                    ImageSolution = rs.getString("Image_Solution");
                }
                ConnectionResult = "Successfull";
                connect.close();
            }
        } catch (Exception ex) {
            ConnectionResult = ex.getMessage();
        }
    }
    public void setPicture(){
            byte[] decodeStringProb = Base64.decode(ImageProblem, Base64.DEFAULT);
            Bitmap decodebitmapProb = BitmapFactory.decodeByteArray(decodeStringProb,0,decodeStringProb.length);
            Image_Problem.setImageBitmap(decodebitmapProb);
            byte[] decodeStringSol = Base64.decode(ImageSolution, Base64.DEFAULT);
            Bitmap decodebitmapSol = BitmapFactory.decodeByteArray(decodeStringSol,0,decodeStringSol.length);
            Image_Solution.setImageBitmap(decodebitmapSol);
        }
    public class LoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
            //do initialization of required objects objects here
        };
        @Override
        protected Void doInBackground(Void... params)
        {
            getPicture();
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            setPicture();
            progressBar.setVisibility(View.GONE);
        };
    }
}

