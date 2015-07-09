package com.teamawesome.swap.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toolbar;

import com.teamawesome.swap.R;


/*
 * MainActivity is responsible for holding all fragments and managing them through
 * FragmentTransaction. The Toolbar and side bar should also be set up throguh this Activity
 *
 * We should ActionBarActivity to support Android 4. Otherwise, we have to make the minimum
 * version to 5
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
    }
}
