package com.teamawesome.geese.fragment;

import android.app.FragmentManager;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.fragment.debug.FlockFullScreenMapFragment;
import com.teamawesome.geese.task.URLImageLoader;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.view.RoundedImageView;

/**
 * Created by MichaelQ on 2015-07-18.
 */
public class FlockProfileFragment extends FlockFragment {
    private static final String ARG_POSITION = "position";

    private int mPosition;
    private TextView mFlockTitleTextView;
    private TextView mFlockInfoMemberCountTextView;
    private TextView mFlockInfoPrivacyTextView;
    private TextView mFlockInfoDescriptionTextView;
    private RoundedImageView mFlockProfileImageView;
    private TextView mFlockInfoCreationDateTextView;
    private MapView mGMapView;
    private GoogleMap map;
    private ImageView mMapImageView;
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

        mFlockTitleTextView = (TextView)v.findViewById(R.id.profile_title);
        if (mFlockTitleTextView != null) {
            mFlockTitleTextView.setText(mFlock.getName());
        }

        mFlockInfoMemberCountTextView = (TextView)v.findViewById(R.id.profile_info_member_count);
//        if (mFlock.members >= 0) {
//            mFlockInfoMemberCountTextView.setText(mFlock.members + " members");
//        } else {
//            mFlockInfoMemberCountTextView.setVisibility(View.GONE);
//        }
        mFlockInfoMemberCountTextView.setText("50 members");

        mFlockInfoPrivacyTextView = (TextView)v.findViewById(R.id.profile_info_privacy);
//        if (mFlock.privacy != null) {
//            mFlockInfoPrivacyTextView.setText(mFlock.privacy);
//        } else {
//            mFlockInfoPrivacyTextView.setVisibility(View.GONE);
//        }
        mFlockInfoPrivacyTextView.setText("Public");

        mFlockProfileImageView = (RoundedImageView)v.findViewById(R.id.profile_image);
        URLImageLoader profileImageLoader = new URLImageLoader(mFlockProfileImageView);
//        profileImageLoader.execute(mFlock.imageURL);
        profileImageLoader.execute("http://justinhackworth.com/canada-goose-01.jpg");

        mFlockInfoCreationDateTextView = (TextView)v.findViewById(R.id.profile_info_creation_date);
//        if (mFlock.createdDate != null) {
//            mFlockInfoCreationDateTextView.setText("Created on " + mFlock.createdDate);
//        } else {
//            mFlockInfoCreationDateTextView.setVisibility(View.GONE);
//        }
        mFlockInfoCreationDateTextView.setText("Created on " + "January 1, 2016");

        mGMapView = (MapView) v.findViewById(R.id.profile_google_map);
        mMapImageView = (ImageView)v.findViewById(R.id.profile_image_map);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        int googleVersionCode = getResources().getInteger(R.integer.google_play_services_version);
        if ((status == ConnectionResult.SUCCESS) && (GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE >= googleVersionCode)) {
            mGMapView.setVisibility(View.VISIBLE);
            mMapImageView.setVisibility(View.GONE);

            mGMapView.onCreate(savedInstanceState);
            map = mGMapView.getMap();
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.getUiSettings().setMapToolbarEnabled(false);
            map.setMyLocationEnabled(true);

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    FlockFullScreenMapFragment fragment = (FlockFullScreenMapFragment) getFragmentManager().findFragmentByTag(Constants.FLOCK_FULL_SCREEN_MAP_FRAGMENT_TAG);
                    if (fragment == null) {
                        fragment = new FlockFullScreenMapFragment();
                    }
                    fragment.setFlockTitle(mFlock.getName());
                    fragment.setLatLng(new LatLng(mFlock.getLatitude(), mFlock.getLongitude()));
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.switchFragment(
                            fragment,
                            R.anim.fragment_slide_in_left,
                            R.anim.fragment_slide_out_right,
                            Constants.FLOCK_FULL_SCREEN_MAP_FRAGMENT_TAG,
                            false,
                            false,
                            true
                    );
                }
            });

            try {
                MapsInitializer.initialize(this.getActivity());
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.addMarker(new MarkerOptions()
                                .position(new LatLng(mFlock.getLatitude(), mFlock.getLongitude()))
                );
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mFlock.getLatitude(), mFlock.getLongitude()), 10));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mGMapView.setVisibility(View.GONE);
            mMapImageView.setVisibility(View.VISIBLE);

            //grab static image of map based on location
            //http://maps.google.com/maps/api/staticmap?center=48.858235,2.294571&zoom=15&size=1000x200&sensor=false
            URLImageLoader imageLoader = new URLImageLoader(mMapImageView);
            //TODO size should be based on size of ImageView
            imageLoader.execute("http://maps.google.com/maps/api/staticmap?center=" + mFlock.getLatitude() + "," + mFlock.getLongitude() + "&zoom=15&size=1000x200&sensor=false");
        }

        mFlockInfoDescriptionTextView = (TextView)v.findViewById(R.id.profile_description);
        if (mFlock.getDescription() != null) {
            mFlockInfoDescriptionTextView.setText(mFlock.getDescription());
        } else {
            mFlockInfoDescriptionTextView.setVisibility(View.GONE);
        }

        mJoinFlockButton = (Button)v.findViewById(R.id.profile_join);
        mJoinFlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: join flock plz
            }
        });

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
