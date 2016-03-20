package eda.eda.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import eda.eda.Card;
import eda.eda.GlobalValue;
import eda.eda.JsonConnection;
import eda.eda.R;

public class DetailActivity extends AppCompatActivity {

    ImageView userShow, profileImage;
    TextView detailUserName, detailPostTime, detailLikeNum;
    TextView detailBrand,detailStyle,detailDescribe;
    String userName, postTime, likeNum;
    String userShowPic,userProfilePic,uuid;
    String brand,style,describe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        uuid = getIntent().getStringExtra("uuid");
        findView();
        getImage();
        init();

    }

    private void findView() {
        userShow = (ImageView) findViewById(R.id.details_usershow);
        profileImage = (ImageView) findViewById(R.id.profile_image);
        detailUserName = (TextView) findViewById(R.id.details_username);
        detailPostTime = (TextView) findViewById(R.id.details_posttime);
        detailLikeNum = (TextView) findViewById(R.id.details_likenum);
        detailBrand = (TextView) findViewById(R.id.details_brand);
        detailStyle = (TextView) findViewById(R.id.details_style);
        detailDescribe = (TextView) findViewById(R.id.details_describe);
    }

    private void init() {
        final Bitmap profileBitmap = AsyncGetHttpBitmap(userProfilePic);
        final Bitmap collectBitmap = AsyncGetHttpBitmap(userShowPic);
        userShow.setImageBitmap(collectBitmap);
        profileImage.setImageBitmap(profileBitmap);
        detailUserName.setText(userName);
        detailPostTime.setText(postTime);
        detailLikeNum.setText(likeNum);
        detailBrand.setText(brand);
        detailStyle.setText(style);
        detailDescribe.setText(describe);

    }

    private void getImage() {
        JSONObject json = new JSONObject();
        try {
            json.put("articleuid", uuid);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject detailJson = GlobalValue.AsyncConnect(GlobalValue.getActicleDetailsUrl, json);
        if (detailJson != null) {
            try {
                userName = detailJson.getString("username");
                postTime = detailJson.getString("posttime");
                likeNum = detailJson.getString("likenum");
                brand = detailJson.getString("brand");
                style = detailJson.getString("style");
                describe = detailJson.getString("describe");
                userProfilePic = GlobalValue.imageUrl + detailJson.getString("userProfilepicture");
                userShowPic = GlobalValue.imageUrl + detailJson.getString("pictureurl");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(
                    this,
                    "连接失败",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap AsyncGetHttpBitmap(String url) {
        ExecutorService executor = Executors.newCachedThreadPool();
        GetBitmapTask task = new GetBitmapTask(url);
        Future<Bitmap> result = executor.submit(task);
        executor.shutdown();
        Bitmap bitmap = null;
        try {
            bitmap = result.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    class GetBitmapTask implements Callable<Bitmap> {

        String url;

        public GetBitmapTask(String url) {
            this.url = url;
        }

        @Override
        public Bitmap call() throws Exception {

            return getHttpBitmap(url);
        }
    }

    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;

    }

}