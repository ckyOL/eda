package eda.eda;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class MasterCollectActivity extends AppCompatActivity{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Card> cardList;

    private DrawerLayout friendActDrawerLayout;//Drawer
    private Toolbar friendActToolbar;//AppBar
    private ActionBarDrawerToggle friendActDrawerToggle;//侧边栏监听器
    private NavigationView friendActNavigationView;//侧边栏

    private SharedPreferences data; //存储数据
    private SharedPreferences.Editor editor; //编辑器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);

        friendActToolbar = (Toolbar) findViewById(R.id.toolbar);//初始化
        setSupportActionBar(friendActToolbar);//替换 ActionBar
        friendActToolbar.setTitle("@string/navigation_mastercollect");//标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //按钮监听器
        friendActToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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

        //drawer、监听器初始化
        friendActDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        friendActDrawerToggle =
                new ActionBarDrawerToggle(this, friendActDrawerLayout, friendActToolbar, R.string.drawer_open,
                        R.string.drawer_close);
        friendActDrawerToggle.syncState();
        friendActDrawerLayout.setDrawerListener(friendActDrawerToggle);

        //侧边栏初始化
        friendActNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(friendActNavigationView);


        inits();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CardAdapter(this,cardList);
        mRecyclerView.setAdapter(mAdapter);

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
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_post:
                                //switchToExample();
                                break;
                            case R.id.navigation_mycollect:

                                break;
                            case R.id.navigation_mastercollect:
                                //switchToAbout();
                                break;
                            case R.id.navigation_friendcollect:
                                Intent mainAct = new Intent(MasterCollectActivity.this, MainActivity.class);
                                startActivity(mainAct);
                                break;
                            case R.id.navigation_brandcollect:
                                Intent brandCollAct = new Intent(MasterCollectActivity.this, BrandCollectActivity.class);
                                startActivity(brandCollAct);
                                break;
                            case R.id.navigation_logout:
                                data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
                                editor = data.edit();
                                editor.putBoolean("login",false);
                                editor.commit();
                                Intent loginAct = new Intent(MasterCollectActivity.this, LoginActivity.class);
                                startActivity(loginAct);
                                finish();

                        }
                        menuItem.setChecked(true);
                        friendActDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void inits(){
        cardList= new ArrayList<Card>();
        Card card1 = new Card("user1","card1_collect","card1_profile");
        Card card2 = new Card("User2","card2_collect","card1_profile");
        cardList.add(card1);
        cardList.add(card2);
    }
}


