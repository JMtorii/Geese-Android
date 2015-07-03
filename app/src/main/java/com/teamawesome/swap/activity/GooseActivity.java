package com.teamawesome.swap.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.teamawesome.swap.R;


public class GooseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // depending on whether user has already signed in, forward to the appropriate activity
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);

        // this may not be necessary
        setContentView(R.layout.activity_goose);
    }
}
