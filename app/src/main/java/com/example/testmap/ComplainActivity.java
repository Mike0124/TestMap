package com.example.testmap;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hb.dialog.myDialog.MyAlertInputDialog;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ComplainActivity extends AppCompatActivity {

    private TextView textView;
    private Button button_xitongguzhang;
    private Button button_zhifu;
    private Button button_qita;
    private String info = null;
    private Drawable drawable_xitongguzhang;
    private Drawable drawable_zhifu;
    private Drawable drawable_qita;
    private MyAlertInputDialog myAlertInputDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        toolbar = findViewById(R.id.toolbar_inform);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView = findViewById(R.id.tousu_text);
        button_xitongguzhang = findViewById(R.id.tousu_xitongguzhang);
        button_zhifu = findViewById(R.id.tousu_zhifu);
        button_qita = findViewById(R.id.tousu_qita);
        drawable_xitongguzhang = getResources().getDrawable(R.mipmap.xitongguzhang);
        drawable_zhifu = getResources().getDrawable(R.mipmap.zhifu);
        drawable_qita = getResources().getDrawable(R.mipmap.qita);
        drawable_xitongguzhang.setBounds(0, 0, 160, 160);
        drawable_zhifu.setBounds(0, 0, 160, 160);
        drawable_qita.setBounds(0, 0, 160, 160);
        button_qita.setCompoundDrawables(drawable_qita, null, null, null);
        button_zhifu.setCompoundDrawables(drawable_zhifu, null, null, null);
        button_xitongguzhang.setCompoundDrawables(drawable_xitongguzhang, null, null, null);
        TextPaint textPaint = textView.getPaint();
        textPaint.setFakeBoldText(true);
        setListener();
    }

    private void setListener() {
        OnClick onClick = new OnClick();
        button_xitongguzhang.setOnClickListener(onClick);
        button_zhifu.setOnClickListener(onClick);
        button_qita.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tousu_xitongguzhang:
                    Thread thread_xitongguzhang = new Thread(new SubThread());
                    info = "设备故障";
                    thread_xitongguzhang.start();
                    Toast toast_xitongguzhang = Toast.makeText(ComplainActivity.this, "投诉提交成功！", Toast.LENGTH_SHORT);
                    toast_xitongguzhang.setGravity(Gravity.CENTER, 0, 0);
                    toast_xitongguzhang.show();
                    break;
                case R.id.tousu_zhifu:
                    Thread thread_zhifu = new Thread(new SubThread());
                    info = "支付故障";
                    thread_zhifu.start();
                    Toast toast_zhifu = Toast.makeText(ComplainActivity.this, "投诉提交成功！", Toast.LENGTH_SHORT);
                    toast_zhifu.setGravity(Gravity.CENTER, 0, 0);
                    toast_zhifu.show();
                    break;
                case R.id.tousu_qita:
                    myAlertInputDialog = new MyAlertInputDialog(ComplainActivity.this).builder()
                            .setTitle("请输入您的投诉信息")
                            .setEditText("");
                    myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null == myAlertInputDialog.getContentEditText()) {
                                Toast toast_error = Toast.makeText(ComplainActivity.this, "输入信息不能为空", Toast.LENGTH_SHORT);
                                toast_error.setGravity(Gravity.CENTER, 0, 0);
                                toast_error.show();
                                myAlertInputDialog.dismiss();
                            } else {
                                info = myAlertInputDialog.getContentEditText().toString();
                                Thread thread_qita = new Thread(new SubThread());
                                thread_qita.start();
                                Toast toast_qita = Toast.makeText(ComplainActivity.this, "投诉提交成功！", Toast.LENGTH_SHORT);
                                toast_qita.setGravity(Gravity.CENTER, 0, 0);
                                toast_qita.show();
                                myAlertInputDialog.dismiss();
                            }
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myAlertInputDialog.dismiss();
                        }
                    });
                    myAlertInputDialog.show();
                    break;
            }
        }
    }

    private class SubThread implements Runnable {

        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "openid=&complaints_content=" + info);
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
