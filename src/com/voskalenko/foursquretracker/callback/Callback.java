package com.voskalenko.foursquretracker.callback;


import java.io.Serializable;

public interface Callback extends Serializable{
    public void onFail(String error, Exception e);
}