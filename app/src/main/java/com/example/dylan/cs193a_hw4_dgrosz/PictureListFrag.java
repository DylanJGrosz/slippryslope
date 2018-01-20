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
 * Fragment - Picture List Fragment
 */

package com.example.dylan.cs193a_hw4_dgrosz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import stanford.androidlib.SimpleActivity;
import stanford.androidlib.SimpleFragment;

/* PICTURE LIST FRAGMENT
    This fragment builds and displays all users in a given set of users drawn from the internet
    data source.
 */
public class PictureListFrag extends SimpleFragment {
    public PictureListFrag() {
        // Required empty public constructor
    }

    /* ON CREATE VIEW
        Uses Ion and the URL passed over from the Main Activity to pull all the user's names from
        the list given with each data set. The set of names is used to load every person's name and
        image.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SimpleActivity activity = getSimpleActivity();
        final String textURL = activity.getStringExtra("textURL");
        Ion.with(this)
                .load(textURL + "list")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    public void onCompleted(Exception e, String result) {
                        loadPeople(result, textURL);
                    }
                });

        return inflater.inflate(R.layout.fragment_picture_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /* LOAD PEOPLE HELPER METHOD
        Creates an array of each individual name based off of the text extracted using Ion and adds
        an image button for each name in a GridLayout.
     */
    private void loadPeople(String result, String textURL) {
        String[] allNames = result.split("\n");
        for (int i = 0; i < allNames.length; i++) {
            addPerson(allNames[i], textURL);
        }
    }

    /* ADD PERSON HELPER METHOD
        Given a name, grabs the layout from person.xml and builds its required image button while
        also adding this image button to this fragment's GridLayout (which is sized depending on
        screen orientation).
     */
    private void addPerson(String name, String textURL) {
        SimpleActivity activity = getSimpleActivity();
        View person = activity.getLayoutInflater()
                .inflate(R.layout.person, /* parent */ null);

        ImageButton imgB = (ImageButton) person.findViewById(R.id.personIB);
        buildImageButton(imgB, name, textURL);

        GridLayout layout = (GridLayout) getView().findViewById(R.id.GLayout);
        if(getSimpleActivity().isPortrait()) {
            layout.setColumnCount(2);
        } else {
            layout.setColumnCount(1);
        }
        layout.addView(person);
    }

    /* BUILD IMAGE BUTTON HELPER METHOD
        Uses Picasso to pull the image given the person's name and the overall URL. The data source
        is organized such that [URL + name.jpg] is URL for the image. Using Picasso, the image is
        formatted and loaded into the image button, whose on click listener is set up to navigate
        to the user's profile.
     */
    private void buildImageButton(ImageButton imgB, final String name, final String textURL) {
        Context activity = getSimpleActivity();
        int screenPadding = 96;
        double singleImageDim = getSimpleActivity().getScreenWidth()/2 - screenPadding;

        Picasso.with(activity)
                .load(textURL + name.toLowerCase().replace(" ", "") + ".jpg")
                .resize((int) singleImageDim, (int) singleImageDim) //image is square
                .centerInside()
                .placeholder(R.drawable.loading)
                .into(imgB);

        imgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgClick(textURL, name);
            }
        });
    }

    /* IMAGE ON CLICK FUNCTION
        If the screen is portrait-oriented, then the user is navigated to an entirely different
        profile activity. If not, since there is more screen real estate, the layout is such that
        both the list of users and a detailed profile can be displayed side by side, so no activity
        navigation occurs and the detailed profile fragment is simply refreshed with the name of the
        clicked user.
     */
    private void imgClick(String textURL, String name) {
        if(getSimpleActivity().isPortrait()) {
            Intent intent = new Intent(getSimpleActivity(), ProfileActivity.class);
            intent.putExtra("textURL", textURL);
            intent.putExtra("name", name);
            startActivity(intent);
        } else {
           // implement landscape`
            SimpleActivity activity = getSimpleActivity();
            ProfileFrag frag = (ProfileFrag) activity.getFragmentManager()
                                                .findFragmentById(R.id.ProfileFrag);
            frag.setLayout(textURL, name);
        }
    }
}
