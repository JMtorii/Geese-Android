package com.teamawesome.geese.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teamawesome.geese.R;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by JMtorii on 15-07-29.
 */
public class SettingsListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private String[] titles = {"test1", "test2", "test3", "test4", "test5", "test6", "test7",
            "test8", "test9", "test10", "test11", "test12", "test13", "test14", "hello1", "hello2",
            "hello3", "hello4", "hello5", "hello6", "hello7", "hello8", "hello9", "world"};
    private LayoutInflater inflater;

    public SettingsListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.settings_list_item, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.settings_item_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(titles[position]);

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.settings_list_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.header_text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        String headerText = "" + titles[position].subSequence(0, 1).charAt(0);
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return titles[position].subSequence(0, 1).charAt(0);
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }
}
