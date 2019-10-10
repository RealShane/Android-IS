package com.is.shane.secondclass;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.is.shane.R;
import com.is.shane.bean.Lectures;

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

public class LecturesFragment extends Fragment {

    private List<Lectures> Lecture_List = new ArrayList<>();
    private ListView lecture_item;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_class_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lecture_item = view.findViewById(R.id.list_item);
        new Lectures_Asnyc().execute("https://serv.huihuagongxue.top/IS/public/Android_Lectures");
    }

    //url请求回的数据转化成封装好的bean
    private List<Lectures> get_Lectures_item(String url){
        try {
            String jsonString = readStream(new URL(url).openStream());
            System.out.println(jsonString);
            JSONObject jsonObject;
            Lectures lectures;
            try {
                jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("all_lectures");
                for (int i = 0; i < jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    lectures = new Lectures();
                    lectures.imgimage = jsonObject.getString("imgimage");
                    lectures.name = jsonObject.getString("name");
                    lectures.classes = jsonObject.getString("classes");
                    Lecture_List.add(lectures);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Lecture_List;
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
    class Lectures_Asnyc extends AsyncTask<String, Void, List<Lectures>> {

        @Override
        protected List<Lectures> doInBackground(String... strings) {
            return get_Lectures_item(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Lectures> lectures) {
            super.onPostExecute(lectures);
            LectureAdapter adapter = new LectureAdapter(getActivity(),lectures);
            lecture_item.setAdapter(adapter);
        }
    }
}
