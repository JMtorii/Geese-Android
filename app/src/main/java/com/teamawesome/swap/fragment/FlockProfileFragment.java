package com.teamawesome.swap.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamawesome.swap.R;
import com.teamawesome.swap.util.Constants;

/**
 * Created by JMtorii on 15-07-14.
 */
public class FlockProfileFragment extends Fragment {
    private final static String TAG_FRAGMENT = Constants.FLOCK_PROFILE_FRAGMENT_TAG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flock_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
