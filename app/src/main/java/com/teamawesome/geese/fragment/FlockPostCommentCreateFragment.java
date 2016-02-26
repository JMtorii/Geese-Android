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
import com.teamawesome.geese.rest.model.CreateCommentRequestBody;
import com.teamawesome.geese.util.RestClient;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by MichaelQ on 2016-01-20.
 */
public class FlockPostCommentCreateFragment extends Fragment {

    private EditText mCommentField;
    private Button mCreateCommentButton;

    private int mPostId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flock_post_comment_create, container, false);
        // stop making clicks fallthrough
        view.setClickable(true);
        mCommentField = (EditText)view.findViewById(R.id.flock_post_comment_create_field);
        mCreateCommentButton = (Button)view.findViewById(R.id.button);
        mCreateCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createComment();
            }
        });
        return view;
    }

    public void setPostId(int postId) {
        mPostId = postId;
    }

    private void createComment() {
        mCreateCommentButton.setEnabled(false);
        CreateCommentRequestBody requestBody = new CreateCommentRequestBody(mPostId, mCommentField.getText().toString());
        RestClient.postService
                .saveCommentForPost(requestBody)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            //TODO: add comment to details fragment
                            MainActivity mainActivity = (MainActivity) getActivity();
                            View view = mainActivity.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                            mainActivity.popFragment();
                        } else {
                            // TODO: better error handling
                            mCreateCommentButton.setEnabled(true);
                            Log.e("CommentCreate", "Create comment failed");
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // TODO: better error handling
                        mCreateCommentButton.setEnabled(true);
                        Log.e("CommentCeate", "Create comment failed " + t.getMessage());
                    }
                });
    }
}
