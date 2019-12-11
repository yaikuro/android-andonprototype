package com.example.andonprototype.Dashboard;

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

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.R;
import com.example.andonprototype.Background.SaveSharedPreference;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.andonprototype.Background.SaveSharedPreference.getUserName;
import static com.example.andonprototype.Background.SaveSharedPreference.setUserName;

public class LoginActivity extends AppCompatActivity {
    public String ID,PIC;
    public SaveSharedPreference saveSharedPreference;
    public ConnectionClass connectionClass;
    public EditText etuserid, etpass;
    Button btnlogin;
    ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        saveSharedPreference = new SaveSharedPreference();
        setContentView(R.layout.activity_login);
        if (getUserName(this).length()!=0)
        {
            loadDashboard2();
        }
        connectionClass = new ConnectionClass();
        etuserid = findViewById(R.id.edtuserid);
        etpass = findViewById(R.id.edtpass);
        btnlogin = findViewById(R.id.btnlogin);
        pbbar = findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
                setUserName(LoginActivity.this,etuserid.getText().toString());
            }
        });

        etpass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    DoLogin doLogin = new DoLogin();
                    doLogin.execute("");
                    setUserName(LoginActivity.this,etuserid.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    public class DoLogin extends AsyncTask<String,String,String> {
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
            Toast.makeText(LoginActivity.this,z,Toast.LENGTH_SHORT).show();
            if(isSuccess) {
                loadDashboard();
            }

        }
        @Override
        protected String doInBackground(String... params) {
            if(userid.equals("")&&password.equals("")) {
                z = "Please Enter User ID and Password";
            }
            else if(userid.equals("")){
                z = "User ID cannot be empty";
            }
            else if(password.equals("")){
                z = "Password cannot be empty";
            }
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "select * from userid where npk='" + userid + "' and Password='" + password + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if(rs.next())
                        {
                            z = "Login successfull";
                            isSuccess=true;
                            ID = rs.getString("Nama");
                            PIC = rs.getString("npk");
                        }
                        else
                        {
                            z = "Invalid Credentials";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }
        private void loadDashboard(){
            Intent i = new Intent(getApplicationContext(), MainDashboard.class);
            SaveSharedPreference.setID(LoginActivity.this,ID);
            startActivity(i);
            finish();
        }
        private void loadDashboard2(){
            Intent i = new Intent(getApplicationContext(), MainDashboard.class);
            startActivity(i);
            finish();
        }
}
