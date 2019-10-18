package com.example.andonprototype.barcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.andonprototype.DetailBreakdownPage2;
import com.example.andonprototype.R;

import java.time.ZonedDateTime;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScanner extends BaseScannerActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private ZonedDateTime currentZoneStart = ZonedDateTime.now();
    private String currentDateStart = currentZoneStart.toString();

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
        Intent i = new Intent(SimpleScanner.this, DetailBreakdownPage2.class);
        i.putExtra("EXTRA_SESSION_ID", rawResult.getText());
        i.putExtra("StartTime", currentDateStart);
        startActivity(i);
    }
}
