package com.is.shane;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

//抽屉和Tab显示公共方法 让activity继承这里
public class DrawerTabFragment extends AppCompatActivity {

    //此处为公共变量 否则其他类无法访问
    public DrawerLayout drawerLayout;
    //抽屉开关
    public int click=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //底部tab
    protected void setBottomTabHost() {
        TabHost tabHost = findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab_IS").setIndicator("综合系统")
                .setContent(R.id.tab_IS));
        tabHost.addTab(tabHost.newTabSpec("tab_personal").setIndicator("个人中心")
                .setContent(R.id.tab_personal));
    }

    //抽屉显示
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_button, menu);
        return true;
    }

    //Menu方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(click==0){
            item.setIcon(R.drawable.ic_close);
            drawerLayout.openDrawer(Gravity.LEFT);
            click=1;
        }else{
            item.setIcon(R.drawable.ic_drawer);
            drawerLayout.closeDrawer(Gravity.LEFT);
            click=0;
        }
        return true;
    }
}
