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
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.EBean;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.callback.ProposedVenuesCallback;
import com.voskalenko.foursquretracker.model.Venue;
import com.voskalenko.foursquretracker.views.ProposedVenueGroupView_;
import com.voskalenko.foursquretracker.views.ProposedVenueView;
import com.voskalenko.foursquretracker.views.ProposedVenueView_;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@EBean
public class ProposedVenuesAdapter extends CursorAdapter implements View.OnClickListener {

    private static final int TYPE_GROUP_HEADER_NEARBY = 0;
    private static final int TYPE_GROUP_HEADER_MY_PLACES = 1;

    private static final int TYPE_ITEM = 2;
    private static final int ITEM_TYPE_COUNT = 3;
    private boolean[] distanceHeaders = new boolean[]{false, false};

    private ProposedVenuesCallback callback;
    private int idIndex;
    private Map<String, Map.Entry<Boolean, Boolean>> checkedButtonList;
    private String[] groupsTitle;
    int nearbyDistance;
    private Context context;

    public ProposedVenuesAdapter(Context context) {
        super(context, null, false);
        this.context = context;
        checkedButtonList = new HashMap<String, Map.Entry<Boolean, Boolean>>();
        groupsTitle = context.getResources().getStringArray(R.array.proposed_venue_group_title);
        nearbyDistance = 90;
    }

    public void setCheckInCallback(ProposedVenuesCallback callback) {
        this.callback = callback;
    }

    public int getViewTypeCount() {
        return ITEM_TYPE_COUNT;
    }

    public int getItemViewType(Cursor cursor) {

        float currDistance = cursor.getFloat(cursor.getColumnIndex(Venue.FIELD_DISTANCE));
        if (currDistance < nearbyDistance &&
                distanceHeaders[TYPE_GROUP_HEADER_NEARBY] == false) {
            distanceHeaders[TYPE_GROUP_HEADER_NEARBY] = true;
            return TYPE_GROUP_HEADER_NEARBY;
        }

        if (currDistance >= nearbyDistance &&
                distanceHeaders[TYPE_GROUP_HEADER_NEARBY] == false) {
            distanceHeaders[TYPE_GROUP_HEADER_MY_PLACES] = true;
            return TYPE_GROUP_HEADER_MY_PLACES;
        }

        return TYPE_ITEM;
    }

    @Override
    public Object getItem(int position) {
        return getView(position, null, null);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        ProposedVenueView convertView = ProposedVenueView_.build(context);
        convertView.getBtnCheckIn().setOnClickListener(this);
        convertView.getBtnMute().setOnClickListener(this);

        int groupType = getItemViewType(cursor);

        switch (groupType) {
            case TYPE_GROUP_HEADER_NEARBY:
            case TYPE_GROUP_HEADER_MY_PLACES:
                View view = ProposedVenueGroupView_.build(context);
                convertView.setTag(groupType);
                convertView.getGroupLayout().addView(view);
                break;
        }

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final Venue venue = Venue.fromCursor(cursor);
        final ProposedVenueView wrappedItemView = (ProposedVenueView) view;
        wrappedItemView.setData(venue);

        String venueId = cursor.getString(idIndex);
        if (checkedButtonList.containsKey(venueId)) {
            Map.Entry<Boolean, Boolean> entry = checkedButtonList.get(venueId);
            wrappedItemView.getBtnMute().setVisibility(entry.getKey() == true ? View.GONE : View.VISIBLE);
            wrappedItemView.getBtnCheckIn().setVisibility(entry.getValue() == true ? View.GONE : View.VISIBLE);
        }

        TextView txtHeader = (TextView) wrappedItemView.getGroupLayout().findViewById(R.id.txt_header);

        if (txtHeader != null) {
            int groupType = (Integer) wrappedItemView.getTag();
            String groupTitle = groupsTitle[groupType];
            txtHeader.setText(groupTitle);
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
