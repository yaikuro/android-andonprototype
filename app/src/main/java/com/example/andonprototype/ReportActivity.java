package com.example.andonprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.andonprototype.Background.ConnectionClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    public String id;
    public String Mesin;
    public String Station;
    public String Duration;
    public String Line;
    public String Number;
    ListView ListReport;
    SimpleAdapter AR;
    public ImageView imageView;
    public EditText edtInsertID;
    private boolean success = false;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ListReport = (ListView) findViewById(R.id.ListReport);
        ListReport.setOnItemClickListener(this);
        Button btnSearch = findViewById(R.id.btnSearch);
        edtInsertID = findViewById(R.id.edtInsertID);
        edtInsertID.setText(getIntent().getStringExtra("PIC"));
        id = getIntent().getStringExtra("PIC");
        getReport();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = edtInsertID.getText().toString();
                getReport();
            }
        });
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReport();
                pullToRefresh.setRefreshing(false);
            }
        });
    }
    public void getReport() {
        List<Map<String, String>> MyReportList = null;
        MyReportList = getRep();
        String[] fromwhere = {"MachineID", "Line", "Station", "Repair_Time_Start", "Repair_Time_Finish", "Repair_Duration"};
        int[] viewwhere = {R.id.MachineID, R.id.Line, R.id.Station, R.id.RepairTimeStart, R.id.RepairTimeFinish, R.id.Duration};
        AR = new SimpleAdapter(ReportActivity.this, MyReportList, R.layout.report_activity_listitem, fromwhere, viewwhere);
        ListReport.setAdapter(AR);
    }
    public List<Map<String, String>> getRep() {
        List<Map<String, String>> data = null;
        data = new ArrayList<Map<String, String>>();

        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select No,MachineID,Line,Station,CONVERT(VARCHAR(10),Repair_Time_Start) AS Repair_Time_Start," +
                        "CONVERT(VARCHAR(10),Repair_Time_Finish) AS Repair_Time_Finish," +
                        "Repair_Duration from machinestatustest where PIC='" + id + "'";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String No = rs.getString("No");
                    String MachineID = rs.getString("MachineID");
                    String Line = rs.getString("Line");
                    String Station = rs.getString("Station");
                    String Repair_Time_Start = rs.getString("Repair_Time_Start");
                    String Repair_Time_Finish = rs.getString("Repair_Time_Finish");
                    String Repair_Duration = rs.getString("Repair_Duration");
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("No", No);
                    datanum.put("MachineID", MachineID);
                    datanum.put("Line", Line);
                    datanum.put("Station", Station);
                    datanum.put("Repair_Time_Start",Repair_Time_Start);
                    datanum.put("Repair_Time_Finish",Repair_Time_Finish);
                    datanum.put("Repair_Duration",Repair_Duration);
                    data.add(datanum);
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return data;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent i = new Intent(this,DetailReport.class);
        Map<String,String> mp = (Map<String, String>) ListReport.getItemAtPosition(position);
        Object No = mp.get("No");
        Object machine = mp.get("MachineID");
        Object line = mp.get("Line");
        Object station = mp.get("Station");
        Object duration = mp.get("Repair_Duration");
        Mesin = machine.toString();
        Line = line.toString();
        Station = station.toString();
        Duration = duration.toString();
        Number = No.toString();
        i.putExtra("No", Number);
        //Toast.makeText(this, Mesin, Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}
