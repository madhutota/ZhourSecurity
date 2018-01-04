package com.zhour.zhoursecurity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.APIConstants;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.Utils.Utility;
import com.zhour.zhoursecurity.asynctask.IAsyncCaller;
import com.zhour.zhoursecurity.aynctaskold.ServerIntractorAsync;
import com.zhour.zhoursecurity.fragments.HomeFragment;
import com.zhour.zhoursecurity.models.LogoutModel;
import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.parser.LogoutParser;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends BaseActivity implements IAsyncCaller{

    private DrawerLayout drawer_layout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.menu_icon)
    ImageView menu_icon;

    @BindView(R.id.iv_home)
    ImageView iv_home;

    @BindView(R.id.tv_home)
    TextView tv_home;

    @BindView(R.id.iv_logout)
    ImageView iv_logout;

    @BindView(R.id.tv_logout)
    TextView tv_logout;


    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_logout)
    LinearLayout ll_logout;

    @BindView(R.id.tv_username)
    TextView tv_username;

    @BindView(R.id.tv_phone)
    TextView tv_phone;

    private LogoutModel logoutModel;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_new);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        ll_home.performClick();

        /*USER NAME */
        tv_username.setTypeface(Utility.setRobotoRegular(this));
        String UserName = Utility.getSharedPrefStringData(this, Constants.USER_NAME);
        tv_username.setText(UserName);

         /*USER NAME */
        tv_phone.setTypeface(Utility.setRobotoRegular(this));
        String phoneNumber = Utility.getSharedPrefStringData(this, Constants.CONTACT_NUMBER);
        tv_phone.setText(phoneNumber);
    }


    @OnClick(R.id.menu_icon)
    public void toggelMenu() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            drawer_layout.openDrawer(GravityCompat.START);
        }
    }


    @OnClick(R.id.ll_home)
    public void navigatingHome() {
        iv_home.setImageDrawable(Utility.getDrawable(this, R.drawable.ic_home));
        tv_home.setTextColor(Utility.getColor(this, R.color.yello_text));

        iv_logout.setImageDrawable(Utility.getDrawable(this, R.drawable.ic_logout));
        tv_logout.setTextColor(Utility.getColor(this, R.color.colorWhite));

        drawer_layout.closeDrawer(GravityCompat.START);
        Bundle bundle = new Bundle();
        Utility.navigateDashBoardFragment(new HomeFragment(), HomeFragment.TAG, bundle, this);

    }

    @OnClick(R.id.ll_logout)
    public void navigateToLogout() {

        String userID = Utility.getSharedPrefStringData(this, Constants.USER_ID);

        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("userid", userID);

        LogoutParser logoutParser = new LogoutParser();
        ServerIntractorAsync serverJSONAsyncTask = new ServerIntractorAsync(
                this, Utility.getResourcesString(this, R.string.please_wait), true,
                APIConstants.LOGOUT, linkedHashMap,
                APIConstants.REQUEST_TYPE.POST, this, logoutParser);
        Utility.execute(serverJSONAsyncTask);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onComplete(Model model) {
        if (model != null) {
            if (model instanceof LogoutModel) {
                logoutModel = (LogoutModel) model;
                logout();
            }
        }
    }

    private void logout() {
        Intent intent = new Intent(this, SignInActivity.class);
        Utility.setSharedPrefStringData(this, Constants.TOKEN, "");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
