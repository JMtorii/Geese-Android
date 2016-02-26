package com.teamawesome.geese.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;

import java.io.File;
import java.io.FileOutputStream;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.teamawesome.geese.R;
import com.teamawesome.geese.adapter.FlockAdapter;
import com.teamawesome.geese.rest.model.Flock;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.RestClient;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ecrothers on 2016-02-25.
 */
public class CreateFlockFragment extends GeeseFragment {
    //private GridView gridView;
    private static final String LOG_TAG = "CreateFlockFragment";

    private static final int PHOTO_SELECTED = 1;
    private View mView;
    private Button mCreateFlockButton;
    private Button mChooseImageButton;
    private EditText mFlockNameText;
    private File mImageFile = null;

    private void findWidgets(View view) {
        mCreateFlockButton = (Button) view.findViewById(R.id.create_flock_button);
        mChooseImageButton = (Button) view.findViewById(R.id.choose_image_button);
        mFlockNameText = (EditText) view.findViewById(R.id.create_flock_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_flock, container, false);
        mView = view;
        //gridView = (GridView) view.findViewById(R.id.create_flock_gridview);
        findWidgets(mView);
        setupChooseImageButton();
        setupCreateFlockButton();

        return view;
    }

    public void setupChooseImageButton() {
        mChooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_SELECTED);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // TODO: Check result code
        if (requestCode == PHOTO_SELECTED && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            mImageFile = new File(getRealPathFromURI(selectedImage).getPath());
            new UploadToS3().execute(mImageFile);
        }
    }

    private Uri getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = parentActivity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return Uri.parse(result);
    }

    public void setupCreateFlockButton() {
        mCreateFlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flockname = mFlockNameText.getText().toString().trim();
                // TODO: Finish populating this
                Flock flock = new Flock(flockname, "description", 0, 0, 0, 0);
                Call<Void> call = RestClient.flockService.createFlock(flock);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Response<Void> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            //attemptLogin(username, email, hashedPassword);
                        } else {
                            Log.e(LOG_TAG, "Create Flock failed!");
                            // TODO: member context
                            Toast.makeText(getContext().getApplicationContext(), "Flock creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
//                        Log.e(loggingTag, "Failed to create goose");
//                        Log.e(loggingTag, t.getMessage().toString());
//                        t.printStackTrace();
//                        Toast.makeText(mContext.getApplicationContext(), "Server down, try again later...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void update() {
        Log.i("CreateFlockFragment", "update");
    }

    private class UploadToS3 extends AsyncTask<File, Integer, Long> {
        protected Long doInBackground(File... files) {
            // Configure AWS S3 Access
            // Create an S3 client

            String accessKey, secretKey;
            BasicAWSCredentials bac = new BasicAWSCredentials("AKIAI67CPRBHXAGTO33A",
                    "wxETPVLbptOst5dn4C9MBHnrJuc5vb+scnOE7fBd");
            AmazonS3 s3 = new AmazonS3Client(bac);

            // Set the region of your S3 bucket
            s3.setRegion(Region.getRegion(Regions.US_EAST_1));

            String fname = "uploaded";
            fname += (int)(Math.random()*100000);
            fname += ".txt";

            TransferUtility transferUtility = new TransferUtility(s3, getContext().getApplicationContext());
            TransferObserver observer = transferUtility.upload(
                    Constants.PICTURE_BUCKET, /* The bucket to upload to */
                    fname,      /* The key for the uploaded object */
                    files[0]        /* The file where the data to upload exists */
            );

            long result = 0;

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //showDialog("Uploaded " + result + " bytes");
        }
    }
}
