package com.teamawesome.geese.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamawesome.geese.R;
import com.teamawesome.geese.fragment.HomeFragment;
import com.teamawesome.geese.fragment.SettingsMainFragment;
import com.teamawesome.geese.util.Constants;


/*
 * MainActivity is responsible for holding all fragments and managing them through
 * FragmentTransaction. The Toolbar and side bar should also be set up through this Activity
 *
 * We should use ActionBarActivity to support Android 4. Otherwise, we have to change the minimum
 * version to 5
 */
public class MainActivity extends AppCompatActivity {
    // Toolbar
    private Toolbar mToolbar;

    // Navigation drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mNavDrawerTitles;

    // Fragment manager
    private String curFragmentTag = Constants.HOME_FRAGMENT_TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Main Activity");

        // TODO: add appropriate action items to menu
        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // TODO: add appropriate menu items
//                switch (menuItem.getItemId()) {
//                    case R.id.action_settings:
//                        // TODO: add custom animation
//                        Fragment fragment = new SettingsMainFragment();
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        fragmentManager.beginTransaction().add(R.id.content_frame, fragment).commit();
//                        return true;
//                }

                return false;
            }
        });

        mNavDrawerTitles = getResources().getStringArray(R.array.nav_drawer_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<Object>(this, R.layout.drawer_list_item, mNavDrawerTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                mToolbar,  /* nav drawer image to replace 'Up' caret */                  // THIS WAS MODIFIED
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectDrawerItem(0);
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // TODO: possibly move listener and selectItem to a separate class
    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectDrawerItem(position);
        }
    }

    private void selectDrawerItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment;
        String tag;

        switch (position) {
            case 0:         // Home
                fragment = new HomeFragment();
                tag = Constants.HOME_FRAGMENT_TAG;
                break;
            case 1:         // Nearby but Home for now
                fragment = new HomeFragment();
                tag = "";
                break;
            case 2:         // Setting
                fragment = new SettingsMainFragment();
                tag = Constants.SETTINGS_MAIN_FRAGMENT_TAG;
                break;
            default:        // this should never happen
                fragment = null;
                tag = "";
                break;
        }

        if (fragment != null) {
            switchFragment(
                    fragment,
                    R.anim.fragment_slide_in_left,
                    R.anim.fragment_slide_out_right,
                    tag,
                    true,
                    true,
                    true
            );

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mToolbar.setTitle(mNavDrawerTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }

    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentByTag(curFragmentTag);
        Log.v("test", f.getTag());
        if (!f.getTag().equals(Constants.HOME_FRAGMENT_TAG)) {
            super.onBackPressed();
        }
    }

    public void switchFragment(Fragment fragment, int enterAnim, int exitAnim, String tag,
                               boolean isReplace, boolean clearBackStack, boolean isAddedToBackStack) {
        FragmentManager fm = getSupportFragmentManager();

        if (clearBackStack) {
            fm.popBackStack(curFragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(enterAnim, exitAnim);

        if (isReplace) {
            ft.replace(R.id.content_frame, fragment, tag);
        } else {
            ft.add(R.id.content_frame, fragment, tag);
        }

        if (isAddedToBackStack) {
            ft.addToBackStack(tag);
        }

        ft.commit();
        curFragmentTag = tag;
    }
}