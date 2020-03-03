package com.app.andonprototype.ui.AssetManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.R;
import com.app.andonprototype.ui.pop_dialog_add_part;
import com.app.andonprototype.ui.pop_dialog_renew_part;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetManagementReport extends AppCompatActivity implements ListView.OnItemClickListener, pop_dialog_renew_part.ExampleDialogListener,pop_dialog_add_part.ExampleDialogListener {
    private String No, Name, Format, Query, currentDate;
    Button addPart;
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
        addPart = findViewById(R.id.btnAdd_Part);
        Name = getIntent().getStringExtra("Name");
        currentDate = getIntent().getStringExtra("Current_Date");
        Format = getIntent().getStringExtra("Format");
        ListAsset = findViewById(R.id.ListAssetPart);
        ListAsset.setOnItemClickListener(this);
        Machine = findViewById(R.id.dataMachineID);
        Machine.setText(Name);
        getAsset();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAsset();
//                finish();
//                Intent i = new Intent (AssetManagementReport.this, AssetManagementReport.class);
//                overridePendingTransition( 0, 0);
//                startActivity(i);
//                overridePendingTransition( 0, 0);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    public void getAsset() {
        List<Map<String, String>> MyPartList = null;
        MyPartList = getProblem();
        String[] fromwhere = {"Part", "Type", "Durability", "Date", "Date_Register", "Quantity"};
        int[] viewwhere = {R.id.Part, R.id.Type, R.id.Durability, R.id.Date, R.id.Date_Register, R.id.Quantity};
        AP = new SimpleAdapter(this, MyPartList, R.layout.part_list, fromwhere, viewwhere);
        ListAsset.setAdapter(AP);
    }

    public List<Map<String, String>> getProblem() {
        List<Map<String, String>> data = new ArrayList<>();
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                switch (Format) {
                    case "1": {
                        Query = "Select m.No, i.Nama_Part, i.Jenis_Part, i.Umur, m.Quantity, CONVERT (date, m.Date_Start) AS Date_Register, CONVERT(date, m.Due_Date) AS Due_Date " +
                                "from machinelist m,inventorypart i " +
                                "where m.Machine_Name = '" + Name + "' and m.PartID = i.PartID";
                        break;
                    }
                    case "2": {
                        Query = "Select m.No, i.Nama_Part, i.Jenis_Part, i.Umur, m.Quantity, CONVERT (date, m.Date_Start) AS Date_Register, CONVERT(date, m.Due_Date) AS Due_Date " +
                                "from machinelist m,inventorypart i " +
                                "where m.Machine_Name = '" + Name + "' and m.PartID = i.PartID " +
                                "and DATEDIFF(second, '" + currentDate + "', m.Due_Date) < 300";
                        break;
                    }
                }
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(Query);
                while (rs.next()) {
                    String No = rs.getString("No");
                    String Part = rs.getString("Nama_Part");
                    String Type = rs.getString("Jenis_Part");
                    String Durability = rs.getString("Umur");
                    String Date = rs.getString("Due_Date");
                    String Date_Register = rs.getString("Date_Register");
                    String Quantity = rs.getString("Quantity");
                    Map<String, String> datanum = new HashMap<>();
                    datanum.put("No", No);
                    datanum.put("Part", Part);
                    datanum.put("Type", Type);
                    datanum.put("Durability", Durability);
                    datanum.put("Date", Date);
                    datanum.put("Date_Register", Date_Register);
                    datanum.put("Quantity", Quantity);
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

    public void btnRenew(View view) {
        updateDateNow();
        updateDueDate();
        finish();
        Intent i = new Intent(AssetManagementReport.this, AssetManagementReport.class);
        overridePendingTransition(0, 0);
        i.putExtra("Name", Name);
        i.putExtra("Current_Date", currentDate);
        i.putExtra("Format", Format);
        startActivity(i);
        overridePendingTransition(0, 0);
    }

    public void updateDateNow() {
        //UPDATE machinelist SET Date_Start = GETDATE()
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            String query = "UPDATE machinelist SET Date_Start = GETDATE() " +
                    "where No='" + No + "'";
            PreparedStatement stmt = connect.prepareStatement(query);
            stmt.execute();
        } catch (SQLException ignored) {
        }
    }

    public void updateDueDate() {
        //UPDATE machinelist SET Due_Date = DATEADD(SECOND, i.umur, m.Date_Start) from inventorypart i, machinelist m where m.PartID = i.PartID
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            String query = "UPDATE machinelist SET Due_Date = DATEADD(SECOND, i.Umur, m.Date_Start) " +
                    "from inventorypart i, machinelist m " +
                    "where m.PartID = i.PartID and No='" + No + "'";
            PreparedStatement stmt = connect.prepareStatement(query);
            stmt.execute();
            Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show();
        } catch (SQLException ignored) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, String> mp = (Map<String, String>) parent.getItemAtPosition(position);
        Object ID = mp.get("No");
        No = ID.toString();
        openDialog_Renew();
    }

    public void openDialog_Part() {
        pop_dialog_add_part popDialogAddPart = new pop_dialog_add_part();
        popDialogAddPart.show(getSupportFragmentManager(), "part dialog");
    }

    public void openDialog_Renew(){
        pop_dialog_renew_part popDialogRenewPart = new pop_dialog_renew_part();
        popDialogRenewPart.show(getSupportFragmentManager(),"renew dialog");
    }

    @Override
    public void applyTexts(String username, String password) {

    }

    public void btn_addPart(View view) {
        Intent i = new Intent(this,Add_Part.class);
        i.putExtra(Name,"Nama");
        i.putExtra(No,"No");
        startActivity(i);
    }

    public static class getP{
        Connection connection;
        public ArrayList ListPartArray;
        public void getPart(){
            String z = "";
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                connection = connectionClass.CONN();
                if (connectionClass == null) {
                    z = "Check Your Internet Connection";
                } else {
                    String query = "SELECT Nama_Part FROM inventorypart";
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    ListPartArray = new ArrayList();
                    while (rs.next()) {
                        ListPartArray.add(rs.getString("Nama"));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                z = "Check Your Internet";
            }
        }
    }
}
