package com.voskalenko.foursquretracker.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import com.voskalenko.foursquretracker.net.ApiClient;
import com.voskalenko.foursquretracker.net.ApiClient_;

public class BaseFragment extends DialogFragment {

    private ApiClient_ apiClient;

    public ApiClient getApiClient() {
        return apiClient;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiClient = ApiClient_.getInstance_(getActivity());
    }
}
