package com.zhour.zhoursecurity.parser;


import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.models.SuccessModel;

import org.json.JSONObject;

/**
 * Created by shankar on 7/17/2017.
 */

public class SuccessParser implements Parser<Model> {
    @Override
    public Model parse(String s) {
        SuccessModel mSuccessModel = new SuccessModel();
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("IsError"))
                mSuccessModel.setError(jsonObject.optBoolean("IsError"));
            if (jsonObject.has("Output"))
                mSuccessModel.setMessage(jsonObject.optString("Output"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mSuccessModel;
    }
}