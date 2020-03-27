package com.app.andonprototype.ui.ProblemList;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.Background.Query;
import com.app.andonprototype.R;
import com.app.andonprototype.Background.SaveSharedPreference;
import com.app.andonprototype.barcodescanner.SimpleScanner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ProblemListFragment extends Fragment implements ProblemListAdapter.OnPressListener {
    public String pic, Line, Station, MachineID, Person;
    public int Status;
    Connection connect;
    public ImageView imageView;
    public SaveSharedPreference saveSharedPreference;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    String currentDateStart = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());

    private ProblemListViewModel problemListViewModel;

    RecyclerView recyclerView;
    private ArrayList<ProblemListItems> itemsArrayList;
    private ProblemListAdapter problemListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public static ProblemListFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        ProblemListFragment secondFragment = new ProblemListFragment();
        secondFragment.setArguments(args);
        return secondFragment;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        problemListViewModel =
                ViewModelProviders.of(this).get(ProblemListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_problemwaitinglist, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        imageView = root.findViewById(R.id.image);
        getProblem();
        pic = SaveSharedPreference.getNama(getActivity());
        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProblem();
                pullToRefresh.setRefreshing(false);
            }
        });
        return root;
    }

    // Tampilkan semua status masalah di aplikasi
    public void getProblem() {
        itemsArrayList = new ArrayList<>();
        GetProblem();
        problemListAdapter = new ProblemListAdapter(itemsArrayList,this,getActivity());
        recyclerView.setAdapter(problemListAdapter);
    }

    // Ambil semua status masalah di database
    public void GetProblem () {
        boolean isSuccess = false;
        String ConnectionResult;
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            if (connect == null) {
                ConnectionResult = "Check your Internet Connection";
            } else {
                String query = "Select * " +
                        "from stationdashboard " +
                        "where Status = 2 or Status = 3 or Status = 4";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String status = rs.getString("Status");
                    int i = Integer.parseInt(status) - 1;
                    itemsArrayList.add(new ProblemListItems(i, rs.getString("Line"), rs.getString("Station"),rs.getString("PIC")));
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            }
        }catch (Exception ex){
            isSuccess=false;
            ConnectionResult = ex.getMessage();
        }
    }

    @Override
    public void onPressClick(int position) {
        Class<?> clss = SimpleScanner.class;
        if (pic.equals("")) {
            Toast.makeText(getActivity(), "ID not Detected, Please Re-login to verify", Toast.LENGTH_SHORT).show();
        } else {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
            } else {
                Intent i = new Intent(getActivity(), clss);
                Line = itemsArrayList.get(position).getLine();
                Station = itemsArrayList.get(position).getStation();
                Status = itemsArrayList.get(position).getStatus() + 1;
                i.putExtra("Line", Line);
                i.putExtra("Station", Station);
                i.putExtra("PIC", pic);

                if (pic.equals("admin")) {
                    startActivity(i);
                } else {
                    String person = itemsArrayList.get(position).getPIC();
                    switch (Status) {
                        case 3:
                            if (person != null) {
                                Person = person;
                                if (person.equals(pic)) {
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getActivity(), Person + " is currently repairing", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Another PIC is currently repairing", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 4:
                            if (person == null) {
                                Person = person;
                                Toast.makeText(getActivity(), "Waiting for Production Approval, Done by " + Person, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Waiting for Production Approval", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            startActivity(i);
                            break;
                    }
                }
            }
        }
    }
}
