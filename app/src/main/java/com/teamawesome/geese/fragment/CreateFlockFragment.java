package com.teamawesome.geese.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.teamawesome.geese.R;
import com.teamawesome.geese.rest.model.Flock;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.RestClient;

import java.io.File;

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
                int permissionCheck = ContextCompat.checkSelfPermission(parentActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(parentActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(parentActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(parentActivity,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String imageFilePath = null;
        if (requestCode == PHOTO_SELECTED && resultCode == Activity.RESULT_OK && null != data) {
            Uri uri = data.getData();

            if (uri != null) {
                try {
                    if( uri == null ) {
                         imageFilePath = uri.getPath();
                    } else {
                        // get the id of the image selected by the user
                        Uri wholeID = data.getData();
                        String id = wholeID.toString().split(":")[1];

                        String[] projection = { MediaStore.Images.Media.DATA };
                        String whereClause = MediaStore.Images.Media._ID + "=?";
                        Cursor cursor = parentActivity.getContentResolver().query(getUri(), projection, whereClause, new String[]{id}, null);
                        if( cursor != null ){
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            if (cursor.moveToFirst()) {
                                imageFilePath = cursor.getString(column_index);
                            }

                            cursor.close();
                        } else {
                            imageFilePath = uri.getPath();
                        }

                        mImageFile = new File(imageFilePath);
                        new UploadToS3().execute(mImageFile);
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Failed to get image");
                }
            }
        }
    }

    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        }

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PHOTO_SELECTED);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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

            // TODO: Use hashing.
            String fname = "uploaded";
            fname += (int)(Math.random()*1000000000);

            TransferUtility transferUtility = new TransferUtility(s3, parentActivity.getApplicationContext());
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
