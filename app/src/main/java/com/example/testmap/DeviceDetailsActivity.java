package com.example.testmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DeviceDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deivce_details);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String deviceName = intent.getStringExtra("deviceName");
        String exactPosition = intent.getStringExtra("exactPosition");
        TextView detailsText = findViewById(R.id.details1);
        detailsText.setText("欢迎"+userName+"来到");
        detailsText = findViewById(R.id.details2);
        detailsText.setText(deviceName);
        detailsText = findViewById(R.id.details3);
        detailsText.setText(exactPosition+"\n该地点设备数量：1台");
    }
}
