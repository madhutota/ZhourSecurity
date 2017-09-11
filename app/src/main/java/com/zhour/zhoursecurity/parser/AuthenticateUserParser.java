package com.zhour.zhoursecurity.parser;

import com.zhour.zhoursecurity.models.AuthenticateUserModel;
import com.zhour.zhoursecurity.models.CommunityUserModel;
import com.zhour.zhoursecurity.models.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shankar on 7/17/2017.
 */

public class AuthenticateUserParser implements Parser<Model> {



    @Override
    public Model parse(String s) {
        AuthenticateUserModel mAuthenticateUserModel = new AuthenticateUserModel();
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("IsError"))
                mAuthenticateUserModel.setError(jsonObject.optBoolean("IsError"));
            if (jsonObject.has("Message"))
                mAuthenticateUserModel.setMessage(jsonObject.optString("Message"));
            if (jsonObject.has("Output")) {
                JSONArray jsonArray = jsonObject.optJSONArray("Output");
                JSONObject dataObject = jsonArray.optJSONObject(0);
                mAuthenticateUserModel.setUserid(dataObject.optString("userid"));
                mAuthenticateUserModel.setUsername(dataObject.optString("username"));
                mAuthenticateUserModel.setContactnumber(dataObject.optString("contactnumber"));
                mAuthenticateUserModel.setLastlogin(dataObject.optString("lastlogin"));
                mAuthenticateUserModel.setSesid(dataObject.optString("sesid"));
                mAuthenticateUserModel.setToken(dataObject.optString("token"));

                JSONArray jsonDataArray = dataObject.optJSONArray("communities");


                ArrayList<CommunityUserModel> communitiesList = new ArrayList<>();

                for (int j = 0; j < jsonDataArray.length(); j++) {
                    CommunityUserModel mCommunityUserModel = new CommunityUserModel();
                    JSONObject communityObject = jsonDataArray.optJSONObject(j);
                    mCommunityUserModel.setUserid(communityObject.optString("userid"));
                    mCommunityUserModel.setRoleid(communityObject.optString("userid"));
                    mCommunityUserModel.setCommunityid(communityObject.optString("communityid"));
                    mCommunityUserModel.setCommunityname(communityObject.optString("communityname"));
                    mCommunityUserModel.setResidentid(communityObject.optString("residentid"));
                    mCommunityUserModel.setRolename(communityObject.optString("rolename"));
                    communitiesList.add(mCommunityUserModel);

                }

                mAuthenticateUserModel.setCommunitiesList(communitiesList);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mAuthenticateUserModel;
    }
}