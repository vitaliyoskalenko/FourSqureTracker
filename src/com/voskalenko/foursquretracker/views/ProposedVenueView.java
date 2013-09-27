/*
 * @(#)ProposedVenueView.java  1.0 2013/09/21
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.model.Venue;

import java.text.NumberFormat;

@EViewGroup(R.layout.view_proposed_venue_item)
public class ProposedVenueView extends RelativeLayout {

    @ViewById(R.id.txt_venue_name)
    TextView venueNameView;
    @ViewById(R.id.txt_city)
    TextView cityView;
    @ViewById(R.id.txt_distance)
    TextView distanceView;
    @ViewById(R.id.btn_checkin)
    ImageButton btnCheckIn;
    @ViewById(R.id.btn_mute)
    ImageButton btnMute;
    private Context context;
    private NumberFormat nFormater;

    public ProposedVenueView(Context context) {
        super(context);
        this.context = context;
        nFormater = NumberFormat.getInstance();
        nFormater.setMinimumFractionDigits(0);
        nFormater.setMaximumFractionDigits(3);
    }

    public ProposedVenueView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView getVenueNameView() {
        return venueNameView;
    }

    public TextView getCityView() {
        return cityView;
    }

    public TextView getDistanceView() {
        return distanceView;
    }

    public ImageButton getBtnCheckIn() {
        return btnCheckIn;
    }

    public ImageButton getBtnMute() {
        return btnMute;
    }

    public void setData(Venue venue) {
        if (venue != null) {
            getBtnCheckIn().setTag(venue);
            getBtnMute().setTag(venue);
            getVenueNameView().setText(venue.getName());
            getCityView().setText(venue.getLocation().getCity());
            String distance = context.getString(R.string.distance_to_me, nFormater.format(venue.getDistance()));
            getDistanceView().setText(distance);
        }
    }
}
