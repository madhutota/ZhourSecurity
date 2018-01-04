package com.zhour.zhoursecurity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.Utils.Utility;
import com.zhour.zhoursecurity.activities.DashboardActivity;
import com.zhour.zhoursecurity.activities.GuestAndStaffActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Shankar on 1/4/2018.
 */

public class HomeFragment extends Fragment {
    public static final String TAG = HomeFragment.class.getSimpleName();
    private DashboardActivity mParent;
    private View view;

    @BindView(R.id.ll_move_in)
    LinearLayout ll_move_in;

    @BindView(R.id.ll_move_out)
    LinearLayout ll_move_out;

    @BindView(R.id.tv_move_in)
    TextView tv_move_in;

    @BindView(R.id.tv_move_out)
    TextView tv_move_out;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParent = (DashboardActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        initui();
        return view;
    }

    /**
     * This method is used for initialization
     */
    private void initui() {
        tv_move_in.setTypeface(Utility.setRobotoRegular(mParent));
        tv_move_out.setTypeface(Utility.setRobotoRegular(mParent));
    }

    @OnClick(R.id.ll_move_in)
    void navigateIn() {
        Intent intent = new Intent(mParent, GuestAndStaffActivity.class);
        intent.putExtra(Constants.PURPOSE, Constants.IN);
        startActivity(intent);
    }

    @OnClick(R.id.ll_move_out)
    void navigateOut() {
        Intent intent = new Intent(mParent, GuestAndStaffActivity.class);
        intent.putExtra(Constants.PURPOSE, Constants.OUT);
        startActivity(intent);
    }

}
