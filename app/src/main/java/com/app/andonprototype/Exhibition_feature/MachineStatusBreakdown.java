package com.app.andonprototype.Exhibition_feature;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.Background.GetData;
import com.app.andonprototype.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MachineStatusBreakdown extends AppCompatActivity implements ListView.OnItemClickListener{

    android.widget.ListView ListView;
    SimpleAdapter AD;
    ImageView imageView;
    public String Mesin;
    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_dashboard);
        connectionClass = new ConnectionClass();
        ListView = findViewById(R.id.ListView);
        ListView.setOnItemClickListener(MachineStatusBreakdown.this);
        imageView = findViewById(R.id.image);
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
        AD = new SimpleAdapter(MachineStatusBreakdown.this, MydataList, R.layout.listitem, fromwhere, viewwhere);
        ListView.setAdapter(AD);
        Toast.makeText(this, "Found", Toast.LENGTH_SHORT).show();
    }

    public void updatePICstatus1() {
        try {
            Connection connection = connectionClass.CONN();
            String query = "UPDATE machinedashboard SET Status=2, PIC=NULL where MachineID='" + Mesin +"'";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.execute();
        }catch (SQLException ex){
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Map<String,String> mp = (Map<String, String>) ListView.getItemAtPosition(position);
        Object machine = mp.get("MachineID");
        Mesin = machine.toString();
        updatePICstatus1();
        Intent i = new Intent(MachineStatusBreakdown.this,MachineStatusBreakdown.class);
        finish();
        startActivity(i);
    }
}
