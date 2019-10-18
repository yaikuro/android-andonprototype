package com.example.andonprototype;

import android.os.Bundle;
import android.view.View;
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

public class ReportActivity extends AppCompatActivity {
    public String id;
    public String Mesin;
    ListView ListReport;
    SimpleAdapter AR;
    public ImageView imageView;
    public EditText edtInsertID;
    private boolean success = false;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;
    int[] listviewImage = new int[]
            {
                    R.drawable.green,
                    R.drawable.red,
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ListReport = (ListView) findViewById(R.id.ListReport);
        imageView = (ImageView) findViewById(R.id.image);
        Button btnSearch = findViewById(R.id.btnSearch);
        edtInsertID = findViewById(R.id.edtInsertID);
        edtInsertID.setText(getIntent().getStringExtra("PICR"));
        id = edtInsertID.getText().toString();
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

    public List<Map<String, String>> getRep() {
        List<Map<String, String>> data = null;
        data = new ArrayList<Map<String, String>>();

        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select MachineID,Line,Station,Status from machinestatustest where PIC=" + id;
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String status = rs.getString("Status");
                    String MachineID = rs.getString("MachineID");
                    String Line = rs.getString("Line");
                    String Station = rs.getString("Station");
                    Mesin = rs.getString("MachineID");
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("Status", status);
                    //int i = Integer.parseInt(status);
                    if (status.equals("1")) {
                        int i = 0;
                        datanum.put("Image", Integer.toString(listviewImage[i]));
                    } else if (status.equals("2")) {
                        int i = 1;
                        datanum.put("Image", Integer.toString(listviewImage[i]));
                    }
                    datanum.put("MachineID", MachineID);
                    datanum.put("Line", Line);
                    datanum.put("Station", Station);
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

    public void getReport() {
        List<Map<String, String>> MyReportList = null;
        MyReportList = getRep();
        String[] fromwhere = {"Image", "MachineID", "Line", "Station"};
        int[] viewwhere = {R.id.image, R.id.MachineID, R.id.Line, R.id.Station};
        AR = new SimpleAdapter(ReportActivity.this, MyReportList, R.layout.listitem, fromwhere, viewwhere);
        ListReport.setAdapter(AR);
        Toast.makeText(this, Mesin, Toast.LENGTH_SHORT).show();
    }
}
