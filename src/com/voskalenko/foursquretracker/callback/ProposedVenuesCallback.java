package com.voskalenko.foursquretracker.callback;

import com.voskalenko.foursquretracker.model.Venue;

public interface ProposedVenuesCallback extends Callback{
    public void onSuccess(Venue venue, int id);
}
