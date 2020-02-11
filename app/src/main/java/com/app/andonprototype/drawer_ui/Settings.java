package com.app.andonprototype.drawer_ui;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.andonprototype.Background.ConnectionClass;
import com.app.andonprototype.Background.SwipeDismissBaseActivity;
import com.app.andonprototype.R;
import com.app.andonprototype.Success_Page;
import com.app.andonprototype.ui.Dashboard.DetailBreakdownPage2;

import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static com.app.andonprototype.Background.SaveSharedPreference.getNama;

public class Settings extends SwipeDismissBaseActivity {

    ConnectionClass connectionClass;
    String encodedProfilePic;
    public String ID, pic;
    ProgressBar pbbar;
    public ContentValues values;
    public Uri imageUri;
    TextView txtmsg;
    ImageView profile_pic;
    Button profile_pic_btn, btnConfirm;
    private static final int pic_id = 120;
    private static final int ZBAR_CAMERA_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        connectionClass = new ConnectionClass();
        values = new ContentValues();

        pic = getNama(this);

        txtmsg = findViewById(R.id.txtmsg);
        profile_pic = findViewById(R.id.profile_picture);
        profile_pic_btn = findViewById(R.id.camera_button_profile_picture);
        btnConfirm = findViewById(R.id.btn_savePic);
        pbbar = findViewById(R.id.pbbar);

        pbbar.setVisibility(View.INVISIBLE);

        profile_pic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Settings.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Settings.this,
                            new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
                }else{
                camera_profile_pic();
            }
            }
        });

        GetPicture();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UploadData uploadData = new UploadData();
//                uploadData.execute();
                UploadPictoDB();
            }
        });


    }

    public class UploadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
            //do initialization of required objects objects here
        }

        @Override
        protected Void doInBackground(Void... params) {
            UploadPictoDB();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pbbar.setVisibility(View.GONE);
            Loadsuccesspage();
        }
    }

    private void Loadsuccesspage() {
        Intent i = new Intent(this, Success_Page.class);
        startActivity(i);
        finish();
    }

    private void UploadPictoDB() {
        String msg = "unknown";
        try {
            Connection con = connectionClass.CONN();
            String commands =
                    "UPDATE userid SET Image = '" + encodedProfilePic + "' WHERE Nama = '" + pic + "' ";
            PreparedStatement preStmt = con.prepareStatement(commands);
            preStmt.execute();
            msg = encodedProfilePic;
        } catch (IOError | Exception ex) {
            msg = ex.getMessage();
            Log.d("hitesh", msg);
            pbbar.setVisibility(View.INVISIBLE);
        }
        txtmsg.setText(msg);
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
                    RotatedselectedImageProblem = rotateImage(selectedImageProblem, 270);

                    profile_pic.setImageBitmap(RotatedselectedImageProblem);

                    if (selectedImageProblem != null) {
                        encodedProfilePic = encodeImage(RotatedselectedImageProblem);
                    } else {
                        Toast.makeText(this, "Cancelled",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void GetPicture() {
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public void camera_profile_pic() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, pic_id);
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

}
