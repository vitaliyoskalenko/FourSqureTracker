package com.voskalenko.foursquretracker.callback;

import com.voskalenko.foursquretracker.model.CheckIn;

public interface LikeCheckInCallback extends Callback {
    public void onSuccess(CheckIn checkIn);
}
