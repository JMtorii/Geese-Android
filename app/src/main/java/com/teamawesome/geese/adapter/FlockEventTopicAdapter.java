package com.teamawesome.geese.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.teamawesome.geese.R;
import com.teamawesome.geese.object.EventItem;

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
        View view;
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
                    convertView = mInflater.inflate(R.layout.flock_event_topic_header, null);
                    break;
                case 1:
                    // Topic
                    convertView = mInflater.inflate(R.layout.flock_event_topic_item, null);
                    break;
            }
            holder.view = getItem(position).getView(mInflater, convertView);
            convertView.setTag(holder);
        }
        return convertView;
    }
}
