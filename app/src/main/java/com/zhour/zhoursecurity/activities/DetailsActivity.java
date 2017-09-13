package com.zhour.zhoursecurity.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.APIConstants;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.Utils.Utility;
import com.zhour.zhoursecurity.adapters.SpinnerAdapter;
import com.zhour.zhoursecurity.asynctask.IAsyncCaller;
import com.zhour.zhoursecurity.asynctask.ServerJSONAsyncTask;
import com.zhour.zhoursecurity.models.LookUpVehicleTypeModel;
import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.models.SpinnerModel;
import com.zhour.zhoursecurity.models.VisitorModel;
import com.zhour.zhoursecurity.parser.LookUpVehicleTypeParser;
import com.zhour.zhoursecurity.parser.VisitorParser;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Manikanta on 9/11/2017.
 */

public class DetailsActivity extends BaseActivity implements IAsyncCaller {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.et_visitor_name)
    EditText et_visitor_name;

    @BindView(R.id.et_visitor_contact)
    EditText et_visitor_contact;

    @BindView(R.id.et_vehicle_type)
    EditText et_vehicle_type;

    @BindView(R.id.et_resident_name)
    EditText et_resident_name;

    @BindView(R.id.et_event_type)
    EditText et_event_type;

    @BindView(R.id.et_count)
    EditText et_count;

    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_vehicle_number)
    EditText et_vehicle_number;
    @BindView(R.id.et_resident_contact)
    EditText et_resident_contact;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    private LookUpVehicleTypeModel lookUpVehicleTypeModel;
    private VisitorModel visitorModel;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        initUI();

    }

    /**
     * METHOD TO INITIALIZE THE UI
     */
    private void initUI() {
        et_visitor_name.setTypeface(Utility.setRobotoRegular(this));
        et_visitor_contact.setTypeface(Utility.setRobotoRegular(this));
        et_vehicle_type.setTypeface(Utility.setRobotoRegular(this));
        et_resident_name.setTypeface(Utility.setRobotoRegular(this));
        et_event_type.setTypeface(Utility.setRobotoRegular(this));
        et_count.setTypeface(Utility.setRobotoRegular(this));
        et_email.setTypeface(Utility.setRobotoRegular(this));
        et_vehicle_number.setTypeface(Utility.setRobotoRegular(this));
        et_resident_contact.setTypeface(Utility.setRobotoRegular(this));
        btn_submit.setTypeface(Utility.setRobotoRegular(this));

        intent = getIntent();
        if (intent.hasExtra(Constants.VISITOR_MODEL)) {
            visitorModel = (VisitorModel) intent.getSerializableExtra(Constants.VISITOR_MODEL);
            setVisitorModelData();
        }

        getVehicleTypes("Vehicle%20Types");
    }

    /**
     * This method is used to set the data
     */
    private void setVisitorModelData() {
        et_visitor_name.setText(visitorModel.getVisitorname());
        et_visitor_contact.setText(visitorModel.getContactnumber());
        et_resident_name.setText(visitorModel.getVisitorname());
        et_event_type.setText(visitorModel.getInvitetype());
    }

    /**
     * ON CLICK FOR VEHICLE TYPE
     */
    @OnClick(R.id.et_vehicle_type)
    void vehicleDialog() {
        showSpinnerDialog(this, Utility.getResourcesString(this, R.string.vehicle_type),
                lookUpVehicleTypeModel.getLookupNames(), 2);
    }

    /**
     * ON CLICK FOR SUBMIT FORM
     */
    @OnClick(R.id.btn_submit)
    void submitDetails() {
        if (isValid()) {
            try {
                LinkedHashMap linkedHashMap = new LinkedHashMap();

                linkedHashMap.put("visitorid", "0");
                linkedHashMap.put("visitorname", et_visitor_name.getText().toString());
                linkedHashMap.put("visitorcontact", et_visitor_contact.getText().toString());
                linkedHashMap.put("emailid", et_email.getText().toString());
                linkedHashMap.put("visitorcount", et_count.getText().toString());
                linkedHashMap.put("vehicletypeid", getVehicleTypeId(et_vehicle_type.getText().toString()));
                linkedHashMap.put("vehiclenumber", et_vehicle_number.getText().toString());
                linkedHashMap.put("eventtypeid", visitorModel.getInvitetypeid());
                linkedHashMap.put("residentid", visitorModel.getResidentid());
                linkedHashMap.put("communityid", "12"/*Utility.getSharedPrefStringData(this, Constants.COMMUNITY_ID)*/);

                VisitorParser visitorParser = new VisitorParser();
                ServerJSONAsyncTask serverJSONAsyncTask = new ServerJSONAsyncTask(
                        this, Utility.getResourcesString(this, R.string.please_wait), true,
                        APIConstants.CREATE_OR_UPDATE_VISITOR, linkedHashMap,
                        APIConstants.REQUEST_TYPE.POST, this, visitorParser);
                Utility.execute(serverJSONAsyncTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to get the vehicle type id
     */
    private String getVehicleTypeId(String s) {
        String mVehicleTypeId = "";
        for (int i = 0; i < lookUpVehicleTypeModel.getLookupNames().size(); i++) {
            if (lookUpVehicleTypeModel.getLookUpModels().get(i).getLookupname().equals(s)) {
                mVehicleTypeId = lookUpVehicleTypeModel.getLookUpModels().get(i).getLookupid();
            }
        }
        return mVehicleTypeId;
    }

    /**
     * METHOD FOR VALIDATIONS
     */
    private boolean isValid() {
        boolean isTrue = true;
        if (Utility.isValueNullOrEmpty(et_visitor_name.getText().toString())) {
            isTrue = false;
            Utility.setSnackBar(this, et_visitor_name, Utility.getResourcesString(this, R.string.please_enter_visitor_name));
            et_visitor_name.setFocusable(true);
        } else if (Utility.isValueNullOrEmpty(et_visitor_contact.getText().toString())) {
            isTrue = false;
            Utility.setSnackBar(this, et_visitor_contact, Utility.getResourcesString(this, R.string.please_enter_visitor_contact));
            et_visitor_contact.setFocusable(true);
        } else if (Utility.isValueNullOrEmpty(et_vehicle_type.getText().toString())) {
            isTrue = false;
            Utility.setSnackBar(this, et_vehicle_type, Utility.getResourcesString(this, R.string.please_select_vehicle_type));

        } else if (Utility.isValueNullOrEmpty(et_resident_name.getText().toString())) {
            isTrue = false;
            Utility.setSnackBar(this, et_resident_name, Utility.getResourcesString(this, R.string.please_select_resident_name));

        } else if (Utility.isValueNullOrEmpty(et_count.getText().toString())) {
            isTrue = false;
            Utility.setSnackBar(this, et_count, Utility.getResourcesString(this, R.string.please_enter_count));
            et_count.setFocusable(true);

        } else if (Utility.isValueNullOrEmpty(et_email.getText().toString())) {
            isTrue = false;
            Utility.setSnackBar(this, et_email, Utility.getResourcesString(this, R.string.please_enter_email));
            et_email.setFocusable(true);

        } else if (Utility.isValueNullOrEmpty(et_vehicle_number.getText().toString())) {
            isTrue = false;
            Utility.setSnackBar(this, et_vehicle_number, Utility.getResourcesString(this, R.string.please_enter_vehicle_number));
            et_vehicle_number.setFocusable(true);

        } else if (Utility.isValueNullOrEmpty(et_resident_contact.getText().toString())) {
            isTrue = false;
            Utility.setSnackBar(this, et_resident_contact, Utility.getResourcesString(this, R.string.please_enter_resident_contact));
            et_resident_contact.setFocusable(true);

        }
        return isTrue;
    }


    /**
     * API CALL TO GET THE VEHICLE TYPES
     */
    private void getVehicleTypes(String invite_types) {
        try {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("entityname", invite_types);
            LookUpVehicleTypeParser mLookUpVehicleTypeParser = new LookUpVehicleTypeParser();
            ServerJSONAsyncTask serverJSONAsyncTask = new ServerJSONAsyncTask(
                    this, Utility.getResourcesString(this, R.string.please_wait), true,
                    APIConstants.GET_LOOKUP_DATA_BY_ENTITY_NAME, linkedHashMap,
                    APIConstants.REQUEST_TYPE.POST, this, mLookUpVehicleTypeParser);
            Utility.execute(serverJSONAsyncTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * API RESPONSE MODEL METHOD
     */
    @Override
    public void onComplete(Model model) {
        if (model != null) {
            if (model instanceof LookUpVehicleTypeModel) {
                lookUpVehicleTypeModel = (LookUpVehicleTypeModel) model;
            }
        }

    }

    /**
     * METHOD TO SHOW SPINNER DIALOG
     */
    public void showSpinnerDialog(final Context context, final String title,
                                  ArrayList<SpinnerModel> itemsList, final int id) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        /*CUSTOM TITLE*/
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_alert_dialog_title, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_alert_dialog_title);
        RelativeLayout dialog_back_ground = (RelativeLayout) view.findViewById(R.id.dialog_back_ground);
        dialog_back_ground.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        tv_title.setText(title);
        tv_title.setTextColor(context.getResources().getColor(R.color.white));
        builderSingle.setCustomTitle(view);


        final SpinnerAdapter adapter = new SpinnerAdapter(context, itemsList);
        builderSingle.setAdapter(adapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpinnerModel mData = (SpinnerModel) adapter.getItem(which);
                        String text = mData.getTitle();
                        switch (id) {
                            case 2:
                                et_vehicle_type.setText(text);
                                break;
                        }
                    }
                });
        builderSingle.show();
    }
}
