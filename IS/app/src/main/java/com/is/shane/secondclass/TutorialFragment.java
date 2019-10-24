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
import com.is.shane.bean.Tutorial;

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

public class TutorialFragment extends Fragment {

    private List<Tutorial> Tutorial_List = new ArrayList<>();
    private ListView tutorial_item;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_class_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tutorial_item = view.findViewById(R.id.list_item);
        new Tutorial_Asnyc().execute("https://serv.huihuagongxue.top/IS/public/Android_Tutorial_List");
        tutorial_item = view.findViewById(R.id.list_item);
        //为每个item添加点击事件
        tutorial_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("id",Tutorial_List.get(i).id);
                intent.putExtra("tutorial_id",Tutorial_List.get(i).title);
                intent.setClass(getActivity(),SingleTutorialActivity.class);
                startActivity(intent);
            }
        });
    }

    //url请求回的数据转化成封装好的bean
    private List<Tutorial> get_Tutorial_item(String url){
        try {
            String jsonString = readStream(new URL(url).openStream());
            System.out.println(jsonString);
            JSONObject jsonObject;
            Tutorial tutorial;
            try {
                jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("all_tutorial");
                for (int i = 0; i < jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    tutorial = new Tutorial();
                    tutorial.id = jsonObject.getString("id");
                    tutorial.title = jsonObject.getString("title");
                    tutorial.txtcontent = jsonObject.getString("txtcontent");
                    tutorial.time = jsonObject.getString("time");
                    Tutorial_List.add(tutorial);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Tutorial_List;
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
    class Tutorial_Asnyc extends AsyncTask<String, Void, List<Tutorial>> {

        @Override
        protected List<Tutorial> doInBackground(String... strings) {
            return get_Tutorial_item(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Tutorial> tutorials) {
            super.onPostExecute(tutorials);
            TutorialAdapter adapter = new TutorialAdapter(getActivity(),tutorials);
            tutorial_item.setAdapter(adapter);
        }
    }
}
