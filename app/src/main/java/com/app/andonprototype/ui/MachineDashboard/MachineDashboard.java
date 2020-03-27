package com.app.andonprototype.ui.MachineDashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.Background.SaveSharedPreference;
import com.app.andonprototype.R;
import com.app.andonprototype.barcodescanner.SimpleScanner;
import com.app.andonprototype.ui.Dashboard.MainDashboard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class MachineDashboard extends AppCompatActivity implements MachineDashboardAdapter.OnPushListener {
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private final LinkedList<String> mLineList = new LinkedList<>();
    public Boolean isSuccess, success;
    public String Line, Station, PIC, Person;
    ImageView imageView;
    Connection connection;
    ProgressBar pbbar;
    int Status;
    FloatingActionButton refresh;
    String currentDateStart = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
    RecyclerView recyclerView11, lineRecyclerView;
    private ArrayList<MachineListItems> itemsArrayList;
    private LineListAdapter mAdapter;

    private MachineDashboardAdapter machineDashboardAdapter11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_dashboard);
        recyclerView11 = findViewById(R.id.recyclerView11);
        lineRecyclerView = findViewById(R.id.line_recyclerView);

        lineRecyclerViewSetup();
        recyclerSetup();

        isSuccess = false;
        success = false;
        pbbar = findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        PIC = SaveSharedPreference.getNama(this);
        refresh = findViewById(R.id.refresh);
        imageView = findViewById(R.id.image);

        Loading loading = new Loading();
        loading.execute();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(MachineDashboard.this, MachineDashboard.class);
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
                startActivity(i);
            }
        });
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                Intent i = new Intent(MachineDashboard.this, MachineDashboard.class);
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    // Menampilkan Line pada Machine Dashboard
    public void lineRecyclerViewSetup() {
        for (int i = 1; i < 12; i++) {
            mLineList.addLast("Line " + i);
        }
        mAdapter = new LineListAdapter(this, mLineList);
        lineRecyclerView.setAdapter(mAdapter);
        int numberOfColumns = 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns, GridLayoutManager.HORIZONTAL, false);
        lineRecyclerView.setLayoutManager(gridLayoutManager);
    }

    // Atur format Line yang ingin ditampilkan
    public void recyclerSetup() {
        recyclerView11.setHasFixedSize(true);
        int numberOfColumns = 6;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns, GridLayoutManager.HORIZONTAL, false);
        recyclerView11.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainDashboard.class);
        startActivity(i);
    }


    @Override
    public void onPushClick(int position) {
        Class<?> clss = SimpleScanner.class;
        if (PIC.equals("")) {
            Toast.makeText(this, "ID Required, Try to Re-Login", Toast.LENGTH_SHORT).show();
        } else {
            if (ContextCompat.checkSelfPermission(MachineDashboard.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MachineDashboard.this,
                        new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
            } else {
                Intent i = new Intent(this, clss);
                Line = itemsArrayList.get(position).getLine();
                Station = itemsArrayList.get(position).getStation();
                Status = itemsArrayList.get(position).getStatus() + 1;
                i.putExtra("Line", Line);
                i.putExtra("Station", Station);
                i.putExtra("PIC", PIC);
                if (PIC.equals("admin")) {
                    startActivity(i);
                } else {
                    String person = itemsArrayList.get(position).getPIC();
                    switch (Status) {
                        case 1:
                            Toast.makeText(this, "Machine is Running", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            startActivity(i);
                            break;
                        case 3:
                            if (person != null) {
                                Person = person;
                                if (person.equals(PIC)) {
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
                    }
                }
            }
        }
    }

    public void getStatusInfo() {
        itemsArrayList = new ArrayList<>();
        getData();
        machineDashboardAdapter11 = new MachineDashboardAdapter(itemsArrayList, this, this);
    }

    public void setAdapter() {
        recyclerView11.setAdapter(machineDashboardAdapter11);
    }

    public void getData() {
        Boolean isSuccess = false;
        String ConnectionResult;
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            if (connection == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select * " +
                        "from stationdashboard";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String status = rs.getString("Status");
                    int i = Integer.parseInt(status) - 1;
                    itemsArrayList.add(new MachineListItems(i, rs.getString("Line"), rs.getString("Station"), rs.getString("PIC")));
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                connection.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
    }

    public class Loading extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);

            //do initialization of required objects objects here
        }

        @Override
        protected Void doInBackground(Void... params) {
            getStatusInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            setAdapter();
            pbbar.setVisibility(View.GONE);
        }

    }
}


