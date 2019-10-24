package com.example.andonprototype.Background;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass
{
    String ipHP = "192.168.43.117";
    String classXAMPP = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://10.0.2.2/sem7";
    String unXAMPP = "root";
    String passwordXAMPP = "";
    String ip = "192.168.0.100";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "Winteq";
    String un = "admin";
    String password = "admin";

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
// Ini buat MySQL
//              Class.forName(classXAMPP);
////            conn = DriverManager.getConnection(url,unXAMPP,passwordXAMPP);
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return conn;
    }
}


