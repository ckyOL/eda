package eda.eda.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import eda.eda.Coder;
import eda.eda.Dialog;
import eda.eda.GlobalValue;
import eda.eda.JsonConnection;
import eda.eda.R;

public class PostActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1;
    private String capturePath = null;
    private String SAVED_IMAGE_DIR_PATH="pic/";
    private Button pictureButoon;
    private Button nextButoon;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        pictureButoon =(Button) findViewById(R.id.pictureButoon);
        nextButoon =(Button) findViewById(R.id.nextButoon);
        image=(ImageView)findViewById(R.id.userPic);
        pictureButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出候选框
            }
        });
        nextButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到下一个fragment并传参
            }
        });
    }

    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            String out_file_path = SAVED_IMAGE_DIR_PATH;
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            capturePath = SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".jpg";
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capturePath)));
            getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
        }
        else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri=null;
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            uri = data.getData();

        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA ) {
            uri = Uri.fromFile(new File(capturePath));
        }
        if(uri!=null)
        {
            nextButoon.setClickable(true);
            image.setImageURI(uri);
        }
    }

}
