package com.teamawesome.geese.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.adapter.FlockAdapter;
import com.teamawesome.geese.rest.model.Flock;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.RestClient;
import com.teamawesome.geese.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JMtorii on 15-07-14.
 */
public class HomeFragment extends GeeseFragment {
    private final static String TAG_FRAGMENT = Constants.HOME_FRAGMENT_TAG;

    private List<Flock> flocks = new ArrayList<>();
    private ArrayAdapter<Flock> flockAdapter;

    private SwipeRefreshLayout swipeContainer;
    private ListView listView;
    private View emptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flockAdapter = new FlockAdapter(parentActivity, flocks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = (ListView) view.findViewById(R.id.home_listview);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.home_swipe_container);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNearbyFlocks();

                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // TODO: Hack for empty view, should use setEmptyView but unfortunately, that doesn't work
        emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_view,
                listView,
                false);
        listView.addFooterView(emptyView);
        listView.setAdapter(flockAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: Sorry for this. Satan told me to do this
                if (view == emptyView) {
                    return;
                }
                if (flocks.get(position).getFavourited()) {
                    MainFlockFragment fragment = (MainFlockFragment) getFragmentManager().findFragmentByTag(Constants.FLOCK_FRAGMENT_TAG);
                    if (fragment == null) {
                        fragment = new MainFlockFragment();
                    }
                    fragment.setFlock(flocks.get(position));
                    parentActivity.switchFragment(
                            fragment,
                            R.anim.fragment_slide_in_left,
                            R.anim.fragment_slide_out_right,
                            Constants.FLOCK_FRAGMENT_TAG,
                            flocks.get(position).getName(),
                            false,
                            false,
                            true
                    );

                } else {
//                    FlockProfileFragment fragment = (FlockProfileFragment) getFragmentManager().findFragmentByTag(Constants.FLOCK_PROFILE_FRAGMENT_TAG);
//                    if (fragment == null) {
//                        fragment = new FlockProfileFragment();
//                    }

                    // This is not optimized. We instantiate it all the time
                    FlockProfileFragment fragment = FlockProfileFragment.newInstance(0);

                    fragment.setFlock(flocks.get(position));
                    parentActivity.switchFragment(
                            fragment,
                            R.anim.fragment_slide_in_left,
                            R.anim.fragment_slide_out_right,
                            Constants.FLOCK_FRAGMENT_TAG,
                            flocks.get(position).getName(),
                            false,
                            false,
                            true
                    );
                }
            }
        });

        getNearbyFlocks();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void update() {
        Log.i("HomeFragment", "update");
        getNearbyFlocks();
    }

    public void getNearbyFlocks() {
        // TODO use interceptor instead to add token to all REST calls
        if (useDummyData) {
            // fix for when network requests fail
            flockAdapter.clear();
            Flock f = new Flock.Builder().name("Network Failed")
                    .description("So here's a dummy one instead")
                    .latitude(43.4707224f)
                    .longitude(80.5429343f)
                    .radius(1)
                    .id(9)
                    .authorid(1)
                    .build();
            flockAdapter.insert(f, 0);
            flockAdapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
            return;
        }
        if (SessionManager.checkLogin()) {
            progressDialog.show();
            Observable<List<Flock>> observable;
            Location location = parentActivity.getLatestLocation();
            if (location != null) {
                observable = RestClient.flockService.getNearbyFlocks((float)location.getLatitude(), (float)location.getLongitude());
            } else {
                // Use default in Waterloo if no location found
                observable = RestClient.flockService.getNearbyFlocks(43.471086f, -80.541875f);
            }
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Flock>>() {
                        @Override
                        public void onCompleted() {
                            // nothing to do here
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("HomeFragment", "Something happened: " + e.getMessage());
                            swipeContainer.setRefreshing(false);

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(List<Flock> flocks) {
                            Log.i("HomeFragment", "onNext called");

                            flockAdapter.clear();
                            if (flocks != null) {
                                if (flocks.isEmpty()) {
                                    emptyView.setVisibility(View.VISIBLE);
                                    emptyView.setPadding(0, 0, 0, 0);
                                } else {
                                    // TODO hackity hack hack
                                    emptyView.setVisibility(View.GONE);
                                    emptyView.setPadding(0, -1*emptyView.getHeight(), 0, 0);
                                    for (Flock flock : flocks) {
                                        flockAdapter.insert(flock, flockAdapter.getCount());
                                    }
                                }
                            }

                            flockAdapter.notifyDataSetChanged();
                            listView.invalidateViews();
                            swipeContainer.setRefreshing(false);

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }
}
