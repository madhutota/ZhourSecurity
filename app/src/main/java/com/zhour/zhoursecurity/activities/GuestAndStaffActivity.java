package com.zhour.zhoursecurity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.Utility;

public class GuestAndStaffActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_staff;
    private LinearLayout ll_guest;

    private TextView tv_guest;
    private TextView tv_staff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_and_staff);

        ll_staff = (LinearLayout) findViewById(R.id.ll_staff);

        ll_staff.setOnClickListener(this);
        ll_guest = (LinearLayout) findViewById(R.id.ll_guest);
        ll_guest.setOnClickListener(this);

        tv_guest = (TextView) findViewById(R.id.tv_guest);
        tv_guest.setTypeface(Utility.getFontAwesomeWebFont(this));
        tv_staff = (TextView) findViewById(R.id.tv_staff);
        tv_staff.setTypeface(Utility.getFontAwesomeWebFont(this));


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_staff:
                break;
            case R.id.ll_guest:
                Intent intent = new Intent(getApplicationContext(), GuestDetailActivity.class);
                startActivity(intent);
                break;
        }


    }
}
