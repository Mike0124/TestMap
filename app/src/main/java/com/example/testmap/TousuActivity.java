package com.example.testmap;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TousuActivity extends AppCompatActivity {

    private TextView textView;
    private Button button_xitongguzhang;
    private Button button_zhifu;
    private Button button_qita;
    private Button button_cancel;
    private Button button_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tousu);
        textView = findViewById(R.id.tousu_text);
        button_xitongguzhang = findViewById(R.id.tousu_xitongguzhang);
        button_zhifu = findViewById(R.id.tousu_zhifu);
        button_qita = findViewById(R.id.tousu_qita);
        button_commit = findViewById(R.id.button_commit);
        button_cancel = findViewById(R.id.button_cancel);
        TextPaint textPaint = textView.getPaint();
        textPaint.setFakeBoldText(true);
        setListener();
    }

    private void setListener() {
        OnClick onClick = new OnClick();
        button_xitongguzhang.setOnClickListener(onClick);
        button_zhifu.setOnClickListener(onClick);
        button_qita.setOnClickListener(onClick);
        button_cancel.setOnClickListener(onClick);
        button_commit.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tousu_xitongguzhang:
                    Thread thread_xitongguzhang = new Thread(new SubThread_xitongguzhang());
                    thread_xitongguzhang.start();
                    Toast toast_xitongguzhang = Toast.makeText(TousuActivity.this, "投诉提交成功！", Toast.LENGTH_SHORT);
                    toast_xitongguzhang.setGravity(Gravity.CENTER, 0, 0);
                    toast_xitongguzhang.show();
                    break;
                case R.id.tousu_zhifu:
                    Thread thread_zhifu = new Thread(new SubThread_zhifu());
                    thread_zhifu.start();
                    Toast toast_zhifu = Toast.makeText(TousuActivity.this, "投诉提交成功！", Toast.LENGTH_SHORT);
                    toast_zhifu.setGravity(Gravity.CENTER, 0, 0);
                    toast_zhifu.show();
                    break;
                case R.id.tousu_qita:
                    Intent intent = new Intent(TousuActivity.this, TousuActivity_edit.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    private class SubThread_xitongguzhang implements Runnable {

        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "openid=&complaints_content=设备故障");
            Request request = new Request.Builder()
                    .url("https://zhixiaogai.com/api/submit_complaints")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            try {
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private class SubThread_zhifu implements Runnable {

        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "openid=&complaints_content=支付故障");
            Request request = new Request.Builder()
                    .url("https://zhixiaogai.com/api/submit_complaints")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            try {
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
