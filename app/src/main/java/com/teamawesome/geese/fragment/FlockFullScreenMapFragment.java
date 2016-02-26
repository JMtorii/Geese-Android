package com.teamawesome.geese.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.teamawesome.geese.util.Constants;

import org.parceler.Parcel;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by lcolam on 12/14/15.
 */
public class FlockFullScreenMapFragment extends Fragment {
    private String flockTitle;
    private LatLng latLng;
    private TextView mFlockTitleTextView;
    private MapView mGMapView;
    private GoogleMap map;

    public void setFlockTitle(String s) {
        flockTitle = s;
    }

    public void setLatLng(LatLng l) {
        latLng = l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_screen_map, container, false);

        mFlockTitleTextView = (TextView)view.findViewById(R.id.flock_title);
        if (mFlockTitleTextView != null) {
            mFlockTitleTextView.setText(flockTitle);
        }

        mGMapView = (MapView) view.findViewById(R.id.google_map);
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        int googleVersionCode = getResources().getInteger(R.integer.google_play_services_version);
        if ((status == ConnectionResult.SUCCESS) && (GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE >= googleVersionCode)) {
            mGMapView.onCreate(savedInstanceState);
            map = mGMapView.getMap();
            map.getUiSettings().setMyLocationButtonEnabled(false);

            try {
                MapsInitializer.initialize(this.getActivity());
                if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                }
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setAllGesturesEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setMapToolbarEnabled(true);

                map.addMarker(new MarkerOptions()
                                .position(latLng)
                );
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getFragmentManager().popBackStack();
        }
        return view;
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
}
