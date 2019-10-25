package com.example.andonprototype.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.R;
import com.example.andonprototype.SaveSharedPreference;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.andonprototype.SaveSharedPreference.clearUserName;
import static com.example.andonprototype.SaveSharedPreference.getUserName;


public class LoginActivity extends AppCompatActivity {
    public Context mContext;
    public String ID;
    public String PIC;
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
        if (getUserName(this).length()!=0)
        {
            loadDashboard2();
        }
        connectionClass = new ConnectionClass();
        etuserid = (EditText) findViewById(R.id.edtuserid);
        etpass = (EditText) findViewById(R.id.edtpass);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
                saveSharedPreference.setUserName(LoginActivity.this,etuserid.getText().toString());
            }
        });
    }

    public class DoLogin extends AsyncTask<String,String,String>
    {
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
            i.putExtra("ID",ID);
            i.putExtra("PIC",PIC);
            saveSharedPreference.setID(LoginActivity.this,ID);
            startActivity(i);
            finish();
        }
        private void loadDashboard2(){
            Intent i = new Intent(getApplicationContext(), MainDashboard.class);
            startActivity(i);
            finish();
        }
}
