/*
 * @(#)ProposedVenuesAdapter.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import com.googlecode.androidannotations.annotations.EBean;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.callback.ProposedVenuesCallback;
import com.voskalenko.foursquretracker.model.Venue;
import com.voskalenko.foursquretracker.views.ProposedVenueView;
import com.voskalenko.foursquretracker.views.ProposedVenueView_;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@EBean
public class ProposedVenuesAdapter extends CursorAdapter implements View.OnClickListener {


    private ProposedVenuesCallback callback;
    private int idIndex;
    private Map<String, Map.Entry<Boolean, Boolean>> checkedButtonList;

    public ProposedVenuesAdapter(Context context) {
        super(context, null, false);
        checkedButtonList = new HashMap<String, Map.Entry<Boolean, Boolean>>();
    }

    public void setCheckInCallback(ProposedVenuesCallback callback) {
        this.callback = callback;
    }

    @Override
    public Object getItem(int position) {
        return getView(position, null, null);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        ProposedVenueView view = ProposedVenueView_.build(context);
        view.getBtnCheckIn().setOnClickListener(this);
        view.getBtnMute().setOnClickListener(this);

        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ProposedVenueView wrappedView = (ProposedVenueView) view;
        final Venue venue = Venue.fromCursor(cursor);
        wrappedView.setData(venue);

        String venueId = cursor.getString(idIndex);
        if (checkedButtonList.containsKey(venueId)) {
            Map.Entry<Boolean, Boolean> entry = checkedButtonList.get(venueId);
            wrappedView.getBtnMute().setVisibility(entry.getKey() == true ? View.GONE : View.VISIBLE);
            wrappedView.getBtnCheckIn().setVisibility(entry.getValue() == true ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        idIndex = cursor.getColumnIndex(Venue.FIELD_ID);
    }

    @Override
    public void onClick(View view) {
        Venue venue = (Venue) view.getTag();
        Map.Entry<Boolean, Boolean> entry;
        if (checkedButtonList.containsKey(venue.getId())) {
            Map.Entry<Boolean, Boolean> entryTmp = checkedButtonList.get(venue.getId());

            entry = new AbstractMap.SimpleEntry<Boolean, Boolean>(
                    view.getId() == R.id.btn_mute || entryTmp.getKey(),
                    view.getId() == R.id.btn_checkin || entryTmp.getValue());
        } else {
            entry = new AbstractMap.SimpleEntry<Boolean, Boolean>(
                    view.getId() == R.id.btn_mute,
                    view.getId() == R.id.btn_checkin);
        }

        checkedButtonList.put(venue.getId(), entry);
        callback.onSuccess(venue, view.getId());
        notifyDataSetInvalidated();
    }
}
