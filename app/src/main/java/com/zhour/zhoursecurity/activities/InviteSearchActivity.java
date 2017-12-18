package com.zhour.zhoursecurity.activities;

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
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.Utils.Utility;
import com.zhour.zhoursecurity.adapters.InviteSearchAdapter;
import com.zhour.zhoursecurity.adapters.InviteSearchExpandableAdapter;
import com.zhour.zhoursecurity.models.LookUpVehicleTypeModel;
import com.zhour.zhoursecurity.models.VisitorListModel;
import com.zhour.zhoursecurity.models.VisitorModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zhour.zhoursecurity.Utils.Constants.PHOTO_REQUEST;

public class InviteSearchActivity extends BaseActivity /*implements AdapterView.OnItemClickListener*/ {


    @BindView(R.id.list_expand__invite_search)
    ExpandableListView list_expand__invite_search;

    @BindView(R.id.tv_resident_plotno)
    TextView tv_resident_plotno;


    @BindView(R.id.tv_resident_name)
    TextView tv_resident_name;

    private Intent intent;
    private VisitorListModel visitorListModel;
    private InviteSearchAdapter inviteSearchAdapter;
    public static String et_vehicle_scan_text;


    private InviteSearchExpandableAdapter inviteSearchExpandableAdapter;
    List<VisitorModel> visiterModelList;


    private HashMap<String, List<VisitorModel>> listHashMap;
    private List<VisitorModel> visitersHeader;

    private LookUpVehicleTypeModel lookUpVehicleTypeModel;
    public static Uri imageUri;
    TextRecognizer detector;

    private void getExpandableListData() {

        for (int i = 0; i < visitorListModel.getVisitorModels().size(); i++) {

            VisitorModel visitorModel = visitorListModel.getVisitorModels().get(i);
            visiterModelList = new ArrayList<>();
            visiterModelList.add(visitorModel);
            listHashMap.put(visitorModel.getVisitorname(), visiterModelList);

        }
        inviteSearchExpandableAdapter = new InviteSearchExpandableAdapter(this, visitorListModel.getVisitorModels(), listHashMap, InviteSearchActivity.this);
        list_expand__invite_search.setAdapter(inviteSearchExpandableAdapter);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_search);
        ButterKnife.bind(this);
        getIntentData();
        detector = new TextRecognizer.Builder(this).build();


        list_expand__invite_search.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        visiterModelList.get(groupPosition)
                                + " : "
                                + listHashMap.get(
                                visiterModelList.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

    }


    /**
     * This method is used to get the intent data
     */
    private void getIntentData() {
        listHashMap = new HashMap<String, List<VisitorModel>>();
        intent = getIntent();
        if (intent.hasExtra(Constants.VISITOR_MODEL)) {
            visitorListModel = (VisitorListModel) intent.getSerializableExtra(Constants.VISITOR_MODEL);
        }
        visitersHeader = visitorListModel.getVisitorModels();
        getResidentDetails(visitorListModel);
        getExpandableListData();


    }

    private void getListData() {
        if (visitorListModel != null && visitorListModel.getVisitorModels() != null && visitorListModel.getVisitorModels().size() > 0) {
            inviteSearchAdapter = new InviteSearchAdapter(InviteSearchActivity.this, visitorListModel.getVisitorModels());
            list_expand__invite_search.setAdapter(inviteSearchAdapter);
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
                        Utility.setSharedPrefStringData(this,Constants.CAR_NUMBER,blocks);
                        inviteSearchExpandableAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "" + blocks, Toast.LENGTH_SHORT).show();

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

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "picture.jpg");
        imageUri = FileProvider.getUriForFile(this,
                this.getApplicationContext().getPackageName() + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_REQUEST);

    }







    /*@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent detailsIntent = new Intent(InviteSearchActivity.this, DetailsActivity.class);
        detailsIntent.putExtra(Constants.VISITOR_MODEL, visitorListModel.getVisitorModels().get(i));
        startActivity(detailsIntent);
    }*/
}
