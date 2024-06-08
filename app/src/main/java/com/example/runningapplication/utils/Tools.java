package com.example.runningapplication.utils;

import android.nfc.Tag;

import com.example.runningapplication.R;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Tools {
    static public int selectHp(String id){
        int hp_id = Integer.parseInt(id);
        System.out.println("tttttttttttttttttttttttttttttttttttttt:"+hp_id);
        if(hp_id == 1){
            return R.drawable.avatar1;
        }else if (hp_id == 2){
            return R.drawable.avatar2;
        } else if (hp_id == 3){
            return R.drawable.avatar3;
        } else if (hp_id == 4){
            return R.drawable.avatar4;
        }else if (hp_id == 5){
            return R.drawable.avatar5;
        }else {
            return R.drawable.img;
        }
    }

    public static String formatMillisecondsToTimeString(long milliseconds) {
        int totalSeconds = (int) (milliseconds / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        // 使用String.format来确保每个字段都是两位数
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String convertTimestampToDateString(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return sdf.format(date);
    }
}
