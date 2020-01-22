package com.app.andonprototype.ui.Report;

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
import com.app.andonprototype.ui.MachineReport.DetailMachineReport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReportActivityFragment extends Fragment implements ListView.OnItemClickListener{
    public String id,Mesin,Station,Duration,Line,Number,Person;
    Spinner spinner;
    ListView ListReport;
    SimpleAdapter AR;
    private boolean success = false;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;
    ArrayList NameArray;
    int nama;
    List<Map<String, String>> MyReportList;

    private ReportActivityViewModel mainDashboardViewModel;

    public static ReportActivityFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        ReportActivityFragment thirdFragment = new ReportActivityFragment();
        thirdFragment.setArguments(args);
        return thirdFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mainDashboardViewModel =
                ViewModelProviders.of(this).get(ReportActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reportactivity, container, false);
        spinner = root.findViewById(R.id.spinner);
        ListReport = root.findViewById(R.id.ListReport);
        ListReport.setOnItemClickListener(this);
        GetPIC();
        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReport();
                if (MyReportList.isEmpty()){
                    Toast.makeText(getActivity(), "No Report Activity", Toast.LENGTH_SHORT).show();
                }
                pullToRefresh.setRefreshing(false);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nama = NameArray.indexOf(spinner.getSelectedItem());
                Person = (String) parent.getItemAtPosition(nama);
                getReport();
                if (MyReportList.isEmpty()){
                    Toast.makeText(getActivity(), "No Report Activity", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return root;
    }

    private void getReport() {
        MyReportList = getRep();
        String[] fromwhere = {"MachineID", "Line", "Station", "Repair_Time_Start", "Repair_Time_Finish", "Repair_Duration", "PIC"};
        int[] viewwhere = {R.id.MachineID, R.id.Line, R.id.Station, R.id.RepairTimeStart, R.id.RepairTimeFinish, R.id.Duration, R.id.PIC};
        AR = new SimpleAdapter(getActivity(), MyReportList, R.layout.report_activity_listitem, fromwhere, viewwhere);
        ListReport.setAdapter(AR);
    }
    private List<Map<String, String>> getRep() {
        List<Map<String, String>> data = null;
        data = new ArrayList<Map<String, String>>();
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select No,MachineID,Line,Station,Repair_Time_Start," +
                        "Repair_Time_Finish,PIC," +
                        "Repair_Duration from machinestatustest where PIC='" + Person + "' ORDER BY Response_Time_Finish DESC";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String No = rs.getString("No");
                    String MachineID = rs.getString("MachineID");
                    String Line = rs.getString("Line");
                    String Station = rs.getString("Station");
                    String Repair_Time_Start = rs.getString("Repair_Time_Start");
                    String Repair_Time_Finish = rs.getString("Repair_Time_Finish");
                    String PIC = rs.getString("PIC");
                    String Repair_Duration = rs.getString("Repair_Duration");
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("No", No);
                    datanum.put("MachineID", MachineID);
                    datanum.put("Line", Line);
                    datanum.put("Station", Station);
                    datanum.put("Repair_Time_Start",Repair_Time_Start);
                    datanum.put("Repair_Time_Finish",Repair_Time_Finish);
                    datanum.put("Repair_Duration",Repair_Duration);
                    datanum.put("PIC",PIC);
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
    private void GetPIC(){
        String z = "";
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connectionClass == null){
                z = "Check Your Internet Connection";
            }
            else{
                String query = "SELECT Nama FROM userid ORDER BY No OFFSET 1 ROWS";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                NameArray = new ArrayList();
                while(rs.next()){
                    NameArray.add(rs.getString("Nama"));
                }
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
            z = "Check Your Internet Connection";
        }
        ArrayAdapter adapter = new ArrayAdapter(
                getActivity(),R.layout.spinner_item,NameArray);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent i = new Intent(getActivity(), DetailMachineReport.class);
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
