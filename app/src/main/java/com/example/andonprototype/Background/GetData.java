package com.example.andonprototype.Background;

import com.example.andonprototype.Configuration.Query;
import com.example.andonprototype.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetData {
    Connection connect;
    String ConnectionResult="";
    Boolean isSuccess = false;
    int[] listviewImage = new int[]
            {
                    R.drawable.green,
                    R.drawable.red,
            };
    public List<Map<String,String>>getdata()
    {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        try
        {
            ConnectionClass connectionClass = new ConnectionClass();
            connect=connectionClass.CONN();
            if (connect == null)
            {
                ConnectionResult="Check your Internet Connection";
            }
            else
            {
                String query= Query.getdataquery;
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    String status = rs.getString("Status");
                    String MachineID = rs.getString("MachineID");
                    String Line = rs.getString("Line");
                    String Station = rs.getString("Station");
                    Map<String,String> datanum = new HashMap<String,String>();
                    datanum.put("Status",status);
                    //int i = Integer.parseInt(status);
                    if (status.equals("1"))
                    {
                        int i = 0;
                        datanum.put("Image", Integer.toString(listviewImage[i]));
                    }
                    else if(status.equals("2"))
                    {
                        int i = 1;
                        datanum.put("Image", Integer.toString(listviewImage[i]));
                    }
                    datanum.put("MachineID",MachineID);
                    datanum.put("Line",Line);
                    datanum.put("Station",Station);;
                    data.add(datanum);
                }
                ConnectionResult="Successful";
                isSuccess=true;
                connect.close();
            }
        }catch (Exception ex)
        {
            isSuccess=false;
            ConnectionResult=ex.getMessage();
        }
        return data;
    }
}
