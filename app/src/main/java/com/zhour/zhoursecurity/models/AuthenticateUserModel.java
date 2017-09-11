package com.zhour.zhoursecurity.models;

import java.util.ArrayList;

/**
 * Created by shankar on 7/17/2017.
 */

public class AuthenticateUserModel extends Model {
    private boolean IsError;
    private String Message;
    private String userid;
    private String username;
    private String contactnumber;
    private String lastlogin;
    private String sesid;
    private String token;
    private ArrayList<CommunityUserModel> communitiesList = new ArrayList<>();

    public ArrayList<CommunityUserModel> getCommunitiesList() {
        return communitiesList;
    }

    public void setCommunitiesList(ArrayList<CommunityUserModel> communitiesList) {
        this.communitiesList = communitiesList;
    }

    public boolean isError() {
        return IsError;
    }

    public void setError(boolean error) {
        IsError = error;
    }

    @Override
    public String getMessage() {
        return Message;
    }

    @Override
    public void setMessage(String message) {
        Message = message;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }
    public String getSesid() {
        return sesid;
    }

    public void setSesid(String sesid) {
        this.sesid = sesid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(String lastlogin) {
        this.lastlogin = lastlogin;
    }
}
