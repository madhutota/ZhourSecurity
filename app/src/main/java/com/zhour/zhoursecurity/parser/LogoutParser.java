package com.zhour.zhoursecurity.parser;

import com.zhour.zhoursecurity.models.LogoutModel;
import com.zhour.zhoursecurity.models.Model;

import org.json.JSONObject;

/**
 * Created by Shankar on 1/4/2018.
 */

public class LogoutParser implements Parser<Model> {
    @Override
    public Model parse(String s) {
        LogoutModel logoutModel = new LogoutModel();
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("IsError"))
                logoutModel.setError(jsonObject.optBoolean("IsError"));
            if (jsonObject.has("Message"))
                logoutModel.setMessage(jsonObject.optString("Message"));
            if (jsonObject.has("Output")) {
                logoutModel.setOutput(jsonObject.optString("Output"));
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return logoutModel;
    }
}
