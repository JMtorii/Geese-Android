package com.teamawesome.geese.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.object.EventItem;
import com.teamawesome.geese.object.EventTopic;

import java.util.ArrayList;

/**
 * Created by codychung on 10/12/15.
 */
public class FlockEventTopicAdapter extends ArrayAdapter<EventItem> {
    public enum RowType {
        Header,
        Topic,
    }

    private static class ViewHolder {
        TextView title;
        TextView eventDateTime;
        TextView numGuests;
        TextView description;
    }

    private LayoutInflater mInflater;

    public FlockEventTopicAdapter(Context context, ArrayList<EventItem> events) {
        super(context, 0, events);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position).getViewType() == RowType.Topic.ordinal();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            switch (getItemViewType(position)) {
                case 0:
                    // Header
                    convertView = mInflater.inflate(R.layout.flock_event_topic_header, parent, false);
                    holder.title = (TextView) convertView.findViewById(R.id.flock_event_topic_header_text);
                    break;
                case 1:
                    // Topic
                    convertView = mInflater.inflate(R.layout.flock_event_topic_item, parent, false);
                    holder.title = (TextView) convertView.findViewById(R.id.flock_event_topic_name);
                    holder.eventDateTime = (TextView) convertView.findViewById(R.id.flock_event_topic_time);
                    holder.numGuests = (TextView) convertView.findViewById(R.id.flock_event_topic_attendance);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(getItem(position).getTitle());
        if (getItemViewType(position) == RowType.Topic.ordinal()) {
            holder.eventDateTime.setText(((EventTopic) getItem(position)).getEventDateTime().toString());
            holder.numGuests.setText(String.valueOf(((EventTopic) getItem(position)).getNumGuests()));
        }
        return convertView;
    }
}
