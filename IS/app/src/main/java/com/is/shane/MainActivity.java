package com.is.shane;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.is.shane.banner.LoopViewAdapter;
import com.is.shane.banner.pagerOnClickListener;
import com.is.shane.repairorder.RepairOrderActivity;

import java.util.ArrayList;

public class MainActivity extends DrawerTabFragment {

    //viewpager实现banner
    private ViewPager viewPager;
    private int[] Img;
    private int[] Img_id;
    private String[] Dec;
    private ArrayList<ImageView> ImgList;
    private LinearLayout ll_dots_container;
    private TextView loop_dec;
    private int previousSelectedPosition = 0;
    boolean isRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //找到抽屉
        drawerLayout = findViewById(R.id.draw);
        //调用底部菜单显示
        setBottomTabHost();
        //banner开启
        initLoopView();
    }

    private void initLoopView() {
        viewPager = findViewById(R.id.loopviewpager);
        ll_dots_container = findViewById(R.id.ll_dots_loop);
        loop_dec = findViewById(R.id.loop_dec);

        // 图片地址
        Img = new int[]{
                R.mipmap.slide01,
                R.mipmap.slide02,
                R.mipmap.slide03
        };

        //标题
        Dec = new String[]{
                "第二课堂",
                "综合测评",
                "时光邮箱"
        };

        Img_id = new int[]{
                R.id.second_class,
                R.id.origin,
                R.id.post_office
        };

        ImgList = new ArrayList<ImageView>();
        ImageView imageView;
        View dotView;
        LinearLayout.LayoutParams layoutParams;
        for(int i=0;i<Img.length;i++){
            //初始化要显示的图片对象
            imageView = new ImageView(this);
            imageView.setBackgroundResource(Img[i]);
            imageView.setId(Img_id[i]);
            imageView.setOnClickListener(new pagerOnClickListener(getApplicationContext()));
            ImgList.add(imageView);
            //加引导点
            dotView = new View(this);
            dotView.setBackgroundResource(R.drawable.dot);
            layoutParams = new LinearLayout.LayoutParams(10,10);
            if(i!=0){
                layoutParams.leftMargin=10;
            }

            dotView.setEnabled(false);
            ll_dots_container.addView(dotView,layoutParams);
        }

        ll_dots_container.getChildAt(0).setEnabled(true);
        loop_dec.setText(Dec[0]);
        previousSelectedPosition=0;
        //设置适配器
        viewPager.setAdapter(new LoopViewAdapter(ImgList));
        //m最大值->循环
        int m = (Integer.MAX_VALUE / 2) %ImgList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        viewPager.setCurrentItem(currentPosition);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int newPosition = i % ImgList.size();
                loop_dec.setText(Dec[newPosition]);
                ll_dots_container.getChildAt(previousSelectedPosition).setEnabled(false);
                ll_dots_container.getChildAt(newPosition).setEnabled(true);
                previousSelectedPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        // 开启子线程
        new Thread(){
            public void run(){
                isRunning = true;
                while(isRunning){
                    try{
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //下一条
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        }
                    });
                }
            }
        }.start();

    }


    //跳转方法
    public void  Undone(View view){
        Intent intent = new Intent(MainActivity.this, UndoneActivity.class);
        startActivity(intent);
    }

    //跳转方法
    public void  Second_Class_Link(View view){
        Intent intent = new Intent(MainActivity.this, SecondClassActivity.class);
        startActivity(intent);
    }

    //跳转方法
    public void  Repair_Order_Link(View view){
        Intent intent = new Intent(MainActivity.this, RepairOrderActivity.class);
        startActivity(intent);
    }
}
