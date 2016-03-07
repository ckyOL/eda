package eda.eda;

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

    DrawerLayout mainActDrawerLayout;//Drawer
    Toolbar mainActToolbar;//AppBar
    ActionBarDrawerToggle mainActDrawerToggle;//侧边栏监听器
    NavigationView mainActNavigationView;//侧边栏
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
                                //switchToBlog();
                                break;
                            case R.id.navigation_mastercollect:
                                //switchToAbout();
                                break;
                            case R.id.navigation_friendcollect:
                                Intent newAct = new Intent(MainActivity.this, FriendCollectActivity.class);
                                startActivity(newAct);
                                break;

                        }
                        menuItem.setChecked(true);
                        mainActDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

}
