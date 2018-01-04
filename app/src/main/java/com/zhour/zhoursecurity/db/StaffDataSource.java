package com.zhour.zhoursecurity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhour.zhoursecurity.models.StaffDetailsModel;

import java.util.ArrayList;

/**
 * Created by Shankar on 12/24/2017.
 */

public class StaffDataSource {

    private SQLiteDatabase mDatabase;
    private DatabaseHandler mHandler;
    private Context mContext;
    private String mColumns[] = {DBConstants.STAFF_DETAILS_ID, DBConstants.STAFF_DETAILS_STAFF_ID,
            DBConstants.STAFF_DETAILS_STAFF_NAME, DBConstants.STAFF_DETAILS_DEPT_ID,
            DBConstants.STAFF_DETAILS_DEPT_NAME, DBConstants.STAFF_DETAILS_PHOTO,
            DBConstants.STAFF_DETAILS_RFID, DBConstants.STAFF_DETAILS_COMMUNITY_ID};

    public StaffDataSource(Context context) {
        if (context != null) {
            mContext = context;
            mHandler = new DatabaseHandler(mContext);
        }
    }

    private void open() {
        if (mHandler != null) {
            mDatabase = mHandler.getWritableDatabase();
        }
    }

    private void close() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public long insertData(StaffDetailsModel model) {
        long insertValue = -1;

        ContentValues values = new ContentValues();
        values.put(DBConstants.STAFF_DETAILS_STAFF_ID, model.getStaff_id());
        values.put(DBConstants.STAFF_DETAILS_STAFF_NAME, model.getStaff_name());
        values.put(DBConstants.STAFF_DETAILS_DEPT_ID, model.getDept_id());
        values.put(DBConstants.STAFF_DETAILS_DEPT_NAME, model.getDept_name());
        values.put(DBConstants.STAFF_DETAILS_PHOTO, model.getPhoto());
        values.put(DBConstants.STAFF_DETAILS_RFID, model.getRfid());
        values.put(DBConstants.STAFF_DETAILS_COMMUNITY_ID, model.getCommunity_id());
        open();
        insertValue = mDatabase.insert(DBConstants.TABLE_CREATE_STAFF_DETAILS, null,
                values);
        close();
        return insertValue;
    }


    /* Get all workouts models */
    public ArrayList<StaffDetailsModel> selectAll() {
        ArrayList<StaffDetailsModel> staffDetailsModels = null;
        open();
        Cursor cursor = mDatabase.query(DBConstants.TABLE_CREATE_STAFF_DETAILS, mColumns,
                null, null, null, null, null);
        if (cursor.getCount() > 0) {
            staffDetailsModels = new ArrayList<StaffDetailsModel>();
            while (cursor.moveToNext()) {
                StaffDetailsModel model = new StaffDetailsModel();
                model.setId(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_ID)));
                model.setStaff_id(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_STAFF_ID)));
                model.setStaff_name(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_STAFF_NAME)));
                model.setDept_id(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_DEPT_ID)));
                model.setDept_name(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_DEPT_NAME)));
                model.setPhoto(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_PHOTO)));
                model.setRfid(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_RFID)));
                model.setCommunity_id(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_COMMUNITY_ID)));
                staffDetailsModels.add(model);
            }
        }
        cursor.close();
        close();
        return staffDetailsModels;
    }


    public int deleteAll() {
        int deleteValue = -1;
        open();
        deleteValue = mDatabase.delete(DBConstants.TABLE_CREATE_STAFF_DETAILS, null, null);
        close();

        return deleteValue;
    }

    /* Get count */
    public int getDataCount() {
        int brandCount = -1;
        open();
        Cursor cursor = mDatabase.query(DBConstants.TABLE_CREATE_STAFF_DETAILS, new String[]{DBConstants.STAFF_DETAILS_STAFF_ID},
                null, null, null, null, null);
        if (cursor.getCount() > 0) {
            brandCount = cursor.getCount();
        }
        cursor.close();
        close();
        return brandCount;
    }

    public StaffDetailsModel getStaffDetails(String id) {
        boolean isRead = false;
        open();
        Cursor cursor = mDatabase.query(DBConstants.TABLE_CREATE_STAFF_DETAILS, mColumns,
                DBConstants.STAFF_DETAILS_RFID + " = ?", new String[]{"" + id}, null, null, null);
        StaffDetailsModel model = new StaffDetailsModel();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                model.setId(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_ID)));
                model.setStaff_id(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_STAFF_ID)));
                model.setStaff_name(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_STAFF_NAME)));
                model.setDept_id(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_DEPT_ID)));
                model.setDept_name(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_DEPT_NAME)));
                model.setPhoto(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_PHOTO)));
                model.setRfid(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_RFID)));
                model.setCommunity_id(cursor.getString(cursor
                        .getColumnIndex(DBConstants.STAFF_DETAILS_COMMUNITY_ID)));
            }
        }
        cursor.close();
        close();
        return model;
    }
}