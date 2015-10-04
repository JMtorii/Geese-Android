package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.adapter.FlockAdapter;
import com.teamawesome.geese.object.Flock;
import com.teamawesome.geese.util.Constants;

import java.util.ArrayList;

/**
 * Created by JMtorii on 15-07-14.
 */
public class HomeFragment extends ListFragment {
    private final static String TAG_FRAGMENT = Constants.HOME_FRAGMENT_TAG;

    private ArrayList<Flock> flocks = new ArrayList<Flock>();

    public HomeFragment() {
        //set up initial flocks for now
        flocks.add(new Flock.FlockBuilder().name("Hearthstone").description("Welcome to the Hearthstone flock").members(100).privacy("Invite Only").build());
        flocks.add(new Flock.FlockBuilder().name("Pokemon").description("Welcome to the Pokemon flock").members(50).privacy("Public").latitude(43.6413496).longitude(-79.3874165).build());
        flocks.add(new Flock.FlockBuilder().name("Android").description("Welcome to the Android flock").members(200).privacy("Public").build());
        flocks.add(new Flock.FlockBuilder().name("iOS").description("Welcome to the iOS flock").members(200).privacy("Public").latitude(43.6413496).longitude(-79.3874165).build());
        flocks.add(new Flock.FlockBuilder().name("League of Legends").description("Welcome to the LoL flock").members(200).privacy("Public").latitude(43.6413496).longitude(-79.3874165).build());
        flocks.add(new Flock.FlockBuilder().name("UWaterloo").description("Welcome to the UW flock").members(200).privacy("Public").build());
        for (int i = 0; i < 10; i++) {
            flocks.add(new Flock.FlockBuilder().name("Filler").description("filler").members(200).privacy("Public").build());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<Flock> adapter = new FlockAdapter(getActivity(), flocks);
        setListAdapter(adapter);
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
