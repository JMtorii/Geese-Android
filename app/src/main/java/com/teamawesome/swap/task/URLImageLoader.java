package com.teamawesome.swap.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by MichaelQ on 2015-07-18.
 */
public class URLImageLoader extends AsyncTask<String, Void, Bitmap> {
    private ImageView mImageView;
    private OnImageLoaded mImageLoadedCallback;


    public URLImageLoader(ImageView imageView) {
        mImageView = imageView;
    }

    public URLImageLoader(OnImageLoaded imageLoadedCallback) {
        mImageLoadedCallback = imageLoadedCallback;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        if (mImageView != null) {
            mImageView.setImageBitmap(result);
        } else {
            mImageLoadedCallback.onImageLoaded(result);
        }
    }

}
