package com.teamawesome.geese.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * Created by ecrothers on 16-03-03.
 */
public class ImageUploader {

    private File mImageFile = null;
    private String mRemoteName = null;
    private String mUploadedImageUrl = null;

    private Context mAppContext;
    private TransferListener mTransferListener;

    public ImageUploader(Context appContext, TransferListener transferListener) {
        mAppContext = appContext;
        mTransferListener = transferListener;
    }

    public void uploadPhotoSelection(Uri uri) {
        String imageFilePath = null;

        if (uri != null) {
            try {
                if( uri == null ) {
                    imageFilePath = uri.getPath();
                } else {
                    // get the id of the image selected by the user
                    imageFilePath = UriToPathConverter.getPath(mAppContext, uri);

                    mImageFile = new File(imageFilePath);
                    Toast.makeText(mAppContext, "Uploading...", Toast.LENGTH_SHORT).show();
                    new UploadToS3().execute(mImageFile);
                }
            } catch (Exception e) {
                Log.e("ImageUploader", "Failed to get image");
            }
        }
    }

    private class UploadToS3 extends AsyncTask<File, Integer, Long> {
        protected Long doInBackground(File... files) {
            // Create an S3 client
//            String accessKey, secretKey;
            BasicAWSCredentials bac = new BasicAWSCredentials("AKIAI67CPRBHXAGTO33A",
                    "wxETPVLbptOst5dn4C9MBHnrJuc5vb+scnOE7fBd");
            AmazonS3 s3 = new AmazonS3Client(bac);

            // Set the region of your S3 bucket
            s3.setRegion(Region.getRegion(Regions.US_EAST_1));

            // TODO: Use hashing to guarantee no collisions.
            String fname = "uploaded";
            fname += (int)(Math.random()*1000000000);
            mRemoteName = fname;

            TransferUtility transferUtility = new TransferUtility(s3, mAppContext);
            TransferObserver observer = transferUtility.upload(
                    Constants.PICTURE_BUCKET, /* The bucket to upload to */
                    fname,      /* The key for the uploaded object */
                    files[0]        /* The file where the data to upload exists */
            );

            ResponseHeaderOverrides override = new ResponseHeaderOverrides();
            override.setContentType("image/jpeg"); // TODO: Does this hold?

            // TODO: Do not need presigned URL request, use generic URL
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
                    Constants.PICTURE_BUCKET, mRemoteName);
            urlRequest.setExpiration(new Date(System.currentTimeMillis() + 3600000 * 24 * 365));  // Added an hour's worth of milliseconds to the current time.
            urlRequest.setResponseHeaders(override);
            mUploadedImageUrl = s3.generatePresignedUrl( urlRequest ).toString();
            observer.setTransferListener(mTransferListener);

            long result = 0;
            return result;
        }
    }

    public String getUploadedImageUrl() {
        return mUploadedImageUrl;
    }
}
