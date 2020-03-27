package com.app.andonprototype.ui.AssetManagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Add_Part extends AppCompatActivity implements ListView.OnItemClickListener {
    public String PartID, Name, Line, Station, Quantity;
    int Durability;
    ListView ListPart;
    Boolean isSuccess = false;
    Connection connection;
    SimpleAdapter AP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_part);
        Line = getIntent().getStringExtra("Line");
        Station = getIntent().getStringExtra("Station");
        Name = getIntent().getStringExtra("Name");
        ListPart = findViewById(R.id.ListPart);
        ListPart.setOnItemClickListener(this);

        // Tampilkan list part dari database
        retrievePart();

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrievePart();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    public void retrievePart() {
        List<Map<String, String>> PartList = null;
        PartList = getPart();
        String[] data = {"Part", "Type", "Durability"};
        int[] elements = {R.id.Part, R.id.Type, R.id.Durability};
        AP = new SimpleAdapter(this, PartList, R.layout.list_part_add, data, elements);
        ListPart.setAdapter(AP);
    }

    // Ambil list part dari database
    public List<Map<String, String>> getPart() {
        String ConnectionResult;
        List<Map<String, String>> data = new ArrayList<>();
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            if (connection == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select PartID,Jenis_Part,Nama_Part,Umur from inventorypart " +
                        "EXCEPT " +
                        "SELECT i.PartID,i.Jenis_Part,i.Nama_Part,i.Umur from inventorypart i left join machinelist m on m.PartID = i.PartID " +
                        "where m.Machine_Name = '" + Name + "'";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String PartID = rs.getString("PartID");
                    String Part = rs.getString("Nama_Part");
                    String Type = rs.getString("Jenis_Part");
                    String Durability = rs.getString("Umur");
                    Map<String, String> datanum = new HashMap<>();
                    datanum.put("PartID", PartID);
                    datanum.put("Part", Part);
                    datanum.put("Type", Type);
                    datanum.put("Durability", Durability);
                    data.add(datanum);
                }
                ConnectionResult = "Successfull";
                isSuccess = true;
                connection.close();
            }
        } catch (Exception e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
            Toast.makeText(this, ConnectionResult, Toast.LENGTH_SHORT).show();
        }
        return data;
    }

    // Membuat Simple Dialog untuk memasukkan jumlah part yang dipilih
    public void openDialog_addQty() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View CustomView = inflater.inflate(R.layout.activity_add_qty_assetmanagement, null);
        final EditText etQuantity = CustomView.findViewById(R.id.etqty);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Part")
                .setView(CustomView)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Quantity = etQuantity.getText().toString();
                        add_data();
                        retrievePart();
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }


    // Masukkan part dan jumlahnya ke list
    public void add_data() {
        String ConnectionResult = "";
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            if (connection == null) {
                ConnectionResult = "Connection Failed";
                Toast.makeText(this, ConnectionResult, Toast.LENGTH_SHORT).show();
            } else {
                String query = "INSERT INTO machinelist (Line,Station,Machine_Name,PartID,Date_Start,Due_Date,Quantity) " +
                        "values ('" + Line + "','" + Station + "','" + Name + "','" + PartID + "', GETDATE(), DATEADD(SECOND," + Durability +
                        ",GETDATE()) ,'" + Quantity + "')";
                PreparedStatement preStmt = connection.prepareStatement(query);
                preStmt.execute();
                ConnectionResult = "Inserted Successfully";
                Toast.makeText(this, ConnectionResult, Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ConnectionResult = e.getMessage();
            Toast.makeText(this, ConnectionResult, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, String> mp = (Map<String, String>) parent.getItemAtPosition(position);
        Object age = mp.get("Durability");
        Object No = mp.get("PartID");
        PartID = No.toString();
        Durability = Integer.parseInt(age.toString());
        openDialog_addQty();
    }
}
