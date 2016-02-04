package com.teamawesome.geese.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.teamawesome.geese.util.Constants;

/**
 * Created by lcolam on 2/3/16.
 */
public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // for now, forward the user directly into MainActivity
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
