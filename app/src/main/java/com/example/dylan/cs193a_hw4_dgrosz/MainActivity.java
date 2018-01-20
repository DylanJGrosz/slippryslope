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
 * Activity - Main Activity
 */

package com.example.dylan.cs193a_hw4_dgrosz;

import android.content.Intent;
        import android.os.*;
        import android.view.*;
        import stanford.androidlib.*;

/* MAIN ACTIVITY
    The main activity is home page for each respective type of user, displaying different images and
    playing different music depending on which set of users the user decided to rate.
 */
public class MainActivity extends SimpleActivity {
    public static final String BASE_URL = "http://www.martystepp.com/friendr/";
    private int appMusicID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      }

    /* ON RESUME
        Based on the type of users passed over from the Main Menu Activity, the layout will be set
        up differently.
     */
    @Override
    protected void onResume() {
        super.onResume();
        String typeName = getIntent().getStringExtra("typeName");
        switch (typeName) {
            case "friends":
                setLayout(R.drawable.friendr_logo, "", typeName, R.raw.friends_theme);
                break;
            case "bachelor" :
                setLayout(R.drawable.bachelr_rose,"Bachelr",
                        typeName + "ettes", R.raw.bachelor_in_paradise_theme);
                break;
            case "pokemon" :
                setLayout(R.drawable.tindermon, "",
                        typeName, R.raw.pokemon_theme);
                break;
        }
    }

    /* ON PAUSE
        If this activity is paused, the music will be paused as well (to minimize annoyance).
     */
    @Override
    protected void onPause() {
        super.onPause();
        SimpleMedia.with(this).pause(appMusicID);
    }

    /* SET LAYOUT HELPER METHOD
        Edits the image, text, and background music depending on what's passed in.
     */
    private void setLayout(int imageViewID, String s, String prompt, int musicID) {
        $IV(R.id.logoIV).setImageResource(imageViewID);
        $TV(R.id.optionalText).setText(s);
        $TV(R.id.prompt).setText("The app for rating new " + prompt + "!");
        SimpleMedia.with(this).loop(musicID);
        appMusicID = musicID;
    }

    /* VIEW USERS ONCLICK FUNCTION
        When the user decides to move on and view users, the type name is added to the base URL of
        data (http://www.martystepp.com/friendr/" and this new URL is passed to the View Users
        Activity.
     */
    public void viewUsersClick(View view) {
        String typeName = getIntent().getStringExtra("typeName");

        String textURL = BASE_URL + typeName +"/";
        Intent intent = new Intent(this, ViewUsersActivity.class);
        intent.putExtra("textURL", textURL);
        startActivity(intent);
    }
}

