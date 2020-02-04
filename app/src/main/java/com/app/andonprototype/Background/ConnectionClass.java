package com.app.andonprototype.Background;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class ConnectionClass {
    private String ipHP = "172.20.10.2";
    private String ip = "192.168.1.101";
    private String classs = "net.sourceforge.jtds.jdbc.Driver";
    private String db = "Winteq";
    private String un = "admin";
    private String password = "admin";

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
//                conn = DriverManager.getConnection(url,unXAMPP,passwordXAMPP);
        } catch (Exception se) {
            Log.e("ERROR", Objects.requireNonNull(se.getMessage()));
        }
        return conn;
    }
}


