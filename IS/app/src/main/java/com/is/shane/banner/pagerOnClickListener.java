package com.is.shane.banner;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.is.shane.R;
import com.is.shane.SecondClassActivity;
import com.is.shane.UndoneActivity;

public class pagerOnClickListener implements View.OnClickListener {

    private Context Context;
    public pagerOnClickListener(Context Context){
        this.Context=Context;
    }

    //外部监听器
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.second_class:
                intent.setClass(Context, SecondClassActivity.class);
                break;
            case R.id.origin:
                intent.setClass(Context, UndoneActivity.class);
                break;
            case R.id.post_office:
                intent.setClass(Context, UndoneActivity.class);
                break;
        }
        //truly this is what i want!
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        Context.startActivity(intent);
    }
}
