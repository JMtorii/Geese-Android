package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamawesome.geese.R;

/**
 * Created by JMtorii on 16-01-13.
 */
public class FavouriteFlocksFragment extends GeeseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_flocks, container, false);
        return view;
    }
}
