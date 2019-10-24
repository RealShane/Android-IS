package com.is.shane.repairorder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.is.shane.DrawerTabFragment;
import com.is.shane.R;
import com.is.shane.SecondClassActivity;

public class RepairOrderActivity extends DrawerTabFragment {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_order_main);
        //找到抽屉
        drawerLayout = findViewById(R.id.draw);
    }


    //跳转方法重写
    public void  Second_Class_Link(View view){
        Intent intent = new Intent(RepairOrderActivity.this, SecondClassActivity.class);
        RepairOrderActivity.this.finish();
        startActivity(intent);
    }
    //跳转方法重写
    public void  Repair_Order_Link(View view){
        Intent intent = new Intent(RepairOrderActivity.this, RepairOrderActivity.class);
        RepairOrderActivity.this.finish();
        startActivity(intent);
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
            RepairOrderActivity.this.finish();
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
