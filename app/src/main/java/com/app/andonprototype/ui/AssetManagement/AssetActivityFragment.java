package com.app.andonprototype.ui.AssetManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class AssetActivityFragment extends Fragment implements ListView.OnItemClickListener {
    public String id;
    public String Station;
    public String Line;
    private ListView ListMachineAsset;
    private Connection connect;
    private Spinner LineSpinner, StationSpinner;
    private ArrayList<String> LineArray;
    private ArrayList<String> StationArray;
    private int line, station;
    List<Map<String, String>> MachineReportList;

    private AssetActivityViewModel mainDashboardViewModel;

    public static AssetActivityFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        AssetActivityFragment fourthFragment = new AssetActivityFragment();
        fourthFragment.setArguments(args);
        return fourthFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        mainDashboardViewModel =
                ViewModelProviders.of(this).get(AssetActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_assetmanagementactivity, container, false);
        LineSpinner = root.findViewById(R.id.spinnerLine);
        StationSpinner = root.findViewById(R.id.spinnerStation);
        ListMachineAsset = root.findViewById(R.id.ListMachineAsset);
        ListMachineAsset.setOnItemClickListener(this);
        GetLine();
        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMachineReport();
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
                    Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
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
        String[] fromwhere = {"Machine_Name"};
        int[] viewwhere = {R.id.MachineName};
        SimpleAdapter AR = new SimpleAdapter(getActivity(), MachineReportList, R.layout.asset_list, fromwhere, viewwhere);
        ListMachineAsset.setAdapter(AR);
    }

    private List<Map<String, String>> getRep() {
        List<Map<String, String>> data;
        data = new ArrayList<>();
        String ConnectionResult;
        Boolean isSuccess = false;
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select Machine_Name " +
                        "from machinelist " +
                        "where Line='" + Line + "' and Station='" + Station + "' " +
                        "group by Machine_Name";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String MachineName = rs.getString("Machine_Name");
                    Map<String, String> datanum = new HashMap<>();
                    datanum.put("Machine_Name", MachineName);
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

    private void GetLine() {
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            String query = "Select Line from stationdashboard group by Line";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            LineArray = new ArrayList<>();
            while (rs.next()) {
                LineArray.add(rs.getString("Line"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                Objects.requireNonNull(getActivity()), R.layout.spinner_item, LineArray);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        LineSpinner.setAdapter(adapter);
    }

    private void GetStation() {
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            String query = "Select Station from stationdashboard where Line = '" + Line + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            StationArray = new ArrayList<>();
            while (rs.next()) {
                StationArray.add(rs.getString("Station"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                Objects.requireNonNull(getActivity()), R.layout.spinner_item, StationArray);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        StationSpinner.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), AssetManagementReport.class);
        Map<String, String> mp = (Map<String, String>) parent.getItemAtPosition(position);
        Object MachineName = mp.get("Machine_Name");
        String name = MachineName.toString();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String format = "1";
        i.putExtra("Name", name);
        i.putExtra("Format", format);
        i.putExtra("Current_Date", currentDate);
        //Toast.makeText(this, Mesin, Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}
