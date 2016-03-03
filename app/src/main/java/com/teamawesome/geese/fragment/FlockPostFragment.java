package com.teamawesome.geese.fragment;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.adapter.FlockPostTopicAdapter;
import com.teamawesome.geese.rest.model.Post;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.RestClient;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MichaelQ on 2015-07-18.
 */
public class FlockPostFragment extends FlockFragment {
    private static final String ARG_POSITION = "position";

    private int mPosition;
    ArrayList<Post> mPostTopics = new ArrayList<>();
    private ArrayAdapter<Post> mPostAdapter = null;
    private SwipeRefreshLayout swipeContainer;

    private Snackbar snackbar;

    public static FlockPostFragment newInstance(int position) {
        FlockPostFragment f = new FlockPostFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flock_post_topic_list, container, false);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.flock_post_topic_list_fragment_layout);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPostTopics();

                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        ListView listView = (ListView) view.findViewById(R.id.flock_post_topic_list);
        mPostAdapter = new FlockPostTopicAdapter(parentActivity, mPostTopics);
        listView.setAdapter(mPostAdapter);
        fetchPostTopics();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FlockPostDetailsFragment fragment = (FlockPostDetailsFragment) getFragmentManager().findFragmentByTag(Constants.FLOCK_POST_DETAILS_FRAGMENT_TAG);
                if (fragment == null) {
                    fragment = new FlockPostDetailsFragment();
                    fragment.addListener(new FlockPostDetailsFragment.OnPostUpdatedListener() {
                        @Override
                        public void onPostUpdated() {
                            mPostAdapter.notifyDataSetChanged();
                        }
                    });
                }
                if (position >= mPostTopics.size()) {
                    // the footer counts as an item clicked
                    return;
                }
                fragment.setPostTopic(mPostTopics.get(position));
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.switchFragment(
                        fragment,
                        FragmentTransaction.TRANSIT_NONE,
                        FragmentTransaction.TRANSIT_NONE,
                        Constants.FLOCK_POST_DETAILS_FRAGMENT_TAG,
                        mPostTopics.get(position).getTitle(),
                        false,
                        false,
                        true
                );
            }
        });
        // attach floating button to listview
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlockPostTopicCreateFragment fragment = (FlockPostTopicCreateFragment) getFragmentManager().findFragmentByTag(Constants.FLOCK_POST_TOPIC_CREATE_FRAGMENT);
                if (fragment == null) {
                    fragment = new FlockPostTopicCreateFragment();
                    fragment.addListener(new FlockPostTopicCreateFragment.OnPostCreatedListener() {
                        @Override
                        public void onPostCreated() {
                            fetchPostTopics();
                        }
                    });
                }

                fragment.setFlockId(mFlock.getId());
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.switchFragment(
                        fragment,
                        R.anim.fragment_slide_in_left,
                        R.anim.fragment_slide_out_right,
                        Constants.FLOCK_POST_TOPIC_CREATE_FRAGMENT,
                        Constants.NEW_POST_TITLE,
                        false,
                        false,
                        true
                );
            }
        });

        return view;
    }

    private void fetchPostTopics() {
        Observable<List<Post>> observable = RestClient.postService.getPostsForFlock(mFlock.getId());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Post>>() {
                    @Override
                    public void onCompleted() {
                        // nothing to do here
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("FlockPostFragment", "error: " + e.getMessage());

                        snackbar = Snackbar
                                .make(parentActivity.findViewById(R.id.flock_post_topic_list_fragment_layout), "Error Occurred", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.RED);

                        View snackbarView = snackbar.getView();
                        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setGravity(Gravity.CENTER);

                        swipeContainer.setRefreshing(false);

                        snackbar.show();
                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        Log.i("FlockPostFragment", "onNext called");

                        mPostAdapter.clear();
                        if (posts != null) {
                            for(Post post: posts) {
                                mPostAdapter.insert(post, mPostAdapter.getCount());
                            }
                        }

                        mPostAdapter.notifyDataSetChanged();
//                        swipeContainer.setRefreshing(false);

                        swipeContainer.setRefreshing(false);
                    }
                });

    }
}
