package com.teamawesome.swap.activity;

import android.app.Activity;
import android.os.Bundle;

import com.teamawesome.swap.R;


/*
 * MainActivity is responsible for holding all fragments and managing them through
 * FragmentTransaction. The Toolbar and side bar should also be set up throguh this Activity
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");

        // implement toolbar and navigation bar for all fragment
    }
}
