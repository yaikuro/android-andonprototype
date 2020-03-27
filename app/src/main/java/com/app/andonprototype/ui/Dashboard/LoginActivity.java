package com.app.andonprototype.ui.Dashboard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.Background.SaveSharedPreference;
import com.app.andonprototype.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.app.andonprototype.Background.SaveSharedPreference.getNama;
import static com.app.andonprototype.Background.SaveSharedPreference.setID;
import static com.app.andonprototype.Background.SaveSharedPreference.setNama;

public class LoginActivity extends AppCompatActivity {
    public String ID, PIC;
    public SaveSharedPreference saveSharedPreference;
    public ConnectionClass connectionClass;
    public EditText etuserid, etpass;
    Button btnlogin;
    ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveSharedPreference = new SaveSharedPreference();
        setContentView(R.layout.activity_login);

        // Kalau sudah pernah Login, langsung masuk activity Main Dashboard
        if (!getNama(this).isEmpty()) {
            loadDashboard2();
        }

        // Panggil ConnectionClass java
        connectionClass = new ConnectionClass();

        // ID
        etuserid = findViewById(R.id.edtuserid);

        // Password
        etpass = findViewById(R.id.edtpass);

        // Tombol Login
        btnlogin = findViewById(R.id.btnlogin);

        // Loading bar
        pbbar = findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);

        // User bisa menekan tombol Login atau tombol enter(keyboard) untuk melakukan login
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");

                // Simpan ID user di SaveSharedPreference
                setID(LoginActivity.this, etuserid.getText().toString());
            }
        });

        // Berikut ini code ketika user menekan tombol enter(keyboard)
        etpass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    DoLogin doLogin = new DoLogin();
                    doLogin.execute("");
                    setID(LoginActivity.this, etuserid.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }


    public class DoLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        String userid = etuserid.getText().toString();
        String password = etpass.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, z, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                loadDashboard();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            // Logic ketika user ingin login
            if (userid.isEmpty() && password.isEmpty()) {
                z = "Please Enter User ID and Password";
            } else if (userid.isEmpty()) {
                z = "User ID cannot be empty";
            } else if (password.isEmpty()) {
                z = "Password cannot be empty";
            } else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "select * from userid where npk='" + userid + "' and Password='" + password + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if (rs.next()) {
                            z = "Login successfull";
                            isSuccess = true;
                            ID = rs.getString("npk");
                            PIC = rs.getString("Nama");

                            // Simpan nama user di SaveSharedPreference
                            setNama(LoginActivity.this,PIC);
                        } else {
                            z = "Invalid Credentials";
                            isSuccess = false;
                        }
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Invalid Credentials";
                }
            }
            return z;
        }
    }

    // Activity Main Dashboard
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), MainDashboard.class);
        setNama(LoginActivity.this, PIC);
        startActivity(i);
        finish();
    }

    private void loadDashboard2() {
        Intent i = new Intent(getApplicationContext(), MainDashboard.class);
        startActivity(i);
        finish();
    }
}
