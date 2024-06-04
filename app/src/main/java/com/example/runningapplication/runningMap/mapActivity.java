package com.example.runningapplication.runningMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;

import com.amap.api.maps2d.model.PolylineOptions;
import com.example.runningapplication.R;
import com.example.runningapplication.utils.mapTools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class mapActivity extends Activity implements LocationSource, AMapLocationListener {
    private MapView mMapView = null;

    private AMap aMap = null;
//    组件
    private TextView runningStartBtn;

    private LinearLayout runningBtns;

    private LinearLayout instrumentpanel;

    private TextView runningPauseBtn;

    private TextView runningEndBtn;

    private Chronometer passTime;

    private TextView tvMileage;

    private TextView tvSpeed;


    private float ranDistance = 0;

    private float avgSpeed = 0;

    private long allTime = 0;
    private List<LatLng> latLngs = new ArrayList<LatLng>();

    private boolean runState = false;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private PolylineOptions mPolylineOptions;

    private String TAG = "mapActivity";

    private boolean permissionStatus = false;


    private String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.ACCESS_BACKGROUND_LOCATION
            , Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS};

    private static final int OPEN_SET_REQUEST_CODE = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(lackPermissions(permissions)){
            ActivityCompat.requestPermissions(this, permissions, OPEN_SET_REQUEST_CODE);
        }else{
            Log.d("permissions","Haven permissions");
        }
//       设置权限
        AMapLocationClient.updatePrivacyShow(getApplicationContext(), true, true);
        AMapLocationClient.updatePrivacyAgree(getApplicationContext(), true);
        setContentView(R.layout.activity_map);
        init();
        initAddListeners();
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

//        获取地图对象
        aMap = mMapView.getMap();
//设置定位
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.999391,116.135972),aMap.getMaxZoomLevel()));
// 绘画路线
//        new Thread(() -> {
//            while (true){
//                aMap.addPolyline(new PolylineOptions().
//                        addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }).start();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

//    权限判断

    private boolean lackPermissions(String[] permissions){
        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return true;
        }
        return false;
    }



    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if(mlocationClient == null){
            try {
                mlocationClient = new AMapLocationClient(this);
                mlocationClient.setLocationListener(this);
//          设置定位参数
                mLocationOption = new AMapLocationClientOption();
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                mLocationOption.setSensorEnable(true);
//            连续定位 2s
                mLocationOption.setInterval(2000);

                if(null != mlocationClient){
//                设置场景
                    mlocationClient.setLocationOption(mLocationOption);
                    mlocationClient.stopLocation();
                    //          开启定位
                    mlocationClient.startLocation();
                }


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if(mlocationClient!=null){
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    //    回调定位改变
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(null != aMapLocation){
            if(aMapLocation.getErrorCode() == 0){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                String dateStr = df.format(date);
                String now_addr = "TIME:"+ dateStr + "|address:"
                        +aMapLocation.getCountry()
                        +aMapLocation.getProvince()
                        +aMapLocation.getCity()
                        +aMapLocation.getDistrict();

                Log.d(TAG, now_addr);
                if(runState){
                    long times = passTime.getBase();
                    Log.d(TAG,"times:" +(SystemClock.elapsedRealtime()-times));
                    LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    if(latLngs.size()>0){
                        LatLng startLatLng = latLngs.get(latLngs.size()-1);
    //                    double distance = mapTools.calculateDistance(startLatLng.latitude,startLatLng.longitude,latLng.latitude,latLng.longitude);
                        float distance = AMapUtils.calculateLineDistance(startLatLng,latLng);
                        Log.d(TAG, String.valueOf(distance));
                        allTime = SystemClock.elapsedRealtime()-times;
                        if(distance > 1 ){
                            drawPathLine(latLng);
                            ranDistance += distance;
    //                        km/min
                            avgSpeed = ((float) allTime/60)/ranDistance;
                            tvSpeed.setText(String.format("%.2f",avgSpeed));
                            tvMileage.setText(String.format("%.2f",ranDistance/1000));
                        }
                    }else {
                        latLngs.add(latLng);
                    }
                }
                mListener.onLocationChanged(aMapLocation);
            }else {
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    private void init(){
        mPolylineOptions = new PolylineOptions();
        runningStartBtn = (TextView) findViewById(R.id.running_start_btn);

        runningBtns = (LinearLayout) findViewById(R.id.running_btns);

        instrumentpanel = (LinearLayout) findViewById(R.id.instrumentpanel);

        runningPauseBtn = (TextView) findViewById(R.id.running_pause_btn);

        runningEndBtn = (TextView) findViewById(R.id.running_end_btn);

        passTime = (Chronometer) findViewById(R.id.cm_passtime);

        tvMileage = (TextView) findViewById(R.id.tvMileage);

        tvSpeed = (TextView) findViewById(R.id.tvSpeed);

    }

    private void initAddListeners(){
        runningStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runState = true;
                runningBtns.setVisibility(View.VISIBLE);
                instrumentpanel.setVisibility(View.VISIBLE);
                runningStartBtn.setVisibility(View.GONE);
                passTime.setBase(SystemClock.elapsedRealtime());
                passTime.start();
            }
        });

        runningPauseBtn.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if(runState){
                    runState = false;
                    passTime.stop();
                    runningPauseBtn.setBackground(getDrawable(R.drawable.map_runstart_btn));
                }else {
                    runState = true;
                    passTime.setBase(SystemClock.elapsedRealtime()-allTime);
                    passTime.start();
                    runningPauseBtn.setBackground(getDrawable(R.drawable.map_runstop_btn));
                }
            }
        });
    }

    private void drawPathLine(LatLng nowLatLng){
        latLngs.add(nowLatLng);
        aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
    }
}
