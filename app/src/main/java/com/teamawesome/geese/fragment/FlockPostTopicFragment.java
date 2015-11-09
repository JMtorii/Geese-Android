package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.adapter.FlockPostTopicAdapter;
import com.teamawesome.geese.object.PostComment;
import com.teamawesome.geese.object.PostTopic;
import com.teamawesome.geese.util.Constants;

import java.util.ArrayList;

/**
 * Created by MichaelQ on 2015-10-04.
 */
public class FlockPostTopicFragment extends ListFragment {

    ArrayList<PostTopic>  mPostTopics = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flock_post_topic_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
        // use Bundle and fragment.setArguments if required to pass additional data
        FlockPostDetailsFragment fragment = (FlockPostDetailsFragment)getFragmentManager().findFragmentByTag(Constants.FLOCK_POST_DETAILS_FRAGMENT_TAG);
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
}
