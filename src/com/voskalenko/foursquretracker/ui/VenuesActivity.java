package com.voskalenko.foursquretracker.ui;

import android.app.Activity;
import com.googlecode.androidannotations.annotations.*;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.model.Venue;

import java.util.ArrayList;

@EActivity(R.layout.activity_venues)
@NoTitle
public class VenuesActivity extends Activity {

    private static  final String VENUES_DIALOG = "venues_dialog";
    public static final String VENUES = "venues";

    private VenuesDialog_ venuesDialog;

    @Extra(value = "VENUES")
    ArrayList<Venue> venues;

     @AfterInject
     void init() {
         venuesDialog = VenuesDialog_.newInstance(venues);
         venuesDialog.show(getFragmentManager(), VENUES_DIALOG);
     }
}