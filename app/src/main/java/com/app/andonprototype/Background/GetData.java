package com.app.andonprototype.Background;

import com.app.andonprototype.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetData {
    private Connection connect;
    private String ConnectionResult="";
    private Boolean isSuccess = false;
    private int[] listviewImage = new int[]
            {
                    R.drawable.color_green,
                    R.drawable.color_red,
                    R.drawable.color_yellow,
                    R.drawable.color_blue
            };
    public List<Map<String,String>>getdata()
    {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        try
        {
            ConnectionClass connectionClass = new ConnectionClass();
            connect=connectionClass.CONN();
            if (connect == null)
                ConnectionResult = "Check your Internet Connection";
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
                    Map<String,String> datanum = new HashMap<>();
                    datanum.put("Status",status);
                    //int i = Integer.parseInt(status);
                    switch (status) {
                        case "1": {
                            int i = 0;
                            datanum.put("Image", Integer.toString(listviewImage[i]));
                            break;
                        }
                        case "2": {
                            int i = 1;
                            datanum.put("Image", Integer.toString(listviewImage[i]));
                            break;
                        }
                        case "3": {
                            int i = 2;
                            datanum.put("Image", Integer.toString(listviewImage[i]));
                            break;
                        }
                        case "4": {
                            int i = 3;
                            datanum.put("Image", Integer.toString(listviewImage[i]));
                            break;
                        }
                    }
                    datanum.put("MachineID",MachineID);
                    datanum.put("Line",Line);
                    datanum.put("Station",Station);
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
