package com.app.andonprototype.drawer_ui.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.andonprototype.Background.SwipeDismissBaseActivity;
import com.app.andonprototype.R;

public class Settings extends SwipeDismissBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnChangePic = findViewById(R.id.btn_changePic);

        btnChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.this, ChangeProfilePicture.class);
                startActivity(i);
            }
        });


    }


}
