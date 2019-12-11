package com.example.andonprototype.ui.AssetManagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.Background.Query;
import com.example.andonprototype.R;
import com.example.andonprototype.ui.ProblemList.ProblemListFragment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetManagementReport extends AppCompatActivity {
    private String mesin, pic, num, station, duration, line, Nomor, RepairTimeStart;
    private String RepairTimeFinish, Desc_Problem, Desc_Solution, ImageProblem, ImageSolution, Name;
    ListView ListAsset;
    TextView Machine;
    SimpleAdapter AP;
    Boolean isSuccess = false;
    Connection connect;
    String ConnectionResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_management);
        Name = getIntent().getStringExtra("Name");
        ListAsset = findViewById(R.id.ListAssetPart);
        Machine = findViewById(R.id.dataMachineID);
        Machine.setText(Name);
        getAsset();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                Intent i = new Intent (AssetManagementReport.this, AssetManagementReport.class);
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

        public void getAsset () {
            List<Map<String, String>> MyPartList = null;
            MyPartList = getProblem();
            String[] fromwhere = {"Part","Type","Durability"};
            int[] viewwhere = {R.id.Part,R.id.Type,R.id.Durability};
            AP = new SimpleAdapter(this, MyPartList, R.layout.part_list, fromwhere, viewwhere);
            ListAsset.setAdapter(AP);
        }

        public List<Map<String, String>> getProblem() {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                connect = connectionClass.CONN();
                if (connect == null) {
                    ConnectionResult = "Check your Internet Connection";
                } else {
                    String query = "Select i.Nama_Part, i.Jenis_Part, i.Umur " +
                            "from machinelist m,inventorypart i " +
                            "where m.Machine_Name = '" + Name + "' and m.PartID = i.PartID";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String Part = rs.getString("Nama_Part");
                        String Type = rs.getString("Jenis_Part");
                        String Durability = rs.getString("Umur");
                        Map<String, String> datanum = new HashMap<>();
                        datanum.put("Part", Part);
                        datanum.put("Type", Type);
                        datanum.put("Durability", Durability);
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
