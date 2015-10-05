package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.adapter.FlockPostTopicAdapter;
import com.teamawesome.geese.object.PostTopic;

import java.util.ArrayList;

/**
 * Created by MichaelQ on 2015-10-04.
 */
public class FlockPostTopicFragment extends ListFragment {
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
        ArrayList<PostTopic> posts = new ArrayList<PostTopic>();
        for (int i = 0; i < 10; i++) {
            posts.add(new PostTopic("Title " + i, "Description " + i, i));
        }
        ArrayAdapter<PostTopic> adapter = new FlockPostTopicAdapter(getActivity(), posts);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //TODO: post details
    }
}
