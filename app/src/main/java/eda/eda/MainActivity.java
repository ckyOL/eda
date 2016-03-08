package eda.eda;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mainActDrawerLayout;//Drawer
    private Toolbar mainActToolbar;//AppBar
    private ActionBarDrawerToggle mainActDrawerToggle;//侧边栏监听器
    private NavigationView mainActNavigationView;//侧边栏

    private SharedPreferences data; //存储数据
    private SharedPreferences.Editor editor; //编辑器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActToolbar = (Toolbar) findViewById(R.id.toolbar);//初始化
        setSupportActionBar(mainActToolbar);//替换 ActionBar
        mainActToolbar.setTitle("@string/app_name");//标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                                Intent masterCollAct = new Intent(MainActivity.this, MasterCollectActivity.class);
                                startActivity(masterCollAct);
                                break;
                            case R.id.navigation_friendcollect:

                                break;
                            case R.id.navigation_brandcollect:
                                Intent brandCollAct = new Intent(MainActivity.this, BrandCollectActivity.class);
                                startActivity(brandCollAct);
                                break;
                            case R.id.navigation_logout:
                                data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
                                editor = data.edit();
                                editor.putBoolean("login",false);
                                editor.commit();
                                Intent loginAct = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(loginAct);
                                finish();

                        }
                        menuItem.setChecked(true);
                        mainActDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

}
