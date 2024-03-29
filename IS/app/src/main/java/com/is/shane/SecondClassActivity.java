package com.is.shane;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.is.shane.bean.Classes;
import com.is.shane.repairorder.RepairOrderActivity;
import com.is.shane.secondclass.CourseFragment;
import com.is.shane.secondclass.LecturesFragment;
import com.is.shane.secondclass.TutorialFragment;

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
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SecondClassActivity extends DrawerTabFragment {
    private Fragment Course_Fragment;
    private Fragment Lectures_Fragment;
    private Fragment Tutorial_Fragment;
    private TabHost tabHost;

    private static final int GET = 1;
    OkHttpClient client = new OkHttpClient();
    private AlertDialog alertDialog;
    private AlertDialog alertInfo;
    //若取消则无值
    private int temp;
    //显示名字
    private String temp_name;
    //课程选择变量
    private int classes_chose;
    //显示选择的班级
    private TextView show_classes;
    //学号
    private EditText myid;
    private Button button_post;
    //学号变量
    private String myid_get;
    //班级选项定位
    private int classes_position = -1;

    private List<Classes> Classes_List = new ArrayList<>();

    //报名handler
    private Handler handler_search = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET:
                    try {
                        handle_res(msg.obj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

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
                        Tutorial_Fragment = new TutorialFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.Tutorial_Frag, Tutorial_Fragment).commitAllowingStateLoss();
                        break;
                }
            }
        });
        //学号输入框找到
        myid = findViewById(R.id.myid);
        //学号只能输入数字
        myid.setInputType(InputType.TYPE_CLASS_NUMBER);

        button_post = findViewById(R.id.post);
        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myid_get = myid.getText().toString();
                String str = "https://serv.huihuagongxue.top/IS/public/check?studentid="+myid_get+"&classes="+classes_chose;
                System.out.println(str);
                search(str);
            }
        });
    }

    //底部tab菜单
    private void setTabHost(){
        tabHost.addTab(tabHost.newTabSpec("tab_NewCourse").setIndicator("新课程")
                .setContent(R.id.tab_NewCourse));
        tabHost.addTab(tabHost.newTabSpec("tab_Lecturers").setIndicator("讲师列表")
                .setContent(R.id.tab_Lecturers));
        tabHost.addTab(tabHost.newTabSpec("tab_Tutorial").setIndicator("微课报名")
                .setContent(R.id.tab_Tutorial));
        tabHost.addTab(tabHost.newTabSpec("tab_Search").setIndicator("查询报名")
                .setContent(R.id.tab_Search));
    }



    //开启子线程
    private String search(final String url){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    String res = run_search(url);
                    Message msg = Message.obtain();
                    msg.what = GET;
                    msg.obj = res;
                    handler_search.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return url;
    }

    //查询请求
    private String run_search(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    //处理返回信息
    private void handle_res(String res) throws JSONException {
        JSONObject jsonObject = new JSONObject(res);
        String classes = (String) jsonObject.get("class");
        String course = (String) jsonObject.get("course");

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("报名情况");
        alertBuilder.setMessage("班级："+classes+"\n"+"选择的课程："+course);
        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertInfo.dismiss();
            }
        });
        alertInfo = alertBuilder.create();
        alertInfo.show();
    }



    //班级选择
    public void showSingleAlertDialog(View view) throws ExecutionException, InterruptedException {

        final AsyncTask<String, Void, List<Classes>> items = new Classes_Asnyc().execute("https://serv.huihuagongxue.top/IS/public/Android_Tutorial_Info_Classes");
        final String[] item_classes = new String[items.get().size()];
        for(int i = 0; i<items.get().size(); i++){
            item_classes[i] = items.get().get(i).name;
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择班级");
        alertBuilder.setSingleChoiceItems(item_classes, classes_position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    temp = items.get().get(i).id;
                    temp_name = items.get().get(i).name;
                    classes_position = i;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
                classes_chose = temp;
                show_classes = findViewById(R.id.search_show_classes);
                show_classes.setText(temp_name);
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    //url请求回的数据转化成封装好的bean
    private List<Classes> get_Classes_item(String url){
        try {
            String jsonString = readStream(new URL(url).openStream());
            JSONObject jsonObject;
            Classes classes;
            try {
                jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("article_info_classes");
                for (int i = 0; i < jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    classes = new Classes();
                    classes.name = jsonObject.getString("name");
                    classes.id = jsonObject.getInt("id");
                    Classes_List.add(classes);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Classes_List;
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
    class Classes_Asnyc extends AsyncTask<String, Void, List<Classes>> {

        @Override
        protected List<Classes> doInBackground(String... strings) {
            return get_Classes_item(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Classes> classes) {
            super.onPostExecute(classes);
        }
    }

    //跳转方法重写
    public void  Second_Class_Link(View view){
        Intent intent = new Intent(SecondClassActivity.this, SecondClassActivity.class);
        SecondClassActivity.this.finish();
        startActivity(intent);
    }
    //跳转方法重写
    public void  Repair_Order_Link(View view){
        Intent intent = new Intent(SecondClassActivity.this, RepairOrderActivity.class);
        SecondClassActivity.this.finish();
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
