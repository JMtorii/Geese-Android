package com.teamawesome.geese.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.object.Flock;
import com.teamawesome.geese.task.OnImageLoaded;
import com.teamawesome.geese.task.URLImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by MichaelQ on 2015-07-19.
 */
public class FlockAdapter extends ArrayAdapter<Flock> {
    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView location;
        TextView members;
        TextView privacy;
        ImageView image;
        int position;
    }

    public FlockAdapter(Context context, ArrayList<Flock> flocks) {
        super(context, R.layout.flock_list_item, flocks);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Flock flock = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.flock_list_item, parent, false);
            viewHolder.name = (TextView)convertView.findViewById(R.id.flock_list_item_name);
            viewHolder.location = (TextView)convertView.findViewById(R.id.flock_list_item_location);
            viewHolder.members = (TextView)convertView.findViewById(R.id.flock_list_item_members);
            viewHolder.privacy = (TextView)convertView.findViewById(R.id.flock_list_item_privacy);
            viewHolder.image = (ImageView)convertView.findViewById(R.id.flock_list_item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
        viewHolder.name.setText(flock.name);

        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(flock.latitude, flock.longitude, 1);
            if (addresses.size() > 0) {
                viewHolder.location.setText(addresses.get(0).getLocality());
            } else {
                viewHolder.location.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            viewHolder.location.setVisibility(View.GONE);
        }

        viewHolder.members.setText("Members: " + flock.members);
        viewHolder.privacy.setText("Privacy: " + flock.privacy);
        URLImageLoader profileImageLoader = new URLImageLoader(viewHolder.image);
        profileImageLoader.execute(flock.imageURL);

        return convertView;
    }
}
