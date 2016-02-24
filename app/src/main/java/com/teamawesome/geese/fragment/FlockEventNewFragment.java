package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;

/**
 * Created by Cody on 2016-01-11.
 */
public class FlockEventNewFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_flock_event_new, container, false);
        // Inflate the layout for this fragment
        Button button = (Button)view.findViewById(R.id.flock_event_create_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Do something meaningful here.
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.popFragment();
            }
        });
        return view;
    }
}
