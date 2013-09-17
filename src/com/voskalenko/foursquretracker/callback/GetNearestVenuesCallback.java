package com.voskalenko.foursquretracker.callback;

import com.voskalenko.foursquretracker.model.Venue;

import java.util.List;

public interface GetNearestVenuesCallback extends  Callback{
    public void onSuccess(List<Venue> venues);
}
