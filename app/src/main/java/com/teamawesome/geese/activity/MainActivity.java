package com.teamawesome.geese.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
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
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.teamawesome.geese.R;
import com.teamawesome.geese.adapter.NavDrawerAdapter;
import com.teamawesome.geese.fragment.FavouriteFlocksFragment;
import com.teamawesome.geese.fragment.HomeFragment;
import com.teamawesome.geese.fragment.settings.SettingsMainFragment;
import com.teamawesome.geese.object.NavDrawerItem;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.RestClient;
import com.teamawesome.geese.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
    private List<NavDrawerItem> mNavDrawerItems;
    private NavDrawerAdapter mNavDrawerAdapter;

    // Fragment manager
    private String curFragmentTag = Constants.HOME_FRAGMENT_TAG;

    // Custom back stack
    private Stack<CustomFragment> customBackStack;

    // Fragment information useful for the custom back stack
    private class CustomFragment {
        // Fragment tag used to find the fragment
        private String tag;
        // Title to display on the toolbar
        private String title;

        CustomFragment(String tag, String title) {
            this.tag = tag;
            this.title = title;
        }

        String getTag() {
            return this.tag;
        }

        String getTitle() {
            return this.title;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customBackStack = new Stack<>();
        customBackStack.push(new CustomFragment(Constants.HOME_FRAGMENT_TAG, Constants.HOME_TITLE));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Main Activity");

        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_settings:
                        // TODO: add custom animation
//                        Fragment fragment = new SettingsMainFragment();
//                        switchFragment(fragment, 0, 0, Constants.SETTINGS_MAIN_FRAGMENT_TAG, true, true, true);
//                        return true;
                }

                return false;
            }
        });

        mNavDrawerItems = new ArrayList<NavDrawerItem>();
        int defaultValue = R.drawable.ic_drawer;
        TypedArray mNavDrawerIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        String[] mNavDrawerTitles = getResources().getStringArray(R.array.nav_drawer_strings);
        for (int i = 0; i < mNavDrawerTitles.length; ++i) {
            NavDrawerItem item = new NavDrawerItem.Builder()
                    .icon(mNavDrawerIcons.getResourceId(i, defaultValue))
                    .name(mNavDrawerTitles[i])
                    .build();
            mNavDrawerItems.add(item);
        }
        mNavDrawerAdapter = new NavDrawerAdapter(this, mNavDrawerItems);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerList.setAdapter(mNavDrawerAdapter);
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
        String title;

        switch (position) {
            case 0:         // Home
                fragment = new HomeFragment();
                tag = Constants.HOME_FRAGMENT_TAG;
                title = Constants.HOME_TITLE;
                break;
            case 1:         // Favourites
                fragment = new FavouriteFlocksFragment();
                tag = Constants.FAVOURITE_FLOCKS_FRAGMENT_TAG;
                title = Constants.FAVOURITE_TITLE;
                break;
            case 2:         // Setting
                fragment = new SettingsMainFragment();
                tag = Constants.SETTINGS_MAIN_FRAGMENT_TAG;
                title = Constants.SETTING_TITLE;
                break;
            case 3:         // Logout
                fragment = null;
                tag = "";
                title = "";
                if (AccessToken.getCurrentAccessToken() != null) {
                    new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                            .Callback() {

                        @Override
                        public void onCompleted(GraphResponse graphResponse) {
                            LoginManager.getInstance().logOut();
                        }
                    }).executeAsync();
                }
                SessionManager.deleteLoginSession();
                RestClient.headerInterceptor.removeTokenHeader();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            default:        // this should never happen
                fragment = null;
                tag = "";
                title = "";
                break;
        }

        if (position == 3 && SessionManager.checkLogin()) {
            Toast.makeText(getApplicationContext(), "Already login-ed", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fragment != null) {
            switchFragment(
                    fragment,
                    R.anim.fragment_slide_in_left,
                    R.anim.fragment_slide_out_right,
                    tag,
                    title,
                    true,
                    true,
                    true
            );

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mToolbar.setTitle(mNavDrawerItems.get(position).getName());
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void onBackPressed() {
        popFragment();
    }

    public void popFragment() {
        Fragment f = getSupportFragmentManager().findFragmentByTag(curFragmentTag);

        // This is a temporary fix due to the viewpager being weird
        if (f != null && !f.getTag().equals(Constants.HOME_FRAGMENT_TAG) && customBackStack.size() > 1) {
            super.onBackPressed();

            customBackStack.pop();
            CustomFragment curFragment = customBackStack.peek();
            curFragmentTag = curFragment.getTag();
            mToolbar.setTitle(curFragment.getTitle());

        }
    }

    public void switchFragment(Fragment fragment, int enterAnim, int exitAnim, String tag, String title,
                               boolean isReplace, boolean clearBackStack, boolean isAddedToBackStack) {
        FragmentManager fm = getSupportFragmentManager();

        if (clearBackStack) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(enterAnim, exitAnim);

        if (isReplace) {
            ft.replace(R.id.content_frame, fragment, tag);
            if (!customBackStack.empty()) customBackStack.pop();
            customBackStack.push(new CustomFragment(tag, title));
        } else {
            ft.add(R.id.content_frame, fragment, tag);
            customBackStack.push(new CustomFragment(tag, title));
        }

        if (isAddedToBackStack) {
            ft.addToBackStack(tag);
        }

        ft.commit();
        curFragmentTag = tag;
        mToolbar.setTitle(title);
    }
}
