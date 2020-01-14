package com.example.testmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.testmap.ui.login.LoginActivity;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class MainActivity extends AppCompatActivity{

    private Button mscanbutton;
    private ImageButton mloginbutton;
    MapView mapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//设置对应的XML布局文件
        mapView = (MapView)findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        AMap aMap;
            aMap = mapView.getMap();
        //设置地图的放缩级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        MyLocationStyle myLocationStyle;
//初始化定位蓝点样式类
        myLocationStyle = new MyLocationStyle();
//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
//设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
//设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
//设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        mloginbutton =  findViewById(R.id.Login);
        mloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        ZXingLibrary.initDisplayOpinion(this);
        mscanbutton = findViewById(R.id.scan_button);
        mscanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(Build.VERSION.SDK_INT > 22) {        //判断手机版本是否在6.0以上，如在以上则需要动态申请权限
                if(ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 1);
                }
                else{
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent,1);
                }
            }
            else {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent,1);
            }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    //获取到扫描的结果
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.d("res:",result);
                }
            }
        }
    }
    @Override
    protected  void onResume(){
        super.onResume();       //重新绘制加载地图
        mapView.onResume();
    }
    @Override
    protected  void onPause(){
        super.onPause();        //暂停地图的绘制
        mapView.onPause();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);      //保存地图
        mapView.onSaveInstanceState(outState);
    }

}
