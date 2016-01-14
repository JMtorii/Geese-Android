package com.teamawesome.geese.activity;

import android.content.pm.ActivityInfo;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.teamawesome.geese.R;
import com.teamawesome.geese.fragment.FavouriteFlocksFragment;
import com.teamawesome.geese.fragment.HomeFragment;
import com.teamawesome.geese.fragment.SignupFragment;
import com.teamawesome.geese.fragment.settings.SettingsMainFragment;
import com.teamawesome.geese.rest.service.FlockService;
import com.teamawesome.geese.rest.service.GeeseService;
import com.teamawesome.geese.rest.service.LoginService;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.SessionManager;

import java.util.Stack;

import retrofit.GsonConverterFactory;
import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

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

    // Session manager
    private SessionManager sessionManager;

    // Custom back stack
    private Stack<CustomFragment> customBackStack;

    // Retrofit client
    // TODO Remove later
    private Retrofit retrofitClient;


    // Retrofit observable client
    private Retrofit retrofitReactiveClient;

    // REST services
    public FlockService flockService;
    public GeeseService geeseService;
    public LoginService loginService;

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

        sessionManager = new SessionManager(getApplicationContext());
        customBackStack = new Stack<>();
        customBackStack.push(new CustomFragment(Constants.HOME_FRAGMENT_TAG, Constants.HOME_TITLE));
        FacebookSdk.sdkInitialize(getApplicationContext());

        retrofitClient = new Retrofit.Builder()
                .baseUrl(Constants.GEESE_SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitReactiveClient = new Retrofit.Builder()
                .baseUrl(Constants.GEESE_SERVER_ADDRESS)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        flockService = retrofitReactiveClient.create(FlockService.class);
        geeseService = retrofitReactiveClient.create(GeeseService.class);
        loginService = retrofitReactiveClient.create(LoginService.class);

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
        String title;

        switch (position) {
            case 0:         // Home
                fragment = new HomeFragment();
                tag = Constants.HOME_FRAGMENT_TAG;
                title = Constants.HOME_TITLE;
                break;
            case 1:         // Nearby but Home for now
                fragment = new FavouriteFlocksFragment();
                tag = Constants.FAVOURITE_FLOCKS_FRAGMENT_TAG;
                title = Constants.FAVOURITE_TITLE;
                break;
            case 2:         // Setting
                fragment = new SettingsMainFragment();
                tag = Constants.SETTINGS_MAIN_FRAGMENT_TAG;
                title = Constants.SETTING_TITLE;
                break;
            case 3:         // Signup
                fragment = new SignupFragment();
                tag = Constants.SIGNUP_FRAGMENT_TAG;
                title = Constants.SIGN_UP_TITLE;
                break;
            case 4:         // DEBUG SIGNOUT
                if (AccessToken.getCurrentAccessToken() != null) {
                    new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                            .Callback() {

                        @Override
                        public void onCompleted(GraphResponse graphResponse) {
                            LoginManager.getInstance().logOut();
                        }
                    }).executeAsync();
                }
                sessionManager.deleteLoginSession();
            default:        // this should never happen
                fragment = null;
                tag = "";
                title = "";
                break;
        }

        if (position == 3 && sessionManager.checkLogin()) {
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
            mToolbar.setTitle(mNavDrawerTitles[position]);
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

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public Retrofit getRetrofitClient() {
        return retrofitClient;
    }
}
