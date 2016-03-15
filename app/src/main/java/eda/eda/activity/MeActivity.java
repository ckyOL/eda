package eda.eda.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import eda.eda.R;

public class MeActivity extends AppCompatActivity {

    Toolbar meActToolbar; //AppBar
    TabLayout meTabLayout; //TabLayout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        meActToolbar = (Toolbar) findViewById(R.id.metoolbar);//初始化
        setSupportActionBar(meActToolbar);//替换 ActionBar


        //按钮监听器
        meActToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_post:
                        //发布
                        break;
                }
                return true;
            }
        });

        meTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        meTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        meTabLayout.addTab(meTabLayout.newTab().setText("搭配"));
        meTabLayout.addTab(meTabLayout.newTab().setText("收藏"));

    }

    /**
     *开启主页按钮
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.me, menu);
        return true;
    }

}
