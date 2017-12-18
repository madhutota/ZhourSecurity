package com.zhour.zhoursecurity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.APIConstants;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.Utils.Utility;
import com.zhour.zhoursecurity.asynctask.IAsyncCaller;
import com.zhour.zhoursecurity.asynctask.ServerJSONAsyncTask;
import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.models.RFIDModel;
import com.zhour.zhoursecurity.parser.RFIDParser;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.supercharge.shimmerlayout.ShimmerLayout;

/**
 * Created by shankar on 8/4/2017.
 */

public class StaffScanActivity extends BaseActivity implements IAsyncCaller {

    @BindView(R.id.et_id)
    EditText et_id;

    @BindView(R.id.iv_maid)
    ImageView iv_maid;

    @BindView(R.id.tv_maid_name)
    TextView tv_maid_name;

    @BindView(R.id.tv_bar_code)
    TextView tv_bar_code;

    @BindView(R.id.shimmer_text)
    ShimmerLayout shimmer_text;


    private String mPurpose;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_scan);
        ButterKnife.bind(this);
        shimmer_text.startShimmerAnimation();
        setTypeFaces();


        intent = getIntent();
        if (intent.hasExtra(Constants.PURPOSE)) {
            mPurpose = intent.getStringExtra(Constants.PURPOSE);
        }

        et_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //  Utility.showLog("beforeTextChanged", "" + s);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              /*  Utility.showLog("onTextChanged", "" + s);
                Toast.makeText(StaffScanActivity.this, "" + s, Toast.LENGTH_SHORT).show();*/

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utility.showLog("afterTextChanged", "" + s);
                if (s.length() > 2) {
                    shimmer_text.stopShimmerAnimation();

                    // Toast.makeText(StaffScanActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                    sendDataToServer();
                }


            }
        });
    }

    private void setTypeFaces() {
        tv_bar_code.setTypeface(Utility.getFontAwesomeWebFont(this));
        et_id.setTypeface(Utility.getFontAwesomeWebFont(this));
    }

    void sendDataToServer() {
        if (isValidFields()) {
            if (mPurpose.equalsIgnoreCase(Constants.OUT)) {
                try {
                    LinkedHashMap linkedHashMap = new LinkedHashMap();
                    linkedHashMap.put("staffid", "1");
                    RFIDParser rfidParser = new RFIDParser();
                    ServerJSONAsyncTask serverJSONAsyncTask = new ServerJSONAsyncTask(
                            this, Utility.getResourcesString(this, R.string.please_wait), true,
                            APIConstants.DEL_STAFF_VISIT, linkedHashMap,
                            APIConstants.REQUEST_TYPE.POST, this, rfidParser);
                    Utility.execute(serverJSONAsyncTask);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    LinkedHashMap linkedHashMap = new LinkedHashMap();
                    linkedHashMap.put("visitid", "0");
                    //linkedHashMap.put("staffid", et_id.getText().toString());
                    linkedHashMap.put("staffid", "1");
                    linkedHashMap.put("communityid", "12");
                    //linkedHashMap.put("communityid", Utility.getSharedPrefStringData(this, Constants.COMMUNITY_ID));
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
                tv_maid_name.setText("Lakshmi");

                // ImageLoaderLibrary.load(R.drawable.house_maid_new, iv_maid, this);

                iv_maid.setImageDrawable(Utility.getDrawable(this, R.drawable.house_maid_new));


                // Utility.showToastMessage(StaffScanActivity.this, "" + mRFIDModel.getOutput());
                et_id.setText("");
                shimmer_text.startShimmerAnimation();
            }
        }
    }
}
