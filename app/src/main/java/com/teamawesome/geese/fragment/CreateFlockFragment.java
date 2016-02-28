package com.teamawesome.geese.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.squareup.picasso.Picasso;
import com.teamawesome.geese.R;
import com.teamawesome.geese.rest.model.Flock;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.RestClient;
import com.teamawesome.geese.util.UriToPathConverter;

import java.io.File;
import java.net.URL;
import java.util.Date;

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
    private URL mUploadedImageUrl = null;
    private String mRemoteName = null;

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
        setupFlockImage();

        return view;
    }

    public void setupFlockImage() {
        // If the current flock image is null, no image was selected yet
        if (mUploadedImageUrl == null) {
            return;
        }

        //Initialize ImageView
        ImageView imageView = (ImageView) parentActivity.findViewById(R.id.create_flock_image);

        // TODO: placeholder and error, also beautify
        //Loading image from below url into imageView
        Picasso.with(parentActivity)
                .load(mUploadedImageUrl.getPath())
                //.placeholder(R.drawable.ic_placeholder) // optional
                //.error(R.drawable.ic_error_fallback) // optional
                //.resize(250, 200)                        // optional
                .into(imageView);

        imageView.invalidate();
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
                if (ContextCompat.checkSelfPermission(parentActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    openPhotoChooserDialog();
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
                        imageFilePath = UriToPathConverter.getPath(
                                parentActivity.getApplicationContext(), uri);

                        mImageFile = new File(imageFilePath);
                        Toast.makeText(getContext().getApplicationContext(), "Uploading...", Toast.LENGTH_SHORT).show();
                        new UploadToS3().execute(mImageFile);
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Failed to get image");
                }
            }
        }
    }

    public void setupCreateFlockButton() {
        mCreateFlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flockname = mFlockNameText.getText().toString().trim();

                // Use latest location, otherwise default to Waterloo if no location
                Location location = parentActivity.getLatestLocation();
                float latitude = location != null ? (float)location.getLatitude() : 43.471086f;
                float longitude = location != null ? (float)location.getLongitude() : -80.541875f;

                Flock flock = new Flock.Builder()
                        .name(flockname)
                        .description("description")
                        .latitude(latitude)
                        .longitude(longitude)
                        .build();

                Call<Void> call = RestClient.flockService.createFlock(flock);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Response<Void> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            //attemptLogin(username, email, hashedPassword);
                        } else {
                            Log.e(LOG_TAG, "Create Flock failed!");
                            Log.e(LOG_TAG, response.raw().toString());
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
                    openPhotoChooserDialog();
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

    public void openPhotoChooserDialog() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_SELECTED);
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
            // Create an S3 client
            String accessKey, secretKey;
            BasicAWSCredentials bac = new BasicAWSCredentials("AKIAI67CPRBHXAGTO33A",
                    "wxETPVLbptOst5dn4C9MBHnrJuc5vb+scnOE7fBd");
            AmazonS3 s3 = new AmazonS3Client(bac);

            // Set the region of your S3 bucket
            s3.setRegion(Region.getRegion(Regions.US_EAST_1));

            // TODO: Use hashing to guarantee no collisions.
            String fname = "uploaded";
            fname += (int)(Math.random()*1000000000);
            mRemoteName = fname;

            TransferUtility transferUtility = new TransferUtility(s3, parentActivity.getApplicationContext());
            TransferObserver observer = transferUtility.upload(
                    Constants.PICTURE_BUCKET, /* The bucket to upload to */
                    fname,      /* The key for the uploaded object */
                    files[0]        /* The file where the data to upload exists */
            );

            ResponseHeaderOverrides override = new ResponseHeaderOverrides();
            override.setContentType("image/jpeg"); // TODO: Does this hold?
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
                    Constants.PICTURE_BUCKET, mRemoteName);
            urlRequest.setExpiration( new Date( System.currentTimeMillis() + 3600000 ));  // Added an hour's worth of milliseconds to the current time.
            urlRequest.setResponseHeaders( override );
            mUploadedImageUrl = s3.generatePresignedUrl( urlRequest );

            long result = 0;

            return result;
        }

        // TODO: Progress bar goes here...
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //showDialog("Uploaded " + result + " bytes");
            if (result == 0) {
                Toast.makeText(getContext().getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                setupFlockImage();
            } else {
                Toast.makeText(getContext().getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
