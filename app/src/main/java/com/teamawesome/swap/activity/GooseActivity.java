package com.teamawesome.swap.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.teamawesome.swap.R;


/*
 * Handle splash screen loading and other loading activities here. MainActivity holds begins the
 * actual app.
 */
public class GooseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // for now, forward the user directly into MainActivity
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

        // this may not be necessary
        setContentView(R.layout.activity_goose);
    }
}
