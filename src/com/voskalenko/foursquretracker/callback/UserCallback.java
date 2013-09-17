package com.voskalenko.foursquretracker.callback;

import com.voskalenko.foursquretracker.model.User;

public interface UserCallback extends Callback{

    public void onSuccess(User userProfile);
}
