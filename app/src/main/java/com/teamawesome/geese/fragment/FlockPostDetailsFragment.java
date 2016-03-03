package com.teamawesome.geese.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.squareup.okhttp.ResponseBody;
import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.adapter.FlockPostCommentAdapter;
import com.teamawesome.geese.rest.model.Comment;
import com.teamawesome.geese.rest.model.Post;
import com.teamawesome.geese.rest.model.UserVote;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.RestClient;
import com.teamawesome.geese.view.UpvoteDownvoteListener;
import com.teamawesome.geese.view.UpvoteDownvoteView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MichaelQ on 2015-10-11.
 */
public class FlockPostDetailsFragment extends GeeseFragment {

    private Post mPostTopic;
    private ArrayList<Comment> mPostComments;
    private FlockPostCommentAdapter mAdapter;
    private List<OnPostUpdatedListener> listeners = new ArrayList<>();
    private Snackbar snackbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setPostTopic(Post postTopic) {
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
        TextView metadata = (TextView)view.findViewById(R.id.flock_post_topic_metadata);
        TextView comments = (TextView)view.findViewById(R.id.flock_post_topic_comment_count);
        UpvoteDownvoteView upvoteDownvoteView = (UpvoteDownvoteView)view.findViewById(R.id.flock_post_topic_upvote_downvote);
        upvoteDownvoteView.setUpvoteDownvoteListener(new UpvoteDownvoteListener() {
            @Override
            public void onUpvoteClicked(UpvoteDownvoteView v) {
                UserVote userVote = mPostTopic.getUserVote();
                int currentVote = userVote.getValue();
                if (currentVote != 1) {
                    if (currentVote == 0) {
                        mPostTopic.setScore(mPostTopic.getScore() + 1);
                    } else {
                        mPostTopic.setScore(mPostTopic.getScore() + 2);
                    }
                    userVote.setValue(1);
                    v.setUpVoted();
                    voteForPost(mPostTopic.getId(), 1);
                } else {
                    userVote.setValue(0);
                    mPostTopic.setScore(mPostTopic.getScore() - 1);
                    v.setNotVoted();
                    voteForPost(mPostTopic.getId(), 0);
                }
                v.setVotesText(Integer.toString(mPostTopic.getScore()));
                notifyListeners();
            }

            @Override
            public void onDownvoteClicked(UpvoteDownvoteView v) {
                UserVote userVote = mPostTopic.getUserVote();
                int currentVote = userVote.getValue();
                if (currentVote != -1) {
                    if (currentVote == 0) {
                        mPostTopic.setScore(mPostTopic.getScore() - 1);
                    } else {
                        mPostTopic.setScore(mPostTopic.getScore() - 2);
                    }
                    userVote.setValue(-1);
                    v.setDownVoted();
                    voteForPost(mPostTopic.getId(), -1);
                } else {
                    userVote.setValue(0);
                    mPostTopic.setScore(mPostTopic.getScore() + 1);
                    v.setNotVoted();
                    voteForPost(mPostTopic.getId(), 0);
                }
                v.setVotesText(Integer.toString(mPostTopic.getScore()));
                notifyListeners();
            }
        });
        upvoteDownvoteView.setVotesText(Integer.toString(mPostTopic.getScore()));
        if (mPostTopic.getUserVote().getValue() == 1) {
            upvoteDownvoteView.setUpVoted();
        } else if (mPostTopic.getUserVote().getValue() == -1) {
            upvoteDownvoteView.setDownVoted();
        } else {
            upvoteDownvoteView.setNotVoted();
        }
        ListView listView = (ListView)view.findViewById(R.id.flock_post_details_list);
        title.setText(mPostTopic.getTitle());
        description.setText(mPostTopic.getDescription());
        //hide the description if nothing to show
        if (mPostTopic.getDescription() == null || mPostTopic.getDescription().equals("")) {
            description.setVisibility(View.GONE);
        } else {
            description.setVisibility(View.VISIBLE);
        }
        metadata.setText(String.format(getResources().getString(R.string.comment_metadata), mPostTopic.getCreatedDate(), mPostTopic.getAuthorName()));
        comments.setText(String.format(getResources().getString(R.string.comment_count_format), mPostTopic.getCommentCount()));

        //uncomment when we have images for posts
//        if (mPostTopic.getImageURL() != null) {
//            // Theoretically, this image is probably loaded if they clicked into the topic, but check anyway
//            if (mPostTopic.getImageData() == null) {
//                URLImageLoader imageLoader = new URLImageLoader(new OnImageLoaded() {
//                    @Override
//                    public void onImageLoaded(Bitmap bitmap) {
//                        mPostTopic.setImageData(bitmap);
//                        image.setImageBitmap(bitmap);
//                    }
//                });
//                imageLoader.execute(mPostTopic.getImageURL());
//            } else {
//                image.setImageBitmap(mPostTopic.getImageData());
//            }
//        }
        View header = view.findViewById(R.id.flock_post_topic_item);
        //remove from parent since that's just a placeholder spot
        ((ViewGroup)header.getParent()).removeView(header);
        // Add layout param or it crashes
        // http://stackoverflow.com/questions/4393775/android-classcastexception-when-adding-a-header-view-to-expandablelistview
        header.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.FILL_PARENT, ListView.LayoutParams.WRAP_CONTENT));
        listView.addHeaderView(header);

        mPostComments = new ArrayList<>();
        mAdapter = new FlockPostCommentAdapter(getActivity(), mPostComments);
        listView.setAdapter(mAdapter);
        fetchPostComments();

        // attach floating button to listview
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlockPostCommentCreateFragment fragment = (FlockPostCommentCreateFragment) getFragmentManager().findFragmentByTag(Constants.FLOCK_POST_COMMENT_CREATE_FRAGMENT);
                if (fragment == null) {
                    fragment = new FlockPostCommentCreateFragment();
                    fragment.addListener(new FlockPostCommentCreateFragment.OnCommentCreatedListener() {
                        @Override
                        public void onCommentCreated() {
                            fetchPostComments();
                            mPostTopic.setCommentCount(mPostTopic.getCommentCount() + 1);
                            notifyListeners();
                            if (getView() != null) {
                                TextView comments = (TextView) getView().findViewById(R.id.flock_post_topic_comment_count);
                                comments.setText(String.format(getResources().getString(R.string.comment_count_format), mPostTopic.getCommentCount()));
                            }
                        }
                    });
                }
                fragment.setPostId(mPostTopic.getId());
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

    public void addListener(OnPostUpdatedListener listener) {
        listeners.add(listener);
    }

    private void fetchPostComments() {
        Observable<List<Comment>> observable = RestClient.postService.getCommentsForPost(mPostTopic.getId());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Comment>>() {
                    @Override
                    public void onCompleted() {
                        // nothing to do here
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("PostDetailsFragment", "error: " + e.getMessage());

                        snackbar = Snackbar
                                .make(parentActivity.findViewById(R.id.flock_post_detail_fragment_layout), "Error Occurred", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.RED);

                        View snackbarView = snackbar.getView();
                        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setGravity(Gravity.CENTER);

                        snackbar.show();
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        Log.i("PostDetailsFragment", "onNext called");
                        mPostComments = new ArrayList<Comment>();
                        mAdapter.clear();
                        if (comments != null) {
                            for (Comment comment : comments) {
                                mPostComments.add(comment);
                                mAdapter.insert(comment, mAdapter.getCount());
                            }
                        }

                        mAdapter.notifyDataSetChanged();
//                        swipeContainer.setRefreshing(false);
                    }
                });

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

                snackbar = Snackbar
                        .make(parentActivity.findViewById(R.id.flock_post_detail_fragment_layout), "Error Occurred", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED);

                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);

                snackbar.show();
            }
        });
    }

    private void notifyListeners() {
        for (OnPostUpdatedListener listener : listeners) {
            listener.onPostUpdated();
        }
    }

    public interface OnPostUpdatedListener {
        void onPostUpdated();
    }
}

