package com.zhour.zhoursecurity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.APIConstants;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.Utils.Utility;
import com.zhour.zhoursecurity.asynctask.IAsyncCaller;
import com.zhour.zhoursecurity.asynctask.ServerJSONAsyncTask;
import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.models.RFIDModel;
import com.zhour.zhoursecurity.parser.RFIDParser;

import org.json.JSONArray;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shankar on 8/4/2017.
 */

public class StaffScanActivity extends BaseActivity implements IAsyncCaller {

    @BindView(R.id.et_id)
    EditText et_id;

    private String mPurpose;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_scan);
        ButterKnife.bind(this);
        intent = getIntent();
        if (intent.hasExtra(Constants.PURPOSE)) {
            mPurpose = intent.getStringExtra(Constants.PURPOSE);
        }

        et_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 9)
                    sendDataToServer();
            }
        });
    }

    void sendDataToServer() {
        if (isValidFields()) {
            try {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                JSONArray jsonArray = new JSONArray();
                linkedHashMap.put("visitid", "0");
                linkedHashMap.put("staffid", et_id.getText().toString());
                //linkedHashMap.put("communityid", "12");
                linkedHashMap.put("communityid", Utility.getSharedPrefStringData(this, Constants.COMMUNITY_ID));
                RFIDParser rfidParser = new RFIDParser();
                ServerJSONAsyncTask serverJSONAsyncTask = new ServerJSONAsyncTask(
                        this, Utility.getResourcesString(this, R.string.please_wait), true,
                        APIConstants.CREATE_OR_UPDATE_STAFF_VISIT, linkedHashMap,
                        APIConstants.REQUEST_TYPE.POST, this, rfidParser);
                Utility.execute(serverJSONAsyncTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isValidFields() {
        boolean isValidated = false;
        if (Utility.isValueNullOrEmpty(et_id.getText().toString().trim())) {
            Utility.setSnackBar(StaffScanActivity.this, et_id, "Please scan your card first");
            et_id.requestFocus();
        } else {
            isValidated = true;
        }
        return isValidated;
    }

    @Override
    public void onComplete(Model model) {
        if (model != null) {
            if (model instanceof RFIDModel) {
                RFIDModel mRFIDModel = (RFIDModel) model;
                // Utility.showToastMessage(StaffScanActivity.this, "Student Name: " + mRFIDModel.getStudentName());
            }
        }
    }
}
