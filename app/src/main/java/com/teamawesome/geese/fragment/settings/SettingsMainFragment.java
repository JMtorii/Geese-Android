package com.teamawesome.geese.fragment.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.adapter.SettingsListAdapter;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by JMtorii on 15-07-14.
 */
public class SettingsMainFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StickyListHeadersListView stickyList = (StickyListHeadersListView)
                getActivity().findViewById(R.id.settings_list);

        SettingsListAdapter adapter = new SettingsListAdapter(getActivity());
        stickyList.setAdapter(adapter);
//        String[] values = new String[] { "one", "two", "three", "four", "five", "six", "seven",
//                "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen" };
//
//        // TODO: use a custom adapter and view
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1, values);
//
//        setListAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        // TODO implement some logic
//    }
}
