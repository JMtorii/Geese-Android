package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamawesome.geese.R;

/**
 * Created by MichaelQ on 2015-07-18.
 */
public class FlockPostFragment extends FlockFragment {
    private static final String ARG_POSITION = "position";

    private int mPosition;
    private FlockPostTopicFragment postTopicFragment = new FlockPostTopicFragment();

    public static FlockPostFragment newInstance(int position) {
        FlockPostFragment f = new FlockPostFragment();
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
        View v = inflater.inflate(R.layout.fragment_flock_posts, container, false);
        getFragmentManager().beginTransaction().add(R.id.flock_post_linear_layout, postTopicFragment).commit();
        return v;
    }
}
