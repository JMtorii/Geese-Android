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
import com.teamawesome.geese.rest.model.Flock;

/**
 * Created by JMtorii on 15-07-14.
 */
public class MainFlockFragment extends Fragment {
    private PagerSlidingTabStrip mTabs;
    private ViewPager mPager;
    private PagerAdapter mAdapter;
    private Flock mFlock;

    public FlockFragment fragments[] = {
            FlockProfileFragment.newInstance(0),
            FlockPostFragment.newInstance(1)
    };

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
        mPager.setCurrentItem(1);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // remove all child fragments, otherwise fragment manager tries to reuse the fragments and fucks up.
        getFragmentManager().beginTransaction().remove(fragments[0]).remove(fragments[1]).commit();
    }

    public void setFlock(Flock f) {
        mFlock = f;
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {
                "Profile",
                "Post"
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
            FlockFragment fragment = fragments[position];


            fragment.setFlock(mFlock);
            return fragment;
        }

        @Override
        public int getItemPosition(Object object){
            for (int i = 0; i < fragments.length; i++) {
                if (object == fragments[i]) {
                    return i;
                }
            }
            return PagerAdapter.POSITION_NONE;
        }
    }
}
