package com.example.testmap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Informationactivity extends AppCompatActivity {

    private TextView mbtnusertext;
    private Button mbtnxiaofei;
    private Button mbtnfapiao;
    private Button mbtntousu;
    private Button mbtnamend;
    private Button mbtnwendang;
    private Button mbtnhezuowoshou;
    private Drawable drawable_right;
    private Drawable drawable_user;
    private Drawable drawable_xiaofei;
    private Drawable drawable_fapiao;
    private Drawable drawable_tousu;
    private Drawable drawable_amend;
    private Drawable drawable_wendang;
    private Drawable drawable_hezuowoshou;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        mbtnxiaofei = findViewById(R.id.xiaofei);
        mbtnfapiao = findViewById(R.id.fapiao);
        mbtntousu = findViewById(R.id.tousu);
        mbtnamend = findViewById(R.id.amend);
        mbtnwendang = findViewById(R.id.wendang);
        mbtnhezuowoshou = findViewById(R.id.hezuowoshou);
        mbtnusertext = findViewById(R.id.usertext);
        drawable_right = getResources().getDrawable(R.mipmap.right);
        drawable_user = getResources().getDrawable(R.mipmap.user_not_login);
        drawable_xiaofei = getResources().getDrawable(R.mipmap.xiaofei);
        drawable_fapiao = getResources().getDrawable(R.mipmap.fapiao);
        drawable_tousu = getResources().getDrawable(R.mipmap.tousu);
        drawable_amend = getResources().getDrawable(R.mipmap.amend);
        drawable_wendang = getResources().getDrawable(R.mipmap.wendang);
        drawable_hezuowoshou = getResources().getDrawable(R.mipmap.hezuowoshou);
        drawable_right.setBounds(0,0,100,100);
        drawable_user.setBounds(0,0,250,250);
        drawable_xiaofei.setBounds(0,0,100,100);
        drawable_fapiao.setBounds(0,0,100,100);
        drawable_tousu.setBounds(0,0,100,100);
        drawable_amend.setBounds(0,0,100,100);
        drawable_wendang.setBounds(0,0,100,100);
        drawable_hezuowoshou.setBounds(0,0,100,100);
        mbtnusertext.setCompoundDrawables(null, null, drawable_user,null);
        mbtnxiaofei.setCompoundDrawables(drawable_xiaofei,null,drawable_right,null);
        mbtnfapiao.setCompoundDrawables(drawable_fapiao,null,drawable_right,null);
        mbtntousu.setCompoundDrawables(drawable_tousu,null,drawable_right,null);
        mbtnamend.setCompoundDrawables(drawable_amend,null,drawable_right,null);
        mbtnwendang.setCompoundDrawables(drawable_wendang,null,drawable_right,null);
        mbtnhezuowoshou.setCompoundDrawables(drawable_hezuowoshou,null,drawable_right,null);
        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        mbtnxiaofei.setOnClickListener(onClick);
        mbtnfapiao.setOnClickListener(onClick);
        mbtntousu.setOnClickListener(onClick);
        mbtnamend.setOnClickListener(onClick);
        mbtnwendang.setOnClickListener(onClick);
        mbtnhezuowoshou.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.xiaofei:
                    intent = new Intent(Informationactivity.this, XiaofeiActivity.class);
                    break;
                case R.id.fapiao:
                    intent = new Intent(Informationactivity.this, FapiaoActivity.class);
                    break;
                case R.id.tousu:
                    intent = new Intent(Informationactivity.this, TousuActivity.class);
                    break;
                case R.id.amend:
                    intent = new Intent(Informationactivity.this, AmendActivity.class);
                    break;
                case R.id.wendang:
                    intent = new Intent(Informationactivity.this, WendangActivity.class);
                    break;
                case R.id.hezuowoshou:
                    intent = new Intent(Informationactivity.this, HezuowoshouActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}
