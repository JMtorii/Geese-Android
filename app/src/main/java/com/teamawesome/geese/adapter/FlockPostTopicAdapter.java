package com.teamawesome.geese.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.ResponseBody;
import com.teamawesome.geese.R;
import com.teamawesome.geese.rest.model.Post;
import com.teamawesome.geese.rest.model.UserVote;
import com.teamawesome.geese.util.RestClient;
import com.teamawesome.geese.view.UpvoteDownvoteListener;
import com.teamawesome.geese.view.UpvoteDownvoteView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by MichaelQ on 2015-10-04.
 */
public class FlockPostTopicAdapter extends ArrayAdapter<Post> {
    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView description;
        ImageView image;
        TextView metadata;
        TextView comments;
        UpvoteDownvoteView upvoteDownvoteView;
        int position;
    }

    //TODO: fix the vote thing, using it to keep track locally for now
    private UpvoteDownvoteListener mUpvoteDownvoteListener = new UpvoteDownvoteListener() {
        @Override
        public void onUpvoteClicked(UpvoteDownvoteView v) {
            int index = (Integer)v.getTag();
            Post postTopic = getItem(index);
            UserVote userVote = postTopic.getUserVote();
            int currentVote = userVote.getValue();
            if (currentVote != 1) {
                if (currentVote == 0) {
                    postTopic.setScore(postTopic.getScore() + 1);
                } else {
                    postTopic.setScore(postTopic.getScore() + 2);
                }
                userVote.setValue(1);
                v.setUpVoted();
                voteForPost(postTopic.getId(), 1);
            } else {
                userVote.setValue(0);
                postTopic.setScore(postTopic.getScore() - 1);
                v.setNotVoted();
                voteForPost(postTopic.getId(), 0);
            }
            v.setVotesText(Integer.toString(postTopic.getScore()));
        }

        @Override
        public void onDownvoteClicked(UpvoteDownvoteView v) {
            int index = (Integer)v.getTag();
            Post postTopic = getItem(index);
            UserVote userVote = postTopic.getUserVote();
            int currentVote = userVote.getValue();
            if (currentVote != -1) {
                if (currentVote == 0) {
                    postTopic.setScore(postTopic.getScore() - 1);
                } else {
                    postTopic.setScore(postTopic.getScore() - 2);
                }
                userVote.setValue(-1);
                v.setDownVoted();
                voteForPost(postTopic.getId(), -1);
            } else {
                userVote.setValue(0);
                postTopic.setScore(postTopic.getScore() + 1);
                v.setNotVoted();
                voteForPost(postTopic.getId(), 0);
            }
            v.setVotesText(Integer.toString(postTopic.getScore()));
        }
    };

    public FlockPostTopicAdapter(Context context, ArrayList<Post> posts) {
        super(context, R.layout.flock_post_topic_item, posts);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Post post = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.flock_post_topic_item, parent, false);
            viewHolder.title = (TextView)convertView.findViewById(R.id.flock_post_topic_title);
            viewHolder.description = (TextView)convertView.findViewById(R.id.flock_post_topic_description);
            viewHolder.image = (ImageView)convertView.findViewById(R.id.flock_post_topic_image);
            viewHolder.metadata = (TextView)convertView.findViewById(R.id.flock_post_topic_metadata);
            viewHolder.comments = (TextView)convertView.findViewById(R.id.flock_post_topic_comment_count);
            viewHolder.upvoteDownvoteView = (UpvoteDownvoteView)convertView.findViewById(R.id.flock_post_topic_upvote_downvote);
            viewHolder.upvoteDownvoteView.setUpvoteDownvoteListener(mUpvoteDownvoteListener);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
        viewHolder.title.setText(post.getTitle());
        viewHolder.description.setText(post.getDescription());
        //hide the description if nothing to show
        if (post.getDescription() == null || post.getDescription().equals("")) {
            viewHolder.description.setVisibility(View.GONE);
        } else {
            viewHolder.description.setVisibility(View.VISIBLE);
        }

        Resources resources =  getContext().getResources();
        viewHolder.metadata.setText(String.format(resources.getString(R.string.post_metadata), post.getCreatedDate(), post.getAuthorName()));
        viewHolder.comments.setText(String.format(resources.getString(R.string.comment_count_format), post.getCommentCount()));

        viewHolder.upvoteDownvoteView.setTag(position);
        viewHolder.upvoteDownvoteView.setVotesText(Integer.toString(post.getScore()));
        if (post.getUserVote().getValue() == 1) {
            viewHolder.upvoteDownvoteView.setUpVoted();
        } else if(post.getUserVote().getValue() == -1) {
            viewHolder.upvoteDownvoteView.setDownVoted();
        } else {
            viewHolder.upvoteDownvoteView.setNotVoted();
        }
        // TODO: check if image scaling is off
        viewHolder.image.setImageDrawable(null);

        // uncomment when posts have images
//        if (post.getImageURL() != null) {
//            if (post.getImageData() == null) {
//                URLImageLoader imageLoader = new URLImageLoader(new OnImageLoaded() {
//                    @Override
//                    public void onImageLoaded(Bitmap bitmap) {
//                        post.setImageData(bitmap);
//                        // check if it is still the same position before setting the image, may have changed
//                        if (position == viewHolder.position) {
//                            viewHolder.image.setImageBitmap(bitmap);
//                        }
//                    }
//                });
//                imageLoader.execute(post.getImageURL());
//            } else {
//                viewHolder.image.setImageBitmap(post.getImageData());
//            }
//        }
        return convertView;
    }

    private void voteForPost(int postId, int value) {
        RestClient.postService.voteForPost(postId, value).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.d("PostTopicVote", "Vote Success");
                } else {
                    // TODO: better error handling
                    Log.e("PostTopicVote", "Vote Failed " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO: better error handling
                Log.e("PostTopicVote", "Vote Failed" + t.getMessage());
            }
        });
    }
}
