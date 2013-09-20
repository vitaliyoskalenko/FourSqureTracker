package com.voskalenko.foursquretracker.callback;

import com.voskalenko.foursquretracker.model.CheckIn;

public interface AddCheckInCallback extends Callback {
    public void onSuccess(CheckIn checkIn);
}
