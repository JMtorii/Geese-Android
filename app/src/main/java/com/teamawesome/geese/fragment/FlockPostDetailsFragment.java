package com.teamawesome.geese.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.adapter.FlockPostCommentAdapter;
import com.teamawesome.geese.object.PostTopic;
import com.teamawesome.geese.task.OnImageLoaded;
import com.teamawesome.geese.task.URLImageLoader;
import com.teamawesome.geese.view.UpvoteDownvoteListener;
import com.teamawesome.geese.view.UpvoteDownvoteView;

/**
 * Created by MichaelQ on 2015-10-11.
 */
public class FlockPostDetailsFragment extends Fragment {

    private PostTopic mPostTopic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setPostTopic(PostTopic postTopic) {
        this.mPostTopic = postTopic;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flock_post_details, container, false);
        TextView title = (TextView)view.findViewById(R.id.flock_post_topic_title);
        TextView description = (TextView)view.findViewById(R.id.flock_post_topic_description);
        final ImageView image = (ImageView)view.findViewById(R.id.flock_post_topic_image);
        UpvoteDownvoteView upvoteDownvoteView = (UpvoteDownvoteView)view.findViewById(R.id.flock_post_topic_upvote_downvote);
        upvoteDownvoteView.setUpvoteDownvoteListener(new UpvoteDownvoteListener() {
            // TODO: vote
            @Override
            public void onUpvoteClicked(UpvoteDownvoteView v) {
                v.setVotesText(Integer.toString(mPostTopic.getUpvotes() + 1));
            }

            @Override
            public void onDownvoteClicked(UpvoteDownvoteView v) {
                v.setVotesText(Integer.toString(mPostTopic.getUpvotes() - 1));
            }
        });
        upvoteDownvoteView.setVotesText(Integer.toString(mPostTopic.getUpvotes()));
        ListView listView = (ListView)view.findViewById(R.id.flock_post_details_list);
        title.setText(mPostTopic.getTitle());
        description.setText(mPostTopic.getDescription());
        if (mPostTopic.getImageURL() != null) {
            // Theoretically, this image is probably loaded if they clicked into the topic, but check anyway
            if (mPostTopic.getImageData() == null) {
                URLImageLoader imageLoader = new URLImageLoader(new OnImageLoaded() {
                    @Override
                    public void onImageLoaded(Bitmap bitmap) {
                        mPostTopic.setImageData(bitmap);
                        image.setImageBitmap(bitmap);
                    }
                });
                imageLoader.execute(mPostTopic.getImageURL());
            } else {
                image.setImageBitmap(mPostTopic.getImageData());
            }
        }
        listView.setAdapter(new FlockPostCommentAdapter(getActivity(), mPostTopic.getPostComments()));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}

