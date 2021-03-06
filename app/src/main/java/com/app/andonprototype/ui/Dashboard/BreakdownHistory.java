package com.app.andonprototype.ui.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.R;
import com.app.andonprototype.ui.MachineReport.DetailMachineReport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BreakdownHistory extends AppCompatActivity implements ListView.OnItemClickListener {
    Connection connect;
    private String MachineID, Number, Validation, Line, Station;
    String ConnectionResult = "";
    ListView BreakdownHistory;
    SimpleAdapter ABH;
    Boolean isSuccess = false;
    TextView Machine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakdown_history);
        BreakdownHistory = findViewById(R.id.ListHistory);
        BreakdownHistory.setOnItemClickListener(this);
        Machine = findViewById(R.id.dataMachineID);
        Line = getIntent().getStringExtra("Line");
        Station = getIntent().getStringExtra("Station");
        validation();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                validation();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    // Tampilkan history mesin dari database
    public void getHistory() {
        List<Map<String, String>> HistoryList = null;
        HistoryList = getData();
        String[] fromwhere = {"Line", "Station", "Repair_Time_Start", "Repair_Time_Finish", "Repair_Duration"};
        int[] viewwhere = {R.id.Line, R.id.Station, R.id.RepairTimeStart, R.id.RepairTimeFinish, R.id.Duration};
        ABH = new SimpleAdapter(BreakdownHistory.this, HistoryList, R.layout.list_history, fromwhere, viewwhere);
        BreakdownHistory.setAdapter(ABH);
    }

    // Ambil history mesin dari database
    public List<Map<String, String>> getData() {
        List<Map<String, String>> data = null;
        data = new ArrayList<Map<String, String>>();

        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select No,MachineID,Line,Station,Repair_Time_Start,Repair_Time_Finish,Repair_Duration" +
                        " from machinestatustest where Line = '" + Line + "' and Station = '" + Station + "' ORDER BY Repair_Time_Start DESC";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String No = rs.getString("No");
                    String Line = rs.getString("Line");
                    String MachineID = rs.getString("MachineID");
                    String Station = rs.getString("Station");
                    String Repair_Time_Start = rs.getString("Repair_Time_Start");
                    String Repair_Time_Finish = rs.getString("Repair_Time_Finish");
                    String Repair_Duration = rs.getString("Repair_Duration");
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("No", No);
                    datanum.put("MachineID", MachineID);
                    datanum.put("Line", Line);
                    datanum.put("Station", Station);
                    datanum.put("Repair_Time_Start", Repair_Time_Start);
                    datanum.put("Repair_Time_Finish", Repair_Time_Finish);
                    datanum.put("Repair_Duration", Repair_Duration);
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

    // Jika tidak ada history yang ditampilkan, tampilkan 'No Data Found'
    public void validation() {
        ValidationData();
        if (Validation == null) {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            getHistory();
        }
    }

    // Periksa semua history mesin pada line dan station yang ditentukan
    public void ValidationData() {
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select No from machinestatustest where Line='" + Line + "' and Station = '" + Station + "'";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    Validation = rs.getString("No");
                }
                ConnectionResult = "Successfull";
                connect.close();
            }
        } catch (Exception ex) {
            ConnectionResult = ex.getMessage();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, DetailMachineReport.class);
        Map<String, String> mp;
        mp = (Map<String, String>) parent.getItemAtPosition(position);
        Object No = mp.get("No");
        Number = No.toString();
        i.putExtra("No", Number);
        startActivity(i);
    }
}
