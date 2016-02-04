package com.teamawesome.geese.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.HashMap;

/**
 * Created by lcolam on 10/12/15.
 * Manages all session-related data
 */
public class SessionManager {
    private static Editor editor;
    private static Context context;

    private static final String PREF_IP_ADDRESS = "ip_address";
    private static final String PREF_NAME = "LoginPref";
    private static final String IS_LOGINED = "IsLoggedIn";
    private static final String KEY_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_TOKEN = "Token";

    public static void init(Context applicationContext) {
        context = applicationContext;
        editor = getSharedPreferences().edit();
    }

    private static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void createLoginSession(String name, String email, String token) {
        editor.putBoolean(IS_LOGINED, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public static void deleteLoginSession() {
        editor.putBoolean(IS_LOGINED, false);
        editor.putString(KEY_NAME, null);
        editor.putString(KEY_EMAIL, null);
        editor.putString(KEY_TOKEN, null);
        editor.commit();
    }

    public static boolean checkLogin() {
        return getSharedPreferences().getBoolean(IS_LOGINED, false);
    }

    public static HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_NAME, getSharedPreferences().getString(KEY_NAME, null));
        user.put(KEY_EMAIL, getSharedPreferences().getString(KEY_EMAIL, null));
        user.put(KEY_EMAIL, getSharedPreferences().getString(KEY_TOKEN, null));

        return user;
    }

    public static void setIPAddress(String address) {
        editor.putString(PREF_IP_ADDRESS, address);
        editor.apply();
    }

    public static String getIPAddress() {
        return getSharedPreferences().getString(PREF_IP_ADDRESS, "");
    }
}
