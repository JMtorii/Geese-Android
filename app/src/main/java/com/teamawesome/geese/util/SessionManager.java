package com.teamawesome.geese.util;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by lcolam on 10/12/15.
 * Manages login session of user.
 */
public class SessionManager {
    private SharedPreferences pref;
    private Editor editor;
    private Context context;
    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LoginPref";
    private static final String IS_LOGINED = "IsLoggedIn";
    private static final String KEY_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_TOKEN = "Token";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String email, String token) {
        editor.putBoolean(IS_LOGINED, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public void deleteLoginSession() {
        editor.putBoolean(IS_LOGINED, false);
        editor.putString(KEY_NAME, null);
        editor.putString(KEY_EMAIL, null);
        editor.putString(KEY_TOKEN, null);
        editor.commit();
    }

    public boolean checkLogin() {
        return pref.getBoolean(IS_LOGINED, false);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        return user;
    }
}
