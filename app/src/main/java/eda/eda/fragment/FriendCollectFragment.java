package eda.eda.fragment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import eda.eda.Card;
import eda.eda.CardAdapter;
import eda.eda.GlobalValue;
import eda.eda.JsonConnection;
import eda.eda.R;

public class FriendCollectFragment extends Fragment{

    /**
     * 卡片
     */
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Card> cardList;

    private JSONArray array;
    private String userName;
    private String userProfilePicture;
    private String pictureUrl;

    public static Fragment newInstance(Context context) {
        FriendCollectFragment friendCollectFragment = new FriendCollectFragment();
        return friendCollectFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View brandLayout = inflater.inflate(R.layout.fragment_card, container, false);
        return brandLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inits();
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CardAdapter(getActivity(),cardList);
        mRecyclerView.setAdapter(mAdapter);
    }
    private void inits(){
        cardList= new ArrayList<Card>();
        String uuid = getUuid();
        JSONObject json = new JSONObject();
        JSONObject jsonCard = new JSONObject();
        getActicle(json, 0);

        //initCard(jsonCard, uuid);
    }

    private String getUuid(){
        SharedPreferences data =
                getActivity().getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        String uuid = data.getString("uuid", "");

        return uuid;
    }

    private void getActicle(JSONObject json,int code){
        try {
            json.put("code",code);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cardConTask(GlobalValue.defaultUri, json);
    }

    private void initCard(JSONObject json,String uuid){
        for(int i=0;i<array.length();i++) {
            int postId=0;
            try {
                postId = array.getInt(i);
                json.put("articleuid",postId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            conTask(GlobalValue.getActicleUrl, json);

            cardList.add(new Card(userName,pictureUrl,userProfilePicture,uuid,postId));
        }
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
                        userName = jsonObject.getString("username");
                        userProfilePicture = jsonObject.getString("userProfilepicture");
                        pictureUrl = jsonObject.getString("pictureurl");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(
                            getActivity(),
                            "连接失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(jsonConn);
    }

    public void cardConTask(String url, final JSONObject json) {
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
                        array = jsonObject.getJSONArray("list");
                        Toast.makeText(getActivity(),array.getInt(0),Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(
                            getActivity(),
                            "连接失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(jsonConn);
    }

}


