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
 * Created by JMtorii on 16-01-13.
 */
public class FavouriteFlocksFragment extends GeeseFragment {

    private SwipeRefreshLayout swipeContainer;
    private ListView listView;

    private ArrayAdapter<Flock> flockAdapter;
    private List<Flock> flocks = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flockAdapter = new FlockAdapter(parentActivity, flocks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_flocks, container, false);

        listView = (ListView) view.findViewById(R.id.favourite_listview);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.favourite_swipe_container);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFavouritedFlocks();

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

                Flock tmpFlock = flocks.get(position);
                tmpFlock.setFavourited(true);
                fragment.setFlock(tmpFlock);
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

        getFavouritedFlocks();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setAdapter(flockAdapter);
    }

    public void getFavouritedFlocks() {
        if (SessionManager.checkLogin()) {
            progressDialog.show();
            Observable<List<Flock>> observable = RestClient.flockService.getFavourited();
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Flock>>() {
                        @Override
                        public void onCompleted() {
                            // nothing to do here
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("FavouriteFlocksFragment", "Something happened: " + e.getMessage());
                            swipeContainer.setRefreshing(false);

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(List<Flock> flocks) {
                            Log.i("FavouriteFlocksFragment", "onNext called");

                            flockAdapter.clear();
                            if (flocks != null) {
                                for (Flock flock : flocks) {
                                    flockAdapter.insert(flock, flockAdapter.getCount());
                                }
                            }

                            flockAdapter.notifyDataSetChanged();
                            swipeContainer.setRefreshing(false);

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }
}
