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

public class DashboardActivity extends BaseActivity {

    @BindView(R.id.ll_move_in)
    LinearLayout ll_move_in;

    @BindView(R.id.ll_move_out)
    LinearLayout ll_move_out;

    @BindView(R.id.tv_move_in)
    TextView tv_move_in;

    @BindView(R.id.tv_move_out)
    TextView tv_move_out;

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
        tv_move_in.setTypeface(Utility.setRobotoRegular(this));
        tv_move_out.setTypeface(Utility.setRobotoRegular(this));

    }

    @OnClick(R.id.ll_move_in)
    void navigateIn() {
        Intent intent = new Intent(getApplicationContext(), GuestAndStaffActivity.class);
        intent.putExtra(Constants.PURPOSE, Constants.IN);
        startActivity(intent);
    }

    @OnClick(R.id.ll_move_out)
    void navigateOut() {
        Intent intent = new Intent(getApplicationContext(), GuestAndStaffActivity.class);
        intent.putExtra(Constants.PURPOSE, Constants.OUT);
        startActivity(intent);
    }

}
