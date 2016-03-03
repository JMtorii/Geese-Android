package com.teamawesome.geese.fragment;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
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
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;
import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.RestClient;
import com.teamawesome.geese.view.RoundedImageView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

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

        mFlockTitleTextView = (TextView) v.findViewById(R.id.profile_title);
        if (mFlockTitleTextView != null) {
            mFlockTitleTextView.setText(mFlock.getName());
        }

        mFlockInfoMemberCountTextView = (TextView) v.findViewById(R.id.profile_info_member_count);
        mFlockInfoMemberCountTextView.setText(Integer.toString(mFlock.getMembers()) + " Members");

        mFlockInfoPrivacyTextView = (TextView) v.findViewById(R.id.profile_info_privacy);
        mFlockInfoPrivacyTextView.setText("Public");

        mFlockProfileImageView = (RoundedImageView) v.findViewById(R.id.profile_image);
        String imageURL = mFlock.getImageUri() != null ? mFlock.getImageUri() : "http://justinhackworth.com/canada-goose-01.jpg";
        Picasso.with(getContext())
                .load(imageURL)
                .resize(100, 100)
                .centerCrop()
                .into(mFlockProfileImageView);

        mFlockInfoCreationDateTextView = (TextView) v.findViewById(R.id.profile_info_creation_date);
        mFlockInfoCreationDateTextView.setText("Created on " + "January 1, 2016");
//        mFlockInfoCreationDateTextView.setText("Created on " + mFlock.getCreatedTime());

        mGMapView = (MapView) v.findViewById(R.id.profile_google_map);
        mMapImageView = (ImageView) v.findViewById(R.id.profile_image_map);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        int googleVersionCode = getResources().getInteger(R.integer.google_play_services_version);
        if ((status == ConnectionResult.SUCCESS) && (GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE >= googleVersionCode)) {
            mGMapView.setVisibility(View.VISIBLE);
            mMapImageView.setVisibility(View.GONE);

            mGMapView.onCreate(savedInstanceState);
            map = mGMapView.getMap();
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.getUiSettings().setMapToolbarEnabled(false);

            if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }

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
                            FragmentTransaction.TRANSIT_NONE,
                            FragmentTransaction.TRANSIT_NONE,
                            Constants.FLOCK_FULL_SCREEN_MAP_FRAGMENT_TAG,
                            mFlock.getName(),
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

            Picasso.with(getContext())
                    .load("http://maps.google.com/maps/api/staticmap?center=" + mFlock.getLatitude() + "," + mFlock.getLongitude() + "&zoom=15&size=1000x200&sensor=false")
                    .into(mMapImageView);
        }

        mFlockInfoDescriptionTextView = (TextView)v.findViewById(R.id.profile_description);
        if (mFlock.getDescription() != null) {
            mFlockInfoDescriptionTextView.setText(mFlock.getDescription());
        } else {
            mFlockInfoDescriptionTextView.setVisibility(View.GONE);
        }

        mJoinFlockButton = (Button) v.findViewById(R.id.profile_join);

        if (mFlock.getFavourited()) {
            mJoinFlockButton.setText(R.string.profile_unjoin);
        } else {
            mJoinFlockButton.setText(R.string.profile_join);
        }

        mJoinFlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFlock.getFavourited()) {
                    joinFlock();
                } else {
                    unjoinFlock();
                }
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

    private void joinFlock() {
        Log.i("FlockProfileFragment", Integer.toString(mFlock.getId()));
        progressDialog.show();
        Observable<ResponseBody> observable = RestClient.flockService.joinFlock(mFlock.getId());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        // nothing to do here
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("FlockProfileFragment", "Something happened in joinFlock: " + e.getMessage());
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        Log.i("FlockProfileFragment", "onNext called");
                        mFlock.setFavourited(true);
                        mJoinFlockButton.setText(R.string.profile_unjoin);
//                        parentActivity.getSupportFragmentManager().popBackStack();
//
//                        MainFlockFragment fragment = (MainFlockFragment) getFragmentManager().findFragmentByTag(Constants.FLOCK_FRAGMENT_TAG);
//                        if (fragment == null) {
//                            fragment = new MainFlockFragment();
//                        }
//                        fragment.setFlock(mFlock);
//                        parentActivity.switchFragment(
//                                fragment,
//                                R.anim.fragment_slide_in_left,
//                                R.anim.fragment_slide_out_right,
//                                Constants.FLOCK_FRAGMENT_TAG,
//                                mFlock.getName(),
//                                false,
//                                false,
//                                true
//                        );
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        parentActivity.joinFlock(mFlock);
                    }
                });
    }

    private void unjoinFlock() {
        Log.i("FlockProfileFragment", Integer.toString(mFlock.getId()));
        progressDialog.show();
        Observable<ResponseBody> observable = RestClient.flockService.unjoinFlock(mFlock.getId());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        // nothing to do here
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("FlockProfileFragment", "Something happened in unjoin: " + e.getMessage());
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        Log.i("FlockProfileFragment", "onNext called");
                        mFlock.setFavourited(false);
                        mJoinFlockButton.setText(R.string.profile_join);

                        parentActivity.unjoinFlock();

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }
}
