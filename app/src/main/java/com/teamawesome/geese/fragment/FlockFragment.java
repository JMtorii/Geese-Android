package com.teamawesome.geese.fragment;

import android.support.v4.app.Fragment;

import com.teamawesome.geese.rest.model.FlockV2;

/**
 * Created by JMtorii on 15-08-23.
 */
public class FlockFragment extends Fragment {
    protected FlockV2 mFlock;

    public void setFlock(FlockV2 flock) {
        mFlock = flock;
    }
}
