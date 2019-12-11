package com.example.andonprototype.ui.AssetManagement;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class AssetActivityFragment extends Fragment implements ListView.OnItemClickListener{
    public String id;
    public String Station;
    public String Line;
    private ListView ListMachineAsset;
    private Connection connect;
    private Spinner LineSpinner, StationSpinner;
    private ArrayList<String> LineArray;
    private ArrayList<String> StationArray;
    private int line,station;

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return root;
    }

    private void getMachineReport() {
        List<Map<String, String>> MachineReportList = null;
        MachineReportList = getRep();
        String[] fromwhere = {"Machine_Name"};
        int[] viewwhere = {R.id.MachineName};
        SimpleAdapter AR = new SimpleAdapter(getActivity(), MachineReportList, R.layout.asset_list, fromwhere, viewwhere);
        ListMachineAsset.setAdapter(AR);
    }
    private List<Map<String, String>> getRep() {
        List<Map<String, String>> data = null;
        data = new ArrayList<Map<String, String>>();
        String ConnectionResult;
        Boolean isSuccess = false;
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select Machine_Name " +
                        "from machinelist where Line='" + Line + "' and Station='" + Station + "' " +
                        "group by Machine_Name";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String MachineName = rs.getString("Machine_Name");
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("Machine_Name",MachineName);
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
    private void GetLine(){
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            String query = "Select Line from stationdashboard group by Line";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            LineArray = new ArrayList<String>();
            StationArray = new ArrayList<>();
            while(rs.next()){
                LineArray.add(rs.getString("Line"));
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                Objects.requireNonNull(getActivity()),R.layout.spinner_item,LineArray);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        LineSpinner.setAdapter(adapter);
    }
    private void GetStation(){
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            String query = "Select Station from stationdashboard where Line = '" + Line +"'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            StationArray = new ArrayList<>();
            while(rs.next()){
                StationArray.add(rs.getString("Station"));
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                Objects.requireNonNull(getActivity()),R.layout.spinner_item,StationArray);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        StationSpinner.setAdapter(adapter);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent i = new Intent(getActivity(), AssetManagementReport.class);
        Map<String,String> mp = (Map<String, String>) parent.getItemAtPosition(position);
        Object MachineName = mp.get("Machine_Name");
        String name = MachineName.toString();
        i.putExtra("Name", name);
        //Toast.makeText(this, Mesin, Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}
