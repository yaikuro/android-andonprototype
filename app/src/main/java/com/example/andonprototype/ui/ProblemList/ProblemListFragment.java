package com.example.andonprototype.ui.ProblemList;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.Background.Query;
import com.example.andonprototype.R;
import com.example.andonprototype.SaveSharedPreference;
import com.example.andonprototype.barcodescanner.SimpleScanner;

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


public class ProblemListFragment extends Fragment implements ListView.OnItemClickListener {
    public String pic,Line,Station,MachineID,Status,Person;
    private ListView ListProblem;
    private SimpleAdapter AP;
    public ImageView imageView;
    public SaveSharedPreference saveSharedPreference;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    String currentDateStart = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());

    private ProblemListViewModel problemListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        problemListViewModel =
                ViewModelProviders.of(this).get(ProblemListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_problemwaitinglist, container, false);
//        final TextView textView = root.findViewById(R.id.text_problemwaitinglist);
//        problemListViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        ListProblem = root.findViewById(R.id.ListProblem);
        ListProblem.setOnItemClickListener(this);
        imageView = root.findViewById(R.id.image);
        getProblem();
        pic = SaveSharedPreference.getID(getActivity());
        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProblem();
                pullToRefresh.setRefreshing(false);
            }
        });


        return root;
    }


    public void getProblem() {
        List<Map<String, String>> MyProblemList = null;
        GetProblem myProblem = new GetProblem();
        MyProblemList = myProblem.getProblem();
        String[] fromwhere = {"Image", "MachineID", "Line", "Station"};
        int[] viewwhere = {R.id.image, R.id.MachineID, R.id.Line, R.id.Station};
        AP = new SimpleAdapter(getActivity(), MyProblemList, R.layout.listitem, fromwhere, viewwhere);
        ListProblem.setAdapter(AP);
    }

    public static class GetProblem {
        Connection connect;
        String ConnectionResult = "";

        Boolean isSuccess = false;
        int[] listviewImage = new int[]
                {
                        R.drawable.green,
                        R.drawable.red,
                        R.drawable.yellow,
                        R.drawable.blue
                };

        public List<Map<String, String>> getProblem() {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            try {
                ConnectionClass connectionClass = new ConnectionClass();
                connect = connectionClass.CONN();
                if (connect == null) {
                    ConnectionResult = "Check your Internet Connection";
                } else {
                    String query = Query.problemquery;
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String status = rs.getString("Status");
                        String MachineID = rs.getString("MachineID");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        String Person = rs.getString("PIC");
                        Map<String, String> datanum = new HashMap<>();
                        datanum.put("Status", status);
                        if (status.equals("1")) {
                            int i = 0;
                            datanum.put("Image", Integer.toString((listviewImage[i])));
                        } else if (status.equals("2")) {
                            int i = 1;
                            datanum.put("Image", Integer.toString(listviewImage[i]));
                        } else if (status.equals("3")) {
                            int i = 2;
                            datanum.put("Image", Integer.toString(listviewImage[i]));
                        } else if (status.equals("4")){
                            int i = 3;
                            datanum.put("Image", Integer.toString(listviewImage[i]));
                        }
                        datanum.put("MachineID", MachineID);
                        datanum.put("Line", Line);
                        datanum.put("Station", Station);
                        datanum.put("PIC", Person);
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Class<?> clss = SimpleScanner.class;
        if (pic.equals("")) {
            Toast.makeText(getActivity(), "ID not Detected, Please Re-login to verify", Toast.LENGTH_SHORT).show();
        } else {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
            } else {
                Intent i = new Intent(getActivity(), clss);
                Map<String, String> mp = (Map<String, String>) parent.getItemAtPosition(position);
                Object machine = mp.get("MachineID");
                Object line = mp.get("Line");
                Object station = mp.get("Station");
                Object status = mp.get("Status");
                MachineID = machine.toString();
                Line = line.toString();
                Station = station.toString();
                Status = status.toString();
                i.putExtra("StartTime", currentDateStart);
                i.putExtra("Line", Line);
                i.putExtra("Station", Station);
                i.putExtra("PIC", pic);
                i.putExtra("MachineID", MachineID);
                //admin akan dihapus di final app
                if (pic.equals("admin")){
                    startActivity(i);
                }
                else {
                    switch (Status) {
                        case "3":
                            Object person = mp.get("PIC");
                            if (person != null) {
                                Person = person.toString();
                                if (person.equals(pic)) {
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getActivity(), "Another PIC is currently repairing", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Another PIC is currently repairing", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "4":
                            Toast.makeText(getActivity(), "Waiting for Production Approval", Toast.LENGTH_SHORT).show();
                            break;
                        case "2":
                            startActivity(i);
                            break;
                    }
                }
            }
        }
    }

}