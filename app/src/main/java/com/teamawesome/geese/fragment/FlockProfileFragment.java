package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamawesome.geese.R;
import com.teamawesome.geese.task.URLImageLoader;

/**
 * Created by MichaelQ on 2015-07-18.
 */
public class FlockProfileFragment extends FlockFragment {
    private static final String ARG_POSITION = "position";

    private int mPosition;

    private MapView mGMapView;
    private GoogleMap map;
    private ImageView mMapImageView;
    private TextView mFlockDescription;
    private TextView mFlockInfo1;
    private TextView mFlockInfo2;
    private TextView mFlockInfo3;
    private Button mJoinFlockButton;

    public static FlockProfileFragment newInstance(int position) {
        FlockProfileFragment f = new FlockProfileFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
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

        mGMapView = (MapView) v.findViewById(R.id.flock_profile_google_map);
        mMapImageView = (ImageView)v.findViewById(R.id.flock_profile_image_map);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        int googleVersionCode = getResources().getInteger(R.integer.google_play_services_version);
        if ((status == ConnectionResult.SUCCESS) && (GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE >= googleVersionCode)) {
            mGMapView.setVisibility(View.VISIBLE);
            mMapImageView.setVisibility(View.GONE);

            mGMapView.onCreate(savedInstanceState);
            map = mGMapView.getMap();
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.setMyLocationEnabled(true);

            try {
                MapsInitializer.initialize(this.getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }

            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(mFlock.latitude, mFlock.longitude))
                    .title("Marker"));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(mFlock.latitude, mFlock.longitude), 10);
            map.animateCamera(cameraUpdate);

        } else {
            mGMapView.setVisibility(View.GONE);
            mMapImageView.setVisibility(View.VISIBLE);

            //grab static image of map based on location
            //http://maps.google.com/maps/api/staticmap?center=48.858235,2.294571&zoom=15&size=1000x200&sensor=false
            URLImageLoader imageLoader = new URLImageLoader(mMapImageView);
            imageLoader.execute("http://maps.google.com/maps/api/staticmap?center=" + mFlock.latitude + "," + mFlock.longitude + "&zoom=15&size=1000x200&sensor=false");
        }

        return v;
    }

    @Override
    public void onResume() {
        mGMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mGMapView.onLowMemory();
    }

//    public void setFlock(Flock f) {
//        mFlock = f;
//    }
}
