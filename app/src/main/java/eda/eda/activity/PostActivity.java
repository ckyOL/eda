package eda.eda.activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import eda.eda.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class PostActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1;
    private String capturePath = null;
    private static String SAVED_IMAGE_DIR_PATH=Environment.getExternalStorageDirectory().toString()+"/edaCemare/";
    private Button pictureButoon;
    private Button nextButton;
    private ImageView image;

    private Fragment fragment;
    private FragmentManager fragmentManager;

    private AlertDialog.Builder dialog;
    private Intent main;

    private boolean flag=false;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        main = new Intent(PostActivity.this,Post2Activity.class);
        setContentView(R.layout.activity_post);
        pictureButoon =(Button) findViewById(R.id.pictureButoon);
        nextButton =(Button) findViewById(R.id.nextButton);
        image=(ImageView)findViewById(R.id.userPic);
        pictureButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出候选框
                setDialog("选取照片","自相册","拍照");
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到下一个fragment并传参
                if(flag) {


                    startActivity(main);
                }

                else
                    Toast.makeText(context,"请添加图片",Toast.LENGTH_SHORT).show();
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
            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
        }
        else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }

//        if (state.equals(Environment.MEDIA_MOUNTED)) {
//            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
//            String out_file_path = SAVED_IMAGE_DIR_PATH;
//            File dir = new File(out_file_path);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            capturePath = SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".jpg";
//            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capturePath)));
//            getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
//        }
//        else {
//            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
//        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Uri uri=null;
//        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
//            uri = data.getData();
//
//        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA ) {
//            uri = Uri.fromFile(new File(capturePath));
//        }
//        if(uri!=null)
//        {
//            flag=true;
//            image.setImageURI(uri);
//            bundle = new Bundle();
//            bundle.putString("url",uri.getPath());
//
//
//        }
        Uri uri=null;
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            uri = data.getData();


        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA ) {
            uri = data.getData();
            if (uri == null) {
                //use bundle to get data
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap photo = (Bitmap) bundle.get("data"); //get bitmap
                    //spath :生成图片取个名字和路径包含类型
                    String path=saveImage(photo);
                    uri=Uri.fromFile(new File(path));
                } else {
                    Toast.makeText(getApplicationContext(), "err****", Toast.LENGTH_LONG).show();
                }
            }
        }

            if(uri!=null)
        {
            flag=true;
            image.setImageURI(uri);
            main.putExtra("url",uri);

        }
    }
    public static String saveImage(Bitmap photo) {
        String spath=SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".jpg";
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spath;
    }


    public void setDialog(String massage,String buttonText,String secButtonText){
        dialog = new AlertDialog.Builder(this);
        dialog.setMessage(massage);
        dialog.setCancelable(false);
        dialog.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getImageFromAlbum();
            }
        });

        dialog.setNeutralButton(secButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                getImageFromCamera();
            }
        });
        dialog.show();
    }
}
