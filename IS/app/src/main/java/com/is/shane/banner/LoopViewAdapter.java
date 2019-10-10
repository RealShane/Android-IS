package com.is.shane.banner;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class LoopViewAdapter extends PagerAdapter {

    private ArrayList<ImageView> imageViewList;

    public LoopViewAdapter(ArrayList<ImageView> ImgList){
        imageViewList = ImgList;
    }

    //返回要显示的条目内容, 创建条目
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        int newPosition = position % imageViewList.size();
        ImageView img = imageViewList.get(newPosition);

        container.addView(img);
        return img;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        //返回最大值->无限循环
        return Integer.MAX_VALUE;
    }

    //判断是否使用缓存, 如果返回的是true, 使用缓存. 不去调用instantiateItem方法创建一个新的对象

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj ;
    }
}
