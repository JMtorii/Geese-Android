package com.teamawesome.geese.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;
import com.teamawesome.geese.R;
import com.teamawesome.geese.adapter.NavDrawerAdapter;
import com.teamawesome.geese.fragment.DatePickerFragment;
import com.teamawesome.geese.fragment.FavouriteFlocksFragment;
import com.teamawesome.geese.fragment.HomeFragment;
import com.teamawesome.geese.fragment.TimePickerFragment;
import com.teamawesome.geese.fragment.settings.SettingsMainFragment;
import com.teamawesome.geese.object.NavDrawerItem;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.SessionManager;
import com.teamawesome.geese.view.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/*
 * MainActivity is responsible for holding all fragments and managing them through
 * FragmentTransaction. The Toolbar and side bar should also be set up through this Activity
 *
 * We should use ActionBarActivity to support Android 4. Otherwise, we have to change the minimum
 * version to 5
 */
public class MainActivity
        extends AppCompatActivity
        implements TimePickerFragment.TimePickerDialogListener, DatePickerFragment.DatePickerDialogListener,
        LocationListener {

    // Toolbar
    private Toolbar mToolbar;

    // Navigation drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<NavDrawerItem> mNavDrawerItems;
    private View mHeaderView;
    private NavDrawerAdapter mNavDrawerAdapter;

    // Fragment manager
    private String curFragmentTag = Constants.HOME_FRAGMENT_TAG;

    // Custom back stack
    private Stack<CustomFragment> customBackStack;

    // Location
    private LocationManager mLocationManager;
    private Location mLatestLocation;
    private boolean mLocationEnabled = false;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;

    // Time picker
    private static final int START_TIME_PICKER_ID = 1;
    private static final int END_TIME_PICKER_ID = 2;

    // Date picker
    private static final int START_DATE_PICKER_ID = 3;
    private static final int END_DATE_PICKER_ID = 4;

    public void showStartTimePickerDialog(View v) {
        DialogFragment newFragment = TimePickerFragment.newInstance(START_TIME_PICKER_ID);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showEndTimePickerDialog(View v) {
        DialogFragment newFragment = TimePickerFragment.newInstance(END_TIME_PICKER_ID);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showStartDatePickerDialog(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(START_DATE_PICKER_ID);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showEndDatePickerDialog(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(END_DATE_PICKER_ID);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onTimeSet(int id, TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) {
            if (id == START_TIME_PICKER_ID) {
                TextView textView = (TextView) findViewById(R.id.flock_event_create_start_time);
                Date dt = new Date(0, 0, 0, hourOfDay, minute);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                textView.setText(sdf.format(dt));
            } else if (id == END_TIME_PICKER_ID) {
                TextView textView = (TextView) findViewById(R.id.flock_event_create_end_time);
                Date dt = new Date(0, 0, 0, hourOfDay, minute);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                textView.setText(sdf.format(dt));
                textView.setTextColor(Color.BLACK);
            }
        }
    }

    @Override
    public void onDateSet(int id, DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (view.isShown()) {
            if (id == START_DATE_PICKER_ID) {
                TextView textView = (TextView) findViewById(R.id.flock_event_create_start_date);
                SimpleDateFormat sdf = new SimpleDateFormat("E, MMMM dd, yyyy");
                Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
                textView.setText(sdf.format(date));
            } else if (id == END_DATE_PICKER_ID) {
                TextView textView = (TextView) findViewById(R.id.flock_event_create_end_date);
                SimpleDateFormat sdf = new SimpleDateFormat("E, MMMM dd, yyyy");
                Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
                textView.setText(sdf.format(date));
                textView.setTextColor(Color.BLACK);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatestLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

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

        mHeaderView = getLayoutInflater().inflate(R.layout.drawer_user_header, null);
        RoundedImageView mUserImageView = (RoundedImageView) mHeaderView.findViewById(R.id.nav_user_image);
        Picasso.with(getApplicationContext())
                .load("http://justinhackworth.com/canada-goose-01.jpg")
                .into(mUserImageView);
        ((TextView) mHeaderView.findViewById(R.id.nav_user_name)).setText(SessionManager.getUsername());
        ((TextView) mHeaderView.findViewById(R.id.nav_user_email)).setText(SessionManager.getUserEmail());

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
        mDrawerList.addHeaderView(mHeaderView);
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

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // TODO: Move this into an util class
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            mLocationEnabled = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        mLocationEnabled = true;
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
            if (position == 0) {
                return;
            }
            position--;
            selectDrawerItem(position);
            mNavDrawerAdapter.setSelectedPosition(position);
            mNavDrawerAdapter.notifyDataSetChanged();
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

    public Location getLatestLocation() {
        return mLatestLocation;
    }

    public boolean getLocationEnabled() {
        return mLocationEnabled;
    }
}
