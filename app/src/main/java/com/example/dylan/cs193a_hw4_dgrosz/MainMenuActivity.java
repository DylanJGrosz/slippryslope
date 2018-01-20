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
 * Activity - Main Menu Activity
 */

package com.example.dylan.cs193a_hw4_dgrosz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import stanford.androidlib.SimpleActivity;

/* MAIN MENU ACTIVITY
    Displays the meta-app to Friendr, SlipprySlope. Referencing the show Black Mirror, this activity
    displays the show's cover picture and allows the user the three options they have to rate:
    friends, the bachelor and Pokemon. The user will be navigated to the Main Activity once they
    make a selection and click the start button.
 */
public class MainMenuActivity extends SimpleActivity {

    /* ON CREATE
        Sets up spinner and has it listen for an item selection and a start button press.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setSpinner();
        spinClickListener();
    }

    /* SPIN CLICK ON SELECT LISTENER FUNCTION
        If a user selects a type of user, it will be stored in a string until they either click
        start or make another selection.
     */
    private void spinClickListener() {
        Spinner chooseSpinner = $SP(R.id.chooseSpinner);
        chooseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String typeName = (String) parent.getItemAtPosition(pos);

                //Waits for start button click to navigate to respective activity
                startButtClickListener(typeName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { /*do nothing*/ }
        });
    }

    /* START BUTTON ONCLICK LISTENER FUNCTION
        Given that the user has made a selection, clicking the start button will navigate the user
        to the Main Activity (given they're connected to the internet) while also passing the name
        of the type of user they're rating.
     */
    private void startButtClickListener(final String typeName) {
        Button startButt = $B(R.id.startButt);
        startButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!typeName.isEmpty()) {
                    if(isNetworkAvailable()) {
                        Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                        intent.putExtra("typeName", typeName);
                        startActivity(intent);
                    } else {
                        toast("You must be connected to the internet!");
                    }
                }
            }
        });
    }

    /* SET SPINNER HELPER METHOD
        Attaches an array adapter to the spinner and adds the three different kinds of users the
        user can rate.
     */
    private void setSpinner() {
        Spinner chooseSpinner = $SP(R.id.chooseSpinner);
        ArrayList<String> thingsToRate = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, thingsToRate);
        chooseSpinner.setAdapter(adapter);
        //add three options
        thingsToRate.add("friends");
        thingsToRate.add("bachelor");
        thingsToRate.add("pokemon");
        adapter.notifyDataSetChanged();
    }

    /* IS NETWORK AVAILABLE CHECK
        Pulled from Stack Overflow. Returns whether or not the user is connected to the internet.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
