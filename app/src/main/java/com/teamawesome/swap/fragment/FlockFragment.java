package com.teamawesome.swap.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;

import com.teamawesome.swap.R;
import com.teamawesome.swap.object.Flock;
import com.teamawesome.swap.task.URLImageLoader;
import com.teamawesome.swap.util.Constants;

/**
 * Created by JMtorii on 15-07-14.
 */
public class FlockFragment extends Fragment {

    Flock mFlock;
    String currentChildFragmentID;

    private final static String TAG_FRAGMENT = Constants.FLOCK_FRAGMENT_TAG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_flock, container, false);
        final int linearLayoutId = v.findViewById(R.id.flock_linear_layout).getId();
        RadioGroup radioGroup = (RadioGroup)v.findViewById(R.id.flock_radio_group);
        radioGroup.check(R.id.flock_profile_button);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                Fragment fragment = null;
                String fragmentTag = null;
                switch (checkedId) {
                    case R.id.flock_profile_button: {
                        FlockProfileFragment profileFragment = new FlockProfileFragment();
                        profileFragment.setFlock(mFlock);
                        fragment = profileFragment;
                        fragmentTag = Constants.FLOCK_PROFILE_FRAGMENT_TAG;
                        break;
                    }
                    case R.id.flock_activity_button: {
                        FlockActivityFragment activityFragment = new FlockActivityFragment();
                        fragment = activityFragment;
                        fragmentTag = Constants.FLOCK_ACTIVITY_FRAGMENT_TAG;
                        break;
                    }
                    case R.id.flock_chat_button: {
                        FlockChatFragment chatFragment = new FlockChatFragment();
                        fragment = chatFragment;
                        fragmentTag = Constants.FLOCK_CHAT_FRAGMENT_TAG;
                        break;
                    }
                    default:
                        return;
                }
                transaction.replace(linearLayoutId, fragment, currentChildFragmentID);
                currentChildFragmentID = fragmentTag;
                transaction.commit();

            }
        });
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        FlockProfileFragment profile = new FlockProfileFragment();
        profile.setFlock(mFlock);
        transaction.add(linearLayoutId, profile, Constants.FLOCK_PROFILE_FRAGMENT_TAG);
        currentChildFragmentID = Constants.FLOCK_PROFILE_FRAGMENT_TAG;
        transaction.commit();
        return v;
    }

    @Override
    public void onDestroyView() {
        //TODO: this should work but it crashes for some reason
//        FragmentManager fragmentManager = getChildFragmentManager();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        Fragment childFragment = fragmentManager.findFragmentByTag(Constants.FLOCK_PROFILE_FRAGMENT_TAG);
//        transaction.remove(childFragment);
//        transaction.commit();
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setFlock(Flock f) {
        mFlock = f;
    }
}
