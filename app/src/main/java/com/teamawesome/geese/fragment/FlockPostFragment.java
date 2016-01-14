package com.teamawesome.geese.fragment;

import android.os.Bundle;
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
import com.teamawesome.geese.object.PostComment;
import com.teamawesome.geese.object.PostTopic;
import com.teamawesome.geese.util.Constants;

import java.util.ArrayList;

/**
 * Created by MichaelQ on 2015-07-18.
 */
public class FlockPostFragment extends FlockFragment {
    private static final String ARG_POSITION = "position";

    private int mPosition;
    ArrayList<PostTopic> mPostTopics = null;

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

        if (mPostTopics == null) {
            ArrayList<PostComment> comments = new ArrayList<PostComment>();
            comments.add(new PostComment("Short comment", 100));
            comments.add(new PostComment("long long long long long long long long long long long long long long long long long long long comment", 4));
            comments.add(new PostComment("Short comment", 100));
            comments.add(new PostComment("long long long long long long long long long long long long long long long long long long long " +
                    "long long long long long long long long long long long long long long long long long long long comment", 4));
            comments.add(new PostComment("Short comment", 100));
            comments.add(new PostComment("long long long long long long long long long long long long long long long long long long long comment", 4));
            ArrayList<PostTopic> posts = new ArrayList<PostTopic>();
            for (int i = 0; i < 10; i++) {
                if (i == 5) {
                    //ITS PIKACHU!!!
                    posts.add(new PostTopic("PIKACHU", "I CHOOSE YOU", comments, "http://www.darktheatre.net/wiki/images/8/89/Pikachu.jpg", i));
                } else if (i == 6) {
                    posts.add(new PostTopic("DR BALANCED", "Boom bots OP", comments, "http://hydra-media.cursecdn.com/hearthstone.gamepedia.com/thumb/f/f5/Dr._Boom_by_Alex_Garner.png/390px-Dr._Boom_by_Alex_Garner.png?version=8d87b25588cea42813c20de77a5314e2", i));
                } else {
                    posts.add(new PostTopic("Title " + i, "Description " + i, comments, null, i));
                }
            }
            mPostTopics = posts;
        }
        ArrayAdapter<PostTopic> adapter = new FlockPostTopicAdapter(getActivity(), mPostTopics);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FlockPostDetailsFragment fragment = (FlockPostDetailsFragment) getFragmentManager().findFragmentByTag(Constants.FLOCK_POST_DETAILS_FRAGMENT_TAG);
                if (fragment == null) {
                    fragment = new FlockPostDetailsFragment();
                }
                fragment.setPostTopic(mPostTopics.get(position));
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.switchFragment(
                        fragment,
                        R.anim.fragment_slide_in_left,
                        R.anim.fragment_slide_out_right,
                        Constants.FLOCK_POST_DETAILS_FRAGMENT_TAG,
                        false,
                        false,
                        true
                );
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
}
