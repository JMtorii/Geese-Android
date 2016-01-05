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

// TODO: Move string arrays to res/values and make adapter dynamic
public class SettingsListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    // TODO: move to values
//    private String[] mTitles = {"Share Goose", "Rate Goose", "Follow Us on Twitter",
//            "Follow Us on Facebook", "Follow Us on Instagram", "Contact Us/ Get Help",
//            "Rules and Info", "Terms of Service", "Privacy Policy"};
    private String[] mTitles = {"Rules and Info", "Terms of Service", "Privacy Policy"};
    private LayoutInflater mInflater;

    public SettingsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitles[position];
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
            convertView = mInflater.inflate(R.layout.settings_list_item, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.settings_item_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(mTitles[position]);

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.settings_list_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.header_text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        String headerText;
//        if (position >= 0 && position < 3) {
//            headerText = "Show Us Some Love";
//        } else if (position >= 5) {
//            headerText = "Important Stuff";
//        } else {
//            headerText = "Miscellaneous";
//        }
        headerText = "Important Stuff";
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        if (position >= 0 && position < 5) {
            return 0;
        } else if (position >= 5) {
            return 5;
        }

        return position;
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }
}
