package com.teamawesome.geese.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.object.NavDrawerItem;

import java.util.List;

/**
 * Created by lcolam on 2/10/16.
 */
public class NavDrawerAdapter extends ArrayAdapter<NavDrawerItem> {
    private static class ViewHolder {
        ImageView icon;
        TextView name;
        int position;
    }

    public NavDrawerAdapter(Context context, List<NavDrawerItem> items) {
        super(context, R.layout.drawer_list_item, items);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final NavDrawerItem item = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);
            viewHolder.icon = (ImageView)convertView.findViewById(R.id.nav_drawer_icon);
            viewHolder.name = (TextView)convertView.findViewById(R.id.nav_drawer_option);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
        viewHolder.icon.setImageResource(item.getIcon());
        viewHolder.name.setText(item.getName());

        return convertView;
    }
}
