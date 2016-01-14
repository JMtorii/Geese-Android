package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.adapter.FlockEventTopicAdapter;
import com.teamawesome.geese.object.EventHeader;
import com.teamawesome.geese.object.EventItem;
import com.teamawesome.geese.object.EventTopic;
import com.teamawesome.geese.util.Constants;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by codychung on 10/11/15.
 */
public class FlockEventTopicFragment extends ListFragment {
    ArrayList<EventItem> events = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flock_event_topic_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 0; i < 2; i++) {
            events.add(new EventHeader("Some Category"));
            for (int j = 0; j < 5; j++) {
                events.add(new EventTopic("Title " + j, new Date(), j * 10, "Description " + j));
            }
        }
        FlockEventTopicAdapter adapter = new FlockEventTopicAdapter(getActivity(), events);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (events.get(position).getViewType() == FlockEventTopicAdapter.RowType.Header.ordinal())
            return;
        FlockEventDetailsFragment fragment = (FlockEventDetailsFragment)getFragmentManager().findFragmentByTag(Constants.FLOCK_EVENT_DETAILS_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new FlockEventDetailsFragment();
        }
        fragment.setEventTopic((EventTopic) events.get(position));
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.switchFragment(
                fragment,
                R.anim.fragment_slide_in_left,
                R.anim.fragment_slide_out_right,
                Constants.FLOCK_EVENT_DETAILS_FRAGMENT_TAG,
                ((EventTopic) events.get(position)).getTitle(),
                false,
                false,
                true
        );
    }
}
