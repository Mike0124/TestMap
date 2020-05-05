package com.example.testmap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.testmap.JavaBean.Device;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationSource, AMapLocationListener {
    private Button mscanbutton;
    private ImageButton mloginbutton;

    //扫描请求码
    private int REQUEST_CODE_SCAN = 1;

    //初始化地图的值
    AMap aMap = null;
    MapView mapView = null;
    List<Device> deviceList = new ArrayList<>();//设备信息ArrayList
    private Bundle savedInstanceState;
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mLocationClient = null;
    public LocationSource.OnLocationChangedListener mLocationListener;
    private AMapLocationClient mlocationClient;

    //权限请求
    private boolean needCheckBackLocation = false;
    //如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
    private static String BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };
    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;
    private static final int PERMISSON_REQUESTCODE = 0;
//    public static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//设置对应的XML布局文件

        if (Build.VERSION.SDK_INT > 28
                && getApplicationContext().getApplicationInfo().targetSdkVersion > 28) {
            needPermissions = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_PHONE_STATE,
                    BACKGROUND_LOCATION_PERMISSION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
            };
        }
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            if (isNeedCheck) {
                checkPermissions(needPermissions);
            }
        }

        initLocationService();//初始化高德地图和定位

        scan();//扫码

        login();//登录
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 扫描二维码/条码回传
            if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                Toast.makeText(this, "扫码成功，结果：" + content, Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();       //重新绘制加载地图
        deactivate();
        mapView.onResume();

        //若定位权限通过，判断是否开启位置信息
        if (Build.VERSION.SDK_INT > 22) {        //判断手机版本是否在6.0以上，如在以上则需要动态申请权限
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (!isLocationEnabled()) {
                    toOpenGPS(this);
                }
            }
        }

        aMap.setLocationSource(this);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模
    }

    @Override
    protected void onPause() {
        super.onPause();        //暂停地图的绘制
        deactivate();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        mBluetoothAdapter.disable();//关闭蓝牙
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);      //保存地图
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
    }

    public void initLocationService() { //初始化高德地图和定位
        //定义了一个地图view
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。·
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        //设置地图的放缩级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        myLocationStyle.strokeColor(Color.BLACK);
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));
        myLocationStyle.strokeWidth(0.6f);//设置定位蓝点精度圈的边框宽度的方法。
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        //设置默认定位按钮是否显示。
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//去掉高德地图右下角隐藏的缩放按钮
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setLocationSource(this);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        aMap.showIndoorMap(true);     //true：显示室内地图；false：不显示；
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。

        setDeviceInfo();

        Iterator<Device> it = deviceList.iterator();
        while (it.hasNext()) {
            Device device = it.next();
            LatLng latLng = new LatLng(device.getLatitude(), device.getLongitude());
            MarkerOptions markerOption = new MarkerOptions()
                    .position(latLng)
                    .title(device.getName())
                    .snippet(device.getExactPosition())
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.mipmap.icon)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            final Marker marker = aMap.addMarker(markerOption);
        }

        AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marker.getTitle().trim();
                Device device = null;
                Iterator<Device> it = deviceList.iterator();
                while (it.hasNext()) {
                    device = it.next();
                    if (title == device.getName().trim()) {//用title和site.name匹配来确定marker
                        break;
                    }
                }
                //BottomDialogs
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.custom_view, null);
                TextView customText = (TextView) customView.findViewById(R.id.custom_text_view1);
                customText.setText(device.getFullName());
                customText = (TextView) customView.findViewById(R.id.custom_text_view2);
                customText.setText("营业时间：" + device.getBusinessHours());
                Button dialButton = (Button) customView.findViewById(R.id.dial_button);
                Button detailsButton = (Button) customView.findViewById(R.id.details_button);
                final Device finalDevice = device;
                final Device finalDevice1 = device;
                final Device finalDevice2 = device;
                detailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userName = null;
                        String deviceName = finalDevice1.getName();
                        String exactPositon = finalDevice2.getExactPosition();
                        showDetails(userName, deviceName, exactPositon);
                    }
                });
                dialButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callPhone(finalDevice.getTelNum());
                    }
                });
                new BottomDialog.Builder(MainActivity.this)//BottomDialogs
                        .setTitle(device.getName())
                        .setCustomView(customView)
                        .show();

                return false;
            }

        };
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(markerClickListener);

    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        // TODO Auto-generated method stub
        if (aMapLocation != null && mLocationListener != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mLocationListener.onLocationChanged(aMapLocation);
            } else {//日志报错
                String errText = "failed to locate," + aMapLocation.getErrorCode() + ": "
                        + aMapLocation.getErrorInfo();
                Log.e("error", errText);
            }

        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        // TODO Auto-generated method stub
        mLocationListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getApplicationContext());
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mLocationListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
        mLocationOption = null;
    }

    //扫描二维码
    public void scan() {
        mscanbutton = findViewById(R.id.scan_button);
        mscanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                // 启用蓝牙 ( 2.Enable Bluetooth )
                if (!mBluetoothAdapter.isEnabled()) {
                    toOpenBT(MainActivity.this);
                }
                if (mBluetoothAdapter.isEnabled()) {
                    ZxingConfig config = new ZxingConfig();
                    config.setShowAlbum(false); //是否显示相册
                    config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
//                if (Build.VERSION.SDK_INT > 22) {        //判断手机版本是否在6.0以上，如在以上则需要动态申请权限
//                    if (ContextCompat.checkSelfPermission(MainActivity.this,
//                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(MainActivity.this,
//                                new String[]{Manifest.permission.CAMERA}, 1);
//                    } else {
//                        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//                        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
//                        startActivityForResult(intent, REQUEST_CODE_SCAN);
//                    }
//                } else {
//                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
//                    startActivityForResult(intent, REQUEST_CODE_SCAN);
//                }
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                }
            }
        });
    }

    //登录
    public void login() {
        mloginbutton = findViewById(R.id.Login);
        mloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InformationActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * @param permissions
     */
    private void checkPermissions(String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= 23
                    && getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                    Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class, int.class});
                    method.invoke(this, array, PERMISSON_REQUESTCODE);
                }
            }
        } catch (Throwable e) {
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> findDeniedPermissions(java.lang.String[] permissions) {
        List<java.lang.String> needRequestPermissonList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            try {
                for (java.lang.String perm : permissions) {
                    Method checkSelfMethod = getClass().getMethod("checkSelfPermission", java.lang.String.class);
                    Method shouldShowRequestPermissionRationaleMethod = getClass().getMethod("shouldShowRequestPermissionRationale",
                            java.lang.String.class);
                    if ((Integer) checkSelfMethod.invoke(this, perm) != PackageManager.PERMISSION_GRANTED
                            || (Boolean) shouldShowRequestPermissionRationaleMethod.invoke(this, perm)) {
                        if (!needCheckBackLocation
                                && BACKGROUND_LOCATION_PERMISSION.equals(perm)) {
                            continue;
                        }
                        needRequestPermissonList.add(perm);
                    }
                }
            } catch (Throwable e) {

            }
        }
        return needRequestPermissonList;
    }

    void setDeviceInfo() {
        Device orientalPearl = new Device("东方明珠电视塔", "上海市浦东新区陆家嘴街道东方明珠广播电视塔", "8:00:00-23:00:00", 121.499717, 31.239702, "16227394639", "一楼卫生间");
        deviceList.add(orientalPearl);
        Device joyCity = new Device("大悦城", "上海市静安区北站街道开封路中粮天悦壹号", "9:00:00-22:30:00", 121.472143, 31.243513, "16228399073", "一楼卫生间");
        deviceList.add(joyCity);
        Device globalHarbor = new Device("环球港", null, null, 121.412636, 31.233221, null, null);
        deviceList.add(globalHarbor);
        Device shu = new Device("上海大学", null, null, 121.457689, 31.275837, null, null);
        deviceList.add(shu);
        Device yanChangRMS = new Device("1212", null, null, 121.455329, 31.271675, null, null);
        deviceList.add(yanChangRMS);
        Device daNingA = new Device("1212", null, null, 121.449489, 31.275846, null, null);
        deviceList.add(daNingA);
        Device yangSiMS = new Device("杨思中学", null, null, 121.494689, 31.156454, null, null);
        deviceList.add(yangSiMS);
        final Device secs = new Device("建筑工程学校", null, null, 121.461276, 31.062121, null, null);
        deviceList.add(secs);
    }

    private void callPhone(String phone) {//打营业电话
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showDetails(String username, String deviceName, String exactPosition) {//显示设备信息
        Intent intent = new Intent(MainActivity.this, DeviceDetailsActivity.class);
        intent.putExtra("userName", username);
        intent.putExtra("deviceName", deviceName);
        intent.putExtra("exactPosition", exactPosition);

        startActivity(intent);
    }

    /**
     * 判断用户是否开启位置信息
     * @return
     */
    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * 提示用户去开启定位服务
     * @param activity
     */
    public static void toOpenGPS(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage("手机定位服务未开启，无法获取到您的准确位置信息，是否前往开启？")
                .setNegativeButton("取消", null)
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(intent);
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    public static void toOpenBT(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage("手机蓝牙未开启，无法连接设备，是否前往开启？")
                .setNegativeButton("取消", null)
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent enableBtIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                        activity.startActivity(enableBtIntent);
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}
