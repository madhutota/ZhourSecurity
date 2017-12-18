package com.zhour.zhoursecurity.Utils;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.zhour.zhoursecurity.activities.BaseActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by madhu on 15-Dec-17.
 */

public class ImageLoaderLibrary extends BaseActivity {

    public static ImageLoaderLibrary imageLoaderLibrary;



    public static void load(String url, ImageView imageView, BaseActivity baseActivity) {
        if (imageLoaderLibrary == null) {
            imageLoaderLibrary = new ImageLoaderLibrary();
        }
        imageLoaderLibrary.loadPrivate(url,imageView,baseActivity);
    }

    protected void loadPrivate(String url, ImageView imageView,BaseActivity  baseActivity) {
        new ImageLoader(imageView,baseActivity).execute(url);
    }

    public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

        // make a URL connection
        // open a stream
        // convert the bytes

        private ImageView imageView;
        private ProgressDialog progressDialog;
        private BaseActivity baseActivity;

        public ImageLoader(ImageView imageView,BaseActivity baseActivity) {
            this.imageView = imageView;
            this.baseActivity = baseActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomDialog.showProgressDialog(baseActivity,false);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                in.close();
                return bitmap;
            } catch (IOException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            CustomDialog.hideProgressBar(baseActivity);
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}
