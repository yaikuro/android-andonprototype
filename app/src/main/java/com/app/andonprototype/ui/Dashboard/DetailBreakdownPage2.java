package com.app.andonprototype.ui.Dashboard;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
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

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.R;
import com.app.andonprototype.Success_Page;

import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        connectionClass = new ConnectionClass();
        values = new ContentValues();

        Line = getIntent().getStringExtra("Line");
        Station = getIntent().getStringExtra("Station");
        MachineID = getIntent().getStringExtra("MachineID");
        picr = getIntent().getStringExtra("PIC");
        ResponseDateFinish = getIntent().getStringExtra("ResponseDateFinish");

        camera_open_id      = findViewById(R.id.camera_button);
        click_image_id      = findViewById(R.id.problem_pic);

        camera_open_id2     = findViewById(R.id.camera_button_solution);
        click_image_id2     = findViewById(R.id.solution_pic);

        problem_desc_text   = findViewById(R.id.problem_desc_text);
        solution_desc_text  = findViewById(R.id.solution_desc_text);

        machine_id          = findViewById(R.id.machine_id);
        date_start_text     = findViewById(R.id.date_start_text);
        pic                 = findViewById(R.id.pic);

        btnSave             = findViewById(R.id.btnSave);
        txtmsg              = findViewById(R.id.txtmsg);

        pbbarDetail         = findViewById(R.id.pbbarDetail);
        pbbarDetail.setVisibility(View.INVISIBLE);

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
    @Override
    protected void onDestroy(){
        super.onDestroy();
        pbbarDetail.setVisibility(View.GONE);
    }

    public void Loadsuccespage() {
        Intent i = new Intent(DetailBreakdownPage2.this, Success_Page.class);
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
            Loadsuccespage();
        }
        catch (IOError | Exception ex) {
            msg = ex.getMessage();
            Log.d("hitesh", msg);
            pbbarDetail.setVisibility(View.INVISIBLE);
        }
        txtmsg.setText(msg);
    }

    public void updatePICstatus4() {
        try {
            Connection connection = connectionClass.CONN();
            String query = "UPDATE stationdashboard SET Status=4, PIC='" + picr + "' where Station='" + Station +"' and Line = '" + Line + "'";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.execute();
        }catch (SQLException ex){
        }
    }

    public void updatePICstatus3() {
        try {
            Connection connection = connectionClass.CONN();
            String query = "UPDATE stationdashboard SET Status=3, PIC='" + picr + "' where Station='" + Station +"' and Line = '" + Line + "'";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.execute();
        }catch (SQLException ex){
        }
    }

    public void updatePICstatus2() {
        try {
            Connection connection = connectionClass.CONN();
            String query = "UPDATE stationdashboard SET Status=2, PIC=NULL where Station='" + Station +"' and Line = '" + Line + "'";
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

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case pic_id: {
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImageProblem = BitmapFactory.decodeStream(imageStream);

                    Bitmap RotatedselectedImageProblem = null;
                    RotatedselectedImageProblem = rotateImage(selectedImageProblem, 90);

                    click_image_id.setImageBitmap(RotatedselectedImageProblem);

                    if (selectedImageProblem != null) {
                        encodedImageProblem = encodeImage(RotatedselectedImageProblem);
                        Toast.makeText(DetailBreakdownPage2.this, "Conversion Done",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailBreakdownPage2.this, "Cancelled",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            case pic_id2:
            {
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImageSolution = BitmapFactory.decodeStream(imageStream);

                    Bitmap RotatedselectedImageSolution = null;
                    RotatedselectedImageSolution = rotateImage(selectedImageSolution, 90);

                    click_image_id2.setImageBitmap(RotatedselectedImageSolution);

                    if (selectedImageSolution != null) {
                        encodedImageSolution = encodeImage(RotatedselectedImageSolution);
                        Toast.makeText(DetailBreakdownPage2.this, "Conversion Done",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailBreakdownPage2.this, "Cancelled",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch (IOException e) {
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
