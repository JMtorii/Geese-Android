package com.teamawesome.geese.object;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.adapter.FlockEventTopicAdapter;

import java.util.Date;

/**
 * Created by codychung on 10/11/15.
 */
public class EventTopic implements EventItem {
    private String title;
    private Date eventDateTime;
    private int numGuests;

    public EventTopic(String title, Date time, Integer guests) {
        this.title = title;
        this.eventDateTime = time;
        this.numGuests = guests;
    }

    @Override
    public int getViewType() {
        return FlockEventTopicAdapter.RowType.Topic.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.flock_event_topic_item, null);
        } else {
            view = convertView;
        }
        TextView titleTextView = (TextView) view.findViewById(R.id.flock_event_topic_name);
        TextView dateTextView = (TextView) view.findViewById(R.id.flock_event_topic_time);
        TextView numGuestTextView = (TextView) view.findViewById(R.id.flock_event_topic_attendance);
        titleTextView.setText(title);
        dateTextView.setText(eventDateTime.toString());
        numGuestTextView.setText(Integer.toString(numGuests));
        return view;
    }
}
