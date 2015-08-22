package com.teamawesome.geese.fragment.debug;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamawesome.geese.R;

/**
 * Created by JMtorii on 15-07-30.
 */
public class SuperAwesomeCardFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    TextView textView;

    private int position;

    public static SuperAwesomeCardFragment newInstance(int position) {
        SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.debug_fragment_card,container,false);
        textView = (TextView) rootView.findViewById(R.id.card_textView);
        textView.setText("CARD "+position);
        return rootView;
    }
}
