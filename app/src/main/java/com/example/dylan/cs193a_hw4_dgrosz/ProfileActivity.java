/*
 * Dylan Grosz <dgrosz@stanford.edu>
 * CS 193A, Winter 2017 (instructor: Marty Stepp)
 * Homework Assignment 4
 * Friendr/SlipprySlope - A dating/rating app that allows you to rate people/pocket monsters. When
 * perusing through "users," the user can switch between portrait and landscape mode, where the
 * layout changes (i.e. portrait just shows a list of users while landscape adds a view of a
 * chosen user). In addition, all ratings are saved so the user can remember/edit their ratings of
 * various users. Works best on 7 inch tablets.
 *
 * Activity - Profile Activity
 */

package com.example.dylan.cs193a_hw4_dgrosz;

import android.os.Bundle;

import stanford.androidlib.SimpleActivity;

/* PROFILE ACTIVITY
    Displays the Profile Fragment.
 */
public class ProfileActivity extends SimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}
