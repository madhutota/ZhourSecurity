package com.zhour.zhoursecurity.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.activities.BaseActivity;

/**
 * Created by madhu on 29-Aug-17.
 */

public class Utility {

    public static final int NO_INTERNET_CONNECTION = 1;
    private static final int NO_GPS_ACCESS = 2;

    private static final int CONNECTION_TIMEOUT = 25000;


    /**
     * GET SHARED PREFERENCES STRING DATA
     */
    public static String getSharedPrefStringData(Context context, String key) {

        try {
            SharedPreferences userAcountPreference = context
                    .getSharedPreferences(Constants.APP_PREF,
                            Context.MODE_PRIVATE);
            return userAcountPreference.getString(key, "");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return "";

    }


    /**
     * ASSIGN THE COLOR
     **/
    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23)
            return ContextCompat.getColor(context, id);
        else
            return context.getResources().getColor(id);
    }

    /**
     * HIDE THE KEYBOARD FOR FRAGMENT
     **/
    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }


    /**
     * GET Resources String
     *
     * @param context Context of the class
     * @param id      Id of the resource
     * @return String
     */
    public static String getResourcesString(Context context, int id) {
        String value = null;
        if (context != null && id != -1) {
            value = context.getResources().getString(id);
        }
        return value;
    }


    /**
     * Check the value is null or empty
     *
     * @param value Value of that string
     * @return Boolean returns the value true or false
     */
    public static boolean isValueNullOrEmpty(String value) {
        boolean isValue = false;
        if (value == null || value.equals("") || value.equals("0.0")
                || value.equals("null") || value.trim().length() == 0) {
            isValue = true;
        }
        return isValue;
    }

    /**
     * Shows toast message
     *
     * @param context Context of the class
     * @param message What message you have to show
     */
    public static void showToastMessage(Context context, String message) {
        try {
            if (!isValueNullOrEmpty(message) && context != null) {
                final Toast toast = Toast.makeText(
                        context.getApplicationContext(), message,
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To Print logcat this method is used.
     *
     * @param logMsg Purpose of the log
     * @param logVal What you want to print
     */
    public static void showLog(String logMsg, String logVal) {
        try {
            if (Constants.logMessageOnOrOff) {
                if (!isValueNullOrEmpty(logMsg) && !isValueNullOrEmpty(logVal)) {
                    Log.e(logMsg, logVal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .getState() == NetworkInfo.State.CONNECTED
                    || connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .getState() == NetworkInfo.State.CONNECTING) {
                return true;
            } else return connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .getState() == NetworkInfo.State.CONNECTED
                    || connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .getState() == NetworkInfo.State.CONNECTING;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * MATERIAL ICONS REGULAR TYPEFACE
     * This method is used to set the icons in Material Icons Regular
     **/
    public static Typeface getMaterialIconsRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/matireal_icons_regular.ttf");
    }

    /**
     * FONT AWESOME WEB FONT TYPEFACE
     * This method is used to set the icons in font awesome
     **/
    public static Typeface getFontAwesomeWebFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");
    }


    public static void setSnackBar(AppCompatActivity parent, View mView, String message) {
        SnackBar snackBar = new SnackBar();
        snackBar.view(mView)
                .customActionFont(getFontAwesomeWebFont(parent))
                .customTitleFont(getFontAwesomeWebFont(parent))
                .text(message, "OK", 2)
                .textColors(Color.WHITE, getColor(parent, R.color.black))
                .backgroundColor(getColor(parent, R.color.error_alert))
                .duration(SnackBar.SnackBarDuration.LONG)
                .show();
    }

    public static void setSuccessSnackBar(AppCompatActivity parent, View mView, String message) {
        SnackBar snackBar = new SnackBar();
        snackBar.view(mView)
                .customActionFont(getFontAwesomeWebFont(parent))
                .customTitleFont(getFontAwesomeWebFont(parent))
                .text(message, "OK", 2)
                .textColors(Color.WHITE, getColor(parent, R.color.white))
                .backgroundColor(getColor(parent, R.color.success_alert))
                .duration(SnackBar.SnackBarDuration.LONG)
                .show();
    }

    public static android.app.AlertDialog showSettingDialog(final Context context,
                                                            String msg, String title, final int id) {
        return new android.app.AlertDialog.Builder(context)
                // .setMobile_icon_code(android.R.attr.alertDialogIcon)
                .setMessage(msg)
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_setting,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                switch (id) {
                                    case Utility.NO_INTERNET_CONNECTION:
                                        context.startActivity(new Intent(
                                                android.provider.Settings.ACTION_SETTINGS));
                                        break;
                                    case Utility.NO_GPS_ACCESS:
                                        context.startActivity(new Intent(
                                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).create();
    }

    public static void showOKOnlyDialog(final Context context, String msg,
                                        String title) {
        SpannableString s = new SpannableString(msg);
        Linkify.addLinks(s, Linkify.ALL);

        AlertDialog d = new AlertDialog.Builder(context)
                .setMessage(s)
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                //logout(context);
                            }
                        }).show();

        ((TextView) d.findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * MAKE THE STATUS BAR TRANSLATE
     */
    public static void setTranslateStatusBar(BaseActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Utility.getColor(activity, R.color.transparent));
        }
    }

    /*Get Font Awesome Web font Type face*/
    public static Typeface setRobotoRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
    }


}
