package com.zhour.zhoursecurity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler instance;

    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ZhourSecurity_DB";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConstants.CREATE_TABLE_STAFF_DETAILS);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is

    }

    public static synchronized DatabaseHandler getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHandler(context);
        return instance;
    }
}