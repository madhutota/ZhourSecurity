package com.zhour.zhoursecurity.Utils;

/**
 * Created by shankar on 4/30/2017.
 */

public class APIConstants {
    public enum REQUEST_TYPE {
        GET, POST, MULTIPART_GET, MULTIPART_POST, DELETE, PUT, PATCH
    }

    private static final String STATUS = "status";
    public final static String SERVER_NOT_RESPONDING = "We are unable to connect the internet. " +
            "Please check your connection and try again.";

    public static String ERROR_MESSAGE = "We could not process your request at this time. Please try again later.";

    // public static String BASE_URL = "http://139.59.30.4:3500/api/v1.0/";

    public static String BASE_URL = "http://139.59.30.4/api/v1.0/";
    public static String HOME_URL = "http://zohr.in/";
    //public static String BASE_URL = "http://icuepro.com/api/v1.0/";

    public static String AUTHENTICATE_USER = BASE_URL + "authenticateUser";
    public static String GET_LOOKUP_DATA_BY_ENTITY_NAME = BASE_URL + "getLookupDataByEntityName";
    public static String SAVE_INVITE = BASE_URL + "saveInvite";
    public static String CREATE_OR_UPDATE_VISITOR = BASE_URL + "createOrUpdateVisitor";
    public static String GET_INVITE_INFO = BASE_URL + "getInviteInfo";
    public static String DEL_VISITOR = BASE_URL + "delVisitor";
    public static String CREATE_OR_UPDATE_STAFF_VISIT = BASE_URL + "createOrUpdateStaffVisit";
    public static String GET_COMMUNITY_STAFF_INFO = BASE_URL + "getCommunityStaffInfo";
    public static String DEL_STAFF_VISIT = BASE_URL + "delStaffVisit";
    public static String LOGOUT = BASE_URL + "logoutUser";

}
