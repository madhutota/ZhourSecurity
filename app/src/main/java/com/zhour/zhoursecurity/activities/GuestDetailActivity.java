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
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.Utility;

import java.io.File;
import java.io.FileNotFoundException;

public class GuestDetailActivity extends BaseActivity {

    private EditText et_car_number;

    private Button btn_submit;

    private LinearLayout ll_details;


    private TextView tv_guest;

    private EditText et_guest_name;

    private TextView tv_phone;
    private EditText et_phone;

    private TextView tv_host;

    private EditText et_host_name;

    private TextView tv_flat_no;

    private EditText et_flat_no;

    private Button btn_scan_vehicle;


    private static final int REQUEST_WRITE_PERMISSION = 20;

    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private Uri imageUri;
    private static final int PHOTO_REQUEST = 10;
    private TextRecognizer detector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_detail);
       /* ButterKnife.bind(this);*/
        inItUi();
        if (savedInstanceState != null) {
            imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));
            // tvScanText.setText(savedInstanceState.getString(SAVED_INSTANCE_RESULT));
        }
        detector = new TextRecognizer.Builder(getApplicationContext()).build();
    }

    private void inItUi() {

        et_car_number = (EditText) findViewById(R.id.et_car_number);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        /*SUBMIT CAR NUMBER*/
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(GuestDetailActivity.this, btn_submit);
                if (isValidCardNumber()) {
                    et_car_number.setText("");

                    Animation slide = AnimationUtils.loadAnimation(GuestDetailActivity.this, R.anim.slide);

                    ll_details.startAnimation(slide);
                    ll_details.setVisibility(View.VISIBLE);



                   // ll_details.setVisibility(View.VISIBLE);


                }

            }
        });

        /*SCAN VEHICLE */

        btn_scan_vehicle = (Button) findViewById(R.id.btn_scan_vehicle);
        btn_scan_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(GuestDetailActivity.this, new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_WRITE_PERMISSION);

            }
        });


        ll_details = (LinearLayout) findViewById(R.id.ll_details);

        /*GUSET NAME */
        tv_guest = (TextView) findViewById(R.id.tv_guest);
        tv_guest.setTypeface(Utility.getFontAwesomeWebFont(this));
        et_guest_name = (EditText) findViewById(R.id.et_guest_name);
        et_guest_name.setTypeface(Utility.getFontAwesomeWebFont(this));

        /*PHONE NUMBER*/
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_phone.setTypeface(Utility.getFontAwesomeWebFont(this));
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_phone.setTypeface(Utility.getFontAwesomeWebFont(this));

       /* HOST NAME */
        tv_host = (TextView) findViewById(R.id.tv_host);
        tv_host.setTypeface(Utility.getFontAwesomeWebFont(this));
        et_host_name = (EditText) findViewById(R.id.et_host_name);
        et_host_name.setTypeface(Utility.getFontAwesomeWebFont(this));

        /*FLAT NO*/
        tv_flat_no = (TextView) findViewById(R.id.tv_flat_no);
        tv_flat_no.setTypeface(Utility.getFontAwesomeWebFont(this));
        et_flat_no = (EditText) findViewById(R.id.et_flat_no);
        et_flat_no.setTypeface(Utility.getFontAwesomeWebFont(this));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Utility.setSnackBar(GuestDetailActivity.this, btn_scan_vehicle, "Permission Denied!");
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


    /*VALIDATIONS  */
    private boolean isValidCardNumber() {

        boolean isValid = true;
        if (Utility.isValueNullOrEmpty(et_car_number.getText().toString())) {
            Utility.setSnackBar(this, et_car_number, "Please write code");
            isValid = false;
        } else if (et_car_number.getText().toString().length() != 4) {
            Utility.setSnackBar(this, et_car_number, "Code must be 4 digit");
            isValid = false;
        }
        return isValid;

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
                        Utility.setSnackBar(GuestDetailActivity.this, btn_scan_vehicle, "Scan Failed: Found nothing to scan");
                    } else {
                        tv_guest.setText(blocks);
                    }
                } else {
                    Utility.setSnackBar(GuestDetailActivity.this, btn_scan_vehicle, "Could not set up the detector!");
                }
            } catch (Exception e) {
                Utility.setSnackBar(GuestDetailActivity.this, btn_scan_vehicle, "Failed to load Image");
            }


        }
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
    }*/
}
