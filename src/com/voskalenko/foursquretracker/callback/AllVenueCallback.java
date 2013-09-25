package com.voskalenko.foursquretracker.callback;

import com.voskalenko.foursquretracker.model.Venue;

import java.util.List;

public interface AllVenueCallback extends Callback{
    public void onSuccess(List<Venue> venueList);
}