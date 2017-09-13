package com.zhour.zhoursecurity.parser;


import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.models.VisitorModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by shankar on 7/17/2017.
 */

public class VisitorParser implements Parser<Model> {
    @Override
    public Model parse(String s) {
        VisitorModel mVisitorModel = new VisitorModel();
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("IsError"))
                mVisitorModel.setError(jsonObject.optBoolean("IsError"));
            if (jsonObject.has("Message"))
                mVisitorModel.setMessage(jsonObject.optString("Message"));

            if (jsonObject.has("Output")) {
                JSONArray jsonArray = jsonObject.optJSONArray("Output");
                JSONObject jsonObject1 = jsonArray.optJSONObject(0);

                mVisitorModel.setVisitorname(jsonObject1.optString("visitorname"));
                mVisitorModel.setContactnumber(jsonObject1.optString("contactnumber"));
                mVisitorModel.setEmailid(jsonObject1.optString("emailid"));
                mVisitorModel.setInvitetypeid(jsonObject1.optString("invitetypeid"));
                mVisitorModel.setInvitetype(jsonObject1.optString("invitetype"));
                mVisitorModel.setInvitenote(jsonObject1.optString("invitenote"));
                mVisitorModel.setResidentid(jsonObject1.optString("residentid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mVisitorModel;
    }
}