package com.voskalenko.foursquretracker.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.UiThread;
import com.voskalenko.foursquretracker.FourSqureTrackerApp;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.callback.AddCheckInCallback;
import com.voskalenko.foursquretracker.callback.VenueCallback;
import com.voskalenko.foursquretracker.model.CheckIn;
import com.voskalenko.foursquretracker.model.Venue;

import java.util.List;

@EFragment
public class VenuesDialog extends DialogFragment {

    @SystemService
    LayoutInflater inflater;
    @Bean
    VenuesAdapter adapter;
    @Bean
    FourSqureTrackerApp trackerApp;

    private static List<Venue> venues;

    private VenueCallback callback;
    private AddCheckInCallback userCallback;
    private ProgressDialog progressDlg;

    public FourSqureTrackerApp getTrackerApp() {
        return trackerApp;
    }

    public static VenuesDialog_ newInstance(List<Venue> venues_) {
        venues = venues_;
        VenuesDialog_ dlg = new VenuesDialog_();
        Bundle args = new Bundle();
        dlg.setArguments(args);
        return dlg;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        progressDlg = new ProgressDialog(getActivity());

        userCallback = new AddCheckInCallback() {
            @Override
            public void onSuccess(CheckIn checkIn) {
                progressDlg.hide();
                showMessage(getActivity().getString(R.string.checkin_is_complete,
                        checkIn.toString(), checkIn.getVenue().toString(), checkIn.getVenue().getLocation()));
            }

            @Override
            public void onFail(String error, Exception e) {
            }
        };

        callback = new VenueCallback() {
            @Override
            public void onSuccess(String venueId) {
                progressDlg.show();
                trackerApp.addCheckIn(venueId, "Message", userCallback);
            }

            @Override
            public void onFail(String error, Exception e) {
            }
        };

        progressDlg.setTitle(R.string.wait_for_moment);
        adapter.setVenues(venues);
        adapter.setCallback(callback);

        final View layout = inflater.inflate(R.layout.dialog_venues_list, null);
        final ListView venuesList = (ListView) layout.findViewById(R.id.list_venues);
        venuesList.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.checkin_notif_title)
                .setView(layout)
                .setNegativeButton(getActivity().getString(android.R.string.cancel), new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    @UiThread
    void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
    }
}
