package com.teamawesome.geese.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;
import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.rest.model.CreatePostRequestBody;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.ImageUploader;
import com.teamawesome.geese.util.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by MichaelQ on 2016-01-13.
 */
public class FlockPostTopicCreateFragment extends GeeseFragment {

    private static final int PHOTO_SELECTED = 2;
    private EditText mTitleField;
    private EditText mDescriptionField;
    private ImageView mPostImageView;
    private Button mCreatePostButton;
    private Button mChooseImageButton;
    private String mUploadedImageUrlStr = null;
    private ImageUploader mUploader;
    private TransferListener mTransferListener;

    private List<OnPostCreatedListener> listeners = new ArrayList<>();
    private int mFlockId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                    refreshPostImage();
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

        mUploader = new ImageUploader(parentActivity.getApplicationContext(), mTransferListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flock_post_topic_create, container, false);
        // stop making clicks fallthrough
        view.setClickable(true);
        mTitleField = (EditText)view.findViewById(R.id.flock_post_topic_create_title);
        mDescriptionField = (EditText)view.findViewById(R.id.flock_post_topic_create_description);
        mDescriptionField.setScroller(new Scroller(getContext()));
        mDescriptionField.setVerticalScrollBarEnabled(true);
        mDescriptionField.setMinLines(4);
        mDescriptionField.setMaxLines(4);
        mCreatePostButton = (Button)view.findViewById(R.id.button);
        mCreatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });
        mChooseImageButton = (Button) view.findViewById(R.id.choose_post_image_button);
        mPostImageView = (ImageView) view.findViewById(R.id.create_post_image);

        setupChooseImageButton();
        refreshPostImage();

        return view;
    }

    public void refreshPostImage() {
        if (mUploadedImageUrlStr == null) {
            mPostImageView.setVisibility(View.GONE);
            return;
        }
        Picasso.with(getContext())
                .load(mUploadedImageUrlStr)
                .resize(300, 240)
                .centerCrop()
                .into(mPostImageView);
        mPostImageView.setVisibility(View.VISIBLE);
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

    public void setFlockId(int flockId) {
        mFlockId = flockId;
    }

    public void addListener(OnPostCreatedListener listener) {
        listeners.add(listener);
    }

    public void removeListener(OnPostCreatedListener listener) {
        listeners.remove(listener);
    }

    private void createPost() {
        mCreatePostButton.setEnabled(false);
        CreatePostRequestBody requestBody = new CreatePostRequestBody(mFlockId, mTitleField.getText().toString(), mDescriptionField.getText().toString(), mUploadedImageUrlStr);
        Log.d("stuff", "" + requestBody.flockId + ", " + requestBody.title + ", " + requestBody.description + ", " + requestBody.imageUri);
        RestClient.postService
                .savePostForFlock(requestBody)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            for (OnPostCreatedListener listener : listeners) {
                                listener.onPostCreated();
                            }
                            //TODO: add post to topic fragment
                            MainActivity mainActivity = (MainActivity) getActivity();
                            View view = mainActivity.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                            mainActivity.popFragment();
                        } else {
                            // TODO: better error handling
                            mCreatePostButton.setEnabled(true);
                            Log.e("PostCreate", "Create post failed");
                            Log.e("PostCreate", response.raw().toString());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // TODO: better error handling
                        mCreatePostButton.setEnabled(true);
                        Log.e("PostCeate", "Create post failed " + t.getMessage());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_SELECTED && resultCode == Activity.RESULT_OK && null != data) {
            mUploader.uploadPhotoSelection(data.getData());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnPostCreatedListener {
        void onPostCreated();
    }
}
