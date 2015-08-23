package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.teamawesome.geese.R;
import com.teamawesome.geese.fragment.debug.SuperAwesomeCardFragment;
import com.teamawesome.geese.object.Flock;
import com.teamawesome.geese.util.Constants;

/**
 * Created by JMtorii on 15-07-14.
 */
public class MainFlockFragment extends Fragment {
    PagerSlidingTabStrip tabs;
    ViewPager pager;

    private MyPagerAdapter adapter;

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
//        final int linearLayoutId = v.findViewById(R.id.flock_linear_layout).getId();

        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        pager = (ViewPager) v.findViewById(R.id.pager_debug);

        adapter = new MyPagerAdapter(getFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        pager.setCurrentItem(0);

//        RadioGroup radioGroup = (RadioGroup)v.findViewById(R.id.flock_radio_group);
//        radioGroup.check(R.id.flock_profile_button);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                Fragment fragment = null;
//                String fragmentTag = null;
//                switch (checkedId) {
//                    case R.id.flock_profile_button: {
//                        FlockProfileFragment profileFragment = new FlockProfileFragment();
//                        profileFragment.setFlock(mFlock);
//                        fragment = profileFragment;
//                        fragmentTag = Constants.FLOCK_PROFILE_FRAGMENT_TAG;
//                        break;
//                    }
//                    case R.id.flock_activity_button: {
//                        FlockPostFragment activityFragment = new FlockPostFragment();
//                        fragment = activityFragment;
//                        fragmentTag = Constants.FLOCK_ACTIVITY_FRAGMENT_TAG;
//                        break;
//                    }
//                    case R.id.flock_chat_button: {
//                        FlockChatFragment chatFragment = new FlockChatFragment();
//                        fragment = chatFragment;
//                        fragmentTag = Constants.FLOCK_CHAT_FRAGMENT_TAG;
//                        break;
//                    }
//                    default:
//                        return;
//                }
//                transaction.replace(linearLayoutId, fragment, currentChildFragmentID);
//                currentChildFragmentID = fragmentTag;
//                transaction.commit();
//
//            }
//        });
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        FlockProfileFragment profile = new FlockProfileFragment();
//        profile.setFlock(mFlock);
//        transaction.add(linearLayoutId, profile, Constants.FLOCK_PROFILE_FRAGMENT_TAG);
//        currentChildFragmentID = Constants.FLOCK_PROFILE_FRAGMENT_TAG;
//        transaction.commit();
        return v;
    }

    public void setFlock(Flock f) {
        mFlock = f;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {
                "Profile",
                "Post",
                "Chat"
        };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            FlockFragment fragment;

            switch(position) {
                case 0:     // Profile
                    fragment = FlockProfileFragment.newInstance(position);
                    break;
                case 1:     // Post
                    fragment = FlockPostFragment.newInstance(position);
                    break;
                case 2:     // Chat
                    fragment = FlockChatFragment.newInstance(position);
                    break;
                default:
                    return null;    // TODO: horrible. should do error checking instead
            }

            fragment.setFlock(mFlock);
            return fragment;
        }
    }
}
