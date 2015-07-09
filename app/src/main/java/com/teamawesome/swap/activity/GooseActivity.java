package com.teamawesome.swap.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.teamawesome.swap.R;
import com.teamawesome.swap.util.Constants;


/*
 * Handle splash screen loading and other loading activities here. MainActivity holds begins the
 * actual app.
 */
public class GooseActivity extends Activity {

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


        // for now, forward the user directly into MainActivity
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

        // this may not be necessary
        setContentView(R.layout.activity_goose);
    }
}
