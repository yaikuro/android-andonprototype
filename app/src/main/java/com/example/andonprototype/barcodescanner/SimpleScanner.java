package com.example.andonprototype.barcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.DetailBreakdownPage2;
import com.example.andonprototype.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScanner extends BaseScannerActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    public String MachineID,PIC,Line,Station;
    ConnectionClass connectionClass;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
        connectionClass = new ConnectionClass();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        MachineID = getIntent().getStringExtra("MachineID");
        PIC = getIntent().getStringExtra("PIC");
        Line = getIntent().getStringExtra("Line");
        Station = getIntent().getStringExtra("Station");
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
        if (rawResult.getText().equals(MachineID)) {
            Intent i = new Intent(SimpleScanner.this, DetailBreakdownPage2.class);
            String currentResponseDateFinish = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
            i.putExtra("ResponseDateFinish",currentResponseDateFinish);
            i.putExtra("MachineID", rawResult.getText());
            i.putExtra("Line",Line);
            i.putExtra("Station",Station);
            i.putExtra("PIC",PIC);
            startActivity(i);
        } else{
            mScannerView.resumeCameraPreview(SimpleScanner.this);
            Toast.makeText(this, "Machine ID tidak sama", Toast.LENGTH_SHORT).show();
        }
    }
}
