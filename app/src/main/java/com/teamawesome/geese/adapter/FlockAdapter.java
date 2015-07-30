package com.teamawesome.geese.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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

/**
 * Created by MichaelQ on 2015-07-19.
 */
public class FlockAdapter extends ArrayAdapter<Flock> {
    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView description;
        TextView members;
        TextView privacy;
        ImageView mapImage;
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
            viewHolder.description = (TextView)convertView.findViewById(R.id.flock_list_item_description);
            viewHolder.members = (TextView)convertView.findViewById(R.id.flock_list_item_members);
            viewHolder.privacy = (TextView)convertView.findViewById(R.id.flock_list_item_privacy);
            viewHolder.mapImage = (ImageView)convertView.findViewById(R.id.flock_list_item_map_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
        viewHolder.name.setText(flock.name);
        viewHolder.description.setText(flock.description);
        viewHolder.members.setText("Members: " + flock.members);
        viewHolder.privacy.setText("Privacy: " + flock.privacy);
        if (flock.mapImage200x200() == null) {
            viewHolder.mapImage.setImageDrawable(null);
            URLImageLoader imageLoader = new URLImageLoader(new OnImageLoaded() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    flock.setMapImage200x200(bitmap);
                    // check if it is still the same position before setting the image, may have changed
                    if (position == viewHolder.position) {
                        viewHolder.mapImage.setImageBitmap(bitmap);
                    }
                }
            });
            imageLoader.execute("http://maps.google.com/maps/api/staticmap?center=" + flock.latitude + "," + flock.longitude + "&zoom=15&size=200x200&sensor=false");
        } else {
            viewHolder.mapImage.setImageBitmap(flock.mapImage200x200());
        }
        return convertView;
    }
}
