package com.teamawesome.swap.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import com.teamawesome.swap.R;
import com.teamawesome.swap.object.Flock;
import com.teamawesome.swap.util.Constants;

/**
 * Created by JMtorii on 15-07-14.
 */
public class FlockProfileFragment extends Fragment {
    private final static String TAG_FRAGMENT = Constants.FLOCK_PROFILE_FRAGMENT_TAG;

    private Flock mFlock;

    private View mMapView;
    private TextView mFlockDescription;
    private TextView mFlockInfo1;
    private TextView mFlockInfo2;
    private TextView mFlockInfo3;
    private Button mJoinFlockButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_flock_profile, container, false);
        mFlockDescription = (TextView)v.findViewById(R.id.flock_profile_description);
        if (mFlockDescription != null) {
            mFlockDescription.setText(mFlock.description);
        }
        mFlockInfo1 = (TextView)v.findViewById(R.id.flock_profile_info1);
        if (mFlock.createdDate != null) {
            mFlockInfo1.setText("Created: " + mFlock.createdDate);
        }
        mFlockInfo2 = (TextView)v.findViewById(R.id.flock_profile_info2);
        if (mFlock.privacy != null) {
            mFlockInfo2.setText("Privacy: " + mFlock.privacy);
        }
        mFlockInfo3 = (TextView)v.findViewById(R.id.flock_profile_info3);
        mFlockInfo3.setText("Members: " + mFlock.members);
        mJoinFlockButton = (Button)v.findViewById(R.id.join_flock_button);
        mJoinFlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: join flock plz
            }
        });
        mMapView = v.findViewById(R.id.flock_profile_map);
        //TODO: can grab an image from google given a latitude and longitude
        //http://maps.google.com/maps/api/staticmap?center=48.858235,2.294571&zoom=15&size=1000x200&sensor=false
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setFlock(Flock f) {
        mFlock = f;
    }
}
