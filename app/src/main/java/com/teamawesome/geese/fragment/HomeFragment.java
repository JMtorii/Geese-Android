package com.teamawesome.geese.fragment;

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
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.adapter.FlockAdapter;
import com.teamawesome.geese.rest.model.FlockV2;
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

    private List<FlockV2> flocks = new ArrayList<>();
    private ArrayAdapter<FlockV2> flockAdapter;
    private MainActivity mainActivity;

    private SwipeRefreshLayout swipeContainer;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        flockAdapter = new FlockAdapter(mainActivity, flocks);
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

        listView.setAdapter(flockAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO implement some logic
                // use Bundle and fragment.setArguments if required to pass additional data
                MainFlockFragment fragment = (MainFlockFragment) getFragmentManager().findFragmentByTag(Constants.FLOCK_FRAGMENT_TAG);
                if (fragment == null) {
                    fragment = new MainFlockFragment();
                }
                fragment.setFlock(flocks.get(position));
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.switchFragment(
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
        });

        getNearbyFlocks();


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        flockAdapter = new FlockAdapter(mainActivity, flocks);
        listView.setAdapter(flockAdapter);
    }

    private void getNearbyFlocks() {

        // TODO use interceptor instead to add token to all REST calls
        if (useDummyData) {
            // fix for when network requests fail
            flockAdapter.clear();
            FlockV2 f = new FlockV2.Builder().name("Network Failed")
                    .description("So here's a dummy one instead")
                    .latitude(43.4707224f)
                    .longitude(80.5429343f)
                    .radius(1)
                    .id(1)
                    .authorid(1)
                    .build();
            flockAdapter.insert(f, 0);
            flockAdapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
            return;
        }
        if (SessionManager.checkLogin()) {
            Observable<List<FlockV2>> observable = RestClient.flockService.getNearbyFlocks(43.471086f, -80.541875f);
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<FlockV2>>() {
                        @Override
                        public void onCompleted() {
                            // nothing to do here
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("HomeFragment", "Something happened: " + e.getMessage());
                        }

                        @Override
                        public void onNext(List<FlockV2> flocks) {
                            Log.i("HomeFragment", "onNext called");

                            flockAdapter.clear();
                            if (flocks != null) {
                                for (FlockV2 flock : flocks) {
                                    flockAdapter.insert(flock, flockAdapter.getCount());
                                }
                            }

                            flockAdapter.notifyDataSetChanged();
                            swipeContainer.setRefreshing(false);
                        }
                    });
        }
    }

    private void getFavouritedFlocks() {
        List<FlockV2> favouritedFlocks = new ArrayList<>();

        Observable<List<FlockV2>> observable = RestClient.flockService.getFavourited();
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<FlockV2>>() {
                    @Override
                    public void onCompleted() {
                        // nothing to do here
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("HomeFragment", "Something happened: " + e.getMessage());
                    }

                    @Override
                    public void onNext(List<FlockV2> flocks) {
                        Log.i("HomeFragment", "onNext called");

                        flockAdapter.clear();
                        if (flocks != null) {
                            for (FlockV2 flock : flocks) {
                                flockAdapter.insert(flock, flockAdapter.getCount());
                            }
                        }

                        flockAdapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }
                });

    }
}
