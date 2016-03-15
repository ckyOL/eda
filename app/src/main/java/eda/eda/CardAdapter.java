package eda.eda;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Card> cardDataSet;
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

    public CardAdapter(Context mContext , List<Card> cardDataSet)
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
    public void onBindViewHolder(ViewHolder holder, int position) {
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
            }
        });

        holder.mStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("posterId", mCard.posterId);
                    json.put("postid", mCard.postId);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                conTask(GlobalValue.likeUri, json);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.mCollectPic.setImageDrawable(mContext.getDrawable(mCard.getImageResourceId(mContext, mCard.imageName)));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.mProfilePic.setImageDrawable(mContext.getDrawable(mCard.getImageResourceId(mContext, mCard.profileName)));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cardDataSet.size();
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