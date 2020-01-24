package com.app.andonprototype.ui.MainDashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.Success_Page;
import com.app.andonprototype.R;
import com.app.andonprototype.ui.Dashboard.MachineDashboard;
import com.app.andonprototype.ui.ProblemList.ProblemWaitingList;
import com.google.android.material.card.MaterialCardView;

import java.sql.Connection;
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

import static com.app.andonprototype.Background.SaveSharedPreference.getID;


public class MainDashboardFragment extends Fragment {
    private MainViewModel mainViewModel;
    private ImageView image_person;
    private String ConnectionResult, pic, image, currentDate;
    private Boolean isSuccess;
    private TextView txtProgress;
    private ProgressBar progressBar;
    private ArrayList<String> list_done;
    private ArrayList<String> list_all;
    private int pStart = 0;
    private int pStatus;
    private int work, all;
    private Handler handler = new Handler();
    private Connection connect;


    public static MainDashboardFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        MainDashboardFragment firstFragment = new MainDashboardFragment();
        firstFragment.setArguments(args);
        return firstFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainViewModel =
                ViewModelProviders.of(this).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_maindashboard, container, false);

        TextView welcomeText = root.findViewById(R.id.welcomeText);
        pic = getID(getActivity());
        welcomeText.setText("Welcome " + pic);
        txtProgress = root.findViewById(R.id.txtProgress);
        progressBar = root.findViewById(R.id.progressBar);
        TextView progress_counter = root.findViewById(R.id.progress_counter);
        currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        image_person = root.findViewById(R.id.image_person);
        Button btnV = root.findViewById(R.id.btnView);

        btnV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MachineDashboard.class);
                startActivity(i);
            }
        });


        MaterialCardView cardView_MachineDasboard = root.findViewById(R.id.cardView_MachineDashboard);
        cardView_MachineDasboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MachineDashboard.class);
                startActivity(i);
            }
        });

        MaterialCardView cardView_ProblemList = root.findViewById(R.id.cardView_ProblemList);
        cardView_ProblemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ProblemWaitingList.class);
                startActivity(i);
            }
        });


        getImage();
        if (isSuccess) {
            setPicture();
        }
        get_list_done();
        get_list_all();
        if (!list_all.isEmpty()) {
            work = list_done.size();
            all = list_all.size();
            pStatus = (work * 100) / all;
            Toast.makeText(getActivity(), Integer.toString(pStatus), Toast.LENGTH_SHORT).show();
            progress_counter.setText(work + "/" + all);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (pStart <= pStatus) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(pStart);
                                txtProgress.setText(pStart + " %");
                            }
                        });
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        pStart++;
                    }
                }
            }).start();
        }


        return root;
    }

    public void machine_maindasboard(View view) {
        Intent i = new Intent(getActivity(), MachineDashboard.class);
        startActivity(i);
    }

    private void getImage() {
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select Image from userid where Nama = '" + pic + "'";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    image = rs.getString("Image");
                    isSuccess = !image.isEmpty();
                }
                ConnectionResult = "Successful";
                connect.close();
            }
        } catch (Exception ex) {
            ConnectionResult = ex.getMessage();
            isSuccess = false;
        }
    }

    private void setPicture() {
        byte[] decodeStringPicture = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodebitmapPicture = BitmapFactory.decodeByteArray(decodeStringPicture, 0, decodeStringPicture.length);
        image_person.setImageBitmap(decodebitmapPicture);
    }

    private void get_list_done() {
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            String query = "Select No from machinestatustest where PIC = '" + pic + "' " +
                    "and Response_Time_Finish LIKE '" + currentDate + "%'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            list_done = new ArrayList<>();
            while (rs.next()) {
                list_done.add(rs.getString("No"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void get_list_all() {
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            String query = "Select No from machinestatustest where Response_Time_Finish LIKE '" + currentDate + "%'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            list_all = new ArrayList<>();
            while (rs.next()) {
                list_all.add(rs.getString("No"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}