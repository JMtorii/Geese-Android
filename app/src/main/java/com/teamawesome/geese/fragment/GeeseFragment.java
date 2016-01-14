package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.teamawesome.geese.activity.MainActivity;

/**
 * Created by JMtorii on 16-01-13.
 */
public class GeeseFragment extends Fragment {
    protected MainActivity parentActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = (MainActivity) getActivity();
    }
}
