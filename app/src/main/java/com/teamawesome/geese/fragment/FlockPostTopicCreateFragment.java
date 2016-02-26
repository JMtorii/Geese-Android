package com.teamawesome.geese.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.ResponseBody;
import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.rest.model.CreatePostRequestBody;
import com.teamawesome.geese.util.RestClient;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by MichaelQ on 2016-01-13.
 */
public class FlockPostTopicCreateFragment extends Fragment {

    private EditText mTitleField;
    private EditText mDescriptionField;
    private Button mCreatePostButton;

    private int mFlockId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flock_post_topic_create, container, false);
        // stop making clicks fallthrough
        view.setClickable(true);
        mTitleField = (EditText)view.findViewById(R.id.flock_post_topic_create_title);
        mDescriptionField = (EditText)view.findViewById(R.id.flock_post_topic_create_description);
        mCreatePostButton = (Button)view.findViewById(R.id.button);
        mCreatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });
        return view;
    }

    public void setFlockId(int flockId) {
        mFlockId = flockId;
    }

    private void createPost() {
        mCreatePostButton.setEnabled(false);
        CreatePostRequestBody requestBody = new CreatePostRequestBody(mFlockId, mTitleField.getText().toString(), mDescriptionField.getText().toString());
        Log.d("stuff", "" + requestBody.flockId + ", " + requestBody.title + ", " + requestBody.description);
        RestClient.postService
                .savePostForFlock(requestBody)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
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
}
