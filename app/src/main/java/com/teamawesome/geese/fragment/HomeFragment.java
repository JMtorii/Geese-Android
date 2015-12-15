package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.adapter.FlockAdapter;
import com.teamawesome.geese.rest.model.FlockV2;
import com.teamawesome.geese.rest.service.FlockService;
import com.teamawesome.geese.util.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JMtorii on 15-07-14.
 */
public class HomeFragment extends ListFragment {
    private final static String TAG_FRAGMENT = Constants.HOME_FRAGMENT_TAG;

    private List<FlockV2> flocks = new ArrayList<>();
    private ArrayAdapter<FlockV2> flockAdapter;
    private MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        flockAdapter = new FlockAdapter(mainActivity, flocks);
        setListAdapter(flockAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.57.1:8080")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        FlockService flockService = retrofit.create(FlockService.class);

        Observable<List<FlockV2>> observable = flockService.getNearbyFlocks(43.471086f, -80.541875f);
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
//                        flockAdapter = new FlockAdapter(mainActivity, flocks);
//                        setListAdapter(flockAdapter);
                        Log.i("HomeFragment", "onNext called");

                        flockAdapter.clear();
                        if (flocks != null) {
                            for(FlockV2 flock : flocks) {
                                flockAdapter.insert(flock, flockAdapter.getCount());
                            }
                        }

                        flockAdapter.notifyDataSetChanged();
                    }
                });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        flockAdapter = new FlockAdapter(mainActivity, flocks);
        setListAdapter(flockAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
        // use Bundle and fragment.setArguments if required to pass additional data
        MainFlockFragment fragment = (MainFlockFragment)getFragmentManager().findFragmentByTag(Constants.FLOCK_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new MainFlockFragment();
        }
        fragment.setFlock(flocks.get(position));
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.switchFragment(
                fragment,
                R.anim.fragment_slide_in_left,
                R.anim.fragment_slide_out_right,
                Constants.FLOCK_FRAGMENT_TAG,
                false,
                false,
                true
        );
    }

}
