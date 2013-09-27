/*
 * @(#)ProposedVenuesDialog.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.dialog;

import android.app.*;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.UiThread;
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

@EFragment
public class ProposedVenuesDialog extends DialogFragment
        implements View.OnClickListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    @SystemService
    LayoutInflater inflater;
    @Bean
    ProposedVenuesAdapter adapter;
    @Bean
    ApiClient apiClient;
    @Bean
    AccountManager accountManager;
    @Bean
    DatabaseManager dbManager;

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        progressDlg = new ProgressDialog(getActivity());
        progressDlg.setTitle(R.string.wait_for_moment);

        getLoaderManager().initLoader(LOADER_ID, null, this);
        final AddCheckInCallback checkInCallbackcallback = new AddCheckInCallback() {

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


        final ProposedVenuesCallback proposedVenuesCallback = new ProposedVenuesCallback() {

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

        final View layout = inflater.inflate(R.layout.dialog_proposed_venues_list, null);
        final ListView venuesList = (ListView) layout.findViewById(R.id.list_venues);
        final Button btnRemindLater = (Button) layout.findViewById(R.id.btn_remindLater);
        final Button btnNoThanks = (Button) layout.findViewById(R.id.btn_no_thanks);

        btnRemindLater.setOnClickListener(this);
        btnNoThanks.setOnClickListener(this);

        adapter.setCheckInCallback(proposedVenuesCallback);
        venuesList.setOnItemClickListener(this);
        venuesList.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.checkin_notif_title)
                .setView(layout);
        return builder.create();
    }

    @UiThread
    void showMessage(String message) {
        progressDlg.hide();
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_remindLater:
                close();
                break;
            case R.id.btn_no_thanks:
                getAccountManager().setDisableDetectInCurrRadius(true);
                close();
                break;
        }
    }

    private void close() {
        dismiss();
        ((DialogCallback) getActivity()).onClose();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        LinearLayout layoutVenueCommand = (LinearLayout) view.findViewById(R.id.layout_venue_command);
        layoutVenueCommand.setVisibility(layoutVenueCommand.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }
}
