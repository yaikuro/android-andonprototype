package com.example.andonprototype.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.andonprototype.Background.GetData;
import com.example.andonprototype.R;

import java.util.List;
import java.util.Map;

public class MachineDashboard extends AppCompatActivity {
    ListView ListView;
    SimpleAdapter AD;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_dashboard);
        ListView = (ListView) findViewById(R.id.ListView);
        imageView = (ImageView) findViewById(R.id.image);
        getdata();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdata();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

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
}


