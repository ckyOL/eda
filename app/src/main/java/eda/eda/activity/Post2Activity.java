package eda.eda.activity;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.SQLOutput;

import eda.eda.AsyncFileUpload;
import eda.eda.GlobalValue;
import eda.eda.R;

public class Post2Activity extends AppCompatActivity {

    Uri uri;
    ImageView image;
    EditText brandText;
    EditText kindText;
    EditText styleText;
    Button postButton;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post2);
        context=this;
        image=(ImageView) findViewById(R.id.userPic2);
        brandText=(EditText) findViewById(R.id.brand);
        kindText=(EditText) findViewById(R.id.kind);
        styleText=(EditText) findViewById(R.id.style);
        postButton=(Button) findViewById(R.id.postButton);
        uri=getIntent().getParcelableExtra("url");
        image.setImageURI(uri);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GlobalValue.isEmpty(brandText.getText().toString()))
                {
                    //弹窗
                    Toast.makeText(context, "请填写品牌", Toast.LENGTH_LONG).show();
                }
                else if(GlobalValue.isEmpty(kindText.getText().toString()))
                {

                    //弹窗
                    Toast.makeText(context, "请填写种类", Toast.LENGTH_LONG).show();
                }
                else if(GlobalValue.isEmpty(styleText.getText().toString()))
                {

                    //弹窗
                    Toast.makeText(context, "请填写风格", Toast.LENGTH_LONG).show();
                }
                else
                {
                    AsyncFileUpload fileUpload=new AsyncFileUpload(GlobalValue.getUuid(context));

                    if(fileUpload.pictureUpload(GlobalValue.getAbsoluteImagePath(context,uri),GlobalValue.UploadPictureUrl))
                    {
                        String picUrl=fileUpload.getFileName();
                        JSONArray lables=new JSONArray();
                        lables.put(brandText.getText().toString());
                        lables.put(kindText.getText().toString());
                        lables.put(styleText.getText().toString());
                        JSONObject json=new JSONObject();
                        try {
                            json.put("uid",GlobalValue.getUuid(context));
                            json.put("picturePath",picUrl);
                            json.put("lables",lables);
                            JSONObject rjson=GlobalValue.AsyncConnect(GlobalValue.PostUrl,json);
                            if(rjson!=null)
                            {
                                Toast.makeText(context, rjson.getString("msg"), Toast.LENGTH_LONG).show();
                                System.out.println(rjson.getString("msg"));
                            }else
                            {
                                Toast.makeText(context, "连接失败", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else
                    {
                        System.out.println(fileUpload.getMessage());

                        Toast.makeText(context, "图片上传失败", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



    }
}
