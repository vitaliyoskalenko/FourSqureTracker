/*
 * @(#)BaseFragment.java  1.0 2013/09/21
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import com.voskalenko.foursquretracker.database.DatabaseManager;
import com.voskalenko.foursquretracker.database.DatabaseManager_;
import com.voskalenko.foursquretracker.net.ApiClient;
import com.voskalenko.foursquretracker.net.ApiClient_;

public class BaseFragment extends DialogFragment {

    private ApiClient_ apiClient;
    private DatabaseManager dbManager;

    public ApiClient getApiClient() {
        return apiClient;
    }

    public DatabaseManager getDbManager() {
        return dbManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiClient = ApiClient_.getInstance_(getActivity());
        dbManager = DatabaseManager_.getInstance_(getActivity());
    }
}
