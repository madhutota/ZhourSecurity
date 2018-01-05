package com.zhour.zhoursecurity.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.APIConstants;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.Utils.Utility;
import com.zhour.zhoursecurity.activities.InviteSearchActivity;
import com.zhour.zhoursecurity.asynctask.IAsyncCaller;
import com.zhour.zhoursecurity.asynctask.ServerJSONAsyncTask;
import com.zhour.zhoursecurity.aynctaskold.ServerIntractorAsync;
import com.zhour.zhoursecurity.interfaces.IupdateScanText;
import com.zhour.zhoursecurity.models.LookUpVehicleTypeModel;
import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.models.SpinnerModel;
import com.zhour.zhoursecurity.models.SuccessModel;
import com.zhour.zhoursecurity.models.VisitorModel;
import com.zhour.zhoursecurity.parser.LookUpVehicleTypeParser;
import com.zhour.zhoursecurity.parser.SuccessParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.zhour.zhoursecurity.Utils.Constants.PHOTO_REQUEST;
import static com.zhour.zhoursecurity.activities.InviteSearchActivity.imageUri;

/**
 * Created by madhu on 17-Dec-17.
 */

public class InviteSearchExpandableAdapter extends BaseExpandableListAdapter implements IAsyncCaller, IupdateScanText {

    private InviteSearchActivity inviteSearchActivity;

    private Context ctx;
    private List<VisitorModel> visitorModels;
    private LayoutInflater layoutInflater;
    private HashMap<String, List<VisitorModel>> listHashMap;
    private LookUpVehicleTypeModel lookUpVehicleTypeModel;
    public static IupdateScanText iupdateScanText;

    public static IupdateScanText getIupdateScanText() {
        return iupdateScanText;
    }


    public InviteSearchExpandableAdapter(Context ctx, List<VisitorModel> visitorModels, HashMap<String, List<VisitorModel>> listHashMap, InviteSearchActivity inviteSearchActivity) {
        this.ctx = ctx;
        this.visitorModels = visitorModels;
        this.listHashMap = listHashMap;
        iupdateScanText = this;
        this.inviteSearchActivity = inviteSearchActivity;
    }

    @Override
    public int getGroupCount() {
        return visitorModels.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listHashMap.get(this.visitorModels.get(groupPosition).getVisitorname()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return visitorModels.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(visitorModels.get(groupPosition).getVisitorname()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        VisitorModel visitorModel = (VisitorModel) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_group, null);
            getVehicleTypes("Vehicle Types");

        }
        TextView textgrp = convertView.findViewById(R.id.lblListHeader);
        textgrp.setText(visitorModel.getVisitorname());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        final VisitorModel visitorModel = (VisitorModel) getChild(groupPosition, childPosition);

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.list_child, null);

        }
        final EditText et_visitor_contact = view.findViewById(R.id.et_visitor_contact);
        et_visitor_contact.setTag(view);
        et_visitor_contact.setTypeface(Utility.setRobotoRegular(ctx));
        et_visitor_contact.setText(visitorModel.getContactnumber());

        final EditText et_vehicle_type = view.findViewById(R.id.et_vehicle_type);
        et_vehicle_type.setTag(view);
        et_vehicle_type.setTypeface(Utility.setRobotoRegular(ctx));
        //et_vehicle_type.setText(visitorModel.getContactnumber());
        et_vehicle_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSpinnerDialog(ctx, Utility.getResourcesString(ctx, R.string.vehicle_type),
                        lookUpVehicleTypeModel.getLookupNames(), 2, et_vehicle_type);

            }
        });

        final EditText et_vehicle_num = view.findViewById(R.id.et_vehicle_num);

        String carNumber = Utility.getSharedPrefStringData(ctx, Constants.CAR_NUMBER);
        if (!Utility.isValueNullOrEmpty(carNumber))
            et_vehicle_num.setText(carNumber);


        TextView tv_scanner = view.findViewById(R.id.tv_scanner);
        tv_scanner.setTypeface(Utility.getFontAwesomeWebFont(ctx));
        tv_scanner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(inviteSearchActivity,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(inviteSearchActivity,
                                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(inviteSearchActivity,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.CAMERA},
                            Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    takePicture();
                }

            }
        });


        Button btn_submit = view.findViewById(R.id.btn_submit);
        btn_submit.setTypeface(Utility.setRobotoRegular(ctx));
        btn_submit.setTag(view);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValid(et_visitor_contact, et_vehicle_type)) {
                    try {
                        LinkedHashMap linkedHashMap = new LinkedHashMap();

                        linkedHashMap.put("visitorid", "0");
                        linkedHashMap.put("visitorname", "ABC");
                        linkedHashMap.put("visitorcontact", et_visitor_contact.getText().toString());
                        linkedHashMap.put("emailid", "-");
                        linkedHashMap.put("visitorcount", "3");
                        linkedHashMap.put("vehicletypeid", getVehicleTypeId(et_vehicle_type.getText().toString()));
                        linkedHashMap.put("vehiclenumber", "AP29AG7410");
                        linkedHashMap.put("eventtypeid", visitorModel.getInvitetypeid());
                        linkedHashMap.put("residentid", visitorModel.getResidentid());
                        linkedHashMap.put("communityid", "12"/*Utility.getSharedPrefStringData(this, Constants.COMMUNITY_ID)*/);

                        SuccessParser successParser = new SuccessParser();
                        ServerIntractorAsync serverJSONAsyncTask = new ServerIntractorAsync(ctx, Utility.getResourcesString(ctx, R.string.please_wait), true,
                                APIConstants.CREATE_OR_UPDATE_VISITOR, linkedHashMap,
                                APIConstants.REQUEST_TYPE.POST, InviteSearchExpandableAdapter.this, successParser);
                        Utility.execute(serverJSONAsyncTask);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });


        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * METHOD TO SHOW SPINNER DIALOG
     */
    public void showSpinnerDialog(final Context context, final String title,
                                  ArrayList<SpinnerModel> itemsList, final int id, final EditText et_vehicle_type) {

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

    private void getVehicleTypes(String invite_types) {

        try {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("entityname", invite_types);
            LookUpVehicleTypeParser mLookUpVehicleTypeParser = new LookUpVehicleTypeParser();
            ServerIntractorAsync serverJSONAsyncTask = new ServerIntractorAsync(
                    ctx, Utility.getResourcesString(this.ctx, R.string.please_wait), true,
                    APIConstants.GET_LOOKUP_DATA_BY_ENTITY_NAME, linkedHashMap,
                    APIConstants.REQUEST_TYPE.POST, this, mLookUpVehicleTypeParser);
            Utility.execute(serverJSONAsyncTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(Model model) {
        if (model instanceof LookUpVehicleTypeModel) {
            lookUpVehicleTypeModel = (LookUpVehicleTypeModel) model;

        } else if (model instanceof SuccessModel) {
            SuccessModel successModel = (SuccessModel) model;
            Utility.showToastMessage(ctx, successModel.getMessage());

        }

    }

    private boolean isValid(EditText contactNumber, EditText vehicleType) {

        boolean isTrue = true;
        if (Utility.isValueNullOrEmpty(contactNumber.getText().toString())) {
            isTrue = false;
            Utility.setSnackBar(inviteSearchActivity, contactNumber, Utility.getResourcesString(ctx, R.string.please_enter_visitor_contact));
            contactNumber.setFocusable(true);

        } else if (Utility.isValueNullOrEmpty(vehicleType.getText().toString())) {
            isTrue = false;
            Utility.setSnackBar(inviteSearchActivity, vehicleType, Utility.getResourcesString(ctx, R.string.please_select_vehicle_type));

        }
        return isTrue;

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

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "picture.jpg");
        imageUri = FileProvider.getUriForFile(ctx,
                ctx.getApplicationContext().getPackageName() + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        inviteSearchActivity.startActivityForResult(intent, PHOTO_REQUEST);

    }


    @Override
    public void updateScanTest(String text) {


    }

}
