package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
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
 * Created by codychung on 10/7/15.
 */
public class FlockEventFragment extends FlockFragment {
    private static final String ARG_POSITION = "position";

    ArrayList<EventItem> events = new ArrayList<>();

    private int mPosition;
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

    private void addMockEvents() {
        for (int i = 0; i < 2; i++) {
            events.add(new EventHeader("Some Category"));
            for (int j = 0; j < 5; j++) {
                events.add(new EventTopic("Title " + j, new Date(), j * 10, "Description " + j));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_flock_events, container, false);
        ListView listView = (ListView) v.findViewById(R.id.flock_event_topic_list);

        // Set up the event list
        addMockEvents();
        FlockEventTopicAdapter adapter = new FlockEventTopicAdapter(getActivity(), events);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (events.get(position).getViewType() == FlockEventTopicAdapter.RowType.Header.ordinal())
                    return;
                FlockEventDetailsFragment fragment = (FlockEventDetailsFragment) getFragmentManager().findFragmentByTag(Constants.FLOCK_EVENT_DETAILS_FRAGMENT_TAG);
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
        });

        // Add listener to the fab
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.attachToListView((ListView) v.findViewById(R.id.flock_event_topic_list));
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
        return v;
    }
}
