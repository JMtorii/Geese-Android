package com.teamawesome.geese.fragment.debug;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamawesome.geese.R;

/**
 * Created by JMtorii on 15-07-04.
 */
public class DebugLoginIntroFragment extends Fragment {

    public static final String ARG_PAGE = "page";
    private int mPageNumber;

    public static DebugLoginIntroFragment create(int pageNumber) {
        DebugLoginIntroFragment fragment = new DebugLoginIntroFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public DebugLoginIntroFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.debug_fragment_login_intro, container, false);

        // Set the title view to show the page number.
        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                "pageNumberTest");

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
