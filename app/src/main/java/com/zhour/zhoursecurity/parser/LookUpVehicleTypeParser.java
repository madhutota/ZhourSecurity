package com.zhour.zhoursecurity.parser;


import com.zhour.zhoursecurity.models.LookUpEventsTypeModel;
import com.zhour.zhoursecurity.models.LookUpModel;
import com.zhour.zhoursecurity.models.LookUpVehicleTypeModel;
import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.models.SpinnerModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shankar on 7/17/2017.
 */

public class LookUpVehicleTypeParser implements Parser<Model> {
    @Override
    public Model parse(String s) {
        LookUpVehicleTypeModel mLookUpEventsTypeModel = new LookUpVehicleTypeModel();
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("IsError"))
                mLookUpEventsTypeModel.setError(jsonObject.optBoolean("IsError"));
            if (jsonObject.has("Message"))
                mLookUpEventsTypeModel.setMessage(jsonObject.optString("Message"));
            ArrayList<LookUpModel> lookUpModels = new ArrayList<>();
            ArrayList<SpinnerModel> lookUpStrings = new ArrayList<>();
            if (jsonObject.has("Output")) {
                JSONArray jsonArray = jsonObject.optJSONArray("Output");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                    LookUpModel lookUpModel = new LookUpModel();
                    SpinnerModel spinnerModel = new SpinnerModel();
                    lookUpModel.setLookupid(jsonObject1.optString("lookupid"));
                    lookUpModel.setLookupname(jsonObject1.optString("lookupname"));
                    spinnerModel.setTitle(jsonObject1.optString("lookupname"));
                    lookUpModels.add(lookUpModel);
                    lookUpStrings.add(spinnerModel);
                }
                mLookUpEventsTypeModel.setLookUpModels(lookUpModels);
                mLookUpEventsTypeModel.setLookupNames(lookUpStrings);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mLookUpEventsTypeModel;
    }
}