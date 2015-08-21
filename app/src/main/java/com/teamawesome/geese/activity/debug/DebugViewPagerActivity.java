package com.teamawesome.geese.activity.debug;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.teamawesome.geese.R;
import com.teamawesome.geese.fragment.debug.SuperAwesomeCardFragment;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by JMtorii on 15-07-30.
 */
public class DebugViewPagerActivity extends AppCompatActivity {
    @Bind(R.id.toolbar_view_pager) Toolbar toolbar;
    @Bind(R.id.tabs) PagerSlidingTabStrip tabs;
    @Bind(R.id.pager_debug) ViewPager pager;

    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_activity_view_pager);
        ButterKnife.bind(this);
//        toolbar = (Toolbar) findViewById(R.id.toolbar_view_pager);
//        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
//        pager = (ViewPager) findViewById(R.id.pager);
        setSupportActionBar(toolbar);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        pager.setCurrentItem(1);

        tabs.setOnTabReselectedListener(new PagerSlidingTabStrip.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int position) {
                Toast.makeText(DebugViewPagerActivity.this, "Tab reselected: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_contact:
//                QuickContactFragment.newInstance().show(getSupportFragmentManager(), "QuickContactFragment");
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid",
                "Top New Free", "Trending"};

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
            return SuperAwesomeCardFragment.newInstance(position);
        }
    }
}
