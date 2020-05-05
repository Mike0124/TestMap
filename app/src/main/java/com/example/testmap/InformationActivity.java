package com.example.testmap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InformationActivity extends AppCompatActivity {

    private TextView mbtnusertext;
    private Button mbtnxiaofei;
    private Button mbtnfapiao;
    private Button mbtntousu;
    private Button mbtnwendang;
    private Button mbtnhezuowoshou;
    private Drawable drawable_right;
    private Drawable drawable_user;
    private Drawable drawable_xiaofei;
    private Drawable drawable_fapiao;
    private Drawable drawable_tousu;
    private Drawable drawable_wendang;
    private Drawable drawable_hezuowoshou;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        toolbar = findViewById(R.id.toolbar_inform);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mbtnxiaofei = findViewById(R.id.xiaofei);
        mbtnfapiao = findViewById(R.id.fapiao);
        mbtntousu = findViewById(R.id.tousu);
        mbtnwendang = findViewById(R.id.wendang);
        mbtnhezuowoshou = findViewById(R.id.hezuowoshou);
        mbtnusertext = findViewById(R.id.usertext);
        drawable_right = getResources().getDrawable(R.mipmap.right);
        drawable_user = getResources().getDrawable(R.mipmap.user_not_login);
        drawable_xiaofei = getResources().getDrawable(R.mipmap.xiaofei);
        drawable_fapiao = getResources().getDrawable(R.mipmap.fapiao);
        drawable_tousu = getResources().getDrawable(R.mipmap.tousu);
        drawable_wendang = getResources().getDrawable(R.mipmap.wendang);
        drawable_hezuowoshou = getResources().getDrawable(R.mipmap.hezuowoshou);
        drawable_right.setBounds(0, 0, 100, 100);
        drawable_user.setBounds(0, 0, 250, 250);
        drawable_xiaofei.setBounds(0, 0, 100, 100);
        drawable_fapiao.setBounds(0, 0, 100, 100);
        drawable_tousu.setBounds(0, 0, 100, 100);
        drawable_wendang.setBounds(0, 0, 100, 100);
        drawable_hezuowoshou.setBounds(0, 0, 100, 100);
        mbtnusertext.setCompoundDrawables(null, null, drawable_user, null);
        mbtnxiaofei.setCompoundDrawables(drawable_xiaofei, null, drawable_right, null);
        mbtnfapiao.setCompoundDrawables(drawable_fapiao, null, drawable_right, null);
        mbtntousu.setCompoundDrawables(drawable_tousu, null, drawable_right, null);
        mbtnwendang.setCompoundDrawables(drawable_wendang, null, drawable_right, null);
        mbtnhezuowoshou.setCompoundDrawables(drawable_hezuowoshou, null, drawable_right, null);
        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mbtnxiaofei.setOnClickListener(onClick);
        mbtnfapiao.setOnClickListener(onClick);
        mbtntousu.setOnClickListener(onClick);
        mbtnwendang.setOnClickListener(onClick);
        mbtnhezuowoshou.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.xiaofei:
                    intent = new Intent(InformationActivity.this, ConsumeActivity.class);
                    break;
                case R.id.fapiao:
                    intent = new Intent(InformationActivity.this, InvoiceActivity.class);
                    break;
                case R.id.tousu:
                    intent = new Intent(InformationActivity.this, ComplainActivity.class);
                    break;
                case R.id.wendang:
                    intent = new Intent(InformationActivity.this, QuestionActivity.class);
                    break;
                case R.id.hezuowoshou:
                    intent = new Intent(InformationActivity.this, JoinActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}
