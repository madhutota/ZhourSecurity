package com.zhour.zhoursecurity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhour.zhoursecurity.R;
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

    }

    @OnClick(R.id.ll_staff)
    void getStaff() {

    }

    @OnClick(R.id.ll_guest)
    void getGuest() {
        Intent intent = new Intent(getApplicationContext(), GuestDetailActivity.class);
        startActivity(intent);

    }


}
