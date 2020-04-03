package com.app.andonprototype.ui.Report;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.Background.SaveSharedPreference;
import com.app.andonprototype.R;
import com.app.andonprototype.ui.MachineReport.DetailMachineReport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ReportActivityFragment extends Fragment implements ReportListAdapter.OnNoteListener {
    public String id, Station, Line, Number, Person;
    public String PIC; //ID dari pengguna
    Spinner spinner;
    RecyclerView recyclerView;
    private boolean success = false;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;
    ArrayList NameArray;
    int nama;
    List<Map<String, String>> MyReportList;

    private ReportActivityViewModel mainDashboardViewModel;

    private ArrayList<ReportListItems> itemArrayList;  //List items Array
    private ReportListAdapter myAppAdapter; //Array Adapter
    private RecyclerView.LayoutManager mLayoutManager;

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
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        PIC = SaveSharedPreference.getNama(getActivity()); //Untuk mendapat nama 'admin'//
        GetPIC();
        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReport();
                if (itemArrayList.isEmpty()) {
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


    // Ambil semua history data mesin yang dikerjakan oleh user tersebut
    private void getRep() {
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
                    itemArrayList.add(new ReportListItems(rs.getString("No"),
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

    // Tampilkan semua history tersebut di database
    private void getReport() {
        itemArrayList = new ArrayList<>();
        getRep();
        myAppAdapter = new ReportListAdapter(itemArrayList, this, getActivity());
        recyclerView.setAdapter(myAppAdapter);
    }

    // Ambil data PIC dari database
    private void GetPIC() {
        String z = "";
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connectionClass == null) {
                z = "Check Your Internet Connection";
            } else {
                String query = "SELECT Nama FROM userid ORDER BY No OFFSET 1 ROWS";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                NameArray = new ArrayList();
                while (rs.next()) {
                    NameArray.add(rs.getString("Nama"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            z = "Check Your Internet Connection";
        }
        ArrayAdapter adapter = new ArrayAdapter(
                getActivity(), R.layout.spinner_item, NameArray);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onNoteClick(int position) {
        Intent i = new Intent(getActivity(), DetailMachineReport.class);
        Number = itemArrayList.get(position).getNo();
        i.putExtra("No", Number);
        startActivity(i);
    }
}
