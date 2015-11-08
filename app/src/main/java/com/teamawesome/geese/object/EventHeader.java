package com.teamawesome.geese.object;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.adapter.FlockEventTopicAdapter;

/**
 * Created by codychung on 11/7/15.
 */
public class EventHeader implements EventItem {
    private String title;

    public EventHeader(String title) {
        this.title = title;
    }

    @Override
    public int getViewType() {
        return FlockEventTopicAdapter.RowType.Header.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.flock_event_topic_header, null);
        } else {
            view = convertView;
        }
        TextView titleTextView = (TextView) view.findViewById(R.id.flock_event_topic_header_text);
        titleTextView.setText(title);
        return view;
    }
}
