package com.teamawesome.geese.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.okhttp.ResponseBody;
import com.teamawesome.geese.R;
import com.teamawesome.geese.rest.model.Comment;
import com.teamawesome.geese.rest.model.UserVote;
import com.teamawesome.geese.util.RestClient;
import com.teamawesome.geese.view.UpvoteDownvoteListener;
import com.teamawesome.geese.view.UpvoteDownvoteView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by MichaelQ on 2015-10-25.
 */
public class FlockPostCommentAdapter extends ArrayAdapter<Comment> {

    // View lookup cache
    private static class ViewHolder {
        TextView commentView;
        TextView metadata;
        UpvoteDownvoteView upvoteDownvoteView;
        int position;
    }

    private UpvoteDownvoteListener mUpvoteDownvoteListener = new UpvoteDownvoteListener() {
        @Override
        public void onUpvoteClicked(UpvoteDownvoteView v) {
            int index = (Integer)v.getTag();
            Comment comment = getItem(index);
            UserVote userVote = comment.getUserVote();
            int currentVote = userVote.getValue();
            if (currentVote != 1) {
                if (currentVote == 0) {
                    comment.setScore(comment.getScore() + 1);
                } else {
                    comment.setScore(comment.getScore() + 2);
                }
                userVote.setValue(1);
                v.setUpVoted();
                voteForComment(comment.getCommentId(), 1);
            } else {
                userVote.setValue(0);
                comment.setScore(comment.getScore() - 1);
                v.setNotVoted();
                voteForComment(comment.getCommentId(), 0);
            }
            v.setVotesText(Integer.toString(comment.getScore()));
        }

        @Override
        public void onDownvoteClicked(UpvoteDownvoteView v) {
            int index = (Integer)v.getTag();
            Comment comment = getItem(index);
            UserVote userVote = comment.getUserVote();
            int currentVote = userVote.getValue();
            if (currentVote != -1) {
                if (currentVote == 0) {
                    comment.setScore(comment.getScore() - 1);
                } else {
                    comment.setScore(comment.getScore() - 2);
                }
                userVote.setValue(-1);
                v.setDownVoted();
                voteForComment(comment.getCommentId(), -1);
            } else {
                userVote.setValue(0);
                comment.setScore(comment.getScore() + 1);
                v.setNotVoted();
                voteForComment(comment.getCommentId(), 0);
            }
            v.setVotesText(Integer.toString(comment.getScore()));
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
            viewHolder.metadata = (TextView) convertView.findViewById(R.id.flock_post_comment_metadata);
            viewHolder.upvoteDownvoteView = (UpvoteDownvoteView)convertView.findViewById(R.id.flock_post_comment_upvote_downvote);
            viewHolder.upvoteDownvoteView.setUpvoteDownvoteListener(mUpvoteDownvoteListener);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
        viewHolder.commentView.setText(comment.getText());
        viewHolder.upvoteDownvoteView.setTag(position);
        viewHolder.upvoteDownvoteView.setVotesText(Integer.toString(comment.getScore()));
        viewHolder.metadata.setText(comment.getAuthorName());
        if (comment.getUserVote().getValue() == 1) {
            viewHolder.upvoteDownvoteView.setUpVoted();
        } else if (comment.getUserVote().getValue() == -1) {
            viewHolder.upvoteDownvoteView.setDownVoted();
        } else {
            viewHolder.upvoteDownvoteView.setNotVoted();
        }
        return convertView;
    }

    private void voteForComment(int commentId, int value) {
        RestClient.postService.voteForComment(commentId, value).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.d("CommentVote", "Vote Success");
                } else {
                    // TODO: better error handling
                    Log.e("CommentVote", "Vote Failed " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO: better error handling
                Log.e("CommentVote", "Vote Failed" + t.getMessage());
            }
        });
    }
}
