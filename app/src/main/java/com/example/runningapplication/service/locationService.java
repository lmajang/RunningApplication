package com.example.runningapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.example.runningapplication.runningMain.ui.home.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class locationService extends Service implements AMapLocationListener {
    private final String TAG = "locationService";

    private IBinder locationServiceBinder = new locationBinder();
    private AMapLocationClient aMapLocationClient = null;

    private AMapLocationClientOption aMapLocationClientOption = null;

    public class locationBinder extends Binder{
        locationService getService(){
            return locationService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return locationServiceBinder;
    }

    @Override
    public void onCreate(){
        Log.d(TAG, "location Created");
        locationInit();
    }

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
                        +aMapLocation.getDistrict()+"||"+
                        +aMapLocation.getLongitude()+" "+aMapLocation.getLatitude();
                HomeFragment.mainHandler.obtainMessage(HomeFragment.LOCATION_GET,new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude())).sendToTarget();


                Log.d(TAG, now_addr);
            }else {
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }


    }

    private void locationInit(){
        try {

            Log.d(TAG, "Start location");
            aMapLocationClient = new AMapLocationClient(getApplicationContext());
            aMapLocationClient.setLocationListener(this);

            aMapLocationClientOption = new AMapLocationClientOption();
            aMapLocationClientOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
            aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            连续定位 1s
            aMapLocationClientOption.setInterval(1000);
            if(null != aMapLocationClient){
//                设置场景
                aMapLocationClient.setLocationOption(aMapLocationClientOption);
                aMapLocationClient.stopLocation();
                aMapLocationClient.startLocation();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
