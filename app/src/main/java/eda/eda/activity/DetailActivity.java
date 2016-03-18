package eda.eda.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

import eda.eda.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ImageView imageView=(ImageView)findViewById(R.id.usershow);
        imageView.setImageURI(Uri.fromFile(new File(getIntent().getStringExtra("url"))));
    }
}
