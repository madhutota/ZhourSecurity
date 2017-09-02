package com.zhour.zhoursecurity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.Utility;

public class DashboardActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_in;
    private RelativeLayout rl_out;

    private TextView tv_in;
    private TextView tv_out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        inItUi();
    }

    private void inItUi() {

        rl_in = (RelativeLayout) findViewById(R.id.rl_in);
        rl_in.setOnClickListener(this);
        rl_out = (RelativeLayout) findViewById(R.id.rl_out);
        rl_out.setOnClickListener(this);
        tv_in = (TextView) findViewById(R.id.tv_in);
        tv_in.setTypeface(Utility.getFontAwesomeWebFont(this));
        tv_out = (TextView) findViewById(R.id.tv_out);
        tv_out.setTypeface(Utility.getFontAwesomeWebFont(this));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_in:
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.slide_left2right);
                Intent intent = new Intent(getApplicationContext(), GuestAndStaffActivity.class);
                startActivity(intent);
              //  Toast.makeText(this, "In", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_out:
                Toast.makeText(this, "Out", Toast.LENGTH_SHORT).show();
                break;
        }


    }
}
