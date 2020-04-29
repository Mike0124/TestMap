package com.example.testmap;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.amap.api.maps.model.BitmapDescriptorFactory.getContext;

public class HezuowoshouActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hezuowoshou);
        TextView textView = findViewById(R.id.hezuowoshou_text);
        textView.setText(gethttpresult("http://zhixiaogai.com/api/query_league_cooperation"));
    }

    public static String gethttpresult(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");
            connect.setConnectTimeout(5000);
            connect.setReadTimeout(5000);
            connect.connect();
            if (connect.getResponseCode() == 200) {
                InputStream input = connect.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                String line = null;
                System.out.println(connect.getResponseCode());
                StringBuffer sb = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            } else {
                return "meiyou";
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return "meiyou222";
        }
    }


}