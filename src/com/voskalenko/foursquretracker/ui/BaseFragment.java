package com.voskalenko.foursquretracker.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import com.voskalenko.foursquretracker.database.DBManager;
import com.voskalenko.foursquretracker.database.DBManager_;
import com.voskalenko.foursquretracker.net.ApiClient;
import com.voskalenko.foursquretracker.net.ApiClient_;

public class BaseFragment extends DialogFragment {

    private ApiClient_ apiClient;
    private DBManager dbManager;

    public ApiClient getApiClient() {
        return apiClient;
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiClient = ApiClient_.getInstance_(getActivity());
        dbManager = DBManager_.getInstance_(getActivity());
    }
}
