package com.example.andonprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.example.andonprototype.Dashboard.LoginActivity;
import com.example.andonprototype.Dashboard.MainDashboard;
import com.example.andonprototype.ui.ProblemList.ProblemListFragment;
import com.example.andonprototype.ui.ProblemList.ProblemWaitingList;

import static com.example.andonprototype.Dashboard.MainDashboard.validate;

public class SwipeProblem extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_problem);
        validate = false;
        SwipeButton enableButton = findViewById(R.id.swipe_btn);
        enableButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
            }
        });

        enableButton.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
//                Toast.makeText(SwipeProblem.this, "Active!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SwipeProblem.this, ProblemWaitingList.class);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            validate = true;
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 3000);
    }

}
