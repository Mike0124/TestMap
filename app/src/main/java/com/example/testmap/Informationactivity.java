package com.example.testmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Informationactivity extends AppCompatActivity {

    private Button mbtnusertext;
    private Button mbtnxiaofei;
    private Button mbtnfapiao;
    private Button mbtntousu;
    private Button mbtnamend;
    private Button mbtnwendang;
    private Button mbtnhezuowoshou;

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
