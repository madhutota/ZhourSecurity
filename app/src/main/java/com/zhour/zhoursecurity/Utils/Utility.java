package com.zhour.zhoursecurity.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by madhu on 29-Aug-17.
 */

public class Utility {

    public static final int NO_INTERNET_CONNECTION = 1;
    private static final int NO_GPS_ACCESS = 2;

    private static final int CONNECTION_TIMEOUT = 25000;


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

    /*Get Font Awesome Web font Type face*/
    public static Typeface setRobotoBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
    }

    public static Typeface setLucidaSansItalic(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Lucida Sans Italic.ttf");
    }

    /**
     * Sharedpreference method to set and get string variable
     */
    public static void setSharedPrefStringData(Context context, String key, String value) {
        try {
            if (context != null) {
                SharedPreferences appInstallInfoSharedPref = context.getSharedPreferences(Constants.APP_PREF,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor appInstallInfoEditor = appInstallInfoSharedPref.edit();
                appInstallInfoEditor.putString(key, value);
                appInstallInfoEditor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
     * ASSIGN THE DRAWBLE
     **/
    public static Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

       /*
     *
	 *
	 * These methods are to make async tasks concurrent, and run on parallel on
	 * android 3+
	 */

    public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task) {
        execute(task, (P[]) null);
    }

    @SafeVarargs
    @SuppressLint("NewApi")
    public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task,
                                                                 P... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    public static String capitalizeFirstLetter(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static String getTime(String time) {
        String timeSym = "";
        if (time.endsWith(" PM"))
            timeSym = " PM";
        else
            timeSym = " AM";

        if (time.endsWith(":00 PM") || time.endsWith(":00 AM")) {
            time = time.substring(0, time.length() - 6);
        }
        return time + "" + timeSym;
    }

    public static String getWithHeader(String url, Context mContext) {
        showLog("Url", url);
        InputStream inputStream = null;
        String result = "";
        try {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams,
                    CONNECTION_TIMEOUT);

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("token", Utility.getSharedPrefStringData(mContext, Constants.TOKEN));
            HttpResponse httpResponse = httpclient.execute(httpGet);
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public static String httpJsonRequest(String url, HashMap<String, String> mParams, Context context) {
        String websiteData = "error";
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(),
                CONNECTION_TIMEOUT); // Timeout
        // Limit
        HttpResponse response;
        HttpPost post = new HttpPost(url);
        post.setHeader("token", Utility.getSharedPrefStringData(context, Constants.TOKEN));
        StringEntity se;
        try {
            se = new StringEntity(getJsonParams(mParams));
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            post.setEntity(se);
            response = client.execute(post);
            //* Checking response *//*
            if (response != null) {
                websiteData = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
            websiteData = "error";
            return websiteData;
        }
        return websiteData;
    }

    public static String getJsonParams(HashMap<String, String> paramMap) {
        if (paramMap == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            try {
                if (entry.getKey().equalsIgnoreCase("contacts")) {
                    JSONArray jsonArray = new JSONArray(entry
                            .getValue());
                    jsonObject.accumulate(entry.getKey(), jsonArray);
                } else if (entry.getKey().equalsIgnoreCase("login")) {
                    JSONObject jsonArrayLogin = new JSONObject(entry
                            .getValue());
                    jsonObject.accumulate(entry.getKey(), jsonArrayLogin);
                } else {
                    jsonObject.accumulate(entry.getKey(), entry
                            .getValue());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonObject.toString();
    }

    public static void navigateDashBoardFragment(Fragment fragment,
                                                 String tag, Bundle bundle, FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity
                .getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        /*fragmentTransaction.setCustomAnimations(R.anim.slide_left_right,
                R.anim.fade_out);*/
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(R.id.content_frame, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public static String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String getDateMM() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    public static String getDatenew() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
