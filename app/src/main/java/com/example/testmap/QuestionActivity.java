package com.example.testmap;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hb.dialog.myDialog.MyAlertDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.util.Objects.requireNonNull;

public class QuestionActivity extends AppCompatActivity {

    private List<Map<String, Object>> dataList;
    private String[] content = new String[6];
    private String[] answer = new String[6];
    private MyAlertDialog myAlertDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        toolbar = findViewById(R.id.toolbar_inform);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GridView gridView = findViewById(R.id.question);
        init_data();
        String[] from = {"text", "img"};
        int[] to = {R.id.text, R.id.img};
        SimpleAdapter adapter = new SimpleAdapter(QuestionActivity.this, dataList, R.layout.activity_question_grim, from, to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myAlertDialog = new MyAlertDialog(QuestionActivity.this).builder()
                        .setTitle("官方回答")
                        .setMsg(answer[position])
                        .setCancelable(true);
                myAlertDialog.show();
            }
        });
    }

    void init_data() {
        Thread subThread = new Thread(new SubThread());
        subThread.start();
        try {
            subThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dataList = new ArrayList<>();
        int[] icon = {R.mipmap.question, R.mipmap.question, R.mipmap.question, R.mipmap.question, R.mipmap.question, R.mipmap.question};
        for (
                int i = 0;
                i < icon.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("text", content[i]);
            map.put("img", icon[i]);
            dataList.add(map);
        }
    }

    private class SubThread implements Runnable {
        public void run() {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://zhixiaogai.com/api/query_common_quesrtion")
                    .get()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    String string = requireNonNull(response.body()).string();
                    new JSONObject();
                    JSONObject jsonObject_question;
                    jsonObject_question = JSONObject.parseObject(string);
                    JSONArray recordMap;
                    recordMap = requireNonNull(jsonObject_question.getJSONArray("rows"));
                    requireNonNull(recordMap);
                    for (int i = 0; i < 6; i++) {
                        JSONObject jsonOb = recordMap.getJSONObject(i);
                        requireNonNull(jsonOb);
                        content[i] = jsonOb.getString("title");
                        answer[i] = jsonOb.getString("content");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}