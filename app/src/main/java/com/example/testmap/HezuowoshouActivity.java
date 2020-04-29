package com.example.testmap;

import android.os.Bundle;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HezuowoshouActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hezuowoshou);
        TextView textView = findViewById(R.id.hezuowoshou_text);
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://zhixiaogai.com/api/query_league_cooperation")
                .get()
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String string = response.body().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}