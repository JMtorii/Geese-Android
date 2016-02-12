package com.teamawesome.geese.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Created by lcolam on 10/12/15.
 * Manages all session-related data
 */
public class SessionManager {
    private static Context context;
    private static SharedPreferences pref;
    private static Editor editor;

    private static final String PREF_IP_ADDRESS = "ip_address";
    private static final String IS_LOGINED = "is_logged_in";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_TOKEN = "token";

    public static void init(Context applicationContext) {
        context = applicationContext;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
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
        editor.putString(KEY_NAME, "");
        editor.putString(KEY_EMAIL, "");
        editor.putString(KEY_TOKEN, "");
        editor.commit();
    }

    public static boolean checkLogin() {
        return pref.getBoolean(IS_LOGINED, false);
    }

    public static String getToken() {
        return pref.getString(KEY_TOKEN, "");
    }

    public static String getUsername() {
        return pref.getString(KEY_NAME, "");
    }

    public static String getUserEmail() {
        return pref.getString(KEY_EMAIL, "");
    }

    public static void setIPAddress(String address) {
        editor.putString(PREF_IP_ADDRESS, address);
        editor.apply();
    }

    public static String getIPAddress() {
        return pref.getString(PREF_IP_ADDRESS, "");
    }
}
