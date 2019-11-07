package com.example.andonprototype;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.AndroidRuntimeException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.Dashboard.MainDashboard;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DetailBreakdownPage2 extends AppCompatActivity {

    ConnectionClass connectionClass;
    private boolean success = false;
    private static final int pic_id = 123;
    private static final int pic_id2 = 124;
    public static final int requestcode = 1;
    public String Line,Station,MachineID,picr,ResponseDateFinish;
    boolean doubleBackToExitPressedOnce = false;
    byte[] byteArray;
    ImageView click_image_id, click_image_id2;
    String encodedImageProblem, encodedImageSolution;
    TextView txtmsg, machine_id, date_start_text,date_finish_text,pic;
    Button btnSave, camera_open_id, camera_open_id2;
    EditText problem_desc_text, solution_desc_text;
    public ContentValues values;
    public Uri imageUri;
    ProgressBar pbbarDetail;
    private Chronometer chronometer;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_breakdown_page2);
        values = new ContentValues();
        Line = getIntent().getStringExtra("Line");
        Station = getIntent().getStringExtra("Station");
        MachineID = getIntent().getStringExtra("MachineID");
        picr = getIntent().getStringExtra("PIC");
        ResponseDateFinish = getIntent().getStringExtra("ResponseDateFinish");
        pbbarDetail = (ProgressBar) findViewById(R.id.pbbarDetail);
        pbbarDetail.setVisibility(View.VISIBLE);

        connectionClass = new ConnectionClass();

        camera_open_id      = (Button)    findViewById(R.id.camera_button);
        click_image_id      = (ImageView) findViewById(R.id.problem_pic);

        camera_open_id2     = (Button)    findViewById(R.id.camera_button_solution);
        click_image_id2     = (ImageView) findViewById(R.id.solution_pic);

        problem_desc_text   = (EditText)  findViewById(R.id.problem_desc_text);
        solution_desc_text  = (EditText)  findViewById(R.id.solution_desc_text);

        machine_id          = (TextView)  findViewById(R.id.machine_id);
        date_start_text     = (TextView)  findViewById(R.id.date_start_text);
        pic                 = (TextView)  findViewById(R.id.pic);

        btnSave             = (Button)    findViewById(R.id.btnSave);
        txtmsg              = (TextView)  findViewById(R.id.txtmsg);

        pic.setText(picr);
        machine_id.setText(MachineID);
        date_start_text.setText(getIntent().getStringExtra("ResponseDateFinish"));

        camera_open_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_problem();
            }
        });

        camera_open_id2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_solution();
            }
        });

        ////////////////////////////////////////////////////////Stop watch Section///////////////////////////////////////////////////////////////////////////////////////////////////////////
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        startChronometer();
        ////////////////////////////////////////////////////////end of stop watch section///////////////////////////////////////////////////////////////////////////////////////////////////
        updatePICstatus3();
        GetPicture();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbbarDetail.setVisibility(View.VISIBLE);
                UploadtoDB();
            }
        });
    }

    public void LoadMainDashboard() {
        Intent i = new Intent(DetailBreakdownPage2.this, MainDashboard.class);
        startActivity(i);
    }
    public void UploadtoDB() {
        String msg = "unknown";
        chronometer.stop();
        long saveTime = SystemClock.elapsedRealtime() - chronometer.getBase();
        int seconds = (int)(saveTime/1000);
        String currentDateFinish= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
        try {
            Connection con = connectionClass.CONN();
            String commands =
                    "Insert into machinestatustest (Line, Station, MachineID, Response_Time_Finish, Repair_Time_Start, " +
                            "Repair_Time_Finish, Repair_Duration, PIC, Image_Problem, Problem_Desc, " +
                            "Image_Solution, Solution_Desc) values " +
                            "('" + Line + "','" + Station + "','" + MachineID + "','" + ResponseDateFinish + "','" +
                            date_start_text.getText() + "','" + currentDateFinish + "','" + seconds + "','" + picr + "','" +
                            encodedImageProblem + "','" + problem_desc_text.getText().toString() + "','" +
                            encodedImageSolution + "','" + solution_desc_text.getText().toString() + "')";
            PreparedStatement preStmt = con.prepareStatement(commands);
            preStmt.execute();
            msg = "Inserted Successfully";
            updatePICstatus4();
            LoadMainDashboard();
        }  // TODO: handle exception
        catch (IOError | Exception ex) {
            msg = ex.getMessage();
            Log.d("hitesh", msg);
        }
        txtmsg.setText(msg);
    }

    public void updatePICstatus4() {
        try {
            Connection connection = connectionClass.CONN();
            String query = "UPDATE machinedashboard SET Status=4, PIC='" + picr + "' where MachineID='" + MachineID +"'";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.execute();
        }catch (SQLException ex){
        }
    }

    public void updatePICstatus3() {
        try {
            Connection connection = connectionClass.CONN();
            String query = "UPDATE machinedashboard SET Status=3, PIC='" + picr + "' where MachineID='" + MachineID +"'";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.execute();
        }catch (SQLException ex){
        }
    }

    public void updatePICstatus2() {
        try {
            Connection connection = connectionClass.CONN();
            String query = "UPDATE machinedashboard SET Status=2, PIC=NULL where MachineID='" + MachineID +"'";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.execute();
        }catch (SQLException ex){
        }
    }

    public void startChronometer() {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            running = true;
        }
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            updatePICstatus2();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case pic_id: {
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImageProblem = BitmapFactory.decodeStream(imageStream);
                    click_image_id.setImageBitmap(selectedImageProblem);

                    if (selectedImageProblem != null) {
                        encodedImageProblem = encodeImage(selectedImageProblem);
                        Toast.makeText(DetailBreakdownPage2.this, "Conversion Done",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailBreakdownPage2.this, "Cancelled",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            case pic_id2:
            {
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImageSolution = BitmapFactory.decodeStream(imageStream);
                    click_image_id2.setImageBitmap(selectedImageSolution);

                    if (selectedImageSolution != null) {
                        encodedImageSolution = encodeImage(selectedImageSolution);
                        Toast.makeText(DetailBreakdownPage2.this, "Conversion Done",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailBreakdownPage2.this, "Cancelled",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void GetPicture(){
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public void camera_problem() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, pic_id);
    }

    public void camera_solution() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, pic_id2);
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,40,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }
}
