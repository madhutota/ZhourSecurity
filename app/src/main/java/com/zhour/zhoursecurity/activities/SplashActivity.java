package com.zhour.zhoursecurity.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.Utils.Utility;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler mSplashHandler = new Handler();
        Runnable action = new Runnable() {
            @Override
            public void run() {

                if (!Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(SplashActivity.this, Constants.TOKEN))) {
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    /*navigateToSignIn();*/
                }
            }
        };
        mSplashHandler.postDelayed(action, Constants.SPLASH_TIME_OUT);


    }
}
