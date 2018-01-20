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
 * Fragment - Profile Fragment
 */

package com.example.dylan.cs193a_hw4_dgrosz;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import stanford.androidlib.SimpleActivity;
import stanford.androidlib.SimpleFragment;
import stanford.androidlib.SimpleMedia;
import stanford.androidlib.SimplePreferences;

/* PROFILE FRAGMENT
    This fragment displays a specific user's editable rating, static picture, name and bio.
 */
public class ProfileFrag extends SimpleFragment {
    public ProfileFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    /* ON ACTIVITY CREATED
        The layout is set given the base URL and the chosen user's name.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SimpleActivity activity = getSimpleActivity();
        final String textURL = activity.getStringExtra("textURL");
        final String name = activity.getStringExtra("name");

        setLayout(textURL, name);
    }

    /* ON RESUME
        If the app is resumed (especially in landscape mode), then the rating bar is updated.
     */
    @Override
    public void onResume() {
        super.onResume();
        final String name = getSimpleActivity().getStringExtra("name");
        setRatingsBar(name);
    }

    /* SET LAYOUT METHOD
        Sets the rating bar to visible and sets it up as well as the profile picture, name, and
        profile description. If the name or URL passed in is nonexistent (such as when the user has
        no one selected), the rating bar hides.
     */
    public void setLayout(String textURL, String name) {
        if(textURL.length() > 0 && name.length() > 0) {
            RatingBar ratingBar = (RatingBar) getView().findViewById(R.id.profileRB);
            ratingBar.setVisibility(View.VISIBLE);

            SimpleActivity activity = getSimpleActivity();
            Context c = activity;

            setRatingsBar(name);

            setProfilePicture(c, textURL, name);

            //set name from parameter
            TextView nameTV = (TextView) getView().findViewById(R.id.profileNameTV);
            nameTV.setText(name);

            setProfileDescription(c, textURL, name);

        } else {
            RatingBar ratingBar = (RatingBar) getView().findViewById(R.id.profileRB);
            ratingBar.setVisibility(View.INVISIBLE);
        }
    }

    /* SET RATINGS BAR HELPER METHOD
        Checks preferences if the user has previously set this specific person's rating. The bar is
        then set to this value (0 if the user hasn't set it yet). If the user edits the rating at
        any time, preferences will be created/updated and a sound based off of how many stars the
        user gives will play
     */
    private void setRatingsBar(final String name) {
        final SimpleActivity activity = getSimpleActivity();
        final RatingBar ratingBar = (RatingBar) getView().findViewById(R.id.profileRB);

        double savedRating = SimplePreferences.with(activity)
                                .getDouble(name, 0);
        ratingBar.setRating((float) savedRating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (!name.isEmpty() && fromUser) {
                    double trueRating = ratingBar.getRating();
                    int roundedRatingFromBar = getRoundedRating(trueRating);
                    SimplePreferences.with(activity)
                            .set(name, trueRating);
                    String rateAudioFilename = "x" + roundedRatingFromBar + "star";
                    int rateAudioID = activity.getResources().getIdentifier(rateAudioFilename,
                                        "raw", activity.getPackageName());
                    SimpleMedia.with((Context) activity).play(rateAudioID);
                }
            }
        });
    }

    /* GET ROUNDED RATING
        Returns an integer version of the double rating and also makes sure that it's between 1 and
        5 inclusive (so no error is thrown when the audioID is pulled from the raw folder).
     */
    private int getRoundedRating(double trueRating) {
        int roundedRating = (int) trueRating;
        if(roundedRating < 1) {
            roundedRating = 1;
        } else if(roundedRating > 5) {
            roundedRating = 5;
        }
        return roundedRating;
    }

    /* SET PROFILE DESCRIPTION
        Much like a person's respective image, their description is based off of the base URL and
        their name [URL + name.txt]. The TextView that displays the profile is updated.
     */
    //set profile description by loading it with Ion
    private void setProfileDescription(Context c, String textURL, String name) {
        Ion.with(c)
                .load(textURL + name.toLowerCase().replace(" ", "") + ".txt")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        TextView descriptionTV = (TextView) getView()
                                .findViewById(R.id.profileDescriptionTV);
                        descriptionTV.setText(result);
                    }
                });
    }

    /* SET PROFILE PICTURE
        Again, the specific person's picture is pulled using Picasso and a combination of the URL
        and the person's name.
     */
    private void setProfilePicture(Context c, String textURL, String name) {
        ImageView profilePic = (ImageView) getView().findViewById(R.id.profilePictureIV);
        double singleImageDim = getSimpleActivity().getScreenWidth()/2;

        Picasso.with(c)
                .load(textURL + name.toLowerCase().replace(" ", "") + ".jpg")
                .resize((int) singleImageDim, (int) singleImageDim) //image is square
                .placeholder(R.drawable.loading)
                .into(profilePic);
    }
}
