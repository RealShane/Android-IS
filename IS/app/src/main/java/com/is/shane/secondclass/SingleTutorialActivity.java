package com.is.shane.secondclass;


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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.is.shane.DrawerTabFragment;
import com.is.shane.R;
import com.is.shane.SecondClassActivity;
import com.is.shane.bean.Classes;
import com.is.shane.repairorder.RepairOrderActivity;

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

public class SingleTutorialActivity extends DrawerTabFragment {

    private AlertDialog alertDialog;
    private static final int GET = 1;
    //报名返回值
    private String sign_back;
    //姓名学号变量
    private String name_get, myid_get;
    //Intent传值课程id
    private String tutorial_id;
    //课程选择变量
    private int classes_chose;
    //若取消则无值
    private int temp;
    //显示名字
    private String temp_name;
    //显示选择的班级
    private TextView show_classes;
    //姓名获取
    private EditText name;
    //学号
    private EditText myid;
    private Button button_post;
    //班级选项定位
    private int classes_position = -1;

    private List<Classes> Classes_List = new ArrayList<>();
    private String urlString;
    OkHttpClient client = new OkHttpClient();
    //id传值
    private String Tutorial_chose;
    //报名handler
    private Handler handler_sign = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET:
                    handle_sign_res(msg.obj.toString());
                    break;
            }
        }
    };
    //获取信息handler
    private Handler handler = new Handler(){
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
        setContentView(R.layout.activity_second_class_single_tutorial);
        //找到抽屉
        drawerLayout = findViewById(R.id.draw);
        //Intent传值课程id获取
        Tutorial_chose = getIntent().getStringExtra("id");
        //Intent传值课程名获取
        tutorial_id = getIntent().getStringExtra("tutorial_id");
        urlString = "https://serv.huihuagongxue.top/IS/public/Android_Tutorial_Info?id="+Tutorial_chose;
        get_article(urlString);

        //姓名输入框找到
        name = findViewById(R.id.name);
        //学号输入框找到
        myid = findViewById(R.id.myid);
        //学号只能输入数字
        myid.setInputType(InputType.TYPE_CLASS_NUMBER);

        button_post = findViewById(R.id.post);
        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name_get = name.getText().toString();
                myid_get = myid.getText().toString();
                String str = "https://serv.huihuagongxue.top/IS/public/tutorial_sign?name="+name_get+"&select="+classes_chose+"&pro="+tutorial_id+"&id="+myid_get;
                sign(str);
            }
        });
    }

    //处理返回结果
    private String handle_sign_res(String res){
        if(res.equals("0")){
            Intent intent = new Intent();
            intent.setClass(SingleTutorialActivity.this, SignSucceedActivity.class);
            startActivity(intent);
            SingleTutorialActivity.this.finish();
        }else if (res.equals("1")){
            Toast toast = Toast.makeText(SingleTutorialActivity.this,"网络原因报名失败！",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else if (res.equals("2")){
            Toast toast = Toast.makeText(SingleTutorialActivity.this,"请勿重复报名！",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else if (res.equals("3")){
            Toast toast = Toast.makeText(SingleTutorialActivity.this,"课程已报满！",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else if (res.equals("4")){
            Toast toast = Toast.makeText(SingleTutorialActivity.this,"填写有空！",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        return res;
    }

    //开启子线程
    private String sign(final String url){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    String res = run_sign(url);
                    sign_back = res;
                    Message msg = Message.obtain();
                    msg.what = GET;
                    msg.obj = res;
                    handler_sign.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return url;
    }

    //报名请求
    private String run_sign(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    //处理返回信息
    private void handle_res(String res) throws JSONException {
        TextView title_view = findViewById(R.id.Tutorial_title);
        TextView content_view = findViewById(R.id.Tutorial_content);
        TextView time_view = findViewById(R.id.Tutorial_time);
        JSONObject jsonObject = new JSONObject(res);
        String title = (String) jsonObject.get("title");
        String content = (String) jsonObject.get("txtcontent");
        String time = (String) jsonObject.get("time");
        title_view.setText(title);
        content_view.setText(content);
        time_view.setText(time);
    }

    //开启子线程
    private String get_article(final String url){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    String res = get_info(url);
                    Message msg = Message.obtain();
                    msg.what = GET;
                    msg.obj = res;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return url;
    }

    //数据请求
    private String get_info(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
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
                show_classes = findViewById(R.id.show_classes);
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
        Intent intent = new Intent(SingleTutorialActivity.this, SecondClassActivity.class);
        SingleTutorialActivity.this.finish();
        startActivity(intent);
    }
    //跳转方法重写
    public void  Repair_Order_Link(View view){
        Intent intent = new Intent(SingleTutorialActivity.this, RepairOrderActivity.class);
        SingleTutorialActivity.this.finish();
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
            SingleTutorialActivity.this.finish();
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
