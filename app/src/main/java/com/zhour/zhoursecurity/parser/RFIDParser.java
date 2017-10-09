package com.zhour.zhoursecurity.parser;

import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.models.RFIDModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shankar on 10/9/2017.
 */

public class RFIDParser implements Parser<Model> {
    @Override
    public Model parse(String s) {
        RFIDModel mRFIDModel = new RFIDModel();
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("IsError"))
                mRFIDModel.setIsError(jsonObject.optBoolean("IsError"));
            if (jsonObject.has("Message"))
                mRFIDModel.setMessage(jsonObject.optString("Message"));
            if (jsonObject.has("Error"))
                mRFIDModel.setError(jsonObject.optString("Error"));
            if (jsonObject.has("Output"))
                mRFIDModel.setOutput(jsonObject.optString("Output"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mRFIDModel;
    }
}