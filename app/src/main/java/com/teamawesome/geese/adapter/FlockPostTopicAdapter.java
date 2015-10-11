package com.teamawesome.geese.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.object.PostTopic;
import com.teamawesome.geese.view.UpvoteDownvoteListener;
import com.teamawesome.geese.view.UpvoteDownvoteView;

import java.util.ArrayList;

/**
 * Created by MichaelQ on 2015-10-04.
 */
public class FlockPostTopicAdapter extends ArrayAdapter<PostTopic> {
    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView description;
        UpvoteDownvoteView upvoteDownvoteView;
        int position;
    }

    private UpvoteDownvoteListener mUpvoteDownvoteListener = new UpvoteDownvoteListener() {
        //TODO: ACTUALLY VOTE, not just change the number
        @Override
        public void onUpvoteClicked(UpvoteDownvoteView v) {
            int index = (Integer)v.getTag();
            PostTopic postTopic = getItem(index);
            v.setVotesText(Integer.toString(postTopic.getUpvotes() + 1));
        }

        @Override
        public void onDownvoteClicked(UpvoteDownvoteView v) {
            int index = (Integer)v.getTag();
            PostTopic postTopic = getItem(index);
            v.setVotesText(Integer.toString(postTopic.getUpvotes() - 1));
        }
    };

    public FlockPostTopicAdapter(Context context, ArrayList<PostTopic> posts) {
        super(context, R.layout.flock_post_topic_item, posts);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PostTopic post = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.flock_post_topic_item, parent, false);
            viewHolder.title = (TextView)convertView.findViewById(R.id.flock_post_topic_title);
            viewHolder.description = (TextView)convertView.findViewById(R.id.flock_post_topic_description);
            viewHolder.upvoteDownvoteView = (UpvoteDownvoteView)convertView.findViewById(R.id.flock_post_topic_upvote_downvote);
            viewHolder.upvoteDownvoteView.setUpvoteDownvoteListener(mUpvoteDownvoteListener);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
        viewHolder.title.setText(post.getTitle());
        viewHolder.description.setText(post.getDescription());
        viewHolder.upvoteDownvoteView.setTag(position);
        viewHolder.upvoteDownvoteView.setVotesText(Integer.toString(getItem(position).getUpvotes()));
        return convertView;
    }
}
