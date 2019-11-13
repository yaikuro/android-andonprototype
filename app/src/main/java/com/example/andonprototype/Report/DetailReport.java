package com.example.andonprototype.Report;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DetailReport extends AppCompatActivity {
    private String mesin,pic,num,station,duration,line,Nomor,RepairTimeStart;
    private String RepairTimeFinish,Desc_Problem,Desc_Solution,ImageProblem,ImageSolution;
    private ImageView Image_Problem,Image_Solution;
    Connection connect;
    String ConnectionResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report);
        Nomor = getIntent().getStringExtra("No");
        TextView MachineID = findViewById(R.id.MachineID);
        TextView Line = findViewById(R.id.Line);
        TextView Station = findViewById(R.id.Station);
        TextView PIC = findViewById(R.id.PIC);
        TextView Duration = findViewById(R.id.Duration);
        TextView RepairStart = findViewById(R.id.RepairTimeStart);
        TextView RepairFinish = findViewById(R.id.RepairTimeFinish);
        TextView DescProblem = findViewById(R.id.DescImageProblem);
        TextView DescSolution = findViewById(R.id.DescImageSolution);
        Image_Problem =findViewById(R.id.ImageProblem);
        Image_Solution =findViewById(R.id.ImageSolution);
        getProblem();
        MachineID.setText(mesin);
        Line.setText(line);
        Station.setText(station);
        PIC.setText(pic);
        Duration.setText(duration);
        RepairStart.setText(RepairTimeStart);
        RepairFinish.setText(RepairTimeFinish);
        DescProblem.setText(Desc_Problem);
        DescSolution.setText(Desc_Solution);
        setPicture();
    }

    public void getProblem() {
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
        public void setPicture()
        {
            byte[] decodeStringProb = Base64.decode(ImageProblem, Base64.DEFAULT);
            Bitmap decodebitmapProb = BitmapFactory.decodeByteArray(decodeStringProb,0,decodeStringProb.length);
            Image_Problem.setImageBitmap(decodebitmapProb);
            byte[] decodeStringSol = Base64.decode(ImageSolution, Base64.DEFAULT);
            Bitmap decodebitmapSol = BitmapFactory.decodeByteArray(decodeStringSol,0,decodeStringSol.length);
            Image_Solution.setImageBitmap(decodebitmapSol);
        }
}
