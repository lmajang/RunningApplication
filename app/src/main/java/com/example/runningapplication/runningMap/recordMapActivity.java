package com.example.runningapplication.runningMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.MovingPointOverlay;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.example.runningapplication.R;
import com.example.runningapplication.entity.locationEntity;
import com.example.runningapplication.entity.runRecordEntity;
import com.example.runningapplication.utils.Tools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class recordMapActivity extends Activity {

    private MapView mRecordMapView = null;
    private AMap aMap;

    private List<LatLng> points;

    private SmoothMoveMarker moveMarker;

    private runRecordEntity runRecord;

    private Chronometer record_cm_passtime;
    private TextView record_tvMileage;
    private TextView record_tvSpeed;
    private TextView record_cadence;
    private LatLngBounds bounds;
    private static final String TAG = "recordMapActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_map);
        mRecordMapView = (MapView) findViewById(R.id.record_map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mRecordMapView.onCreate(savedInstanceState);
//        获取地图对象
        aMap = mRecordMapView.getMap();
        points = new ArrayList<>();
        Intent intent = getIntent();
        runRecord = (runRecordEntity) intent.getSerializableExtra("data");
        init();
        record_cm_passtime.setText(Tools.formatMillisecondsToTimeString(Long.parseLong(runRecord.getSpendTime())));
        Log.d(TAG,String.format("%.2f", Float.parseFloat(runRecord.getRanDistance())));
        record_tvMileage.setText(String.format("%.2f", Float.parseFloat(runRecord.getRanDistance())));
        record_tvSpeed.setText(String.format("%.2f", Double.parseDouble(runRecord.getSpeed())));
        record_cadence.setText(runRecord.getCadence());

        new Thread(new Runnable() {
            @Override
            public void run() {
                initLatLngs(runRecord.getRunLine());
                // 取轨迹点的第一个点 作为 平滑移动的启动
                LatLng drivePoint = points.get(0);
                if (points.size()>=2){
                    bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));
                    aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,50));
                    Marker marker = aMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location))
                            .anchor(0.5f,0.5f));
                    MovingPointOverlay smoothMarker = new MovingPointOverlay(aMap,marker);

                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(drivePoint,aMap.getMaxZoomLevel()-3));
                    Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
                    points.set(pair.first, drivePoint);
                    aMap.addPolyline(new PolylineOptions().
                            addAll(points).width(10).color(Color.argb(255, 1, 1, 1)));

                    List<LatLng> subList = points.subList(pair.first, points.size());
                    // 设置轨迹点
                    smoothMarker.setPoints(subList);
// 设置平滑移动的总时间  单位  秒
                    smoothMarker.setTotalDuration(30);
// 开始移动
                    smoothMarker.startSmoothMove();
                }else {
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(drivePoint,aMap.getMaxZoomLevel()-3));
                    MarkerOptions markerOption = new MarkerOptions();
                    markerOption.position(drivePoint);
//
                    aMap.addMarker(markerOption);
                }
            }
        }).start();


    }

    private void initLatLngs(String line){
        try {
            List<locationEntity> list = JSON.parseArray(line, locationEntity.class);
            if (list.isEmpty()) return;
            for (locationEntity location:list){
                points.add(new LatLng(Double.parseDouble(location.getLatitude()),Double.parseDouble(location.getLongitude())));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init(){
        record_cm_passtime = findViewById(R.id.record_cm_passtime);
        record_tvMileage = findViewById(R.id.record_tvMileage);
        record_tvSpeed = findViewById(R.id.record_tvSpeed);
        record_cadence = findViewById(R.id.record_cadence);
        record_cm_passtime.setFormat("H：MM：SS");
    }
}
