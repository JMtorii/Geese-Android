package com.teamawesome.swap.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamawesome.swap.R;
import com.teamawesome.swap.util.Constants;

/**
 * Created by JMtorii on 15-07-14.
 */
public class HomeFragment extends ListFragment {
    private final static String TAG_FRAGMENT = Constants.HOME_FRAGMENT_TAG;

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
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "test", "test", "test", "test" };

        // TODO: use a custom adapter and view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
        // use Bundle and fragment.setArguments if required to pass additional data
        Fragment fragment = new FlockProfileFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame, fragment, Constants.FLOCK_PROFILE_FRAGMENT_TAG)
                .addToBackStack(Constants.FLOCK_PROFILE_FRAGMENT_TAG).commit();
    }

}
