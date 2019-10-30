package com.example.andonprototype;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.andonprototype.Background.ConnectionClass;
import com.example.andonprototype.Dashboard.MainDashboard;

import java.io.ByteArrayOutputStream;
import java.io.IOError;
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
    

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_breakdown_page2);
        Line = getIntent().getStringExtra("Line");
        Station = getIntent().getStringExtra("Station");
        MachineID = getIntent().getStringExtra("MachineID");
        picr = getIntent().getStringExtra("PIC");
        ResponseDateFinish = getIntent().getStringExtra("ResponseDateFinish");

            connectionClass = new ConnectionClass();

        camera_open_id      = (Button)    findViewById(R.id.camera_button);
        click_image_id      = (ImageView) findViewById(R.id.problem_pic);

        camera_open_id2     = (Button)    findViewById(R.id.camera_button_solution);
        click_image_id2     = (ImageView) findViewById(R.id.solution_pic);

        problem_desc_text   = (EditText)  findViewById(R.id.problem_desc_text);
        solution_desc_text  = (EditText)  findViewById(R.id.solution_desc_text);

        machine_id          = (TextView)  findViewById(R.id.machine_id);
        date_start_text     = (TextView)  findViewById(R.id.date_start_text);
        date_finish_text    = (TextView)  findViewById(R.id.date_finish_text);
        pic                 = (TextView)  findViewById(R.id.pic);

        btnSave             = (Button)    findViewById(R.id.btnSave);
        txtmsg              = (TextView)  findViewById(R.id.txtmsg);

        pic.setText(picr);
        machine_id.setText(MachineID);
        date_start_text.setText(getIntent().getStringExtra("ResponseDateFinish"));


        camera_open_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent
                        = new Intent(MediaStore
                        .ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, pic_id);

            }
        });

        camera_open_id2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent
                        = new Intent(MediaStore
                        .ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, pic_id2);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UploadtoDB();
                updatePICstatus4();
                Intent i = new Intent(DetailBreakdownPage2.this, MainDashboard.class);
                startActivity(i);
            }
        });

        ////////////////////////////////////////////////////////Stop watch Section///////////////////////////////////////////////////////////////////////////////////////////////////////////
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        startChronometer();
        ////////////////////////////////////////////////////////end of stop watch section///////////////////////////////////////////////////////////////////////////////////////////////////
        updatePICstatus3();

    } // end of onCreate

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void UploadtoDB() {
        String msg = "unknown";

        chronometer.stop();
        long saveTime = SystemClock.elapsedRealtime() - chronometer.getBase();
        int seconds = (int)(saveTime/1000);

        String currentDateFinish= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());

        try {
            Connection con = connectionClass.CONN();

            String commands =
                    "Insert into machinestatustest (Line, Station, MachineID, Response_Time_Finish, Repair_Time_Start, Repair_Time_Finish, Repair_Duration, PIC, Image_Problem, Problem_Desc, Image_Solution, Solution_Desc) values " +
                            "('" + Line + "','" + Station + "','" + MachineID + "','" + ResponseDateFinish + "','" +
                            date_start_text.getText() + "','" + currentDateFinish + "','" + seconds + "','" + picr + "','" +
                            encodedImageProblem + "','" + problem_desc_text.getText().toString() + "','" +
                            encodedImageSolution + "','" + solution_desc_text.getText().toString() + "')";

            PreparedStatement preStmt = con.prepareStatement(commands);
            preStmt.execute();
            msg = "Inserted Successfully";
        } catch (SQLException ex) {
            msg = ex.getMessage().toString();
            Log.d("hitesh", msg);

        } catch (IOError ex) {
            // TODO: handle exception
            msg = ex.getMessage().toString();
            Log.d("hitesh", msg);
        } catch (AndroidRuntimeException ex) {
            msg = ex.getMessage().toString();
            Log.d("hitesh", msg);

        } catch (NullPointerException ex) {
            msg = ex.getMessage().toString();
            Log.d("hitesh", msg);
        }

        catch (Exception ex) {
            msg = ex.getMessage().toString();
            Log.d("hitesh", msg);
        }

        txtmsg.setText(msg);

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void updatePICstatus4()
    {
        try {
            Connection connection = connectionClass.CONN();
            String query = "UPDATE machinedashboard SET Status=4, PIC=NULL where MachineID='" + MachineID +"'";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.execute();
        }catch (SQLException ex){
        }
    }

    public void updatePICstatus3()
    {
        try {
            Connection connection = connectionClass.CONN();
            String query = "UPDATE machinedashboard SET Status=3, PIC='" + picr + "' where MachineID='" + MachineID +"'";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.execute();
        }catch (SQLException ex){
        }
    }

    public void updatePICstatus2()
    {
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
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case pic_id: {

                    Bitmap photo = (Bitmap) data.getExtras()
                            .get("data");
                    click_image_id.setImageBitmap(photo);

                    if (photo != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();

                        encodedImageProblem = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        Toast.makeText(DetailBreakdownPage2.this, "Conversion Done",
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                        Toast.makeText(DetailBreakdownPage2.this, "There's an error",
                                Toast.LENGTH_SHORT).show();
                        }
                    break;
                }


                case pic_id2:
                {
                    Bitmap photo = (Bitmap) data.getExtras()
                            .get("data");
                    click_image_id2.setImageBitmap(photo);

                    if (photo != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();

                        encodedImageSolution = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        Toast.makeText(DetailBreakdownPage2.this, "Conversion Done",
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                        Toast.makeText(DetailBreakdownPage2.this, "There's an error",
                                Toast.LENGTH_SHORT).show();
                        }
                    break;
                }
            }
        }
    }

}
