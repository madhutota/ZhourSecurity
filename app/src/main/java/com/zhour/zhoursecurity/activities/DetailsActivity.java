package com.zhour.zhoursecurity.activities;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.Utility;

import butterknife.BindView;

/**
 * Created by Manikanta on 9/11/2017.
 */

public class DetailsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.et_pass_code)
    EditText et_pass_code;

    @BindView(R.id.et_visitor_name)
    EditText et_visitor_name;

    @BindView(R.id.et_visitor_contact)
    EditText et_visitor_contact;

    @BindView(R.id.vehicle_type)
    EditText vehicle_type;

    @BindView(R.id.et_resident_name)
    EditText et_resident_name;

    @BindView(R.id.et_event_type)
    EditText et_event_type;

    @BindView(R.id.et_count)
    EditText et_count;

    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_vehicle_number)
    EditText et_vehicle_number;
    @BindView(R.id.et_resident_contact)
    EditText et_resident_contact;
    @BindView(R.id.btn_submit)
    EditText btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initUI();

    }

    private void initUI() {
        et_pass_code.setTypeface(Utility.setLucidaSansItalic(this));
        et_visitor_name.setTypeface(Utility.setLucidaSansItalic(this));
        et_visitor_contact.setTypeface(Utility.setLucidaSansItalic(this));
        vehicle_type.setTypeface(Utility.setLucidaSansItalic(this));
        et_resident_name.setTypeface(Utility.setLucidaSansItalic(this));
        et_event_type.setTypeface(Utility.setLucidaSansItalic(this));
        et_count.setTypeface(Utility.setLucidaSansItalic(this));
        et_email.setTypeface(Utility.setLucidaSansItalic(this));
        et_vehicle_number.setTypeface(Utility.setLucidaSansItalic(this));
        et_resident_contact.setTypeface(Utility.setLucidaSansItalic(this));
        btn_submit.setTypeface(Utility.setRobotoRegular(this));
    }
}
