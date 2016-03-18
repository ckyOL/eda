package eda.eda.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eda.eda.Card;
import eda.eda.GlobalValue;
import eda.eda.JsonConnection;
import eda.eda.R;
import eda.eda.fragment.BrandCollectFragment;
import eda.eda.fragment.FriendCollectFragment;
import eda.eda.fragment.MasterCollectFragment;

public class MainActivity extends AppCompatActivity {

    /**
     * 界面
     */
    private DrawerLayout mainActDrawerLayout;//Drawer
    private Toolbar mainActToolbar;//AppBar
    private ActionBarDrawerToggle mainActDrawerToggle;//侧边栏监听器
    private NavigationView mainActNavigationView;//侧边栏

    private ArrayList<Card> cardList;
    private Context context;
    /**
     * 数据储存
     */
    private SharedPreferences data; //存储数据
    private SharedPreferences.Editor editor; //编辑器

    /**
     * 碎片
     */
    private MasterCollectFragment masterCollect;
    private Class fragmentClass;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    public static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_main);
        setMainActToolbar();
        setMainActNavigationView();
        progressDialog =
                new ProgressDialog(context);
        //progressDialog.setCancelable(false);
        progressDialog.setMessage("加载中...");
        progressDialog.show();
        conTask();

        ImageView imageView=(ImageView)findViewById(R.id.profile_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(MainActivity.this,MeActivity.class);
                startActivity(main);
            }
        });

        FloatingActionButton fb=(FloatingActionButton)findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GlobalValue.getUuid(context).equals(""))
                {
                    Toast.makeText(context,"请先登录",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent main = new Intent(MainActivity.this,PostActivity.class);
                    startActivity(main);
                }

            }
        });

        //按钮监听器
        mainActToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        //搜索
                        break;
                    case R.id.action_screen:
                        //筛选
                        break;
                    case R.id.action_messages:
                        //信息
                        break;
                }
                return true;
            }
        });

    }

    /**
     *开启主页按钮
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    /**设置NavigationView点击
     *
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        fragment = null;
                        fragmentClass = MasterCollectFragment.class;
                        switch (menuItem.getItemId()) {

                            case R.id.navigation_mycollect:

                                break;
                            case R.id.navigation_mastercollect:
                                fragmentClass = MasterCollectFragment.class;
                                break;
                            case R.id.navigation_friendcollect:
                                fragmentClass = FriendCollectFragment.class;
                                break;
                            case R.id.navigation_brandcollect:
                                fragmentClass = BrandCollectFragment.class;
                                break;
                            case R.id.navigation_logout:
                                data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
                                editor = data.edit();
                                editor.putBoolean("login", false);
                                editor.commit();
                                Intent loginAct = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(loginAct);
                                finish();

                        }

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                        menuItem.setChecked(true);
                        mainActToolbar.setTitle(menuItem.getTitle());
                        mainActDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }



    private void setMainActToolbar(){
        mainActToolbar = (Toolbar) findViewById(R.id.toolbar);//初始化
        setSupportActionBar(mainActToolbar);//替换 ActionBar
        mainActToolbar.setTitle("@string/app_name");//标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setMainActNavigationView(){
        //drawer、监听器初始化
        mainActDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mainActDrawerToggle =
                new ActionBarDrawerToggle(this, mainActDrawerLayout, mainActToolbar, R.string.drawer_open,
                        R.string.drawer_close);
        mainActDrawerToggle.syncState();
        mainActDrawerLayout.setDrawerListener(mainActDrawerToggle);

        //侧边栏初始化
        mainActNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(mainActNavigationView);
    }


    public void conTask()
    {
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                inits();
                masterCollect = new MasterCollectFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("cardlist", (Serializable) cardList);
                masterCollect.setArguments(bundle);
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, masterCollect).commit();
                return null;
            }

            protected void onPostExecute(Void v)
            {
                progressDialog.dismiss();
            }
        }.execute();
    }

    private void inits(){

        JSONObject jsonObject=getActicle(0);
        if (jsonObject != null) {
            try {
                JSONArray array = jsonObject.getJSONArray("list");
                initCard(array);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(
                    context,
                    "获取列表连接失败",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private JSONObject getActicle(int code){
        JSONObject json=new JSONObject();
        try {
            json.put("code",code);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GlobalValue.AsyncConnect(GlobalValue.defaultUri, json);
    }

    private void initCard(JSONArray array){
        cardList= new ArrayList<Card>();
        //System.out.println(array.length()+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        for(int i=0;i<array.length();i++) {
            JSONObject json=new JSONObject();
            int postId=0;
            try {
                postId = array.getInt(i);
                json.put("articleuid", postId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject cardJson=GlobalValue.AsyncConnect(GlobalValue.getActicleUrl, json);
            if (cardJson != null ) {
                try {
                    String userName = cardJson.getString("username");
                    String userProfilePicture = GlobalValue.imageUrl+cardJson.getString("userProfilepicture");
                    String pictureUrl = GlobalValue.imageUrl+cardJson.getString("pictureurl");
                    cardList.add(new Card(userName, pictureUrl, userProfilePicture,
                            GlobalValue.getUuid(context), postId));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(
                        context,
                        "连接失败",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
