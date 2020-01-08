package com.app.andonprototype;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.app.andonprototype.ui.Dashboard.MainDashboard;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class Success_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(Success_Page.this);
        setContentView(R.layout.activity_success_page);
        final Handler handler = new Handler();
        success();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loaddashboard();
            }
        },1700);
    }
    public void success(){
        SimpleDraweeView simpleDraweeView = findViewById(R.id.loading_gif);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.loading).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(imageRequest.getSourceUri())
                .setAutoPlayAnimations(true)
                .build();
        simpleDraweeView.setController(controller);
    }
    public void loaddashboard(){
        finish();
        Intent i = new Intent(Success_Page.this, MainDashboard.class);
        startActivity(i);
    }
}
