package com.app.andonprototype.ui.MachineDashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.Background.SaveSharedPreference;
import com.app.andonprototype.R;
import com.app.andonprototype.barcodescanner.SimpleScanner;
import com.app.andonprototype.ui.Dashboard.MainDashboard;
import com.app.andonprototype.ui.ProblemList.ProblemListItems;

import java.sql.Array;
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

public class MachineDashboard2 extends AppCompatActivity implements MachineDashboardAdapter.OnPushListener {
    ImageView imageView;
    Connection connection;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    ProgressBar pbbar;
    public Boolean isSuccess, success;
    public String Line, Station, PIC, Person;
    int Status;
    Button refresh;
    String currentDateStart = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
    private ArrayList<MachineListItems> itemsArrayList;

    RecyclerView recyclerView11;
    RecyclerView recyclerView10;
    RecyclerView recyclerView9;
    RecyclerView recyclerView8;
    RecyclerView recyclerView7;
    RecyclerView recyclerView6;
    RecyclerView recyclerView5;
    RecyclerView recyclerView4;
    RecyclerView recyclerView3;
    RecyclerView recyclerView2;
    RecyclerView recyclerView1;
    private MachineDashboardAdapter machineDashboardAdapter11;
    private MachineDashboardAdapter machineDashboardAdapter10;
    private MachineDashboardAdapter machineDashboardAdapter9;
    private MachineDashboardAdapter machineDashboardAdapter8;
    private MachineDashboardAdapter machineDashboardAdapter7;
    private MachineDashboardAdapter machineDashboardAdapter6;
    private MachineDashboardAdapter machineDashboardAdapter5;
    private MachineDashboardAdapter machineDashboardAdapter4;
    private MachineDashboardAdapter machineDashboardAdapter3;
    private MachineDashboardAdapter machineDashboardAdapter2;
    private MachineDashboardAdapter machineDashboardAdapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_dashboard2);
        recyclerView11 = findViewById(R.id.recyclerView11);
        recyclerView10 = findViewById(R.id.recyclerView10);
        recyclerView9 = findViewById(R.id.recyclerView9);
        recyclerView8 = findViewById(R.id.recyclerView8);
        recyclerView7 = findViewById(R.id.recyclerView7);
        recyclerView6 = findViewById(R.id.recyclerView6);
        recyclerView5 = findViewById(R.id.recyclerView5);
        recyclerView4 = findViewById(R.id.recyclerView4);
        recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView1 = findViewById(R.id.recyclerView1);

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
                Intent i = new Intent(MachineDashboard2.this, MachineDashboard2.class);
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
                Intent i = new Intent(MachineDashboard2.this, MachineDashboard2.class);
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    public void recyclerSetup(){
        recyclerView11.setHasFixedSize(true);
        recyclerView10.setHasFixedSize(true);
        recyclerView9.setHasFixedSize(true);
        recyclerView8.setHasFixedSize(true);
        recyclerView7.setHasFixedSize(true);
        recyclerView6.setHasFixedSize(true);
        recyclerView5.setHasFixedSize(true);
        recyclerView4.setHasFixedSize(true);
        recyclerView3.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
        recyclerView1.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager11 = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager10 = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager9 = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager8 = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager7 = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager6 = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        recyclerView11.setLayoutManager(layoutManager11);
        recyclerView10.setLayoutManager(layoutManager10);
        recyclerView9.setLayoutManager(layoutManager9);
        recyclerView8.setLayoutManager(layoutManager8);
        recyclerView7.setLayoutManager(layoutManager7);
        recyclerView6.setLayoutManager(layoutManager6);
        recyclerView5.setLayoutManager(layoutManager5);
        recyclerView4.setLayoutManager(layoutManager4);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView1.setLayoutManager(layoutManager1);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainDashboard.class);
        startActivity(i);
    }

    @Override
    public void onPushClick(int position) {
//        Line = itemsArrayList.get(position).getLine();
//        Station = itemsArrayList.get(position).getStation();
//        Status = itemsArrayList.get(position).getStatus() + 1;
//        String person = itemsArrayList.get(position).getPIC();
        String abc = itemsArrayList.get(position).toString();
        Toast.makeText(this, abc, Toast.LENGTH_SHORT).show();
    }
    public void getStatusInfo(){
        itemsArrayList = new ArrayList<>();
        getData();
        ArrayList<MachineListItems> list11 = new ArrayList<>(itemsArrayList.subList(60,66));
        ArrayList<MachineListItems> list10 = new ArrayList<>(itemsArrayList.subList(54,60));
        ArrayList<MachineListItems> list9 = new ArrayList<>(itemsArrayList.subList(48,54));
        ArrayList<MachineListItems> list8 = new ArrayList<>(itemsArrayList.subList(42,48));
        ArrayList<MachineListItems> list7 = new ArrayList<>(itemsArrayList.subList(36,42));
        ArrayList<MachineListItems> list6 = new ArrayList<>(itemsArrayList.subList(30,36));
        ArrayList<MachineListItems> list5 = new ArrayList<>(itemsArrayList.subList(24,30));
        ArrayList<MachineListItems> list4 = new ArrayList<>(itemsArrayList.subList(18,24));
        ArrayList<MachineListItems> list3 = new ArrayList<>(itemsArrayList.subList(12,18));
        ArrayList<MachineListItems> list2 = new ArrayList<>(itemsArrayList.subList(6,12));
        ArrayList<MachineListItems> list1 = new ArrayList<>(itemsArrayList.subList(0,6));
        machineDashboardAdapter11 = new MachineDashboardAdapter(list11,this,this);
        machineDashboardAdapter10 = new MachineDashboardAdapter(list10,this,this);
        machineDashboardAdapter9 = new MachineDashboardAdapter(list9,this,this);
        machineDashboardAdapter8 = new MachineDashboardAdapter(list8,this,this);
        machineDashboardAdapter7 = new MachineDashboardAdapter(list7,this,this);
        machineDashboardAdapter6 = new MachineDashboardAdapter(list6,this,this);
        machineDashboardAdapter5 = new MachineDashboardAdapter(list5,this,this);
        machineDashboardAdapter4 = new MachineDashboardAdapter(list4,this,this);
        machineDashboardAdapter3 = new MachineDashboardAdapter(list3,this,this);
        machineDashboardAdapter2 = new MachineDashboardAdapter(list2,this,this);
        machineDashboardAdapter1 = new MachineDashboardAdapter(list1,this,this);
    }
    public void setAdapter(){
        recyclerView11.setAdapter(machineDashboardAdapter11);
        recyclerView10.setAdapter(machineDashboardAdapter10);
        recyclerView9.setAdapter(machineDashboardAdapter9);
        recyclerView8.setAdapter(machineDashboardAdapter8);
        recyclerView7.setAdapter(machineDashboardAdapter7);
        recyclerView6.setAdapter(machineDashboardAdapter6);
        recyclerView5.setAdapter(machineDashboardAdapter5);
        recyclerView4.setAdapter(machineDashboardAdapter4);
        recyclerView3.setAdapter(machineDashboardAdapter3);
        recyclerView2.setAdapter(machineDashboardAdapter2);
        recyclerView1.setAdapter(machineDashboardAdapter1);
    }
    public void getData(){
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
                    itemsArrayList.add(new MachineListItems(i, rs.getString("Line"), rs.getString("Station"),rs.getString("PIC")));
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                connection.close();
            }
        }catch (Exception ex){
            isSuccess=false;
            ConnectionResult = ex.getMessage();
        }
    }
    public class Loading extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);

            //do initialization of required objects objects here
        }

        ;

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

        ;
    }
}


