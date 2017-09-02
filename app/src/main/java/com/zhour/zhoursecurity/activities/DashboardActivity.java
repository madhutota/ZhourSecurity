package com.zhour.zhoursecurity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends BaseActivity {

    @BindView(R.id.tv_in)
    TextView tv_in;
    @BindView(R.id.tv_out)
    TextView tv_out;
    @BindView(R.id.tv_emergency)
    TextView tv_emergency;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi() {
        tv_in.setTypeface(Utility.setRobotoRegular(this));
        tv_out.setTypeface(Utility.setRobotoRegular(this));
        tv_emergency.setTypeface(Utility.setRobotoRegular(this));
    }

    @OnClick(R.id.tv_in)
    void navigateIn() {
        Intent intent = new Intent(getApplicationContext(), GuestAndStaffActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_out)
    void navigateOut() {
        Intent intent = new Intent(getApplicationContext(), GuestAndStaffActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_emergency)
    void navigateEmergency() {
        Intent intent = new Intent(getApplicationContext(), GuestAndStaffActivity.class);
        startActivity(intent);
    }
}
