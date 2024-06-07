package com.example.runningapplication.runningMain.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amap.api.maps2d.model.LatLng;
import com.example.runningapplication.R;
import com.example.runningapplication.View.CircularStatView;
import com.example.runningapplication.View.Weather;
import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.runningMap.mapActivity;
import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    private TextView tianqi;
    private TextView text;
    CircularStatView circularStatView;
    Weather weather;
    Button btnrun;
    private View view;
    SharedPreferences sp;
    String target="",run="";
    static String citylocation="",citycode = "",tianqi1 = null, wendu = null, shidu = null;
    static LatLng location=null;
    static public final int LOCATION_GET = 2;
    @SuppressLint("HandlerLeak")
    public static Handler mainHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == LOCATION_GET){
                location=(LatLng) msg.obj;
                citylocation=(location.latitude+30)+","+location.longitude;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        weather=view.findViewById(R.id.Weather);
        circularStatView =view.findViewById(R.id.circularStatView);
        btnrun=view.findViewById(R.id.button1);
        tianqi=(TextView) view.findViewById(R.id.textView);
        text=(TextView) view.findViewById(R.id.text);
        sp = this.getActivity().getSharedPreferences("user", this.getActivity().MODE_PRIVATE);
        String id=sp.getString("id",null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody formBody = new FormBody.Builder().add("id", id).build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(appConfig.ipAddress+"/home1")
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    final String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    String avatar=jsonObject.getString("avatar");
                    target=jsonObject.getString("target");
                    avatarchange(avatar);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Calendar cd = Calendar.getInstance();
                    String year= String.valueOf(cd.get(Calendar.YEAR));
                    String month= String.valueOf(cd.get(Calendar.MONTH)+1);
                    String day= String.valueOf(cd.get(Calendar.DATE));
                    FormBody formBody = new FormBody.Builder().add("id", id).add("date",year+"/"+month+"/"+day+"").build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(appConfig.ipAddress+"/home2")
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    final String responseData = response.body().string();
                    run=responseData;
                    while(target.equals("")){}
                    text.setText("当前已跑:\n"+run+"公里/"+target+"公里");
                    // 设置圆环统计图的两个部分的百分比
                    circularStatView.setPercentage((float) (Double.parseDouble(run)/Double.parseDouble(target))*100);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        initWeather();


        // 设置圆环中心的图片
        //circularStatView.setCenterImage(BitmapFactory.decodeResource(getResources(), R.drawable.zh));
        weather.setCenterImage(BitmapFactory.decodeResource(getResources(),R.drawable.sun));
        btnrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), mapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
        getCitycode();
        getWeather();
    }
    private void initWeather() {
        HeConfig.init("HE2406050032451879","2b1977f5e1ec4cbdb28d7ae8c7216c54");
        HeConfig.switchToDevService();
    }
    private void getCitycode(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(citylocation.equals("")) {}
                Log.i("123123",citylocation);
                QWeather.getGeoCityLookup(view.getContext(), citylocation//"120.04,30.23"
                        ,new QWeather.OnResultGeoListener(){
                            @Override
                            public void onError(Throwable throwable) {
                                Log.i("123", "Weather Now onError: ",throwable);
                            }

                            @Override
                            public void onSuccess(GeoBean geoBean) {
                                String jsonCity = new Gson().toJson(geoBean.getLocationBean());
                                Log.i("123123", " Weather Now onSuccess: " + jsonCity);
                                try{
                                    JSONArray jsonArray = new JSONArray(jsonCity);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    citycode=jsonObject.getString("id");
                                    Log.i("123123123", citycode);
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }).start();

    }
    private void getWeather() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(citycode.equals("")){}
                QWeather.getWeatherNow(view.getContext(), citycode,//"101210101"/*CN+中国城市代码*/,
                        Lang.ZH_HANS, Unit.METRIC,
                        new QWeather.OnResultWeatherNowListener() {
                            @Override
                            public void onError(Throwable e) {
                                Log.i("123", "Weather Now onError: ", e);
                            }

                            @Override
                            public void onSuccess(WeatherNowBean weatherBean) {
                                String jsonData = new Gson().toJson(weatherBean);
                                Log.i("123", " Weather Now onSuccess: " + jsonData);
                                if (Code.OK == weatherBean.getCode()) {
                                    String JsonNow = new Gson().toJson(weatherBean.getNow());
                                    String JsonBasic = new Gson().toJson(weatherBean.getBasic());
                                    JSONObject jsonObject = null;
                                    JSONObject jsonObject2 = null;
                                    try {
                                        jsonObject = new JSONObject(JsonNow);
                                        jsonObject2 = new JSONObject(JsonBasic);
                                        tianqi1 = jsonObject.getString("text");
                                        wendu = jsonObject.getString("temp");
                                        shidu = jsonObject.getString("humidity");
                                        tianqi.setText("当前天气:"+tianqi1+"\n当前气温:"+wendu+"℃\n当前湿度:"+shidu);
                                        if(tianqi1.contains("阴")) weather.setCenterImage(BitmapFactory.decodeResource(getResources(),R.drawable.yin));
                                        if(tianqi1.contains("云")) weather.setCenterImage(BitmapFactory.decodeResource(getResources(),R.drawable.cloud));
                                        if(tianqi1.contains("晴")) weather.setCenterImage(BitmapFactory.decodeResource(getResources(),R.drawable.sun));
                                        if(tianqi1.contains("雨")) weather.setCenterImage(BitmapFactory.decodeResource(getResources(),R.drawable.rain));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.i("123", "有错误");
                                    return;
                                }
                            }
                        });
            }
        }).start();

    }
    public void avatarchange(String avatarnum){
        if(avatarnum.equals("1")) circularStatView.setCenterImage(BitmapFactory.decodeResource(getResources(),R.drawable.avatar1));
        if(avatarnum.equals("2")) circularStatView.setCenterImage(BitmapFactory.decodeResource(getResources(),R.drawable.avatar2));
        if(avatarnum.equals("3")) circularStatView.setCenterImage(BitmapFactory.decodeResource(getResources(),R.drawable.avatar3));
        if(avatarnum.equals("4")) circularStatView.setCenterImage(BitmapFactory.decodeResource(getResources(),R.drawable.avatar4));
        if(avatarnum.equals("5")) circularStatView.setCenterImage(BitmapFactory.decodeResource(getResources(),R.drawable.avatar5));
    }
}

