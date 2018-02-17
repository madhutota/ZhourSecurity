package com.zhour.zhoursecurity.parser;


import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.models.VisitorListModel;
import com.zhour.zhoursecurity.models.VisitorModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shankar on 7/17/2017.
 */

public class VisitorParser implements Parser<Model> {
    @Override
    public Model parse(String s) {
        VisitorListModel visitorListModel = new VisitorListModel();
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("IsError"))
                visitorListModel.setError(jsonObject.optBoolean("IsError"));
            if (jsonObject.has("Message"))
                visitorListModel.setMessage(jsonObject.optString("Message"));

            ArrayList<VisitorModel> visitorModels = new ArrayList<>();

            if (jsonObject.has("Output")) {
                JSONArray jsonArray = jsonObject.optJSONArray("Output");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                    VisitorModel mVisitorModel = new VisitorModel();
                    mVisitorModel.setVisitorname(jsonObject1.optString("visitorname"));
                    mVisitorModel.setContactnumber(jsonObject1.optString("contactnumber"));
                    mVisitorModel.setEmailid(jsonObject1.optString("emailid"));
                    mVisitorModel.setInvitetypeid(jsonObject1.optString("invitetypeid"));
                    mVisitorModel.setInvitetype(jsonObject1.optString("invitetype"));
                    mVisitorModel.setInvitenote(jsonObject1.optString("invitenote"));
                    mVisitorModel.setResidentid(jsonObject1.optString("residentid"));
                    mVisitorModel.setResidentname(jsonObject1.optString("residentname"));
                    mVisitorModel.setResidentcontact1(jsonObject1.optString("residentcontact1"));
                    mVisitorModel.setResidentcontact2(jsonObject1.optString("residentcontact2"));
                    mVisitorModel.setFlat(jsonObject1.optString("flat"));
                    mVisitorModel.setInviteid(jsonObject1.optString("inviteid"));
                    mVisitorModel.setInviteeid(jsonObject1.optString("inviteeid"));

                    mVisitorModel.setEventdate(jsonObject1.optString("eventdate"));
                    mVisitorModel.setEventtime(jsonObject1.optString("eventtime"));

                    visitorModels.add(mVisitorModel);
                }

                visitorListModel.setVisitorModels(visitorModels);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return visitorListModel;
    }
}