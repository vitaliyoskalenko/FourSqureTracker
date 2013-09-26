package com.voskalenko.foursquretracker.views;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.model.CheckInsHistory;
import com.voskalenko.foursquretracker.model.Venue;

import java.text.SimpleDateFormat;
import java.util.Date;

@EViewGroup(R.layout.view_check_ins_history_item)
public class CheckInsHistoryView extends RelativeLayout {

    @ViewById(R.id.txt_history_place)
    TextView txtHistoryPlace;
    @ViewById(R.id.txt_history_locate)
    TextView txtHistoryLocate;
    @ViewById(R.id.txt_history_date)
    TextView txtHistoryDate;
    @ViewById(R.id.txt_automatically)
    TextView txtAutomatically;

    private SimpleDateFormat formatter;

    public CheckInsHistoryView(Context context) {
        super(context);
        formatter = new SimpleDateFormat("EEE MMM dd HH:mm");
    }

    public void setData(CheckInsHistory checkInsHistory) {
        Venue venue = checkInsHistory.getVenue();
        txtHistoryPlace.setText(venue.getName());
        txtHistoryLocate.setText(venue.getLocation().getCountry()
                + " ," + venue.getLocation().getCity());
        txtHistoryDate.setText(formatter.format(new Date(checkInsHistory.getCheckInDate())));
        txtAutomatically.setVisibility(checkInsHistory.isAutomatically() == true ? View.VISIBLE : View.GONE);
    }
}
