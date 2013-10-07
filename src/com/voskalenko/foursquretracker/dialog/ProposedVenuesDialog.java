/*
 * @(#)ProposedVenuesDialog.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.dialog;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.googlecode.androidannotations.annotations.*;
import com.voskalenko.foursquretracker.AccountManager;
import com.voskalenko.foursquretracker.Logger;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.adapter.ProposedVenuesAdapter;
import com.voskalenko.foursquretracker.callback.AddCheckInCallback;
import com.voskalenko.foursquretracker.callback.DialogCallback;
import com.voskalenko.foursquretracker.callback.ProposedVenuesCallback;
import com.voskalenko.foursquretracker.database.DatabaseManager;
import com.voskalenko.foursquretracker.database.ProposedVenueProvider;
import com.voskalenko.foursquretracker.model.CheckIn;
import com.voskalenko.foursquretracker.model.Venue;
import com.voskalenko.foursquretracker.net.ApiClient;

/**
 * Dialog shows all suitable places for check-in
 *
 * @author Vitaly Oskalenko
 * @version 1.0 11 Sep 2013
 */

@EFragment(R.layout.dialog_proposed_venues_list)
public class ProposedVenuesDialog extends DialogFragment
        implements AdapterView.OnItemClickListener,LoaderManager.LoaderCallbacks<Cursor> {

    @Bean
    ProposedVenuesAdapter adapter;
    @Bean
    ApiClient apiClient;
    @Bean
    AccountManager accountManager;
    @Bean
    DatabaseManager dbManager;

    @ViewById(R.id.list_venues)
    ListView venuesList;
    @ViewById(R.id.btn_remindLater)
    Button btnRemindLater;
    @ViewById(R.id.btn_no_thanks)
    Button btnNoThanks;

    private static final int LOADER_ID = 0;

    private ProgressDialog progressDlg;

    private ApiClient getApiClient() {
        return apiClient;
    }

    private AccountManager getAccountManager() {
        return accountManager;
    }

    private DatabaseManager getDbManager() {
        return dbManager;
    }

    private final AddCheckInCallback checkInCallbackcallback = new AddCheckInCallback() {

        @Override
        public void onSuccess(CheckIn checkIn) {
            showMessage(getActivity().getString(R.string.checkin_is_complete,
                    checkIn.toString(), checkIn.getVenue().toString(), checkIn.getVenue().getLocation()));
        }

        @Override
        public void onFail(String error, Exception e) {
            Logger.e(error, e);
        }
    };


    private final ProposedVenuesCallback proposedVenuesCallback = new ProposedVenuesCallback() {

        @Override
        public void onSuccess(Venue venue, int id) {
            progressDlg.show();
            switch (id) {
                case R.id.btn_checkin:
                    getApiClient().addCheckIn(venue.getId(), checkInCallbackcallback);
                    break;
                case R.id.btn_mute:
                    venue.setMuted(Venue.FLAG_MUTED);
                    getDbManager().setMuted(venue);
                    adapter.notifyDataSetChanged();
                    progressDlg.hide();
                    break;
            }
        }

        @Override
        public void onFail(String error, Exception e) {
        }
    };


    @AfterViews
    void init() {

        progressDlg = new ProgressDialog(getActivity());
        progressDlg.setTitle(R.string.wait_for_moment);

        adapter.setCheckInCallback(proposedVenuesCallback);
        venuesList.setOnItemClickListener(this);
        venuesList.setAdapter(adapter);
        getDialog().setTitle(R.string.checkin_notif_title);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        close();
        super.onCancel(dialog);
    }

    @Click(R.id.btn_remindLater)
    void remindLaterClick(View view) {
        close();
    }

    @Click(R.id.btn_no_thanks)
    void btnNoThanksClick(View view) {
        getAccountManager().setDisableDetectInCurrRadius(true);
        close();
    }

    /*@ItemClick(R.id.list_venues)
    void venuesListItemClick(ProposedVenueView view) {
        view.getCommandLayout().setVisibility(
                view.getCommandLayout().getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }*/

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        LinearLayout layoutVenueCommand = (LinearLayout) view.findViewById(R.id.layout_venue_command);
        layoutVenueCommand.setVisibility(layoutVenueCommand.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), ProposedVenueProvider.getContentUri(Venue.TABLE_NAME), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private void close() {
        dismiss();
        ((DialogCallback) getActivity()).onClose();
    }

    @UiThread
    void showMessage(String message) {
        progressDlg.hide();
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
