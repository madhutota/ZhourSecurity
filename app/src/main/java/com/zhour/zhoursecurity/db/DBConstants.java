package com.zhour.zhoursecurity.db;

/**
 * Created by Shankar on 12/24/2017.
 */

public class DBConstants {

    public static final String TABLE_CREATE_STAFF_DETAILS = "create_sales";

    public static final String STAFF_DETAILS_ID = "_id";
    public static final String STAFF_DETAILS_STAFF_ID = "staff_id";
    public static final String STAFF_DETAILS_STAFF_NAME = "staff_name";
    public static final String STAFF_DETAILS_DEPT_ID = "dept_id";
    public static final String STAFF_DETAILS_DEPT_NAME = "dept_name";
    public static final String STAFF_DETAILS_PHOTO = "photo";
    public static final String STAFF_DETAILS_RFID = "rfid";
    public static final String STAFF_DETAILS_COMMUNITY_ID = "community_id";

    public static final String CREATE_TABLE_STAFF_DETAILS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_CREATE_STAFF_DETAILS
            + "(" + STAFF_DETAILS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + STAFF_DETAILS_STAFF_ID + "  TEXT NOT NULL, "
            + STAFF_DETAILS_STAFF_NAME + "  TEXT NOT NULL, "
            + STAFF_DETAILS_DEPT_ID + "  TEXT NOT NULL, "
            + STAFF_DETAILS_DEPT_NAME + "  TEXT NOT NULL, "
            + STAFF_DETAILS_PHOTO + "  TEXT NOT NULL, "
            + STAFF_DETAILS_COMMUNITY_ID + "  TEXT NOT NULL, "
            + STAFF_DETAILS_RFID + "  TEXT NOT NULL"
            + ")";
}
