package com.zhour.zhoursecurity.parser;


import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.models.StaffDetailsInfoModel;
import com.zhour.zhoursecurity.models.StaffDetailsModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by shankar on 7/17/2017.
 */

public class StaffDeatilParser implements Parser<Model> {
    @Override
    public Model parse(String s) {
        StaffDetailsInfoModel mStaffDetailsInfoModel = new StaffDetailsInfoModel();
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("IsError"))
                mStaffDetailsInfoModel.setError(jsonObject.optBoolean("IsError"));
            if (jsonObject.has("Message"))
                mStaffDetailsInfoModel.setMessage(jsonObject.optString("Message"));
            StaffDetailsModel staffDetailsModel = new StaffDetailsModel();
            if (jsonObject.has("Output")) {
                JSONArray jsonArray = jsonObject.optJSONArray("Output");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.optJSONObject(0);
                    staffDetailsModel.setId(jsonObject1.optString("id"));
                    staffDetailsModel.setStaff_id(jsonObject1.optString("staffid"));
                    staffDetailsModel.setStaff_name(jsonObject1.optString("staffname"));
                    staffDetailsModel.setDept_id(jsonObject1.optString("deptid"));
                    staffDetailsModel.setDept_name(jsonObject1.optString("deptname"));
                    staffDetailsModel.setPhoto(jsonObject1.optString("photo"));
                }
                mStaffDetailsInfoModel.setStaffDetailsModel(staffDetailsModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mStaffDetailsInfoModel;
    }
}