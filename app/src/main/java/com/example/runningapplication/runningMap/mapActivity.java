package com.example.runningapplication.runningMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;

import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.runningapplication.Login.LoginActivity;
import com.example.runningapplication.R;
import com.example.runningapplication.chatClient.Client;
import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.entity.locationEntity;
import com.example.runningapplication.utils.Tools;
import com.example.runningapplication.utils.httpTools;

import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    private CircleProgressBar runningEndBtn;

    private Chronometer passTime;

    private TextView tvMileage;

    private TextView tvSpeed;


    private float ranDistance = 0;

    private float avgSpeed = 0;

    private long allTime = 0;

    private long startTime = 0;
    private int cadence = 100;

    private long firstDownTime = 0;
    private List<LatLng> latLngs = new ArrayList<LatLng>();

    private List<locationEntity> up_latLngs = new ArrayList<>();

    private boolean runState = false;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private PolylineOptions mPolylineOptions;

    private GeocodeSearch geocodeSearch;
    private String TAG = "mapActivity";

    private boolean permissionStatus = false;

    private long downTime;

    AMapLocation run_location;
    private static final int LONG_PRESS_THRESHOLD = 3000;//长按时间

    private static final class MyProgressFormatter implements CircleProgressBar.ProgressFormatter {
        private static final String DEFAULT_PATTERN = "结束";
        @Override
        public CharSequence format(int progress, int max) {
            return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max));
        }
    }

    private Handler handler = new Handler();

    private Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            // 处理长按事件
            Log.d("CustomLongPress", "Long press detected!");
            runningBtns.setVisibility(View.GONE);
            runState = false;
            passTime.stop();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("targetDistance", LoginActivity.sp.getString("target","1"));
                        jsonObject.put("RanDistance",String.valueOf(ranDistance/1000));
                        jsonObject.put("spendTime",String.valueOf(allTime));
                        jsonObject.put("startTime",String.valueOf(startTime));
                        jsonObject.put("runLine",up_latLngs.toString());
                        jsonObject.put("speed",String.valueOf(avgSpeed));
                        jsonObject.put("address",run_location.getCountry()+run_location.getProvince()+
                                run_location.getCity()+run_location.getDistrict());
                        jsonObject.put("cadence",String.valueOf(cadence));
                        jsonObject.put("runnerId", Client.getUserId());
                        String isSuccess = httpTools.post(appConfig.ipAddress+"/uploadRunRecord",jsonObject.toString());
                        if (isSuccess.equals("0")){
                            Log.d(TAG,"上传失败");
                        }else {
                            Log.d(TAG,"上传成功");
                            JSONObject object = new JSONObject();
                            object.put("userId",Client.getUserId());
                            object.put("run",String.valueOf(ranDistance/1000));
                            Calendar cd = Calendar.getInstance();
                            String year= String.valueOf(cd.get(Calendar.YEAR));
                            String month= String.valueOf(cd.get(Calendar.MONTH)+1);
                            String day= String.valueOf(cd.get(Calendar.DATE));
                            String data = year+"/"+month+"/"+day;
                            object.put("date", data);
                            String isSuc = httpTools.post(appConfig.ipAddress+"/uploadTodayRecord",object.toString());
                            if (!isSuc.equals("0")){
                                Log.d(TAG,"上传成功");
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"上传记录成功",Toast.LENGTH_SHORT).show();
                                    }
                                });
//
                            }
//
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    };

    private Runnable updateProgressRunnable = new Runnable() {
        @Override
        public void run() {
            // 处理长按事件
            Log.d("updateProgress","更新进度");
            downTime = System.currentTimeMillis();
            Log.d(TAG,Long.toString((downTime - firstDownTime)));
            runningEndBtn.setProgress((int)(downTime - firstDownTime));
            handler.postDelayed(this,100);
        }
    };

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
        try {
            init();
        } catch (AMapException e) {
            throw new RuntimeException(e);
        }
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
                        +aMapLocation.getDistrict()
                        +"code:"
                        +aMapLocation.getAdCode();

                Log.d(TAG, now_addr);
                run_location = aMapLocation;
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
                        up_latLngs.add(new locationEntity(String.valueOf(latLng.latitude),String.valueOf(latLng.longitude)));
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

    private void init() throws AMapException {

        mPolylineOptions = new PolylineOptions();
        runningStartBtn = (TextView) findViewById(R.id.running_start_btn);

        runningBtns = (LinearLayout) findViewById(R.id.running_btns);

        instrumentpanel = (LinearLayout) findViewById(R.id.instrumentpanel);

        runningPauseBtn = (TextView) findViewById(R.id.running_pause_btn);

        runningEndBtn = (CircleProgressBar) findViewById(R.id.running_end_btn);

        passTime = (Chronometer) findViewById(R.id.cm_passtime);

        tvMileage = (TextView) findViewById(R.id.tvMileage);

        tvSpeed = (TextView) findViewById(R.id.tvSpeed);

        runningEndBtn.setProgressFormatter(new MyProgressFormatter());

        runningEndBtn.setMax(LONG_PRESS_THRESHOLD);
        geocodeSearch = new GeocodeSearch(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initAddListeners(){
        runningStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runState = true;
                runningBtns.setVisibility(View.VISIBLE);
                instrumentpanel.setVisibility(View.VISIBLE);
                runningStartBtn.setVisibility(View.GONE);
                startTime = System.currentTimeMillis();
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


        runningEndBtn.setOnTouchListener(new View.OnTouchListener() {


            boolean isFirstDown = true;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isFirstDown) firstDownTime = System.currentTimeMillis();
                        isFirstDown = false;
                        Log.d(TAG,"Down");
                        handler.postDelayed(longPressRunnable, LONG_PRESS_THRESHOLD); //保持按下状态指定时间回调
                        updateProgress();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        runningEndBtn.setProgress(0);
                        handler.removeCallbacks(longPressRunnable);
                        handler.removeCallbacks(updateProgressRunnable);
                        downTime = 0;
                        isFirstDown = true;
                        firstDownTime = 0;
                        break;
                }
                return true;
            }
        });
    }

    private void drawPathLine(LatLng nowLatLng){
        latLngs.add(nowLatLng);
        up_latLngs.add(new locationEntity(String.valueOf(nowLatLng.latitude),String.valueOf(nowLatLng.longitude)));
        aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.GREEN));
    }

    private void updateProgress(){
        // 开始更新进度
        handler.post(updateProgressRunnable);

    }
}
