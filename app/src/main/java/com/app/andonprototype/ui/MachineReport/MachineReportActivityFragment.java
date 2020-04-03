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
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.R;
import com.app.andonprototype.ui.Report.ReportListAdapter;
import com.app.andonprototype.ui.Report.ReportListItems;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MachineReportActivityFragment extends Fragment implements ReportListAdapter.OnNoteListener {
    public String id;
    public String Station;
    public String Line;
    public String Number;
    RecyclerView ListMachineReport;
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

    private MachineReportActivityViewModel mainDashboardViewModel;

    private ArrayList<ReportListItems> itemArrayList;  //List items Array
    private ReportListAdapter myAppAdapter; //Array Adapter
    private RecyclerView.LayoutManager mLayoutManager;

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
        ListMachineReport.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        ListMachineReport.setLayoutManager(mLayoutManager);
        GetLine();
        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMachineReport();
                if (itemArrayList.isEmpty()) {
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
                if (itemArrayList.isEmpty()) {
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
        itemArrayList = new ArrayList<>();
        getRep();
        myAppAdapter = new ReportListAdapter(itemArrayList, this, getActivity());
        ListMachineReport.setAdapter(myAppAdapter);
    }

    private void getRep() {
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
                    itemArrayList.add(new ReportListItems(
                            rs.getString("No"),
                            rs.getString("MachineID"),
                            rs.getString("Line"),
                            rs.getString("Station"),
                            rs.getString("Repair_Time_Start"),
                            rs.getString("Repair_Time_Finish"),
                            rs.getString("Repair_Duration"),
                            rs.getString("PIC")));
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
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
    public void onNoteClick(int position) {
        Intent i = new Intent(getActivity(), DetailMachineReport.class);
        Number = itemArrayList.get(position).getNo();
        i.putExtra("No", Number);
        startActivity(i);
    }
}
