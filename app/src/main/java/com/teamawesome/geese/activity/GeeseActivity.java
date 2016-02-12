package com.teamawesome.geese.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.teamawesome.geese.R;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.RestClient;
import com.teamawesome.geese.util.SessionManager;

/*
 * Handle splash screen loading and other loading activities here. MainActivity holds begins the
 * actual app.
 */
public class GeeseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager.init(getApplicationContext());
        SessionManager.setIPAddress(Constants.GEESE_SERVER_ADDRESS);

        RestClient.init();

        FacebookSdk.sdkInitialize(getApplicationContext());

        // Check if logged in and forward to login or main
        Intent i;
        if (SessionManager.checkLogin()) {
            i = new Intent(this, MainActivity.class);
        } else {
            i = new Intent(this, LoginActivity.class);
        }
        startActivity(i);
        finish();
    }
}
