package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.util.Constants;

/**
 * Created by codychung on 10/7/15.
 */
public class FlockEventFragment extends FlockFragment {
    private static final String ARG_POSITION = "position";

    private int mPosition;
    private FlockEventTopicFragment eventTopicFragment = new FlockEventTopicFragment();
    private FlockEventNewFragment eventNewFragment = new FlockEventNewFragment();

    public static FlockEventFragment newInstance(int position) {
        FlockEventFragment f = new FlockEventFragment();
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
        View v = inflater.inflate(R.layout.fragment_flock_events, container, false);

        // Add listener to the fab
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.new_event_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.switchFragment(
                        eventNewFragment,
                        R.anim.fragment_slide_in_left,
                        R.anim.fragment_slide_out_right,
                        Constants.FLOCK_EVENT_NEW_FRAGMENT_TAG,
                        Constants.NEW_EVENT_TITLE,
                        false,
                        false,
                        true
                );
            }
        });

        getFragmentManager().beginTransaction().add(R.id.flock_event_linear_layout, eventTopicFragment).commit();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getFragmentManager().beginTransaction().remove(eventTopicFragment).commit();
    }
}
