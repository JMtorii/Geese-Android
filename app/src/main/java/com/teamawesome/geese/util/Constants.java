package com.teamawesome.geese.util;

/**
 * Created by JMtorii on 15-07-09.
 */
public final class Constants {

    // shared preference
    public static final String PREF_SETTINGS = "PREF_SETTINGS";
    public static final String PREF_ID = "PREF_ID";

    public static final String HOME_FRAGMENT_TAG = "HOME_FRAGMENT";
    public static final String SETTINGS_MAIN_FRAGMENT_TAG = "SETTINGS_MAIN_FRAGMENT";
    public static final String SIGNUP_FRAGMENT_TAG = "SIGNUP_FRAGMENT_TAG";

    public static final String FLOCK_ACTIVITY_FRAGMENT_TAG = "FLOCK_ACTIVITY_FRAGMENT";
    public static final String FLOCK_CHAT_FRAGMENT_TAG = "FLOCK_ACTIVITY_CHAT";
    public static final String FLOCK_PROFILE_FRAGMENT_TAG = "FLOCK_PROFILE_FRAGMENT";

    public static final String FLOCK_FRAGMENT_TAG = "FLOCK_FRAGMENT";
    public static final String FLOCK_EVENT_DETAILS_FRAGMENT_TAG = "FLOCK_EVENT_DETAILS_FRAGMENT";
    public static final String FLOCK_POST_DETAILS_FRAGMENT_TAG = "FLOCK_POST_DETAILS_FRAGMENT";
    public static final String FLOCK_FULL_SCREEN_MAP_FRAGMENT_TAG = "FLOCK_FULL_SCREEN_MAP_FRAGMENT";

    public static final String SETTINGS_RULES_TAG = "SETTINGS_RULES_TAG";
    public static final String SETTINGS_PRIVACY_POLICTY_TAG = "SETTINGS_PRIVACY_POLICTY_TAG";
    public static final String SETTINGS_TERMS_SERVICE_TAG = "SETTINGS_TERMS_SERVICE_TAG";

    // TODO change this to SSL/geeseapp
    // Currently points to own machine (en0 interface)
    public static final String GEESE_SERVER_ADDRESS = "http://geese-app.elasticbeanstalk.com";
}
