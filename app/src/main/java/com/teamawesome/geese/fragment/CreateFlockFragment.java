package com.teamawesome.geese.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Image;
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
import android.widget.Scroller;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.squareup.picasso.Picasso;
import com.teamawesome.geese.R;
import com.teamawesome.geese.rest.model.Flock;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.ImageUploader;
import com.teamawesome.geese.util.RestClient;

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
    private ImageView mFlockImageView;
    private Button mCreateFlockButton;
    private Button mChooseImageButton;
    private EditText mFlockNameText;
    private String mUploadedImageUrlStr = null;
    private EditText mFlockDescriptionText;
    private File mImageFile = null;
    private URL mUploadedImageUrl = null;
    private String mRemoteName = null;

    private ImageUploader mUploader;
    private TransferListener mTransferListener;

    public CreateFlockFragment() {
        mUploader = new ImageUploader(parentActivity.getApplicationContext(),
                mTransferListener);
        /**
         * Handle updates from uploading the image file
         */
        mTransferListener = new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state == TransferState.COMPLETED) {
                    Toast.makeText(parentActivity.getApplicationContext(),
                            "Upload successful", Toast.LENGTH_SHORT).show();
                    mUploadedImageUrlStr = mUploader.getUploadedImageUrl();
                    setupFlockImage();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//                    int percentage = (int) (bytesCurrent / bytesTotal * 100);
//                    progress.setProgress(percentage);
                //Display percentage transfered to user
            }

            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(parentActivity.getApplicationContext(),
                        "Upload failed", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void findWidgets(View view) {
        mCreateFlockButton = (Button) view.findViewById(R.id.create_flock_button);
        mChooseImageButton = (Button) view.findViewById(R.id.choose_image_button);
        mFlockNameText = (EditText) view.findViewById(R.id.create_flock_name);
        mFlockDescriptionText = (EditText) view.findViewById(R.id.create_flock_description);
        mFlockDescriptionText.setScroller(new Scroller(getContext()));
        mFlockDescriptionText.setVerticalScrollBarEnabled(true);
        mFlockDescriptionText.setMinLines(4);
        mFlockDescriptionText.setMaxLines(4);
        mFlockImageView = (ImageView) view.findViewById(R.id.create_flock_image);
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
        if (mUploadedImageUrl == null) {
            mFlockImageView.setVisibility(View.GONE);
            return;
        }
        Picasso.with(getContext())
                .load(mUploadedImageUrl.toString())
                .resize(300, 240)
                .centerCrop()
                .into(mFlockImageView);
        mFlockImageView.setVisibility(View.VISIBLE);
    }

    public void openPhotoChooserDialog() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_SELECTED);
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
            mUploader.uploadPhotoSelection(data.getData());
        }
    }

    public void setupCreateFlockButton() {
        mCreateFlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flockName = mFlockNameText.getText().toString().trim();
                if (flockName.isEmpty()) {
                    Toast.makeText(getContext().getApplicationContext(), "Flock name required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String flockDescription = mFlockDescriptionText.getText().toString().trim();

                // Use latest location, otherwise default to Waterloo if no location
                Location location = parentActivity.getLatestLocation();
                float latitude = location != null ? (float) location.getLatitude() : 43.471086f;
                float longitude = location != null ? (float) location.getLongitude() : -80.541875f;

                Flock flock = new Flock.Builder()
                        .name(flockName)
                        .description(flockDescription)
                        .latitude(latitude)
                        .longitude(longitude)
                        .radius(1.0)
                        .score(0)
                        .imageUri(mUploadedImageUrlStr)
                        .members(1)
                        .build();

                Call<Void> call = RestClient.flockService.createFlock(flock);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Response<Void> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            Toast.makeText(getContext().getApplicationContext(), "Flock created!", Toast.LENGTH_SHORT).show();

                            parentActivity.getSupportFragmentManager().popBackStack();
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void update() {
        Log.i("CreateFlockFragment", "update");
    }

}
