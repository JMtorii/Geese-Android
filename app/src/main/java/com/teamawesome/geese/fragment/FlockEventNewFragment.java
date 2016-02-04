package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamawesome.geese.R;

import java.lang.reflect.Field;

/**
 * Created by Cody on 2016-01-11.
 */
public class FlockEventNewFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragement_flock_event_new, container, false);
    }
}
