package com.app.andonprototype.ui.MachineReport;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MachineReportActivityFragment extends Fragment implements ListView.OnItemClickListener {
    public String id;
    public String Station;
    public String Line;
    public String Number;
    ListView ListMachineReport;
    SimpleAdapter AR;
    public ImageView imageView;
    public EditText edtInsertID;
    private boolean success = false;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;
    Spinner LineSpinner, StationSpinner;
    ArrayList LineArray, StationArray;
    int line, station;
    List<Map<String, String>> MachineReportList;

    private MachineReportActivityViewModel mainDashboardViewModel;

    public static MachineReportActivityFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        MachineReportActivityFragment thirdFragment = new MachineReportActivityFragment();
        thirdFragment.setArguments(args);
        return thirdFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainDashboardViewModel = ViewModelProviders.of(this).get(MachineReportActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_machinereportactivity, container, false);
        LineSpinner = root.findViewById(R.id.spinnerLine);
        StationSpinner = root.findViewById(R.id.spinnerStation);
        ListMachineReport = root.findViewById(R.id.ListMachineReport);
        ListMachineReport.setOnItemClickListener(this);
        GetLine();
        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMachineReport();
                if (MachineReportList.isEmpty()) {
                    Toast.makeText(getActivity(), "No Report Activity", Toast.LENGTH_SHORT).show();
                }
                pullToRefresh.setRefreshing(false);
            }
        });
        LineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                line = LineArray.indexOf(LineSpinner.getSelectedItem());
                Line = (String) parent.getItemAtPosition(line);
                GetStation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        StationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                station = StationArray.indexOf(StationSpinner.getSelectedItem());
                Station = (String) parent.getItemAtPosition(station);
                getMachineReport();
                if (MachineReportList.isEmpty()) {
                    Toast.makeText(getActivity(), "No Report Activity", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return root;
    }

    private void getMachineReport() {
        MachineReportList = getRep();
        String[] fromwhere = {"MachineID", "Line", "Station", "Repair_Time_Start", "Repair_Time_Finish", "Repair_Duration", "PIC"};
        int[] viewwhere = {R.id.MachineID, R.id.Line, R.id.Station, R.id.RepairTimeStart, R.id.RepairTimeFinish, R.id.Duration, R.id.PIC};
        AR = new SimpleAdapter(getActivity(), MachineReportList, R.layout.report_activity_listitem, fromwhere, viewwhere);
        ListMachineReport.setAdapter(AR);
    }

    private List<Map<String, String>> getRep() {
        List<Map<String, String>> data;
        data = new ArrayList<Map<String, String>>();

        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select No,MachineID,Line,Station,Repair_Time_Start," +
                        "Repair_Time_Finish,PIC," +
                        "Repair_Duration from machinestatustest where Line='" + Line + "' and Station = '" + Station + "'ORDER BY Response_Time_Finish DESC";
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
                    String PIC = rs.getString("PIC");
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("No", No);
                    datanum.put("MachineID", MachineID);
                    datanum.put("Line", Line);
                    datanum.put("Station", Station);
                    datanum.put("Repair_Time_Start", Repair_Time_Start);
                    datanum.put("Repair_Time_Finish", Repair_Time_Finish);
                    datanum.put("Repair_Duration", Repair_Duration);
                    datanum.put("PIC", PIC);
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

    public String GetLine() {
        String z = "";
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connectionClass == null) {
                z = "Check Your Internet Connection";
            } else {
                String query = "Select Line from stationdashboard group by Line";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                LineArray = new ArrayList();
                StationArray = new ArrayList();
                while (rs.next()) {
                    LineArray.add(rs.getString("Line"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            z = "Check Your Internet Connection";
        }
        ArrayAdapter adapter = new ArrayAdapter(
                getActivity(), R.layout.spinner_item, LineArray);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        LineSpinner.setAdapter(adapter);
        return z;
    }

    public String GetStation() {
        String z = "";
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connectionClass == null) {
                z = "Check Your Internet Connection";
            } else {
                String query = "Select Station from stationdashboard where Line = '" + Line + "'";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                StationArray = new ArrayList();
                while (rs.next()) {
                    StationArray.add(rs.getString("Station"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            z = "Check Your Internet Connection";
        }
        ArrayAdapter adapter = new ArrayAdapter(
                getActivity(), R.layout.spinner_item, StationArray);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        StationSpinner.setAdapter(adapter);
        return z;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), DetailMachineReport.class);
        Map<String, String> mp = (Map<String, String>) parent.getItemAtPosition(position);
        Object No = mp.get("No");
        Number = No.toString();
        i.putExtra("No", Number);
        //Toast.makeText(this, Mesin, Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}
