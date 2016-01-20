package com.teamawesome.geese.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;

/**
 * Created by MichaelQ on 2016-01-20.
 */
public class FlockPostCommentCreateFragment extends Fragment {

    private EditText mCommentField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flock_post_comment_create, container, false);
        // stop making clicks fallthrough
        view.setClickable(true);
        mCommentField = (EditText)view.findViewById(R.id.flock_post_comment_create_field);
        Button button = (Button)view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: post comment to server and on success pop fragment
                MainActivity mainActivity = (MainActivity)getActivity();
                View view = mainActivity.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                mainActivity.popFragment();
            }
        });
        return view;
    }
}
