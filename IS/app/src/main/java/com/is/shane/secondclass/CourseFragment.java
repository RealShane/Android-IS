package com.is.shane.secondclass;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.is.shane.R;
import com.is.shane.bean.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends Fragment {

    private List<Course> Course_List = new ArrayList<>();
    private ListView course_item;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_class_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        course_item = view.findViewById(R.id.list_item);
        new Course_Asnyc().execute("https://serv.huihuagongxue.top/IS/public/Android_Course");
        //为每个item添加点击事件
        course_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                if (Course_List.get(i).name.equals("PHP")){
                    intent.putExtra("id",Course_List.get(i).name);
                    intent.putExtra("course_id",Integer.toString(Course_List.get(i).id));
                    intent.setClass(getActivity(),SingleCourseActivity.class);
                    startActivity(intent);
                }else if (Course_List.get(i).name.equals("微信小程序")){
                    intent.putExtra("id",Course_List.get(i).name);
                    intent.putExtra("course_id",Integer.toString(Course_List.get(i).id));
                    intent.setClass(getActivity(),SingleCourseActivity.class);
                    startActivity(intent);
                }else if (Course_List.get(i).name.equals("PR视频剪辑")){
                    intent.putExtra("id",Course_List.get(i).name);
                    intent.putExtra("course_id",Integer.toString(Course_List.get(i).id));
                    intent.setClass(getActivity(),SingleCourseActivity.class);
                    startActivity(intent);
                }else if (Course_List.get(i).name.equals("HTML基础")){
                    intent.putExtra("id",Course_List.get(i).name);
                    intent.putExtra("course_id",Integer.toString(Course_List.get(i).id));
                    intent.setClass(getActivity(),SingleCourseActivity.class);
                    startActivity(intent);
                }else if (Course_List.get(i).name.equals("平面设计（一）")){
                    intent.putExtra("id",Course_List.get(i).name);
                    intent.putExtra("course_id",Integer.toString(Course_List.get(i).id));
                    intent.setClass(getActivity(),SingleCourseActivity.class);
                    startActivity(intent);
                }else if (Course_List.get(i).name.equals("平面设计（二）")){
                    intent.putExtra("id",Course_List.get(i).name);
                    intent.putExtra("course_id",Integer.toString(Course_List.get(i).id));
                    intent.setClass(getActivity(),SingleCourseActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    //url请求回的数据转化成封装好的bean
    private List<Course> get_Course_item(String url){
        try {
            String jsonString = readStream(new URL(url).openStream());
            JSONObject jsonObject;
            Course course;
            try {
                jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("all_course");
                for (int i = 0; i < jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    course = new Course();
                    course.imgimage = jsonObject.getString("imgimage");
                    course.name = jsonObject.getString("name");
                    course.description = jsonObject.getString("description");
                    course.id = jsonObject.getInt("id");
                    Course_List.add(course);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Course_List;
    }
    //解析数据
    private String readStream(InputStream is) {

        InputStreamReader isr;
        String result = "";

        try {
            String line = "";
            //字节转字符
            isr = new InputStreamReader(is, "utf-8");
            //将字符以buffer读出来
            BufferedReader br= new BufferedReader(isr);
            while ((line = br.readLine()) !=null){
                result+=line;
            }
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
    //网络异步访问
    class Course_Asnyc extends AsyncTask<String, Void, List<Course>> {

        @Override
        protected List<Course> doInBackground(String... strings) {
            return get_Course_item(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Course> courses) {
            super.onPostExecute(courses);
            CourseAdapter adapter = new CourseAdapter(getActivity(),courses);
            course_item.setAdapter(adapter);
        }
    }
}
