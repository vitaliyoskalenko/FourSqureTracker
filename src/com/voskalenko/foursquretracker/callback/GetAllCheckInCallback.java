package com.voskalenko.foursquretracker.callback;

import com.voskalenko.foursquretracker.model.CheckIns;

public interface GetAllCheckInCallback extends Callback{
    public void onSuccess(CheckIns result);
}