package com.zhour.zhoursecurity.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.APIConstants;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.Utils.Utility;
import com.zhour.zhoursecurity.adapters.InviteSearchListAdapter;
import com.zhour.zhoursecurity.adapters.SpinnerAdapter;
import com.zhour.zhoursecurity.asynctask.IAsyncCaller;
import com.zhour.zhoursecurity.aynctaskold.ServerIntractorAsync;
import com.zhour.zhoursecurity.models.LookUpVehicleTypeModel;
import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.models.SpinnerModel;
import com.zhour.zhoursecurity.models.SuccessModel;
import com.zhour.zhoursecurity.models.VisitorListModel;
import com.zhour.zhoursecurity.models.VisitorModel;
import com.zhour.zhoursecurity.parser.LookUpVehicleTypeParser;
import com.zhour.zhoursecurity.parser.SuccessParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zhour.zhoursecurity.Utils.Constants.PHOTO_REQUEST;
import static com.zhour.zhoursecurity.activities.InviteSearchActivity.imageUri;

public class InviteSearchNewActivity extends BaseActivity implements IAsyncCaller {

    @BindView(R.id.list_expand__invite_search)
    ListView list_expand__invite_search;

    @BindView(R.id.tv_resident_plotno)
    TextView tv_resident_plotno;

    @BindView(R.id.tv_resident_name)
    TextView tv_resident_name;

    private Intent intent;
    private ArrayList<VisitorModel> visitersHeader;
    private VisitorListModel visitorListModel;
    private InviteSearchListAdapter inviteSearchListAdapter;

    private TextRecognizer detector;
    private LookUpVehicleTypeModel lookUpVehicleTypeModel;
    public static String et_vehicle_scan_text;

    private EditText et_vehicle_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_search_list);
        ButterKnife.bind(this);
        detector = new TextRecognizer.Builder(this).build();
        getIntentData();
    }

    private void getIntentData() {
        intent = getIntent();
        if (intent.hasExtra(Constants.VISITOR_MODEL)) {
            visitorListModel = (VisitorListModel) intent.getSerializableExtra(Constants.VISITOR_MODEL);
        }
        visitersHeader = visitorListModel.getVisitorModels();
        getResidentDetails(visitorListModel);
        list_expand__invite_search.setAdapter(new InviteSearchListAdapter(this, visitersHeader));

        list_expand__invite_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialogForEnterData(visitersHeader.get(i));
            }
        });

        getVehicleTypes("Vehicle Types");
    }


    private void getVehicleTypes(String invite_types) {
        try {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("entityname", invite_types);
            LookUpVehicleTypeParser mLookUpVehicleTypeParser = new LookUpVehicleTypeParser();
            ServerIntractorAsync serverJSONAsyncTask = new ServerIntractorAsync(
                    this, Utility.getResourcesString(this, R.string.please_wait), true,
                    APIConstants.GET_LOOKUP_DATA_BY_ENTITY_NAME, linkedHashMap,
                    APIConstants.REQUEST_TYPE.POST, this, mLookUpVehicleTypeParser);
            Utility.execute(serverJSONAsyncTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getResidentDetails(VisitorListModel visitorListModel) {

        if (visitorListModel.getVisitorModels() != null && visitorListModel.getVisitorModels().size() > 0) {
            String residentName = visitorListModel.getVisitorModels().get(0).getResidentname();
            String flatNo = visitorListModel.getVisitorModels().get(0).getFlat();

            tv_resident_name.setTypeface(Utility.setRobotoRegular(this));
            tv_resident_plotno.setTypeface(Utility.setRobotoRegular(this));

            tv_resident_name.setText(residentName);
            tv_resident_plotno.setText("Flat No : " + flatNo);
        }
    }

    private void showDialogForEnterData(final VisitorModel visitorModel) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.list_child);

        final TextView tv_close = dialog.findViewById(R.id.tv_close);
        tv_close.setTag(dialog);
        tv_close.setTypeface(Utility.getMaterialIconsRegular(this));

        final EditText et_visitor_contact = dialog.findViewById(R.id.et_visitor_contact);
        final EditText et_visitor_name = dialog.findViewById(R.id.et_visitor_name);
        et_visitor_contact.setTag(dialog);
        et_visitor_name.setTag(dialog);
        et_visitor_contact.setTypeface(Utility.setRobotoRegular(this));
        et_visitor_name.setTypeface(Utility.setRobotoRegular(this));
        et_visitor_contact.setText(visitorModel.getContactnumber());
        et_visitor_name.setText(visitorModel.getVisitorname());

        et_vehicle_num = dialog.findViewById(R.id.et_vehicle_num);
        et_vehicle_num.setTypeface(Utility.setRobotoRegular(this));

        final EditText et_vehicle_type = dialog.findViewById(R.id.et_vehicle_type);
        et_vehicle_type.setTag(dialog);
        et_vehicle_type.setTypeface(Utility.setRobotoRegular(this));
        //et_vehicle_type.setText(visitorModel.getContactnumber());
        et_vehicle_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpinnerDialog(InviteSearchNewActivity.this, Utility.getResourcesString(InviteSearchNewActivity.this, R.string.vehicle_type),
                        lookUpVehicleTypeModel.getLookupNames(), 2, et_vehicle_type);
            }
        });

        TextView tv_scanner = dialog.findViewById(R.id.tv_scanner);
        tv_scanner.setTypeface(Utility.getFontAwesomeWebFont(this));
        tv_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(InviteSearchNewActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(InviteSearchNewActivity.this,
                                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(InviteSearchNewActivity.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.CAMERA},
                            Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    takePicture();
                }
            }
        });

        Button btn_submit = dialog.findViewById(R.id.btn_submit);
        btn_submit.setTypeface(Utility.setRobotoRegular(this));
        btn_submit.setTag(dialog);
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
                        linkedHashMap.put("inviteid", visitorModel.getInviteid());
                        linkedHashMap.put("inviteeid", visitorModel.getInviteeid());
                        linkedHashMap.put("tagid", "12312");
                        linkedHashMap.put("vstm", Utility.getDateMM());
                        linkedHashMap.put("communityid", "12"/*Utility.getSharedPrefStringData(this, Constants.COMMUNITY_ID)*/);

                        SuccessParser successParser = new SuccessParser();
                        ServerIntractorAsync serverJSONAsyncTask = new ServerIntractorAsync(InviteSearchNewActivity.this,
                                Utility.getResourcesString(InviteSearchNewActivity.this, R.string.please_wait), true,
                                APIConstants.CREATE_OR_UPDATE_VISITOR, linkedHashMap,
                                APIConstants.REQUEST_TYPE.POST,
                                InviteSearchNewActivity.this, successParser);
                        Utility.execute(serverJSONAsyncTask);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                }

            }
        });

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private boolean isValid(EditText contactNumber, EditText vehicleType) {

        boolean isTrue = true;
        if (Utility.isValueNullOrEmpty(contactNumber.getText().toString())) {
            isTrue = false;
            Utility.setSnackBar(this, contactNumber, Utility.getResourcesString(this, R.string.please_enter_visitor_contact));
            contactNumber.setFocusable(true);

        } else if (Utility.isValueNullOrEmpty(vehicleType.getText().toString())) {
            isTrue = false;
            Utility.setSnackBar(this, vehicleType, Utility.getResourcesString(this, R.string.please_select_vehicle_type));

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
        imageUri = FileProvider.getUriForFile(this,
                this.getApplicationContext().getPackageName() + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_REQUEST);
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

    @Override
    public void onComplete(Model model) {
        if (model instanceof LookUpVehicleTypeModel) {
            lookUpVehicleTypeModel = (LookUpVehicleTypeModel) model;
        } else if (model instanceof SuccessModel) {
            SuccessModel successModel = (SuccessModel) model;
            Utility.showToastMessage(this, successModel.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            Utility.showLog("From", "Adapter");
            launchMediaScanIntent();
            try {
                Bitmap bitmap = decodeBitmapUri(this, imageUri);
                if (detector.isOperational() && bitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> textBlocks = detector.detect(frame);
                    String blocks = "";
                    String lines = "";
                    String words = "";
                    for (int index = 0; index < textBlocks.size(); index++) {
                        //extract scanned text blocks here
                        TextBlock tBlock = textBlocks.valueAt(index);
                        blocks = blocks + tBlock.getValue() + "\n" + "\n";
                        for (Text line : tBlock.getComponents()) {
                            //extract scanned text lines here
                            lines = lines + line.getValue() + "\n";
                            for (Text element : line.getComponents()) {
                                //extract scanned text words here
                                words = words + element.getValue() + ", ";
                            }
                        }
                    }
                    if (textBlocks.size() == 0) {
                        et_vehicle_scan_text = "Scan Failed: Found nothing to scan";
                    } else {

                        et_vehicle_scan_text = String.valueOf(blocks);
                        Utility.setSharedPrefStringData(this, Constants.CAR_NUMBER, blocks);
                        //Toast.makeText(this, "" + blocks, Toast.LENGTH_SHORT).show();
                        et_vehicle_num.setText(blocks);
                        // outputText.setText("Number : ");
                        //tvScanText.setText(tvScanText.getText() + blocks + "\n");
                      /*  Intent intent = new Intent();
                        blocks.replace("\n", " ");
                        intent.putExtra("id", blocks);
                        setResult(Constants.UNIQUE_CODE, intent);
                        finish();*/
                        /*tvScanText.setText(tvScanText.getText() + "---------" + "\n");
                        tvScanText.setText(tvScanText.getText() + "Lines: " + "\n");
                        tvScanText.setText(tvScanText.getText() + lines + "\n");
                        tvScanText.setText(tvScanText.getText() + "---------" + "\n");
                        tvScanText.setText(tvScanText.getText() + "Words: " + "\n");
                        tvScanText.setText(tvScanText.getText() + words + "\n");
                        tvScanText.setText(tvScanText.getText() + "---------" + "\n");*/
                    }
                } else {
                    et_vehicle_scan_text = "Could not set up the detector!";
                }
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
                Log.e("Tag", e.toString());
            }
        }
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Utility.showToastMessage(this, "Permission Denied!");
                }
        }
    }

}
