package eda.eda.fragment;

import android.content.Intent;
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
import eda.eda.activity.MainActivity;

public class MasterCollectFragment extends Fragment{

    /**
     * 卡片
     */
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Card> cardList;

    private JSONArray array;
    private String userName;
    private String userProfilePicture;
    private String pictureUrl;

    public static Fragment newInstance(Context context) {
        MasterCollectFragment masterCollectFragment = new MasterCollectFragment();
        return masterCollectFragment;
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

        JSONObject json = new JSONObject();
        getActicle(json, 0);

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

            JsonConnection jsonConn =
                    new JsonConnection(GlobalValue.getActicleUrl,
                    json, "POST");
            JsonConnection jc = jsonConn;
            if (jc.connectAndGetJson()) {
                JSONObject jsonObject=jc.getJson();
                try {
                    userName = jsonObject.getString("username");
                    userProfilePicture = GlobalValue.imageUrl+jsonObject.getString("userProfilepicture");
                    pictureUrl = GlobalValue.imageUrl+jsonObject.getString("pictureurl");

                    cardList.add(new Card(userName,pictureUrl,userProfilePicture,uuid,postId));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(
                        getActivity(),
                        "生成卡片连接失败",
                        Toast.LENGTH_SHORT).show();
            }
        }
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
                        initCard(json,getUuid());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(
                            getActivity(),
                            "获取列表连接失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(jsonConn);
    }

}


