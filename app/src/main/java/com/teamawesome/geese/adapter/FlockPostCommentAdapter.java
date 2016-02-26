package com.teamawesome.geese.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.rest.model.Comment;
import com.teamawesome.geese.view.UpvoteDownvoteListener;
import com.teamawesome.geese.view.UpvoteDownvoteView;

import java.util.ArrayList;

/**
 * Created by MichaelQ on 2015-10-25.
 */
public class FlockPostCommentAdapter extends ArrayAdapter<Comment> {

    // View lookup cache
    private static class ViewHolder {
        TextView commentView;
        UpvoteDownvoteView upvoteDownvoteView;
        int position;
    }

    private UpvoteDownvoteListener mUpvoteDownvoteListener = new UpvoteDownvoteListener() {
        @Override
        public void onUpvoteClicked(UpvoteDownvoteView v) {
            int index = (Integer)v.getTag();
            Comment comment = getItem(index);
            if (comment.vote != 1) {
                comment.vote = 1;
                v.setUpVoted();
//                voteForComment(comment.getId(), 1);
            } else {
                comment.vote = 0;
                v.setNotVoted();
//                voteForComment(comment.getId(), 0);
            }
            v.setVotesText(Integer.toString(comment.getScore() + comment.vote));
        }

        @Override
        public void onDownvoteClicked(UpvoteDownvoteView v) {
            int index = (Integer)v.getTag();
            Comment comment = getItem(index);
            if (comment.vote != -1) {
                comment.vote = -1;
                v.setDownVoted();
//                voteForComment(comment.getId(), -1);
            } else {
                comment.vote = 0;
                v.setNotVoted();
//                voteForComment(comment.getId(), 0);
            }
            v.setVotesText(Integer.toString(comment.getScore() + comment.vote));
        }
    };

    public FlockPostCommentAdapter(Context context, ArrayList<Comment> comments) {
        super(context, R.layout.flock_post_comment_item, comments);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Comment comment = getItem(position);
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
        viewHolder.commentView.setText(comment.getText());
        viewHolder.upvoteDownvoteView.setTag(position);
        viewHolder.upvoteDownvoteView.setVotesText(Integer.toString(comment.getScore() + comment.vote));
        if (comment.vote == 1) {
            viewHolder.upvoteDownvoteView.setUpVoted();
        } else if (comment.vote == -1) {
            viewHolder.upvoteDownvoteView.setDownVoted();
        } else {
            viewHolder.upvoteDownvoteView.setNotVoted();
        }
        return convertView;
    }
}
