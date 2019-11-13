package com.example.andonprototype.ui.MainDashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.andonprototype.Dashboard.LoginActivity;
import com.example.andonprototype.Dashboard.MachineDashboard;
import com.example.andonprototype.R;

import static com.example.andonprototype.SaveSharedPreference.clearUserName;
import static com.example.andonprototype.SaveSharedPreference.getID;


public class MainDashboardFragment extends Fragment {
    private MainViewModel mainViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainViewModel =
                ViewModelProviders.of(this).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_maindashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_reportactivity);
//        mainViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        TextView welcomeText = root.findViewById(R.id.welcomeText);
        String pic = getID(getActivity());
        welcomeText.setText("Welcome " + pic);

        Button logoutBtn                = root.findViewById(R.id.btnLogout);
        Button btnV                     = root.findViewById(R.id.btnView);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearUserName(getActivity());
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });

        btnV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MachineDashboard.class);
                startActivity(i);
            }
        });


        return root;
    }
}