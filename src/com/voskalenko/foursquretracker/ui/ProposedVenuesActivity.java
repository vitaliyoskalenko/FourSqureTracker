package com.voskalenko.foursquretracker.ui;

import android.app.Activity;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.callback.DialogCallback;
import com.voskalenko.foursquretracker.dialog.ProposedVenuesDialog;
import com.voskalenko.foursquretracker.dialog.ProposedVenuesDialog_;

@EActivity(R.layout.activity_venues)
@NoTitle
public class ProposedVenuesActivity extends Activity implements DialogCallback{

    private static  final String VENUES_DIALOG = "venues_dialog";

    private ProposedVenuesDialog venuesDialog;

     @AfterInject
     void init() {
         venuesDialog = new ProposedVenuesDialog_();
         venuesDialog.show(getFragmentManager(), VENUES_DIALOG);
     }

    @Override
    public void onClose() {
        finish();
    }
}