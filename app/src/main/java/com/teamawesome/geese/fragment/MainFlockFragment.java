package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.teamawesome.geese.R;
import com.teamawesome.geese.object.Flock;
import com.teamawesome.geese.util.Constants;

/**
 * Created by JMtorii on 15-07-14.
 */
public class MainFlockFragment extends Fragment {
    private PagerSlidingTabStrip mTabs;
    private ViewPager mPager;
    private PagerAdapter mAdapter;
    private Flock mFlock;

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
        mTabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        mPager = (ViewPager) v.findViewById(R.id.pager_debug);
        mAdapter = new PagerAdapter(getFragmentManager());
        mPager.setAdapter(mAdapter);
        mTabs.setViewPager(mPager);
        mPager.setCurrentItem(0);
        return v;
    }

    public void setFlock(Flock f) {
        mFlock = f;
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {
                "Profile",
                "Post",
                "Chat"
        };

        public PagerAdapter(FragmentManager fm) {
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
