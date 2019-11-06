package com.example.andonprototype.Dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

public class ProblemWaitingList extends AppCompatActivity implements ListView.OnItemClickListener {
    public String pic,Line,Station,MachineID,Status,Person;
    private ListView ListProblem;
    private SimpleAdapter AP;
    public ImageView imageView;
    public SaveSharedPreference saveSharedPreference;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    String currentDateStart = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_waiting_list);
        saveSharedPreference = new SaveSharedPreference();
        ListProblem = findViewById(R.id.ListProblem);
        ListProblem.setOnItemClickListener(this);
        imageView = findViewById(R.id.image);
        getProblem();
        pic = saveSharedPreference.getID(this);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProblem();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    public void getProblem() {
        List<Map<String, String>> MyProblemList = null;
        GetProblem myProblem = new GetProblem();
        MyProblemList = myProblem.getProblem();
        String[] fromwhere = {"Image", "MachineID", "Line", "Station"};
        int[] viewwhere = {R.id.image, R.id.MachineID, R.id.Line, R.id.Station};
        AP = new SimpleAdapter(ProblemWaitingList.this, MyProblemList, R.layout.listitem, fromwhere, viewwhere);
        ListProblem.setAdapter(AP);
    }

    public void validateData(){

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
                        switch (status) {
                            case "1": {
                                int i = 0;
                                datanum.put("Image", Integer.toString((listviewImage[i])));
                                break;
                            }
                            case "2": {
                                int i = 1;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "3": {
                                int i = 2;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
                            case "4": {
                                int i = 3;
                                datanum.put("Image", Integer.toString(listviewImage[i]));
                                break;
                            }
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
            Toast.makeText(this, "ID not Detected, Please Re-login to verify", Toast.LENGTH_SHORT).show();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
            } else {
                Intent i = new Intent(this, clss);
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
                    Object person = mp.get("PIC");
                    switch (Status){
                        case "3":
                            if (person != null) {
                                Person = person.toString();
                                if (person.equals(pic)) {
                                    startActivity(i);
                                } else {
                                    Toast.makeText(this, Person + " is currently repairing", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Another PIC is currently repairing", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "4":
                            if(person != null){
                                Person = person.toString();
                                Toast.makeText(this, "Waiting for Production Approval, Done by " + Person, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(this, "Waiting for Production Approval", Toast.LENGTH_SHORT).show();
                            }
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
