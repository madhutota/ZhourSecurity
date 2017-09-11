package com.zhour.zhoursecurity.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.APIConstants;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.Utils.Utility;
import com.zhour.zhoursecurity.asynctask.IAsyncCaller;
import com.zhour.zhoursecurity.asynctask.ServerJSONAsyncTask;
import com.zhour.zhoursecurity.models.AuthenticateUserModel;
import com.zhour.zhoursecurity.models.CommunityUserModel;
import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.parser.AuthenticateUserParser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity implements IAsyncCaller {

    public static final String TAG = SignInActivity.class.getSimpleName();

    @BindView(R.id.tv_user_login)
    TextView tv_user_login;

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setTypeFace();
    }

    private void setTypeFace() {
        context = SignInActivity.this;
        tv_user_login.setTypeface(Utility.setRobotoRegular(this));
        btn_login.setTypeface(Utility.setRobotoRegular(this));
        et_username.setTypeface(Utility.setLucidaSansItalic(this));
        et_password.setTypeface(Utility.setLucidaSansItalic(this));

    }


    @OnClick(R.id.btn_login)
    public void loginButtonFunction() {
        try {
            LinkedHashMap linkedHashMap = new LinkedHashMap();

            linkedHashMap.put("contactnumber", et_username.getText().toString());
            linkedHashMap.put("pwd", et_password.getText().toString());

            AuthenticateUserParser authenticateUserParser = new AuthenticateUserParser();
            ServerJSONAsyncTask serverJSONAsyncTask = new ServerJSONAsyncTask(
                    this, Utility.getResourcesString(this, R.string.please_wait), true,
                    APIConstants.AUTHENTICATE_USER, linkedHashMap,
                    APIConstants.REQUEST_TYPE.POST, this, authenticateUserParser);
            Utility.execute(serverJSONAsyncTask);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onComplete(Model model) {
        if (model != null) {
            if (model instanceof AuthenticateUserModel) {
                AuthenticateUserModel authenticateUserModel = (AuthenticateUserModel) model;

                Utility.setSharedPrefStringData(context, Constants.USER_NAME, authenticateUserModel.getUsername());
                Utility.setSharedPrefStringData(context, Constants.CONTACT_NUMBER, authenticateUserModel.getContactnumber());


                if (!authenticateUserModel.isError()) {
                    if (authenticateUserModel != null && authenticateUserModel.getCommunitiesList().size() > 1) {

                        ArrayList<CommunityUserModel> userList = authenticateUserModel.getCommunitiesList();
                        Intent intent = new Intent(context, DashboardActivity.class);
                        intent.putExtra(Constants.COMMUNITY_LIST, userList);
                        intent.putExtra(Constants.TOKEN, authenticateUserModel.getToken());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    } else {
                        Utility.setSharedPrefStringData(context, Constants.USER_ID, authenticateUserModel.getUserid());
                        Utility.setSharedPrefStringData(context, Constants.SESSION_ID, authenticateUserModel.getSesid());
                        Utility.setSharedPrefStringData(context, Constants.TOKEN, authenticateUserModel.getToken());

                        Utility.setSharedPrefStringData(getApplicationContext(), Constants.ROLE_ID, authenticateUserModel.getCommunitiesList().get(0).getRoleid());
                        Utility.setSharedPrefStringData(getApplicationContext(), Constants.ROLE_NAME, authenticateUserModel.getCommunitiesList().get(0).getRolename());
                        Utility.setSharedPrefStringData(getApplicationContext(), Constants.COMMUNITY_ID, authenticateUserModel.getCommunitiesList().get(0).getCommunityid());
                        Utility.setSharedPrefStringData(getApplicationContext(), Constants.COMMUNITY_NAME, authenticateUserModel.getCommunitiesList().get(0).getCommunityname());
                        Utility.setSharedPrefStringData(getApplicationContext(), Constants.RESIDENT_ID, authenticateUserModel.getCommunitiesList().get(0).getResidentid());


                        Intent intent = new Intent(context, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }


                }
            }
        }
    }
}
