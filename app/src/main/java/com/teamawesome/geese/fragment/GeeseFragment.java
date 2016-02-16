package com.teamawesome.geese.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.teamawesome.geese.activity.MainActivity;

/**
 * Created by JMtorii on 16-01-13.
 */
public class GeeseFragment extends Fragment {
    protected MainActivity parentActivity;
    protected ProgressDialog progressDialog;

    // Dont commit this as true
    protected boolean useDummyData = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = (MainActivity) getActivity();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Loading...");
    }

    public void update() {
        // nothing to do here
    }
}
