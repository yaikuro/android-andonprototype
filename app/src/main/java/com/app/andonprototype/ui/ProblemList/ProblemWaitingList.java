package com.app.andonprototype.ui.ProblemList;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.ui.MachineDashboard.MachineDashboard;
import com.app.andonprototype.R;
import com.app.andonprototype.barcodescanner.SimpleScanner;
import com.app.andonprototype.drawer_ui.Settings;
import com.app.andonprototype.ui.pop_dialog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.app.andonprototype.Background.SaveSharedPreference.getNama;
import static com.app.andonprototype.ui.Dashboard.MainDashboard.validate;

public class ProblemWaitingList extends AppCompatActivity implements ProblemListAdapter.OnPressListener, pop_dialog.ExampleDialogListener {
    public String pic, Line, Station, MachineID, Person;
    public int Status;
    int itemcount;
    Connection connect;
    private SimpleAdapter AP;
    boolean doubleBackToExitPressedOnce = false;
    TextView textView;
    public ImageView imageView;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    String currentDateStart = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());

    RecyclerView recyclerView;
    private ArrayList<ProblemListItems> itemsArrayList;
    private ProblemListAdapter problemListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_waiting_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        textView = findViewById(R.id.NoProblem);

        validate = false;
        imageView = findViewById(R.id.image);
        getProblem();
        pic = getNama(this);
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
        itemsArrayList = new ArrayList<>();
        GetProblem();
        problemListAdapter = new ProblemListAdapter(itemsArrayList,this,this);
        recyclerView.setAdapter(problemListAdapter);
        itemcount = itemsArrayList.size();
        if (itemcount < 1) {
            textView.setText("Masalah sudah Terambil");
        } else {
            textView.setText("");
        }
    }

    @Override
    public void applyTexts(String username, String password) {

    }

    public void GetProblem () {
        Boolean isSuccess = false;
        String ConnectionResult;
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select * " +
                        "from stationdashboard " +
                        "where Status = 2";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String status = rs.getString("Status");
                    int i = Integer.parseInt(status) - 1;
                    itemsArrayList.add(new ProblemListItems(i, rs.getString("Line"), rs.getString("Station"),rs.getString("PIC")));
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            }
        }catch (Exception ex){
            isSuccess=false;
            ConnectionResult = ex.getMessage();
        }
    }

    @Override
    public void onPressClick(int position) {
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
                Line = itemsArrayList.get(position).getLine();
                Station = itemsArrayList.get(position).getStation();
                Status = itemsArrayList.get(position).getStatus() + 1;
                i.putExtra("Line", Line);
                i.putExtra("Station", Station);
                i.putExtra("PIC", pic);
                //admin akan dihapus di final app
                if (pic.equals("admin")) {
                    startActivity(i);
                } else {
                    String person = itemsArrayList.get(position).getPIC();
                    switch (Status) {
                        case 3:
                            if (person != null) {
                                Person = person;
                                if (person.equals(pic)) {
                                    startActivity(i);
                                } else {
                                    Toast.makeText(this, Person + " is currently repairing", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Another PIC is currently repairing", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 4:
                            if (person == null) {
                                Person = person;
                                Toast.makeText(this, "Waiting for Production Approval, Done by " + Person, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Waiting for Production Approval", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            startActivity(i);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            validate = true;
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
    }

    public void openDialog() {
        pop_dialog exampleDialog = new pop_dialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void listMenuButton(View view) {
        openDialog();
    }

    public void btnLocation(View view) {
        Intent i = new Intent(this, MachineDashboard.class);
        startActivity(i);
    }

    public void btnMoreInfo(View view) {
        Intent i = new Intent(this, Settings.class);
        startActivity(i);
    }
}
