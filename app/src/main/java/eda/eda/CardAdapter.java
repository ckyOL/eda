package eda.eda;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.HttpGet;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import eda.eda.activity.DetailActivity;
import eda.eda.activity.MainActivity;
import eda.eda.activity.PostActivity;
import eda.eda.fragment.MasterCollectFragment;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private ArrayList<Card> cardDataSet;
    private Context mContext;
    private SharedPreferences data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mUserName;
        public ImageView mCollectPic;
        public ImageView mProfilePic;

        public ImageView mLike;
        public ImageView mStar;

        public ViewHolder( View v ) {
            super(v);
            mUserName = (TextView) v.findViewById(R.id.card_username);
            mCollectPic = (ImageView) v.findViewById(R.id.card_collection_pic);
            mProfilePic = (ImageView) v.findViewById(R.id.card_profile_pic);
            mStar = (ImageView) v.findViewById(R.id.card_star);
            mLike = (ImageView) v.findViewById(R.id.card_like);
        }
    }

    public CardAdapter(Context mContext , ArrayList<Card> cardDataSet)
    {
        this.mContext = mContext;
        this.cardDataSet = cardDataSet;
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Card mCard = cardDataSet.get(position);
        holder.mUserName.setText(mCard.userName);
        holder.mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("uid", mCard.uuid);
                    json.put("postid", mCard.postId);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                conTask(GlobalValue.favoriteUri, json);
                holder.mLike.setImageDrawable(Drawable.createFromPath("card_like_on"));
            }
        });

        holder.mStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("uid", mCard.uuid);
                    System.out.println(mCard.uuid);
                    json.put("postid", mCard.postId);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                conTask(GlobalValue.likeUri, json);
            }
        });

        final Bitmap profileBitmap = AsyncGetHttpBitmap(mCard.profileName);
        final Bitmap collectBitmap = AsyncGetHttpBitmap(mCard.imageName);
        holder.mProfilePic.setImageBitmap(profileBitmap);
        holder.mCollectPic.setImageBitmap(collectBitmap);
        holder.mCollectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(mContext,DetailActivity.class);
                String uuid = mCard.uuid;
                detail.putExtra("uuid",uuid);
                detail.putExtra("profileName",mCard.profileName);
                detail.putExtra("imageName",mCard.imageName);
                mContext.startActivity(detail);
            }
        });
    }

    public Bitmap AsyncGetHttpBitmap(String url) {
        ExecutorService executor = Executors.newCachedThreadPool();
        GetBitmapTask task=new GetBitmapTask(url);
        Future<Bitmap> result=executor.submit(task);
        executor.shutdown();
        Bitmap bitmap=null;
        try
        {
            bitmap=result.get();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    class GetBitmapTask implements Callable<Bitmap> {

        String url;

        public GetBitmapTask(String url)
        {
            this.url=url;
        }

        @Override
        public Bitmap call() throws Exception {

            return getHttpBitmap(url);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cardDataSet.size();
    }

    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
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
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;

    }

    public void conTask(String url, final JSONObject json) {
        JsonConnection jsonConn = new JsonConnection(url, json, "POST");
        new AsyncTask<JsonConnection, Void, JSONObject>() {

            @Override
            protected JSONObject doInBackground(JsonConnection... params) {
                JsonConnection jc = params[0];
                if (jc.connectAndGetJson()) {
                    return jc.getJson();
                } else return null;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);


                if (jsonObject != null) {
                    try {
                        switch (jsonObject.getInt("code")) {

                            case 0:
                                Toast.makeText(mContext,
                                        "服务器异常",
                                        Toast.LENGTH_SHORT).show();

                                break;
                            case 1:
                                Toast.makeText(mContext,
                                        jsonObject.getString("msg"),
                                        Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(
                            mContext,
                            "连接失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(jsonConn);
    }


}