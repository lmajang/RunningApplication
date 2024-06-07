package com.example.runningapplication.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBOpenHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "runningAppDB.db";
    private static final int VERSION = 1;

    public MyDBOpenHelper(@Nullable Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLStatements.createChatTB);
        db.execSQL(SQLStatements.createFriendListTB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
