package com.teamawesome.geese.fragment.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.adapter.SettingsListAdapter;
import com.teamawesome.geese.util.Constants;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StickyListHeadersListView stickyList = (StickyListHeadersListView) getActivity().findViewById(R.id.settings_list);
        stickyList.setAreHeadersSticky(false);
        stickyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = null;
                String tag = null;

                if (position == 0) {
                    Log.v("Position", "0");
                    fragment = new SettingsRulesFragment();
                    tag = Constants.SETTINGS_RULES_TAG;

                } else if (position == 1) {
                    Log.v("Position", "1");
                    fragment = new SettingsTermsServiceFragment();
                    tag = Constants.SETTINGS_TERMS_SERVICE_TAG;

                } else if (position == 2) {
                    Log.v("Position", "2");
                    fragment = new SettingsPrivacyPolicyFragment();
                    tag = Constants.SETTINGS_PRIVACY_POLICTY_TAG;

                }

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.switchFragment(
                        fragment,
                        R.anim.fragment_slide_in_left,
                        R.anim.fragment_slide_out_right,
                        tag,
                        false,
                        false,
                        true
                );
            }
        });

        SettingsListAdapter adapter = new SettingsListAdapter(getActivity());
        stickyList.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}