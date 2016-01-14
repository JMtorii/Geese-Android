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
 * Created by MichaelQ on 2016-01-13.
 */
public class FlockPostTopicCreateFragment extends Fragment {

    private EditText mTitleField;
    private EditText mDescriptionField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flock_post_topic_create, container, false);
        // stop making clicks fallthrough
        view.setClickable(true);
        mTitleField = (EditText)view.findViewById(R.id.flock_post_topic_create_title);
        mDescriptionField = (EditText)view.findViewById(R.id.flock_post_topic_create_description);
        Button button = (Button)view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: post topic to server and on success pop fragment
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
