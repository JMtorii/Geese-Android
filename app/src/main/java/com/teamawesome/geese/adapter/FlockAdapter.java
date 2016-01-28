package com.teamawesome.geese.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.teamawesome.geese.R;
import com.teamawesome.geese.rest.model.FlockV2;
import com.teamawesome.geese.task.URLImageLoader;

import java.util.List;
import java.util.Locale;

/**
 * Created by MichaelQ on 2015-07-19.
 */
public class FlockAdapter extends ArrayAdapter<FlockV2> {
    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView location;
        TextView members;
        TextView privacy;
        ImageView image;
        MaterialFavoriteButton favoriteButton;
        int position;
    }

    public FlockAdapter(Context context, List<FlockV2> flocks) {
        super(context, R.layout.flock_list_item, flocks);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FlockV2 flock = getItem(position);
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
            viewHolder.favoriteButton = (MaterialFavoriteButton) convertView.findViewById(R.id.flock_list_item_fav_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
        viewHolder.name.setText(flock.getName());

        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(flock.getLatitude(), flock.getLongitude(), 1);
            if (addresses.size() > 0) {
                viewHolder.location.setText(addresses.get(0).getLocality());
            } else {
                viewHolder.location.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            viewHolder.location.setVisibility(View.GONE);
        }

//        viewHolder.members.setText("Members: " + flock.members);
//        viewHolder.privacy.setText("Privacy: " + flock.privacy);
        viewHolder.members.setText("Members: 50");
        viewHolder.privacy.setText("Privacy: Public");

        URLImageLoader profileImageLoader = new URLImageLoader(viewHolder.image);
        //profileImageLoader.execute(flock.imageURL);
        profileImageLoader.execute("http://justinhackworth.com/canada-goose-01.jpg");

        viewHolder.favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                Log.i("FlockAdapter", Boolean.toString(favorite));
            }
        });

        return convertView;
    }
}
