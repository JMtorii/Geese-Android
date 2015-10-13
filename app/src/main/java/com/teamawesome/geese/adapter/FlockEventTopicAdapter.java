package com.teamawesome.geese.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.object.EventTopic;

import java.util.ArrayList;

/**
 * Created by codychung on 10/12/15.
 */
public class FlockEventTopicAdapter extends ArrayAdapter<EventTopic> {
    private static class ViewHolder {
        TextView title;
        TextView eventDateTime;
        TextView numGuests;
        int position;
    }

    public FlockEventTopicAdapter(Context context, ArrayList<EventTopic> events) {
        super(context, R.layout.flock_event_topic_item, events);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final EventTopic event = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.flock_event_topic_item, parent, false);
            viewHolder.title = (TextView)convertView.findViewById(R.id.flock_event_topic_name);
            viewHolder.eventDateTime = (TextView)convertView.findViewById(R.id.flock_event_topic_time);
            viewHolder.numGuests = (TextView)convertView.findViewById(R.id.flock_event_topic_attendance);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
        viewHolder.title.setText(event.getTitle());
        viewHolder.eventDateTime.setText(event.getEventDateTime().toString());
        viewHolder.numGuests.setText(Integer.toString(event.getNumGuests()));
        return convertView;
    }
}
