package com.zhour.zhoursecurity.asynctask;

import android.content.Context;
import android.support.compat.BuildConfig;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.APIConstants;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.Utils.Utility;
import com.zhour.zhoursecurity.models.Model;
import com.zhour.zhoursecurity.parser.Parser;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.zhour.zhoursecurity.Utils.APIConstants.REQUEST_TYPE.PATCH;

/**
 * Created by ShankarRao on 3/28/2016.
 **/
public class ServerJSONAsyncTask extends BaseAsyncTask {
    private String mResponse = null;
    private Model model;
    private int CONNECTION_TIME_OUT = 8000;
    static final String COOKIES_HEADER = "Set-Cookie";
    private java.net.CookieManager msCookieManager = new java.net.CookieManager();

    public ServerJSONAsyncTask(Context context, String dialogMessage,
                               boolean showDialog, String url, LinkedHashMap<String, String> mParams,
                               APIConstants.REQUEST_TYPE requestType, IAsyncCaller caller, Parser parser) {
        super(context, dialogMessage, showDialog, url, mParams, requestType,
                caller, parser);

    }

    public ServerJSONAsyncTask(Context context, String dialogMessage,
                               boolean showDialog, String url, LinkedHashMap<String, String> mParams,
                               APIConstants.REQUEST_TYPE requestType, IAsyncCaller caller, Parser parser, String tag, File file, ArrayList<File> mFiles) {
        super(context, dialogMessage, showDialog, url, mParams, requestType,
                caller, parser, tag, file, mFiles);

    }


    @Override
    public Integer doInBackground(Void... params) {
        if (!Utility.isNetworkAvailable(mContext)) {
            return 0;
        }

        if (BuildConfig.DEBUG) {
            Utility.showLog("API CALL :", "REST URL: " + mUrl);
            /*Utility.showLog("API CALL :", "REST URL CODE : " + CODE);*/
            Utility.showLog("API CALL :", "REST TASK METHOD : " + mRequestType);
            Utility.showLog("API CALL :", "REST URL PARAMS : " + mParams);
            //Utility.showLog("API CALL :", "REST URL HEADER_MAP : " + HEADER_MAP);
        }

        switch (mRequestType) {
            case GET:
            case POST:
            case DELETE:
            case PATCH:
                mResponse = requestToServer();
                return parseResponse(mResponse);
            default:
                return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        try {
            if (!isCancelled() && result == 1) {
                if (model != null) {
                    caller.onComplete(model);
                } else {
                    Utility.showToastMessage(mContext, "Server response error!");
                }
            } else if (result == 0) {
                Utility.showSettingDialog(
                        mContext,
                        mContext.getResources().getString(
                                R.string.no_internet_msg),
                        mContext.getResources().getString(
                                R.string.no_internet_title),
                        Utility.NO_INTERNET_CONNECTION).show();
                model = null;
                caller.onComplete(model);
            } else {
                model = null;
                caller.onComplete(model);
                Utility.showOKOnlyDialog(mContext, Utility.getResourcesString(mContext, R.string.unauthorized_access),
                        Utility.getResourcesString(mContext, R.string.app_name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int parseResponse(String response) {
        if (response != null) {
            Utility.showLog("RESPONSE<><>", response);
            return getResponse(response);

        }

        return -1;
    }

    private int getResponse(String response) {
        model = parser.parse(response);
        return 1;
    }


    private String requestToServer() {
        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(mUrl);
            Utility.showLog("mUrl", mUrl);
            connection = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        try {
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(CONNECTION_TIME_OUT);
            if (mRequestType.equals(PATCH)) {
                connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
                connection.setRequestMethod(mRequestType.toString());
                connection.setDoOutput(true);
            } else {
                connection.setRequestMethod(mRequestType.toString());

            /*for (int i = 0; i < HEADER_MAP.size(); i++) {
                connection.setRequestProperty(HEADER_MAP.keyAt(i), HEADER_MAP.valueAt(i));
            }*/
            }

            connection.setRequestProperty("Content", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            if (!Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(mContext, Constants.TOKEN))) {
                String token = Utility.getSharedPrefStringData(mContext, Constants.TOKEN);
                Utility.showLog("Token ", "Token : " + token);
                connection.setRequestProperty("token", token);
            }
            connection.setUseCaches(false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (mParams != null) {
            try {
                String param1 = mParams.toString();
                Utility.showLog("param1", "" + param1);
                OutputStream os = connection.getOutputStream();
                Writer writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                if (mUrl.contains(APIConstants.AUTHENTICATE_USER)
                        || mUrl.contains(APIConstants.GET_LOOKUP_DATA_BY_ENTITY_NAME)
                        || mUrl.contains(APIConstants.GET_INVITE_INFO)
                        || mUrl.contains(APIConstants.CREATE_OR_UPDATE_VISITOR)
                        || mUrl.contains(APIConstants.DEL_STAFF_VISIT)
                        || mUrl.contains(APIConstants.DEL_VISITOR)
                        || mUrl.contains(APIConstants.CREATE_OR_UPDATE_STAFF_VISIT)
                        || mUrl.contains(APIConstants.SAVE_INVITE)
                        ) {
                    Utility.showLog("mParams", "" + getURL(mParams));
                    writer.write(getURL(mParams));
                } else {
                    writer.write(param1);
                }
                //writer.write(param1);
                /*
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(URLEncoder.encode(param1));*/
                writer.flush();
                writer.close();
                os.close();
                connection.connect();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        String result;
        int responseCode;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        /*
        *  ERROR RESPONSE CODE ie NOT the 200
        * */
        if (responseCode != 201 && responseCode != 200 && responseCode != 204) {
            Utility.showLog("API CALL :", "WRONG RESPONSE CODE: " + responseCode);
            JSONObject jsonObject = new JSONObject();
            if (BuildConfig.DEBUG)
                Utility.showLog("API CALL :", "WRONG RESPONSE CODE: " + responseCode);
            else {
                return null;
            }
        } else if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
            return null;
        }

        /*
        *  VALID RESPONSE CODE
        * */
        else {
            InputStream reader;
            try {
                reader = connection.getInputStream();
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            }

            Map<String, List<String>> headerFields = connection.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                }
            }
            if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                List<HttpCookie> cookies = msCookieManager.getCookieStore().getCookies();
                if (cookies != null) {
                    for (HttpCookie cookie : cookies) {
                        //  Utility.setSharedPrefStringData(mContext, Constants.TOKEN, cookie.getValue());
                    }
                }
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            try {
                while ((nRead = reader.read(data, 0, data.length)) != -1)
                    buffer.write(data, 0, nRead);
                buffer.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            byte[] downloadedData = buffer.toByteArray();

            try {
                result = new String(downloadedData, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }

            /*try {
                resultJSON = new JSONObject(result);
                if (BuildConfig.DEBUG)
                    Utility.showLog("API CALL :", "RESULT: " + resultJSON);

            } catch (JSONException e) {
                RESULT_MESSAGE = APIConstants.SERVER_NOT_RESPONDING;
                e.printStackTrace();
                return null;
            }*/
        }
        connection.disconnect();
        //return resultJSON.toString();
        return result;
    }

    public static String getURL(LinkedHashMap<String, String> paramMap) {
        StringBuilder sb = new StringBuilder().append("");
        boolean first = true;
        if (paramMap != null && paramMap.size() > 0) {
            sb.append("");
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (first) {
                    sb.append(entry.getKey()).append("=")
                            .append(entry.getValue());
                    first = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=")
                            .append(entry.getValue());
                }

            }
        }
        return sb.toString();
    }

    /*public static String getJsonParams(LinkedHashMap<String, String> paramMap) {
        if (paramMap == null) {
            return null;
        }

        JSONObject jsonObject = new JSONObject();
        //List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            try {
                if (entry.getKey().equalsIgnoreCase("IsOtpVerified")) {
                    boolean i = Boolean.valueOf(entry.getValue());
                    jsonObject.accumulate(entry.getKey(), i);
                } else {
                    String i = entry.getValue();
                    jsonObject.accumulate(entry.getKey(), i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Utility.showLog("jsonObject", "jsonObject : " + jsonObject.toString());

        return jsonObject.toString();
    }*/

}

