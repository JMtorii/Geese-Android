package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.object.EventTopic;

public class FlockEventDetailsFragment extends Fragment {
    private EventTopic mEventTopic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setEventTopic(EventTopic eventTopic) {
        this.mEventTopic = eventTopic;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flock_event_details, container, false);
        TextView title = (TextView)view.findViewById(R.id.flock_event_detail_name);
        TextView eventDateTime = (TextView)view.findViewById(R.id.flock_event_detail_time);
        TextView attendance = (TextView)view.findViewById(R.id.flock_event_detail_attendance);
        TextView description = (TextView)view.findViewById(R.id.flock_event_detail_description);
        title.setText(mEventTopic.getTitle());
        eventDateTime.setText(mEventTopic.getEventDateTime().toString());
        attendance.setText(Integer.toString(mEventTopic.getNumGuests()));
        description.setText(mEventTopic.getDescription());

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
