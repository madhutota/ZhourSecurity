package com.zhour.zhoursecurity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.APIConstants;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.Utils.Utility;
import com.zhour.zhoursecurity.asynctask.IAsyncCaller;
import com.zhour.zhoursecurity.asynctask.ServerJSONAsyncTask;
import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.models.VisitorModel;
import com.zhour.zhoursecurity.parser.VisitorParser;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuestDetailActivity extends BaseActivity implements IAsyncCaller {

    @BindView(R.id.enter_pass_code)
    EditText enter_pass_code;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private VisitorModel visitorModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_detail);
        ButterKnife.bind(this);
    }

    /**
     * METHOD TO SUBMIT DATA
     */
    @OnClick(R.id.btn_submit)
    void submitCarDetails() {
        Utility.hideSoftKeyboard(GuestDetailActivity.this, btn_submit);

        if (isValidDigitFields()) {
            enter_pass_code.setText("");
            apiCallToGetDetails();
        }
    }

    /**
     * API CALL TO GET THE DETAILS
     */
    private void apiCallToGetDetails() {
        try {
            LinkedHashMap linkedHashMap = new LinkedHashMap();

            linkedHashMap.put("communityid", "12"/*Utility.getSharedPrefStringData(this, Constants.COMMUNITY_ID)*/);
            linkedHashMap.put("passcode", "716536");

            VisitorParser visitorParser = new VisitorParser();
            ServerJSONAsyncTask serverJSONAsyncTask = new ServerJSONAsyncTask(
                    this, Utility.getResourcesString(this, R.string.please_wait), true,
                    APIConstants.GET_INVITE_INFO, linkedHashMap,
                    APIConstants.REQUEST_TYPE.POST, this, visitorParser);
            Utility.execute(serverJSONAsyncTask);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * VALIDATIONS
     */
    private boolean isValidDigitFields() {
        boolean isValid = true;
        if (Utility.isValueNullOrEmpty(enter_pass_code.getText().toString())) {
            Utility.setSnackBar(this, enter_pass_code, "Please write code");
            isValid = false;
        } else if (enter_pass_code.getText().toString().length() < 4) {
            Utility.setSnackBar(this, enter_pass_code, "Code must be 4 digit");
            isValid = false;
        }
        return isValid;
    }


    @Override
    public void onComplete(Model model) {
        if (model != null) {
            if (model instanceof VisitorModel) {
                visitorModel = (VisitorModel) model;
                Intent detailsIntent = new Intent(GuestDetailActivity.this, DetailsActivity.class);
                detailsIntent.putExtra(Constants.VISITOR_MODEL, visitorModel);
                startActivity(detailsIntent);
            }
        }
    }

}
