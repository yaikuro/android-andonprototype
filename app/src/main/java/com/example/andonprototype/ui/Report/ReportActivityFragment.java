package com.example.andonprototype.ui.Report;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.andonprototype.Background.SaveSharedPreference.getID;


public class ReportActivityFragment extends Fragment implements ListView.OnItemClickListener{
    public String id,Mesin,Station,Duration,Line,Number;
    ListView ListReport;
    SimpleAdapter AR;
    public ImageView imageView;
    public EditText edtInsertID;
    private boolean success = false;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

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

        ListReport = root.findViewById(R.id.ListReport);
        ListReport.setOnItemClickListener(this);
        Button btnSearch = root.findViewById(R.id.btnSearch);
        edtInsertID = root.findViewById(R.id.edtInsertID);
        id = getID(getActivity());
        edtInsertID.setText(id);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = edtInsertID.getText().toString();
                getReport();
            }
        });

        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReport();
                pullToRefresh.setRefreshing(false);
            }
        });
        return root;
    }

    public void getReport() {
        List<Map<String, String>> MyReportList = null;
        MyReportList = getRep();
        String[] fromwhere = {"MachineID", "Line", "Station", "Repair_Time_Start", "Repair_Time_Finish", "Repair_Duration"};
        int[] viewwhere = {R.id.MachineID, R.id.Line, R.id.Station, R.id.RepairTimeStart, R.id.RepairTimeFinish, R.id.Duration};
        AR = new SimpleAdapter(getActivity(), MyReportList, R.layout.report_activity_listitem, fromwhere, viewwhere);
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
                        "Repair_Duration from machinestatustest where PIC='" + id + "' ORDER BY Response_Time_Finish DESC";
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
        Intent i = new Intent(getActivity(),DetailReport.class);
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
