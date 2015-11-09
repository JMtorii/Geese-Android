package com.teamawesome.geese.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.object.PostComment;
import com.teamawesome.geese.view.UpvoteDownvoteListener;
import com.teamawesome.geese.view.UpvoteDownvoteView;

import java.util.ArrayList;

/**
 * Created by MichaelQ on 2015-10-25.
 */
public class FlockPostCommentAdapter extends ArrayAdapter<PostComment> {

    // View lookup cache
    private static class ViewHolder {
        TextView commentView;
        UpvoteDownvoteView upvoteDownvoteView;
        int position;
    }

    private UpvoteDownvoteListener mUpvoteDownvoteListener = new UpvoteDownvoteListener() {
        //TODO: ACTUALLY VOTE, not just change the number
        @Override
        public void onUpvoteClicked(UpvoteDownvoteView v) {
            int index = (Integer)v.getTag();
            PostComment postComment = getItem(index);
            v.setVotesText(Integer.toString(postComment.getUpvotes() + 1));
        }

        @Override
        public void onDownvoteClicked(UpvoteDownvoteView v) {
            int index = (Integer)v.getTag();
            PostComment postComment = getItem(index);
            v.setVotesText(Integer.toString(postComment.getUpvotes() - 1));
        }
    };

    public FlockPostCommentAdapter(Context context, ArrayList<PostComment> comments) {
        super(context, R.layout.flock_post_comment_item, comments);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PostComment comment = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.flock_post_comment_item, parent, false);
            viewHolder.commentView = (TextView) convertView.findViewById(R.id.flock_post_comment);
            viewHolder.upvoteDownvoteView = (UpvoteDownvoteView)convertView.findViewById(R.id.flock_post_comment_upvote_downvote);
            viewHolder.upvoteDownvoteView.setUpvoteDownvoteListener(mUpvoteDownvoteListener);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
        viewHolder.commentView.setText(comment.getComment());
        viewHolder.upvoteDownvoteView.setTag(position);
        viewHolder.upvoteDownvoteView.setVotesText(Integer.toString(getItem(position).getUpvotes()));
        return convertView;
    }
}