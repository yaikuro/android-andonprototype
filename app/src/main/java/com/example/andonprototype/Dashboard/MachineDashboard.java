package com.example.andonprototype.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.andonprototype.Background.GetData;
import com.example.andonprototype.BreakdownHistory;
import com.example.andonprototype.DetailReport;
import com.example.andonprototype.R;

import java.util.List;
import java.util.Map;

public class MachineDashboard extends AppCompatActivity implements ListView.OnItemClickListener{
    ListView ListView;
    SimpleAdapter AD;
    ImageView imageView;
    public String Mesin;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_dashboard);
        this.mHandler = new Handler();
        m_Runnable.run();
        ListView = findViewById(R.id.ListView);
        ListView.setOnItemClickListener(this);
        imageView = findViewById(R.id.image);
        getdata();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent i = new Intent(MachineDashboard.this, MachineDashboard.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            Intent i = new Intent(MachineDashboard.this, MachineDashboard.class);
            finish();
            overridePendingTransition(0, 0);
            startActivity(i);
            overridePendingTransition(0, 0);
            MachineDashboard.this.mHandler.postDelayed(m_Runnable, 1000);
        }

    };

    public void getdata() {
        List<Map<String, String>> MydataList = null;
        GetData myData = new GetData();
        MydataList = myData.getdata();
        String[] fromwhere = {"Image", "MachineID", "Line", "Station"};
        int[] viewwhere = {R.id.image, R.id.MachineID, R.id.Line, R.id.Station};
        AD = new SimpleAdapter(MachineDashboard.this, MydataList, R.layout.listitem, fromwhere, viewwhere);
        ListView.setAdapter(AD);
        Toast.makeText(this, "Found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent i = new Intent(this, BreakdownHistory.class);
        Map<String,String> mp = (Map<String, String>) ListView.getItemAtPosition(position);
        Object machine = mp.get("MachineID");
        Mesin = machine.toString();
        i.putExtra("MachineID", Mesin);
        startActivity(i);
    }
}


