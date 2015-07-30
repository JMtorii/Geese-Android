package com.teamawesome.geese.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.object.Flock;
import com.teamawesome.geese.task.URLImageLoader;

/**
 * Created by MichaelQ on 2015-07-18.
 */
public class FlockProfileFragment extends Fragment {

    private Flock mFlock;

    private ImageView mMapView;
    private TextView mFlockDescription;
    private TextView mFlockInfo1;
    private TextView mFlockInfo2;
    private TextView mFlockInfo3;
    private Button mJoinFlockButton;

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
        mMapView = (ImageView)v.findViewById(R.id.flock_profile_map);

        URLImageLoader imageLoader = new URLImageLoader(mMapView);
        imageLoader.execute("http://maps.google.com/maps/api/staticmap?center=" + mFlock.latitude + "," + mFlock.longitude + "&zoom=15&size=1000x200&sensor=false");
        //grab static image of map based on location
        //http://maps.google.com/maps/api/staticmap?center=48.858235,2.294571&zoom=15&size=1000x200&sensor=false
        return v;
    }

    public void setFlock(Flock f) {
        mFlock = f;
    }
}
