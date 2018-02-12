package com.zhour.zhoursecurity.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.zhour.zhoursecurity.asynctask.IAsyncCaller;
import com.zhour.zhoursecurity.asynctask.ServerJSONAsyncTask;
import com.zhour.zhoursecurity.aynctaskold.ServerIntractorAsync;
import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.models.VisitorListModel;
import com.zhour.zhoursecurity.parser.VisitorParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuestDetailActivity extends BaseActivity implements IAsyncCaller {

    @BindView(R.id.et_flat_no)
    EditText et_flat_no;

    @BindView(R.id.tv_flat)
    TextView tv_flat;

    @BindView(R.id.et_guest_no)
    EditText et_guest_no;

    @BindView(R.id.tv_phone)
    TextView tv_phone;


    @BindView(R.id.et_resident_number)
    EditText et_resident_number;

    @BindView(R.id.tv_resident_phno)
    TextView tv_resident_phno;


    @BindView(R.id.et_guest_name)
    EditText et_guest_name;

    @BindView(R.id.tv_user_guest)
    TextView tv_user_guest;


    @BindView(R.id.et_pass)
    EditText et_pass;

    @BindView(R.id.tv_pass_icon)
    TextView tv_pass_icon;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    private VisitorListModel visitorModel;
    private String mPurpose;
    private Intent intent;

    private AlertDialog alertDialog;
    EditText et_vehicle_scan_text;
    public String vehicleNumberText;
    Uri imageUri;

    View mDialogView;


    private static final int PHOTO_REQUEST = 10;
    private TextRecognizer detector;

    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";

    String outputText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_detail);
        ButterKnife.bind(this);
        intent = getIntent();
        if (intent.hasExtra(Constants.PURPOSE)) {
            mPurpose = intent.getStringExtra(Constants.PURPOSE);
        }

        if (savedInstanceState != null) {
            imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));
            et_vehicle_scan_text.setText(savedInstanceState.getString(SAVED_INSTANCE_RESULT));
        }
        detector = new TextRecognizer.Builder(getApplicationContext()).build();


        setTypefaces();

    }

    private void setTypefaces() {
        /*REMOVE EDIT TEXT  ON SCROLL */
        et_flat_no.setMovementMethod(null);

        tv_flat.setTypeface(Utility.getFontAwesomeWebFont(this));
        tv_phone.setTypeface(Utility.getFontAwesomeWebFont(this));
        tv_resident_phno.setTypeface(Utility.getFontAwesomeWebFont(this));
        tv_user_guest.setTypeface(Utility.getFontAwesomeWebFont(this));
        tv_pass_icon.setTypeface(Utility.getFontAwesomeWebFont(this));


    }

    /**
     * METHOD TO SUBMIT DATA
     */
    @OnClick(R.id.btn_submit)
    void submitCarDetails() {
        Utility.setSharedPrefStringData(this,Constants.CAR_NUMBER,"");
        Utility.hideSoftKeyboard(GuestDetailActivity.this, btn_submit);

        if (isValidDigitFields()) {
            et_pass.setText("");
           // showPopUp();

             apiCallToGetDetails();
        }
    }

    private void showPopUp() {

        // askPermissionForCamera();


        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        mDialogView = inflater.inflate(R.layout.visitors_dialog, null);
        mAlertDialogBuilder.setView(mDialogView);
        Button btn_get_details = mDialogView.findViewById(R.id.btn_get_details);
        btn_get_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }

            }
        });


        et_vehicle_scan_text = mDialogView.findViewById(R.id.et_vehicle_number);
        et_vehicle_scan_text.setMovementMethod(null);

        TextView cameraIcon = mDialogView.findViewById(R.id.tv_scanner);
        cameraIcon.setTypeface(Utility.getFontAwesomeWebFont(this));
        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermissionForCamera();


            }
        });

        alertDialog = mAlertDialogBuilder.create();
       /* try {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();

        }*/
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(true);
        alertDialog.show();


    }

    private void askPermissionForCamera() {
        ActivityCompat.requestPermissions(this, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

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
                linkedHashMap.put("passcode", "0");
                linkedHashMap.put("flatnumber", et_flat_no.getText().toString());
                linkedHashMap.put("guestname", "-" /*et_guest_name.getText().toString()*/);
                linkedHashMap.put("guestcontact", "0" /*et_guest_no.getText().toString()*/);
                linkedHashMap.put("residentname", "-");
                linkedHashMap.put("residentcontact", "9014332627" /*et_resident_number.getText().toString()*/);

                VisitorParser visitorParser = new VisitorParser();
                ServerIntractorAsync serverJSONAsyncTask = new ServerIntractorAsync(
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
        if (Utility.isValueNullOrEmpty(et_flat_no.getText().toString()) &&
                Utility.isValueNullOrEmpty(et_guest_no.getText().toString()) &&
                Utility.isValueNullOrEmpty(et_resident_number.getText().toString()) &&
                Utility.isValueNullOrEmpty(et_guest_name.getText().toString()) &&
                Utility.isValueNullOrEmpty(et_pass.getText().toString())) {
            Utility.setSnackBar(this, et_pass, "Please enter data");
            isValid = false;
        }
        return isValid;
    }


    @Override
    public void onComplete(Model model) {
        if (model != null) {
            if (model instanceof VisitorListModel) {
                visitorModel = (VisitorListModel) model;
                Intent detailsIntent = new Intent(GuestDetailActivity.this, InviteSearchNewActivity.class);
                detailsIntent.putExtra(Constants.VISITOR_MODEL, visitorModel);
                startActivity(detailsIntent);
            }
        }
    }

    public void clearData() {
        et_flat_no.setText(null);
        et_pass.setText(null);
        et_resident_number.setText(null);
        et_guest_name.setText(null);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (imageUri != null) {
            outState.putString(Constants.SAVED_INSTANCE_URI, imageUri.toString());
            outState.putString(Constants.SAVED_INSTANCE_RESULT, vehicleNumberText);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Utility.showToastMessage(this, "Permission Denied!");
                }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
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
                        et_vehicle_scan_text.setText("Scan Failed: Found nothing to scan");
                    } else {
                        et_vehicle_scan_text.setText(String.valueOf(blocks));
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
                    outputText = "Could not set up the detector!";
                }
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
                Log.e("Tag", e.toString());
            }
        }
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "picture.jpg");
        imageUri = FileProvider.getUriForFile(GuestDetailActivity.this,
                GuestDetailActivity.this.getApplicationContext().getPackageName() + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_REQUEST);
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



}
