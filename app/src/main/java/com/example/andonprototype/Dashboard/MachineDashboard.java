package com.example.andonprototype.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.Background.GetData;
import com.example.andonprototype.Breakdown.BreakdownHistory;
import com.example.andonprototype.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineDashboard extends AppCompatActivity implements ListView.OnItemClickListener{
    ListView ListView,ListView2,ListView3,ListView4,ListView5,ListView6,ListView7,ListView8,ListView9,ListView10,ListView11;
    SimpleAdapter AD1,AD2,AD3,AD4,AD5,AD6,AD7,AD8,AD9,AD10,AD11;
    ImageView imageView;
    public String Mesin;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_dashboard);
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
        imageView = findViewById(R.id.image);
        getdata();
//        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
//        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Intent i = new Intent(MachineDashboard.this, MachineDashboard.class);
//                finish();
//                overridePendingTransition(0, 0);
//                startActivity(i);
//                overridePendingTransition(0, 0);
//                pullToRefresh.setRefreshing(false);
//            }
//        });
    }
    public void getdata(){
        getdata1();
        getdata2();
        getdata3();
        getdata4();
        getdata5();
        getdata6();
        getdata7();
        getdata8();
        getdata9();
        getdata10();
        getdata11();
    }
    public void getdata1() {
        List<Map<String, String>> MydataList = null;
        GetData myData = new GetData();
        MydataList = myData.getdata1();
        String[] fromwhere = {"Image", "Station"};
        int[] viewwhere = {R.id.image, R.id.Station};
        AD1 = new SimpleAdapter(MachineDashboard.this, MydataList, R.layout.stationlistitem, fromwhere, viewwhere);
        ListView.setAdapter(AD1);
    }
    public void getdata2() {
        List<Map<String, String>> MydataList = null;
        GetData myData = new GetData();
        MydataList = myData.getdata2();
        String[] fromwhere = {"Image", "Station"};
        int[] viewwhere = {R.id.image, R.id.Station};
        AD2 = new SimpleAdapter(MachineDashboard.this, MydataList, R.layout.stationlistitem, fromwhere, viewwhere);
        ListView2.setAdapter(AD2);
    }
    public void getdata3() {
        List<Map<String, String>> MydataList = null;
        GetData myData = new GetData();
        MydataList = myData.getdata3();
        String[] fromwhere = {"Image", "Station"};
        int[] viewwhere = {R.id.image, R.id.Station};
        AD3 = new SimpleAdapter(MachineDashboard.this, MydataList, R.layout.stationlistitem, fromwhere, viewwhere);
        ListView3.setAdapter(AD3);
    }
    public void getdata4() {
        List<Map<String, String>> MydataList = null;
        GetData myData = new GetData();
        MydataList = myData.getdata4();
        String[] fromwhere = {"Image", "Station"};
        int[] viewwhere = {R.id.image, R.id.Station};
        AD4 = new SimpleAdapter(MachineDashboard.this, MydataList, R.layout.stationlistitem, fromwhere, viewwhere);
        ListView4.setAdapter(AD4);
    }
    public void getdata5() {
        List<Map<String, String>> MydataList = null;
        GetData myData = new GetData();
        MydataList = myData.getdata5();
        String[] fromwhere = {"Image", "Station"};
        int[] viewwhere = {R.id.image, R.id.Station};
        AD5 = new SimpleAdapter(MachineDashboard.this, MydataList, R.layout.stationlistitem, fromwhere, viewwhere);
        ListView5.setAdapter(AD5);
    }
    public void getdata6() {
        List<Map<String, String>> MydataList = null;
        GetData myData = new GetData();
        MydataList = myData.getdata6();
        String[] fromwhere = {"Image", "Station"};
        int[] viewwhere = {R.id.image, R.id.Station};
        AD6 = new SimpleAdapter(MachineDashboard.this, MydataList, R.layout.stationlistitem, fromwhere, viewwhere);
        ListView6.setAdapter(AD6);
    }
    public void getdata7() {
        List<Map<String, String>> MydataList = null;
        GetData myData = new GetData();
        MydataList = myData.getdata7();
        String[] fromwhere = {"Image", "Station"};
        int[] viewwhere = {R.id.image, R.id.Station};
        AD7 = new SimpleAdapter(MachineDashboard.this, MydataList, R.layout.stationlistitem, fromwhere, viewwhere);
        ListView7.setAdapter(AD7);
    }
    public void getdata8() {
        List<Map<String, String>> MydataList = null;
        GetData myData = new GetData();
        MydataList = myData.getdata8();
        String[] fromwhere = {"Image", "Station"};
        int[] viewwhere = {R.id.image, R.id.Station};
        AD8 = new SimpleAdapter(MachineDashboard.this, MydataList, R.layout.stationlistitem, fromwhere, viewwhere);
        ListView8.setAdapter(AD8);
    }
    public void getdata9() {
        List<Map<String, String>> MydataList = null;
        GetData myData = new GetData();
        MydataList = myData.getdata9();
        String[] fromwhere = {"Image", "Station"};
        int[] viewwhere = {R.id.image, R.id.Station};
        AD9 = new SimpleAdapter(MachineDashboard.this, MydataList, R.layout.stationlistitem, fromwhere, viewwhere);
        ListView9.setAdapter(AD9);
    }
    public void getdata10() {
        List<Map<String, String>> MydataList = null;
        GetData myData = new GetData();
        MydataList = myData.getdata10();
        String[] fromwhere = {"Image", "Station"};
        int[] viewwhere = {R.id.image, R.id.Station};
        AD10 = new SimpleAdapter(MachineDashboard.this, MydataList, R.layout.stationlistitem, fromwhere, viewwhere);
        ListView10.setAdapter(AD10);
    }
    public void getdata11() {
        List<Map<String, String>> MydataList = null;
        GetData myData = new GetData();
        MydataList = myData.getdata11();
        String[] fromwhere = {"Image", "Station"};
        int[] viewwhere = {R.id.image, R.id.Station};
        AD11 = new SimpleAdapter(MachineDashboard.this, MydataList, R.layout.stationlistitem, fromwhere, viewwhere);
        ListView11.setAdapter(AD11);
    }
    @Override
    public void onBackPressed(){
        Intent i = new Intent(this,MainDashboard.class);
        startActivity(i);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent i = new Intent(this, BreakdownHistory.class);
        Map<String,String> mp = (Map<String, String>) ListView.getItemAtPosition(position);
//        Object machine = mp.get("MachineID");
//        Mesin = machine.toString();
//        i.putExtra("MachineID", Mesin);
//        startActivity(i);
    }
    public static class GetData {
        private Connection connect;
        private String ConnectionResult="";
        private Boolean isSuccess = false;
        private int[] listviewImage = new int[]
                {
                        R.drawable.green,
                        R.drawable.red,
                        R.drawable.yellow,
                        R.drawable.blue
                };
        public List<Map<String,String>>getdata1() {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            try
            {
                ConnectionClass connectionClass = new ConnectionClass();
                connect=connectionClass.CONN();
                if (connect == null)
                    ConnectionResult = "Check your Internet Connection";
                else
                {
                    String query= "Select * from stationdashboard where Line = 1";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        Map<String,String> datanum = new HashMap<>();
                        datanum.put("Status",status);
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
                        datanum.put("Line",Line);
                        datanum.put("Station",Station);
                        data.add(datanum);
                    }
                    ConnectionResult="Successful";
                    isSuccess=true;
                    connect.close();
                }
            }catch (Exception ex)
            {
                isSuccess=false;
                ConnectionResult=ex.getMessage();
            }
            return data;
        }
        public List<Map<String,String>>getdata2() {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            try
            {
                ConnectionClass connectionClass = new ConnectionClass();
                connect=connectionClass.CONN();
                if (connect == null)
                    ConnectionResult = "Check your Internet Connection";
                else
                {
                    String query= "Select * from stationdashboard where Line = 2";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        Map<String,String> datanum = new HashMap<>();
                        datanum.put("Status",status);
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
                        datanum.put("Line",Line);
                        datanum.put("Station",Station);
                        data.add(datanum);
                    }
                    ConnectionResult="Successful";
                    isSuccess=true;
                    connect.close();
                }
            }catch (Exception ex)
            {
                isSuccess=false;
                ConnectionResult=ex.getMessage();
            }
            return data;
        }
        public List<Map<String,String>>getdata3() {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            try
            {
                ConnectionClass connectionClass = new ConnectionClass();
                connect=connectionClass.CONN();
                if (connect == null)
                    ConnectionResult = "Check your Internet Connection";
                else
                {
                    String query= "Select * from stationdashboard where Line = 3";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        Map<String,String> datanum = new HashMap<>();
                        datanum.put("Status",status);
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
                        datanum.put("Line",Line);
                        datanum.put("Station",Station);
                        data.add(datanum);
                    }
                    ConnectionResult="Successful";
                    isSuccess=true;
                    connect.close();
                }
            }catch (Exception ex)
            {
                isSuccess=false;
                ConnectionResult=ex.getMessage();
            }
            return data;
        }
        public List<Map<String,String>>getdata4() {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            try
            {
                ConnectionClass connectionClass = new ConnectionClass();
                connect=connectionClass.CONN();
                if (connect == null)
                    ConnectionResult = "Check your Internet Connection";
                else
                {
                    String query= "Select * from stationdashboard where Line = 4";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        Map<String,String> datanum = new HashMap<>();
                        datanum.put("Status",status);
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
                        datanum.put("Line",Line);
                        datanum.put("Station",Station);
                        data.add(datanum);
                    }
                    ConnectionResult="Successful";
                    isSuccess=true;
                    connect.close();
                }
            }catch (Exception ex)
            {
                isSuccess=false;
                ConnectionResult=ex.getMessage();
            }
            return data;
        }
        public List<Map<String,String>>getdata5() {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            try
            {
                ConnectionClass connectionClass = new ConnectionClass();
                connect=connectionClass.CONN();
                if (connect == null)
                    ConnectionResult = "Check your Internet Connection";
                else
                {
                    String query= "Select * from stationdashboard where Line = 5";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        Map<String,String> datanum = new HashMap<>();
                        datanum.put("Status",status);
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
                        datanum.put("Line",Line);
                        datanum.put("Station",Station);
                        data.add(datanum);
                    }
                    ConnectionResult="Successful";
                    isSuccess=true;
                    connect.close();
                }
            }catch (Exception ex)
            {
                isSuccess=false;
                ConnectionResult=ex.getMessage();
            }
            return data;
        }
        public List<Map<String,String>>getdata6() {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            try
            {
                ConnectionClass connectionClass = new ConnectionClass();
                connect=connectionClass.CONN();
                if (connect == null)
                    ConnectionResult = "Check your Internet Connection";
                else
                {
                    String query= "Select * from stationdashboard where Line = 6";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        Map<String,String> datanum = new HashMap<>();
                        datanum.put("Status",status);
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
                        datanum.put("Line",Line);
                        datanum.put("Station",Station);
                        data.add(datanum);
                    }
                    ConnectionResult="Successful";
                    isSuccess=true;
                    connect.close();
                }
            }catch (Exception ex)
            {
                isSuccess=false;
                ConnectionResult=ex.getMessage();
            }
            return data;
        }
        public List<Map<String,String>>getdata7() {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            try
            {
                ConnectionClass connectionClass = new ConnectionClass();
                connect=connectionClass.CONN();
                if (connect == null)
                    ConnectionResult = "Check your Internet Connection";
                else
                {
                    String query= "Select * from stationdashboard where Line = 7";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        Map<String,String> datanum = new HashMap<>();
                        datanum.put("Status",status);
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
                        datanum.put("Line",Line);
                        datanum.put("Station",Station);
                        data.add(datanum);
                    }
                    ConnectionResult="Successful";
                    isSuccess=true;
                    connect.close();
                }
            }catch (Exception ex)
            {
                isSuccess=false;
                ConnectionResult=ex.getMessage();
            }
            return data;
        }
        public List<Map<String,String>>getdata8() {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            try
            {
                ConnectionClass connectionClass = new ConnectionClass();
                connect=connectionClass.CONN();
                if (connect == null)
                    ConnectionResult = "Check your Internet Connection";
                else
                {
                    String query= "Select * from stationdashboard where Line = 8";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        Map<String,String> datanum = new HashMap<>();
                        datanum.put("Status",status);
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
                        datanum.put("Line",Line);
                        datanum.put("Station",Station);
                        data.add(datanum);
                    }
                    ConnectionResult="Successful";
                    isSuccess=true;
                    connect.close();
                }
            }catch (Exception ex)
            {
                isSuccess=false;
                ConnectionResult=ex.getMessage();
            }
            return data;
        }
        public List<Map<String,String>>getdata9() {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            try
            {
                ConnectionClass connectionClass = new ConnectionClass();
                connect=connectionClass.CONN();
                if (connect == null)
                    ConnectionResult = "Check your Internet Connection";
                else
                {
                    String query= "Select * from stationdashboard where Line = 9";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        Map<String,String> datanum = new HashMap<>();
                        datanum.put("Status",status);
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
                        datanum.put("Line",Line);
                        datanum.put("Station",Station);
                        data.add(datanum);
                    }
                    ConnectionResult="Successful";
                    isSuccess=true;
                    connect.close();
                }
            }catch (Exception ex)
            {
                isSuccess=false;
                ConnectionResult=ex.getMessage();
            }
            return data;
        }
        public List<Map<String,String>>getdata10() {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            try
            {
                ConnectionClass connectionClass = new ConnectionClass();
                connect=connectionClass.CONN();
                if (connect == null)
                    ConnectionResult = "Check your Internet Connection";
                else
                {
                    String query= "Select * from stationdashboard where Line = 10";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        Map<String,String> datanum = new HashMap<>();
                        datanum.put("Status",status);
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
                        datanum.put("Line",Line);
                        datanum.put("Station",Station);
                        data.add(datanum);
                    }
                    ConnectionResult="Successful";
                    isSuccess=true;
                    connect.close();
                }
            }catch (Exception ex)
            {
                isSuccess=false;
                ConnectionResult=ex.getMessage();
            }
            return data;
        }
        public List<Map<String,String>>getdata11() {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            try
            {
                ConnectionClass connectionClass = new ConnectionClass();
                connect=connectionClass.CONN();
                if (connect == null)
                    ConnectionResult = "Check your Internet Connection";
                else
                {
                    String query= "Select * from stationdashboard where Line = 11";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        String status = rs.getString("Status");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        Map<String,String> datanum = new HashMap<>();
                        datanum.put("Status",status);
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
                        datanum.put("Line",Line);
                        datanum.put("Station",Station);
                        data.add(datanum);
                    }
                    ConnectionResult="Successful";
                    isSuccess=true;
                    connect.close();
                }
            }catch (Exception ex)
            {
                isSuccess=false;
                ConnectionResult=ex.getMessage();
            }
            return data;
        }
    }
}


