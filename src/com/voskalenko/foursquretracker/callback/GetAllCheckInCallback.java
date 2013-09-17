package com.voskalenko.foursquretracker.callback;

import com.voskalenko.foursquretracker.model.CheckInList;

public interface GetAllCheckInCallback extends Callback{
    public void onSuccess(CheckInList result);
}