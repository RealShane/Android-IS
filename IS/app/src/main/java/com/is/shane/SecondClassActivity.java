package com.is.shane;


import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

import androidx.fragment.app.Fragment;
import com.is.shane.secondclass.CourseFragment;
import com.is.shane.secondclass.LecturesFragment;


public class SecondClassActivity extends DrawerTabFragment {
    private Fragment Course_Fragment;
    private Fragment Lectures_Fragment;
    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_class_main);
        //将CourseFragment引入
        Course_Fragment = new CourseFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.Course_Frag, Course_Fragment).commitAllowingStateLoss();
        //找到抽屉
        drawerLayout = findViewById(R.id.draw);
        //调用菜单显示
        tabHost = findViewById(R.id.tabs);
        tabHost.setup();
        setTabHost();
        //Tab变动监听
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                switch (s){
                    case "tab_NewCourse":
                        Course_Fragment = new CourseFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.Course_Frag, Course_Fragment).commitAllowingStateLoss();
                        break;
                    case "tab_Lecturers":
                        Lectures_Fragment = new LecturesFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.Lecturers_Frag, Lectures_Fragment).commitAllowingStateLoss();
                        break;
                    case "tab_Tutorial":

                        break;
                    case "tab_Search":

                        break;
                }
            }
        });
    }

    //底部tab菜单
    private void setTabHost(){
        tabHost.addTab(tabHost.newTabSpec("tab_NewCourse").setIndicator("新课程")
                .setContent(R.id.tab_NewCourse));
        tabHost.addTab(tabHost.newTabSpec("tab_Lecturers").setIndicator("讲师列表")
                .setContent(R.id.tab_Lecturers));
        tabHost.addTab(tabHost.newTabSpec("tab_Tutorial").setIndicator("微课堂报名")
                .setContent(R.id.tab_Tutorial));
        tabHost.addTab(tabHost.newTabSpec("tab_Search").setIndicator("查询报名")
                .setContent(R.id.tab_Search));
    }

    //抽屉显示
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_button, menu);
        return true;
    }

    //Menu方法重写
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getTitle().equals("back_button")){
            //结束栈
            SecondClassActivity.this.finish();
        }else if(item.getTitle().equals("list")) {
            if(click==0){
                item.setIcon(R.drawable.ic_close);
                drawerLayout.openDrawer(Gravity.LEFT);
                click=1;
            }else{
                item.setIcon(R.drawable.ic_drawer);
                drawerLayout.closeDrawer(Gravity.LEFT);
                click=0;
            }
        }
        return true;
    }
}
