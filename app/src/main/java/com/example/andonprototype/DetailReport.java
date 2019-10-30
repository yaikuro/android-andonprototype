package com.example.andonprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andonprototype.Background.ConnectionClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DetailReport extends AppCompatActivity {
    public String mesin;
    public String pic;
    public String num;
    public String station;
    public String duration;
    public String line;
    public String Nomor;
    public String RepairTimeStart;
    public String RepairTimeFinish;
    public String Desc_Problem;
    public String Desc_Solution;
    public String ImageProblem;
    public String ImageSolution;
    Connection connect;
    String ConnectionResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report);
        Nomor = getIntent().getStringExtra("No");
        TextView MachineID = (TextView) findViewById(R.id.MachineID);
        TextView Line = (TextView) findViewById(R.id.Line);
        TextView Station = (TextView) findViewById(R.id.Station);
        TextView PIC = (TextView) findViewById(R.id.PIC);
        TextView Duration = (TextView) findViewById(R.id.Duration);
        TextView RepairStart = (TextView) findViewById(R.id.RepairTimeStart);
        TextView RepairFinish = (TextView) findViewById(R.id.RepairTimeFinish);
        TextView DescProblem = (TextView) findViewById(R.id.DescImageProblem);
        TextView DescSolution = (TextView) findViewById(R.id.DescImageSolution);
        ImageView Image_Problem = (ImageView) findViewById(R.id.ImageProblem);
        ImageView Image_Solution = (ImageView) findViewById(R.id.ImageSolution);
        getProblem();
        Toast.makeText(this, num, Toast.LENGTH_SHORT).show();
        MachineID.setText(mesin);
        Line.setText(line);
        Station.setText(station);
        PIC.setText(pic);
        Duration.setText(duration);
        RepairStart.setText(RepairTimeStart);
        RepairFinish.setText(RepairTimeFinish);
        DescProblem.setText(Desc_Problem);
        DescSolution.setText(Desc_Solution);
        byte[] decodeStringProb = Base64.decode(ImageProblem, Base64.DEFAULT);
        Bitmap decodebitmapProb = BitmapFactory.decodeByteArray(decodeStringProb,0,decodeStringProb.length);
        Image_Problem.setImageBitmap(decodebitmapProb);
        byte[] decodeStringSol = Base64.decode(ImageSolution, Base64.DEFAULT);
        Bitmap decodebitmapSol = BitmapFactory.decodeByteArray(decodeStringSol,0,decodeStringSol.length);
        Image_Solution.setImageBitmap(decodebitmapSol);
    }

    public void getProblem()
        {
            try
            {
                ConnectionClass connectionClass = new ConnectionClass();
                connect=connectionClass.CONN();
                if(connect==null)
                {
                    ConnectionResult = "Check your Internet Connection";
                }
                else
                {
                    String query = "Select * from machinestatustest where No='" + Nomor + "'";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                    {
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
                        ImageProblem = rs.getString("Image_Problem");
                        ImageSolution = rs.getString("Image_Solution");
                    }
                    ConnectionResult="Successfull";
                    connect.close();
                }
            }
            catch (Exception ex)
            {
                ConnectionResult=ex.getMessage();
            }
        }
}
