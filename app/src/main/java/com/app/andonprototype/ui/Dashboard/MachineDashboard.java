package com.app.andonprototype.ui.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.Background.SaveSharedPreference;
import com.app.andonprototype.R;
import com.app.andonprototype.barcodescanner.SimpleScanner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MachineDashboard extends AppCompatActivity implements ListView.OnItemClickListener {
    ListView ListView, ListView2, ListView3, ListView4, ListView5, ListView6, ListView7, ListView8, ListView9, ListView10, ListView11;
    SimpleAdapter AD1, AD2, AD3, AD4, AD5, AD6, AD7, AD8, AD9, AD10, AD11;
    ImageView imageView;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    ProgressBar pbbar;
    public Boolean isSuccess,success;
    public String Line, Station, Status, PIC, Person;
    Button refresh;
    String currentDateStart = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_dashboard);
        final Handler handler = new Handler();
        isSuccess = false;
        success = false;
        pbbar = findViewById(R.id.pbbar);
        PIC = SaveSharedPreference.getID(this);
        refresh = findViewById(R.id.refresh);
        ListView = findViewById(R.id.ListView);
        ListView2 = findViewById(R.id.ListView2);
        ListView3 = findViewById(R.id.ListView3);
        ListView4 = findViewById(R.id.ListView4);
        ListView5 = findViewById(R.id.ListView5);
        ListView6 = findViewById(R.id.ListView6);
        ListView7 = findViewById(R.id.ListView7);
        ListView8 = findViewById(R.id.ListView8);
        ListView9 = findViewById(R.id.ListView9);
        ListView10 = findViewById(R.id.ListView10);
        ListView11 = findViewById(R.id.ListView11);
        ListView.setOnItemClickListener(this);
        ListView2.setOnItemClickListener(this);
        ListView3.setOnItemClickListener(this);
        ListView4.setOnItemClickListener(this);
        ListView5.setOnItemClickListener(this);
        ListView6.setOnItemClickListener(this);
        ListView7.setOnItemClickListener(this);
        ListView8.setOnItemClickListener(this);
        ListView9.setOnItemClickListener(this);
        ListView10.setOnItemClickListener(this);
        ListView11.setOnItemClickListener(this);
        imageView = findViewById(R.id.image);
        getdata();
        adapter_show();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ListView.setAdapter(AD1);
//                ListView2.setAdapter(AD2);
//                ListView3.setAdapter(AD3);
//                ListView4.setAdapter(AD4);
//                ListView5.setAdapter(AD5);
//                ListView6.setAdapter(AD6);
//                ListView7.setAdapter(AD7);
//                ListView8.setAdapter(AD8);
//                ListView9.setAdapter(AD9);
//                ListView10.setAdapter(AD10);
//                ListView11.setAdapter(AD11);
//                pbbar.setVisibility(View.GONE);
//            }
//        },2000);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(MachineDashboard.this, MachineDashboard.class);
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
                startActivity(i);
            }
        });
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                Intent i = new Intent(MachineDashboard.this, MachineDashboard.class);
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    public void adapter_show(){
        ListView.setAdapter(AD1);
        ListView2.setAdapter(AD2);
        ListView3.setAdapter(AD3);
        ListView4.setAdapter(AD4);
        ListView5.setAdapter(AD5);
        ListView6.setAdapter(AD6);
        ListView7.setAdapter(AD7);
        ListView8.setAdapter(AD8);
        ListView9.setAdapter(AD9);
        ListView10.setAdapter(AD10);
        ListView11.setAdapter(AD11);
        pbbar.setVisibility(View.GONE);
    }

    public void getdata() {
        GetData myData = new GetData();

        List<Map<String, String>> MydataList1;
        MydataList1 = myData.getdata1();
        String[] fromwhere1 = {"Image", "Station"};
        int[] viewwhere1 = {R.id.image, R.id.Station};
        AD1 = new SimpleAdapter(MachineDashboard.this, MydataList1, R.layout.stationlistitem, fromwhere1, viewwhere1);

        List<Map<String, String>> MydataList2;
        MydataList2 = myData.getdata2();
        String[] fromwhere2 = {"Image", "Station"};
        int[] viewwhere2 = {R.id.image, R.id.Station};
        AD2 = new SimpleAdapter(MachineDashboard.this, MydataList2, R.layout.stationlistitem, fromwhere2, viewwhere2);

        List<Map<String, String>> MydataList3;
        MydataList3 = myData.getdata3();
        String[] fromwhere3 = {"Image", "Station"};
        int[] viewwhere3 = {R.id.image, R.id.Station};
        AD3 = new SimpleAdapter(MachineDashboard.this, MydataList3, R.layout.stationlistitem, fromwhere3, viewwhere3);

        List<Map<String, String>> MydataList4;
        MydataList4 = myData.getdata4();
        String[] fromwhere4 = {"Image", "Station"};
        int[] viewwhere4 = {R.id.image, R.id.Station};
        AD4 = new SimpleAdapter(MachineDashboard.this, MydataList4, R.layout.stationlistitem, fromwhere4, viewwhere4);

        List<Map<String, String>> MydataList5;
        MydataList5 = myData.getdata5();
        String[] fromwhere5 = {"Image", "Station"};
        int[] viewwhere5 = {R.id.image, R.id.Station};
        AD5 = new SimpleAdapter(MachineDashboard.this, MydataList5, R.layout.stationlistitem, fromwhere5, viewwhere5);

        List<Map<String, String>> MydataList6;
        MydataList6 = myData.getdata6();
        String[] fromwhere6 = {"Image", "Station"};
        int[] viewwhere6 = {R.id.image, R.id.Station};
        AD6 = new SimpleAdapter(MachineDashboard.this, MydataList6, R.layout.stationlistitem, fromwhere6, viewwhere6);

        List<Map<String, String>> MydataList7;
        MydataList7 = myData.getdata7();
        String[] fromwhere7 = {"Image", "Station"};
        int[] viewwhere7 = {R.id.image, R.id.Station};
        AD7 = new SimpleAdapter(MachineDashboard.this, MydataList7, R.layout.stationlistitem, fromwhere7, viewwhere7);

        List<Map<String, String>> MydataList8;
        MydataList8 = myData.getdata8();
        String[] fromwhere8 = {"Image", "Station"};
        int[] viewwhere8 = {R.id.image, R.id.Station};
        AD8 = new SimpleAdapter(MachineDashboard.this, MydataList8, R.layout.stationlistitem, fromwhere8, viewwhere8);

        List<Map<String, String>> MydataList9;
        MydataList9 = myData.getdata9();
        String[] fromwhere9 = {"Image", "Station"};
        int[] viewwhere9 = {R.id.image, R.id.Station};
        AD9 = new SimpleAdapter(MachineDashboard.this, MydataList9, R.layout.stationlistitem, fromwhere9, viewwhere9);

        List<Map<String, String>> MydataList10;
        MydataList10 = myData.getdata10();
        String[] fromwhere10 = {"Image", "Station"};
        int[] viewwhere10 = {R.id.image, R.id.Station};
        AD10 = new SimpleAdapter(MachineDashboard.this, MydataList10, R.layout.stationlistitem, fromwhere10, viewwhere10);

        List<Map<String, String>> MydataList11;
        MydataList11 = myData.getdata11();
        String[] fromwhere11 = {"Image", "Station"};
        int[] viewwhere11 = {R.id.image, R.id.Station};
        AD11 = new SimpleAdapter(MachineDashboard.this, MydataList11, R.layout.stationlistitem, fromwhere11, viewwhere11);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainDashboard.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Class<?> clss = SimpleScanner.class;
        if (PIC.equals("")) {
            Toast.makeText(this, "ID Required, Try to Re-Login", Toast.LENGTH_SHORT).show();
        } else {
            if (ContextCompat.checkSelfPermission(MachineDashboard.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MachineDashboard.this,
                        new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
            } else{
                Intent i = new Intent(this, clss);
                Map<String, String> mp = (Map<String, String>) parent.getItemAtPosition(position);
                Object line = mp.get("Line");
                Object station = mp.get("Station");
                Object status = mp.get("Status");
                assert line != null;
                Line = line.toString();
                assert station != null;
                Station = station.toString();
                assert status != null;
                Status = status.toString();
                i.putExtra("StartTime", currentDateStart);
                i.putExtra("Line", Line);
                i.putExtra("PIC", PIC);
                i.putExtra("Station", Station);
                if (PIC.equals("admin")) {
                    startActivity(i);
                }
                else {
                    Object person = mp.get("PIC");
                    switch (Status) {
                        case "1":
                            Toast.makeText(this, "Machine is running", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            if (person != null) {
                                Person = person.toString();
                                if (person.equals(PIC)) {
                                    startActivity(i);
                                } else {
                                    Toast.makeText(this, Person + " is currently repairing", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Another PIC is currently repairing", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "4":
                            if (person != null) {
                                Person = person.toString();
                                Toast.makeText(this, "Waiting for Production Approval, Done by " + Person, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Waiting for Production Approval", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "2":
                            startActivity(i);
                            break;
                    }

                }
            }
        }
    }

    public class GetData {
        private Connection connect;
        ConnectionClass connectionClass = new ConnectionClass();
        private int[] listviewImage = new int[]
                {
                        R.drawable.color_green,
                        R.drawable.color_red,
                        R.drawable.color_yellow,
                        R.drawable.color_blue
                };

        List<Map<String, String>> getdata1() {
            List<Map<String, String>> data = new ArrayList<>();

            try {
                connect = connectionClass.CONN();
                if (connect == null) {
                    Toast.makeText(MachineDashboard.this, "No Connection", Toast.LENGTH_SHORT).show();
                } else {
                    String query = "Select * from stationdashboard where Line = 1";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String PIC = rs.getString("PIC");
                        String Station = rs.getString("Station");
                        Map<String, String> datanum = new HashMap<>();
                        datanum.put("Status", status);
                        //int i = Integer.parseInt(status);
                        switch (status) {
                            case "1": {
                                int i = 0;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "2": {
                                int i = 1;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "3": {
                                int i = 2;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "4": {
                                int i = 3;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                        }
                        datanum.put("Line", Line);
                        datanum.put("Station", Station);
                        datanum.put("PIC", PIC);
                        data.add(datanum);
                    }
                    isSuccess = true;
                    connect.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
            }
            return data;
        }

        List<Map<String, String>> getdata2() {
            List<Map<String, String>> data = new ArrayList<>();

            try {
                connect = connectionClass.CONN();
                if (connect == null){
                    Toast.makeText(MachineDashboard.this, "No Connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    String query = "Select * from stationdashboard where Line = 2";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        String PIC = rs.getString("PIC");
                        Map<String, String> datanum = new HashMap<>();
                        datanum.put("Status", status);
                        //int i = Integer.parseInt(status);
                        switch (status) {
                            case "1": {
                                int i = 0;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "2": {
                                int i = 1;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "3": {
                                int i = 2;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "4": {
                                int i = 3;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                        }
                        datanum.put("Line", Line);
                        datanum.put("Station", Station);
                        datanum.put("PIC",PIC);
                        data.add(datanum);
                    }
                    isSuccess = true;
                    connect.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
            }
            return data;
        }

        List<Map<String, String>> getdata3() {
            List<Map<String, String>> data = new ArrayList<>();

            try {
                connect = connectionClass.CONN();
                if (connect == null) {
                    Toast.makeText(MachineDashboard.this, "No Connection", Toast.LENGTH_SHORT).show();
                }else {
                    String query = "Select * from stationdashboard where Line = 3";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        String PIC = rs.getString("PIC");
                        Map<String, String> datanum = new HashMap<>();
                        datanum.put("Status", status);
                        //int i = Integer.parseInt(status);
                        switch (status) {
                            case "1": {
                                int i = 0;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "2": {
                                int i = 1;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "3": {
                                int i = 2;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "4": {
                                int i = 3;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                        }
                        datanum.put("Line", Line);
                        datanum.put("Station", Station);
                        datanum.put("PIC",PIC);
                        data.add(datanum);
                    }
                    isSuccess = true;
                    connect.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
            }
            return data;
        }

        List<Map<String, String>> getdata4() {
            List<Map<String, String>> data = new ArrayList<>();

            try {
                connect = connectionClass.CONN();
                if (connect == null) {
                    Toast.makeText(MachineDashboard.this, "No Connection", Toast.LENGTH_SHORT).show();
                }else {
                    String query = "Select * from stationdashboard where Line = 4";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        String PIC = rs.getString("PIC");
                        Map<String, String> datanum = new HashMap<>();
                        datanum.put("Status", status);
                        //int i = Integer.parseInt(status);
                        switch (status) {
                            case "1": {
                                int i = 0;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "2": {
                                int i = 1;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "3": {
                                int i = 2;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "4": {
                                int i = 3;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                        }
                        datanum.put("Line", Line);
                        datanum.put("Station", Station);
                        datanum.put("PIC",PIC);
                        data.add(datanum);
                    }
                    isSuccess = true;
                    connect.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
            }
            return data;
        }

        List<Map<String, String>> getdata5() {
            List<Map<String, String>> data = new ArrayList<>();

            try {
                connect = connectionClass.CONN();
                if (connect == null) {
                    Toast.makeText(MachineDashboard.this, "No Connection", Toast.LENGTH_SHORT).show();
                }else {
                    String query = "Select * from stationdashboard where Line = 5";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        String PIC = rs.getString("PIC");
                        Map<String, String> datanum = new HashMap<>();
                        datanum.put("Status", status);
                        //int i = Integer.parseInt(status);
                        switch (status) {
                            case "1": {
                                int i = 0;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "2": {
                                int i = 1;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "3": {
                                int i = 2;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "4": {
                                int i = 3;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                        }
                        datanum.put("Line", Line);
                        datanum.put("Station", Station);
                        datanum.put("PIC",PIC);
                        data.add(datanum);
                    }
                    isSuccess = true;
                    connect.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
            }
            return data;
        }

        List<Map<String, String>> getdata6() {
            List<Map<String, String>> data = new ArrayList<>();

            try {
                connect = connectionClass.CONN();
                if (connect == null) {
                }else {
                    String query = "Select * from stationdashboard where Line = 6";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        String PIC = rs.getString("PIC");
                        Map<String, String> datanum = new HashMap<>();
                        datanum.put("Status", status);
                        //int i = Integer.parseInt(status);
                        switch (status) {
                            case "1": {
                                int i = 0;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "2": {
                                int i = 1;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "3": {
                                int i = 2;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "4": {
                                int i = 3;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                        }
                        datanum.put("Line", Line);
                        datanum.put("Station", Station);
                        datanum.put("PIC",PIC);
                        data.add(datanum);
                    }
                    isSuccess = true;
                    connect.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
            }
            return data;
        }

        List<Map<String, String>> getdata7() {
            List<Map<String, String>> data = new ArrayList<>();

            try {
                connect = connectionClass.CONN();
                if (connect == null) {
                    Toast.makeText(MachineDashboard.this, "No Connection", Toast.LENGTH_SHORT).show();
                }else {
                    String query = "Select * from stationdashboard where Line = 7";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        String PIC = rs.getString("PIC");
                        Map<String, String> datanum = new HashMap<>();
                        datanum.put("Status", status);
                        //int i = Integer.parseInt(status);
                        switch (status) {
                            case "1": {
                                int i = 0;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "2": {
                                int i = 1;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "3": {
                                int i = 2;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "4": {
                                int i = 3;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                        }
                        datanum.put("Line", Line);
                        datanum.put("Station", Station);
                        datanum.put("PIC",PIC);
                        data.add(datanum);
                    }
                    isSuccess = true;
                    connect.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
            }
            return data;
        }

        List<Map<String, String>> getdata8() {
            List<Map<String, String>> data = new ArrayList<>();

            try {
                connect = connectionClass.CONN();
                if (connect == null) {
                    Toast.makeText(MachineDashboard.this, "No Connection", Toast.LENGTH_SHORT).show();
                }else {
                    String query = "Select * from stationdashboard where Line = 8";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        String PIC = rs.getString("PIC");
                        Map<String, String> datanum = new HashMap<>();
                        datanum.put("Status", status);
                        //int i = Integer.parseInt(status);
                        switch (status) {
                            case "1": {
                                int i = 0;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "2": {
                                int i = 1;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "3": {
                                int i = 2;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "4": {
                                int i = 3;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                        }
                        datanum.put("Line", Line);
                        datanum.put("Station", Station);
                        datanum.put("PIC",PIC);
                        data.add(datanum);
                    }
                    isSuccess = true;
                    connect.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
            }
            return data;
        }

        List<Map<String, String>> getdata9() {
            List<Map<String, String>> data = new ArrayList<>();

            try {
                connect = connectionClass.CONN();
                if (connect == null) {
                    Toast.makeText(MachineDashboard.this, "No Connection", Toast.LENGTH_SHORT).show();
                }else {
                    String query = "Select * from stationdashboard where Line = 9";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        String PIC = rs.getString("PIC");
                        Map<String, String> datanum = new HashMap<>();
                        datanum.put("Status", status);
                        //int i = Integer.parseInt(status);
                        switch (status) {
                            case "1": {
                                int i = 0;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "2": {
                                int i = 1;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "3": {
                                int i = 2;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "4": {
                                int i = 3;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                        }
                        datanum.put("Line", Line);
                        datanum.put("Station", Station);
                        datanum.put("PIC",PIC);
                        data.add(datanum);
                    }
                    isSuccess = true;
                    connect.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
            }
            return data;
        }

        List<Map<String, String>> getdata10() {
            List<Map<String, String>> data = new ArrayList<>();

            try {
                connect = connectionClass.CONN();
                if (connect == null) {
                    Toast.makeText(MachineDashboard.this, "No Connection", Toast.LENGTH_SHORT).show();
                }else {
                    String query = "Select * from stationdashboard where Line = 10";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        String PIC = rs.getString("PIC");
                        Map<String, String> datanum = new HashMap<>();
                        datanum.put("Status", status);
                        //int i = Integer.parseInt(status);
                        switch (status) {
                            case "1": {
                                int i = 0;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "2": {
                                int i = 1;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "3": {
                                int i = 2;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "4": {
                                int i = 3;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                        }
                        datanum.put("Line", Line);
                        datanum.put("Station", Station);
                        datanum.put("PIC",PIC);
                        data.add(datanum);
                    }
                    isSuccess = true;
                    connect.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
            }
            return data;
        }

        List<Map<String, String>> getdata11() {
            List<Map<String, String>> data = new ArrayList<>();

            try {
                connect = connectionClass.CONN();
                if (connect == null) {
                    Toast.makeText(MachineDashboard.this, "No Connection", Toast.LENGTH_SHORT).show();
                }else {
                    String query = "Select * from stationdashboard where Line = 11";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        String PIC = rs.getString("PIC");
                        Map<String, String> datanum = new HashMap<>();
                        datanum.put("Status", status);
                        //int i = Integer.parseInt(status);
                        switch (status) {
                            case "1": {
                                int i = 0;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "2": {
                                int i = 1;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "3": {
                                int i = 2;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "4": {
                                int i = 3;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                        }
                        datanum.put("Line", Line);
                        datanum.put("Station", Station);
                        datanum.put("PIC", PIC);
                        data.add(datanum);
                    }
                    isSuccess = true;
                    connect.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
            }
            return data;
        }
    }
}


