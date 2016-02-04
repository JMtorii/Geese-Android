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

import com.melnykov.fab.FloatingActionButton;
import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.adapter.FlockPostCommentAdapter;
import com.teamawesome.geese.object.PostTopic;
import com.teamawesome.geese.task.OnImageLoaded;
import com.teamawesome.geese.task.URLImageLoader;
import com.teamawesome.geese.util.Constants;
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
        view.setClickable(true);
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
        View header = view.findViewById(R.id.flock_post_topic_item);
        //remove from parent since that's just a placeholder spot
        ((ViewGroup)header.getParent()).removeView(header);
        // Add layout param or it crashes
        // http://stackoverflow.com/questions/4393775/android-classcastexception-when-adding-a-header-view-to-expandablelistview
        header.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.FILL_PARENT, ListView.LayoutParams.WRAP_CONTENT));
        listView.addHeaderView(header);
        listView.setAdapter(new FlockPostCommentAdapter(getActivity(), mPostTopic.getPostComments()));

        // attach floating button to listview
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlockPostCommentCreateFragment fragment = (FlockPostCommentCreateFragment) getFragmentManager().findFragmentByTag(Constants.FLOCK_POST_COMMENT_CREATE_FRAGMENT);
                if (fragment == null) {
                    fragment = new FlockPostCommentCreateFragment();
                }
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.switchFragment(
                        fragment,
                        R.anim.fragment_slide_in_left,
                        R.anim.fragment_slide_out_right,
                        Constants.FLOCK_POST_COMMENT_CREATE_FRAGMENT,
                        Constants.NEW_POST_COMMENT_TITLE,
                        false,
                        false,
                        true
                );
            }
        });

        // hack to add padding to bottom of listview
        TextView empty = new TextView(getContext());
        empty.setHeight(180);
        listView.addFooterView(empty);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}

