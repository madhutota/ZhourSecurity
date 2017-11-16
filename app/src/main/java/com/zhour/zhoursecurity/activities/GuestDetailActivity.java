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
import com.zhour.zhoursecurity.models.VisitorListModel;
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

    @BindView(R.id.enter_flat_number)
    EditText enter_flat_number;

    @BindView(R.id.enter_guest_name)
    EditText enter_guest_name;

    @BindView(R.id.enter_guest_contact)
    EditText enter_guest_contact;

    @BindView(R.id.enter_resident_name)
    EditText enter_resident_name;

    @BindView(R.id.enter_resident_contact)
    EditText enter_resident_contact;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private VisitorListModel visitorModel;
    private String mPurpose;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_detail);
        ButterKnife.bind(this);
        intent = getIntent();
        if (intent.hasExtra(Constants.PURPOSE)) {
            mPurpose = intent.getStringExtra(Constants.PURPOSE);
        }

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

        if (mPurpose.equalsIgnoreCase(Constants.IN)) {
            try {
                LinkedHashMap linkedHashMap = new LinkedHashMap();

                linkedHashMap.put("communityid", "12"/*Utility.getSharedPrefStringData(this, Constants.COMMUNITY_ID)*/);
                //linkedHashMap.put("passcode", "716536");
                linkedHashMap.put("passcode", "");
                linkedHashMap.put("flatnumber", enter_flat_number.getText().toString());
                linkedHashMap.put("guestname", enter_guest_name.getText().toString());
                linkedHashMap.put("guestcontact", enter_guest_contact.getText().toString());
                linkedHashMap.put("residentname", enter_resident_name.getText().toString());
                linkedHashMap.put("residentcontact", enter_resident_contact.getText().toString());

                VisitorParser visitorParser = new VisitorParser();
                ServerJSONAsyncTask serverJSONAsyncTask = new ServerJSONAsyncTask(
                        this, Utility.getResourcesString(this, R.string.please_wait), true,
                        APIConstants.GET_INVITE_INFO, linkedHashMap,
                        APIConstants.REQUEST_TYPE.POST, this, visitorParser);
                Utility.execute(serverJSONAsyncTask);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                linkedHashMap.put("vehiclenumber", "716536");
                VisitorParser visitorParser = new VisitorParser();
                ServerJSONAsyncTask serverJSONAsyncTask = new ServerJSONAsyncTask(
                        this, Utility.getResourcesString(this, R.string.please_wait), true,
                        APIConstants.DEL_VISITOR, linkedHashMap,
                        APIConstants.REQUEST_TYPE.POST, this, visitorParser);
                Utility.execute(serverJSONAsyncTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * VALIDATIONS
     */
    private boolean isValidDigitFields() {
        boolean isValid = true;
        if (Utility.isValueNullOrEmpty(enter_pass_code.getText().toString()) &&
                Utility.isValueNullOrEmpty(enter_guest_name.getText().toString()) &&
                Utility.isValueNullOrEmpty(enter_guest_contact.getText().toString()) &&
                Utility.isValueNullOrEmpty(enter_resident_name.getText().toString()) &&
                Utility.isValueNullOrEmpty(enter_resident_contact.getText().toString()) &&
                Utility.isValueNullOrEmpty(enter_flat_number.getText().toString())) {
            Utility.setSnackBar(this, enter_pass_code, "Please enter data");
            isValid = false;
        }
        return isValid;
    }


    @Override
    public void onComplete(Model model) {
        if (model != null) {
            if (model instanceof VisitorListModel) {
                visitorModel = (VisitorListModel) model;
                Intent detailsIntent = new Intent(GuestDetailActivity.this, InviteSearchActivity.class);
                detailsIntent.putExtra(Constants.VISITOR_MODEL, visitorModel);
                startActivity(detailsIntent);
            }
        }
    }

}
