package com.teamawesome.geese.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.teamawesome.geese.R;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.SessionManager;

/*
 * Handle splash screen loading and other loading activities here. MainActivity holds begins the
 * actual app.
 */
public class GeeseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(Constants.PREF_SETTINGS, MODE_PRIVATE);
        int id = settings.getInt(Constants.PREF_ID, 0);
        if (id == 0) {
            // id doesn't exist so we gotta fetch one from the server then put it in
            // for now, add id with a value of 1

            // TODO: use HttpGet, HttpClient and HttpResponse. May need a JSON parser
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(Constants.PREF_ID, 1);
            editor.apply();
        }

        // Check if logged in and forward to login or main
        Intent i;
        if (true) {
//        if (sessionManager.checkLogin()) {
            i = new Intent(this, MainActivity.class);
        } else {
            i = new Intent(this, LoginActivity.class);
        }
        startActivity(i);
        finish();
    }
}
