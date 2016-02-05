package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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
        FrameLayout frameLayout = (FrameLayout)inflater.inflate(R.layout.fragment_flock_post_topic_list, container, false);
        ListView listView = (ListView)frameLayout.findViewById(R.id.flock_post_topic_list);
        mPostAdapter = new FlockPostTopicAdapter(parentActivity, mPostTopics);
        listView.setAdapter(mPostAdapter);
        fetchPostTopics();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // uncomment when posts from server have comments
//                FlockPostDetailsFragment fragment = (FlockPostDetailsFragment) getFragmentManager().findFragmentByTag(Constants.FLOCK_POST_DETAILS_FRAGMENT_TAG);
//                if (fragment == null) {
//                    fragment = new FlockPostDetailsFragment();
//                }
//                fragment.setPostTopic(mPostTopics.get(position));
//                MainActivity mainActivity = (MainActivity) getActivity();
//                mainActivity.switchFragment(
//                        fragment,
//                        R.anim.fragment_slide_in_left,
//                        R.anim.fragment_slide_out_right,
//                        Constants.FLOCK_POST_DETAILS_FRAGMENT_TAG,
//                        false,
//                        false,
//                        true
//                );
            }
        });
        // attach floating button to listview
        FloatingActionButton fab = (FloatingActionButton)frameLayout.findViewById(R.id.fab);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlockPostTopicCreateFragment fragment = (FlockPostTopicCreateFragment) getFragmentManager().findFragmentByTag(Constants.FLOCK_POST_TOPIC_CREATE_FRAGMENT);
                if (fragment == null) {
                    fragment = new FlockPostTopicCreateFragment();
                }
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

        // hack to add padding to bottom of listview
        TextView empty = new TextView(getContext());
        empty.setHeight(180);
        listView.addFooterView(empty);

        return frameLayout;
    }

    private void fetchPostTopics() {
        Observable<List<Post>> observable = RestClient.flockService.getPostsForFlock(mFlock.getId());
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
                    }
                });

    }
}
