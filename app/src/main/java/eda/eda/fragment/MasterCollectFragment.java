package eda.eda.fragment;

import android.app.ProgressDialog;
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
import android.view.animation.Animation;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
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
    private static int theadnum;

    public static ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View brandLayout = inflater.inflate(R.layout.fragment_card, container, false);
        mRecyclerView = (RecyclerView) brandLayout.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CardAdapter(getActivity(),cardList);
        mRecyclerView.setAdapter(mAdapter);
        return brandLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardList= Collections.synchronizedList(new ArrayList<Card>());
        progressDialog =
                new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("加载中...");
        progressDialog.show();
        //theadnum=1;
        inits();
        //while(theadnum!=0);

        //initConTask();
    }

    private void initConTask(){

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                theadnum=1;
                inits();
                while(theadnum!=0);
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);

                System.out.println("okokokokokokkokoook");

                mAdapter = new CardAdapter(getActivity(),cardList);
                mRecyclerView.setAdapter(mAdapter);

            }
        }.execute();
    }
    private void inits(){


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
        //theadnum=array.length();
        for(int i=0;i<array.length();i++) {
            int postId = 0;
            try {
                postId = array.getInt(i);
                json.put("articleuid", postId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            conTask(GlobalValue.getActicleUrl, json);

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
                        userProfilePicture = GlobalValue.imageUrl+jsonObject.getString("userProfilepicture");
                        pictureUrl = GlobalValue.imageUrl+jsonObject.getString("pictureurl");
                        cardList.add(new Card(userName, pictureUrl, userProfilePicture,
                                getUuid(), json.getInt("articleuid")));
                        //synchronized (this) {
                        //    theadnum--;
                        //}

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
                    JSONObject jsonObject=jc.getJson();

                    return jsonObject;
                } else return null;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                if (jsonObject != null) {
                    try {
                        array = jsonObject.getJSONArray("list");
                        initCard(json, getUuid());

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


