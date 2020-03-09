package com.app.andonprototype.ui.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.R;
import com.app.andonprototype.ui.AssetManagement.AssetManagementReport;

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

public class Change_Part_List extends AppCompatActivity implements AdapterView.OnItemClickListener {
    //TODO: List of Parts to Change in each machine (From Notifications)
    private String No, Name, currentDate;
    ListView ListMachineChangePart;
    SimpleAdapter ACP;
    Boolean isSuccess = false;
    Connection connect;
    String ConnectionResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__part__list);
        ListMachineChangePart = findViewById(R.id.ListMachineChangePart);
        ListMachineChangePart.setOnItemClickListener(this);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        getAsset();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                Intent i = new Intent(Change_Part_List.this, Change_Part_List.class);
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    public void getAsset() {
        List<Map<String, String>> MyPartList;
        MyPartList = getProblem();
        String[] fromwhere = {"Line", "Station", "Machine_Name"};
        int[] viewwhere = {R.id.Line, R.id.Station, R.id.MachineID};
        ACP = new SimpleAdapter(this, MyPartList, R.layout.machine_part_list, fromwhere, viewwhere);
        ListMachineChangePart.setAdapter(ACP);
    }

    public List<Map<String, String>> getProblem() {
        List<Map<String, String>> data = new ArrayList<>();
        currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select Line, Station, Machine_Name " +
                        "from machinelist " +
                        "where DATEDIFF(second, '" + currentDate + "', Due_Date) < 300 " +
                        "group by Machine_Name, Line, Station";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String Line = rs.getString("Line");
                    String Station = rs.getString("Station");
                    String Machine_Name = rs.getString("Machine_Name");
                    Map<String, String> datanum = new HashMap<>();
                    datanum.put("Line", Line);
                    datanum.put("Station", Station);
                    datanum.put("Machine_Name", Machine_Name);
                    data.add(datanum);
                }
                ConnectionResult = "Successfull";
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, String> mp = (Map<String, String>) parent.getItemAtPosition(position);
        Intent i = new Intent(this, AssetManagementReport.class);
        Object MachineName = mp.get("Machine_Name");
        Object Line = mp.get("Line");
        Object Station = mp.get("Station");
        String name = MachineName.toString();
        String format = "2";
        String line = Line.toString();
        String station = Station.toString();
        i.putExtra("Name", name);
        i.putExtra("Format", format);
        i.putExtra("Current_Date", currentDate);
        i.putExtra("Line", line);
        i.putExtra("Station",station);
        startActivity(i);
    }
}
