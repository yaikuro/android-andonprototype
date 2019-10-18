package com.example.andonprototype.Dashboard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.Configuration.Query;
import com.example.andonprototype.DetailBreakdownPage2;
import com.example.andonprototype.R;

import java.security.PublicKey;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProblemWaitingList extends AppCompatActivity implements ListView.OnItemClickListener {

        public String pic;
        private ListView ListProblem;
        private SimpleAdapter AP;
        public ImageView imageView;
        private ZonedDateTime currentZoneStart = ZonedDateTime.now();
        private String currentDateStart = currentZoneStart.toString();
        String[] fromwhere = {"Image","MachineID","Line","Station"};
        int [] viewwhere = {R.id.image,R.id.MachineID,R.id.Line,R.id.Station};
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_problem_waiting_list);
            ListProblem = (ListView) findViewById(R.id.ListProblem);
            ListProblem.setOnItemClickListener(this);
            imageView = (ImageView) findViewById(R.id.image);
            getProblem();
            pic = getIntent().getStringExtra("PIC");
            final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getProblem();
                    pullToRefresh.setRefreshing(false);
                }
            });
        }
        public void getProblem()
        {
            List<Map<String,String>> MyProblemList=null;
            GetProblem myProblem = new GetProblem();
            MyProblemList = myProblem.getProblem();
            AP = new SimpleAdapter(ProblemWaitingList.this,MyProblemList,R.layout.listitem,fromwhere,viewwhere);
            ListProblem.setAdapter(AP);
            Object s = (ListProblem.getItemAtPosition(1));
            String status = s.toString();
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
        }

    public static class GetProblem {
            Connection connect;
        String ConnectionResult = "";

        Boolean isSuccess = false;
        int[] listviewImage = new int[]
                {
                        R.drawable.green,
                        R.drawable.red
                };
        public List<Map<String,String>> getProblem()
        {
            List<Map<String,String>> data = null;
            data = new ArrayList<Map<String,String>>();
            try
            {
                ConnectionClass connectionClass = new ConnectionClass();
                connect=connectionClass.CONN();
                if(connect==null)
                {
                    ConnectionResult = "Check your Internet Connection";
                }
                else
                {
                    String query = Query.problemquery;
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next())
                    {
                        String status = rs.getString("Status");
                        String MachineID = rs.getString("MachineID");
                        String Line = rs.getString("Line");
                        String Station = rs.getString("Station");
                        Map<String,String> datanum = new HashMap<>();
                        datanum.put("Status",status);
                        if (status.equals("1"))
                        {
                            int i = 0;
                            datanum.put("Image",Integer.toString((listviewImage[i])));
                        }
                        else if (status.equals("2"))
                        {
                            int i = 1;
                            datanum.put("Image",Integer.toString(listviewImage[i]));
                        }
                        datanum.put("MachineID",MachineID);
                        datanum.put("Line",Line);
                        datanum.put("Station",Station);
                        data.add(datanum);
                    }
                    ConnectionResult="Successfull";
                    isSuccess=true;
                    connect.close();
                }
            }
            catch (Exception ex)
            {
                isSuccess=false;
                ConnectionResult=ex.getMessage();
            }
            return data;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, DetailBreakdownPage2.class);
        i.putExtra("StartTime", currentDateStart);
        i.putExtra("PIC",pic);
        startActivity(i);

    }
}
