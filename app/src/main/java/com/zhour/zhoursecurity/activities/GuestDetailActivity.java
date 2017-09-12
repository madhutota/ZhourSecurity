package com.zhour.zhoursecurity.activities;

import android.Manifest;
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
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.parser.AuthenticateUserParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuestDetailActivity extends BaseActivity implements IAsyncCaller{

    @BindView(R.id.et_vehicle_number)
    EditText et_vehicle_number;

    @BindView(R.id.btn_submit)
    Button btn_submit;

   /* @BindView(R.id.ll_details)
    LinearLayout ll_details;*/

    /*@BindView(R.id.tv_guest)
    TextView tv_guest;*/


    /*@BindView(R.id.tv_phone)
    TextView tv_phone;*/

    /*@BindView(R.id.et_phone)
    EditText et_phone;*/

   /* @BindView(R.id.tv_host)
    TextView tv_host;

    @BindView(R.id.et_host_name)
    EditText et_host_name;

    @BindView(R.id.tv_flat_no)
    TextView tv_flat_no;

    @BindView(R.id.et_flat_no)
    EditText et_flat_no;*/

   /* @BindView(R.id.btn_scan_vehicle)
    Button btn_scan_vehicle;*/


    private static final int REQUEST_WRITE_PERMISSION = 20;

    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private Uri imageUri;
    private static final int PHOTO_REQUEST = 10;
    private TextRecognizer detector;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_detail);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));
            et_vehicle_number.setText(savedInstanceState.getString(SAVED_INSTANCE_RESULT));
        }
        detector = new TextRecognizer.Builder(getApplicationContext()).build();
    }

    /**
     * METHOD TO SUBMIT DATA
     * */
    @OnClick(R.id.btn_submit)
    void submitCarDetails() {
        Utility.hideSoftKeyboard(GuestDetailActivity.this, btn_submit);

        if (isValidFields() && isValidDigitFields()) {
            et_vehicle_number.setText("");
            apiCallToGetDetails();

            Intent detailsIntent = new Intent(GuestDetailActivity.this,DetailsActivity.class);
            startActivity(detailsIntent);
        }
    }

    /**
     * API CALL TO GET THE DETAILS
     * */
    private void apiCallToGetDetails() {
        try {
            LinkedHashMap linkedHashMap = new LinkedHashMap();

            linkedHashMap.put("communityid", "12"/*Utility.getSharedPrefStringData(this, Constants.COMMUNITY_ID)*/);
            linkedHashMap.put("passcode", "716536");

            AuthenticateUserParser authenticateUserParser = new AuthenticateUserParser();
            ServerJSONAsyncTask serverJSONAsyncTask = new ServerJSONAsyncTask(
                    this, Utility.getResourcesString(this, R.string.please_wait), true,
                    APIConstants.GET_INVITEINFO, linkedHashMap,
                    APIConstants.REQUEST_TYPE.POST, this, authenticateUserParser);
            Utility.execute(serverJSONAsyncTask);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*VALIDATIONS */

    private boolean isValidFields() {
        boolean isValidated = false;
        if (Utility.isValueNullOrEmpty(et_vehicle_number.getText().toString().trim())) {
            Utility.setSnackBar(this, et_vehicle_number, "Please enter vehicle number");
            et_vehicle_number.requestFocus();
        } else {
            isValidated = true;
        }
        return isValidated;
    }

    private boolean isValidDigitFields() {
        boolean isValid = true;
        if (Utility.isValueNullOrEmpty(et_vehicle_number.getText().toString())) {
            Utility.setSnackBar(this, et_vehicle_number, "Please write code");
            isValid = false;
        } else if (et_vehicle_number.getText().toString().length() < 4) {
            Utility.setSnackBar(this, et_vehicle_number, "Code must be 4 digit");
            isValid = false;
        }
        return isValid;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
/*                    Utility.setSnackBar(GuestDetailActivity.this, btn_scan_vehicle, "Permission Denied!");*/
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                       /* Utility.setSnackBar(GuestDetailActivity.this, btn_scan_vehicle, "Scan Failed: Found nothing to scan");*/
                    } else {

                        /*Intent intent = new Intent();
                        intent.putExtra("id", blocks);
                        startActivityForResult(intent, Constants.UNIQUE_CODE);*/


                        et_vehicle_number.setText("" + blocks);
                    }
                } else {
                   /* Utility.setSnackBar(GuestDetailActivity.this, btn_scan_vehicle, "Could not set up the detector!");*/
                }
            } catch (Exception e) {
               /* Utility.setSnackBar(GuestDetailActivity.this, btn_scan_vehicle, "Failed to load Image");*/
            }


        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (imageUri != null) {
            outState.putString(SAVED_INSTANCE_URI, imageUri.toString());
            outState.putString(SAVED_INSTANCE_RESULT, et_vehicle_number.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onComplete(Model model) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
