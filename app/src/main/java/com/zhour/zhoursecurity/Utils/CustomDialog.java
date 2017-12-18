package com.zhour.zhoursecurity.Utils;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.activities.BaseActivity;


public class CustomDialog {


    /*SHOW PROGRESS BAR*/
    public static Dialog showProgressDialog(BaseActivity baseActivity, boolean cancelable) {

        Dialog mDialog = new Dialog(baseActivity, R.style.pb_dialog_style);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View layout = View.inflate(baseActivity, R.layout.progressbar_custom, null);
        mDialog.setContentView(layout);

        TextView tvProgressMessage = (TextView) layout.findViewById(R.id.tvProgressMessage);
        tvProgressMessage.setText("Loading....");
        tvProgressMessage.setVisibility(View.GONE);

        if (baseActivity.progressDialog != null) {
            baseActivity.progressDialog.dismiss();
            baseActivity.progressDialog = null;
        }

        baseActivity.progressDialog = mDialog;

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();

        return mDialog;
    }

    /*HIDE THE PROGRESS DIALOG*/
    public static void hideProgressBar(BaseActivity parent) {
        if (parent.progressDialog != null)
            parent.progressDialog.dismiss();
    }









}
