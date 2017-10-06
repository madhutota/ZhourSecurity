package com.zhour.zhoursecurity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.Utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuestAndStaffActivity extends BaseActivity {


    @BindView(R.id.ll_staff)
    LinearLayout ll_staff;

    @BindView(R.id.ll_guest)
    LinearLayout ll_guest;

    @BindView(R.id.tv_guest)
    TextView tv_guest;

    @BindView(R.id.tv_staff)
    TextView tv_staff;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String mPurpose;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_and_staff);
        ButterKnife.bind(this);
        setTypeface();

    }

    private void setTypeface() {
        tv_guest.setTypeface(Utility.getFontAwesomeWebFont(this));
        tv_staff.setTypeface(Utility.getFontAwesomeWebFont(this));

        intent = getIntent();
        if (intent.hasExtra(Constants.PURPOSE)) {
            mPurpose = intent.getStringExtra(Constants.PURPOSE);
        }
    }

    /**
     * This method is used to navigate to staff scan
     */
    @OnClick(R.id.ll_staff)
    void getStaff() {
        Intent intent = new Intent(getApplicationContext(), StaffScanActivity.class);
        intent.putExtra(Constants.PURPOSE, mPurpose);
        startActivity(intent);
    }


    /**
     * This method is used to navigate for guest store
     */
    @OnClick(R.id.ll_guest)
    void getGuest() {
        Intent intent = new Intent(getApplicationContext(), GuestDetailActivity.class);
        intent.putExtra(Constants.PURPOSE, mPurpose);
        startActivity(intent);
    }
}
