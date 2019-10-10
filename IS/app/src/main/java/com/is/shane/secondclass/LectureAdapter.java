package com.is.shane.secondclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.is.shane.R;
import com.is.shane.bean.Course;
import com.is.shane.bean.Lectures;

import java.util.List;

public class LectureAdapter extends BaseAdapter {
    private List<Lectures> Lectures_List;
    private LayoutInflater inflater;
    private Context context;
    public LectureAdapter(Context context, List<Lectures> all_lectures){
        this.context = context;
        Lectures_List = all_lectures;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Lectures_List.size();
    }

    @Override
    public Object getItem(int i) {
        return Lectures_List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view_show, ViewGroup viewGroup) {
        ViewHolder viewholder = null;
        if (view_show == null){
            viewholder = new ViewHolder();
            view_show = inflater.inflate(R.layout.second_class_item, null);
            viewholder.icon = view_show.findViewById(R.id.icon);
            viewholder.title = view_show.findViewById(R.id.title);
            viewholder.content = view_show.findViewById(R.id.content);
            view_show.setTag(viewholder);
        }else{
            viewholder = (ViewHolder) view_show.getTag();
        }
        viewholder.title.setText(Lectures_List.get(i).name);
        viewholder.content.setText(Lectures_List.get(i).classes);
        Glide.with(context).load(Lectures_List.get(i).imgimage).into(viewholder.icon);
        return view_show;
    }


    class ViewHolder{
        public TextView title, content;
        public ImageView icon;
    }
}
