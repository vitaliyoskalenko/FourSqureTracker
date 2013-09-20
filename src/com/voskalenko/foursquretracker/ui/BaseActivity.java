package com.voskalenko.foursquretracker.ui;

import android.app.Activity;
import android.os.Bundle;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.net.ApiClient_;

public class BaseActivity extends Activity {


    private ApiClient_ apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        apiClient = ApiClient_.getInstance_(this);


    }

    public ApiClient_ getApiClient() {
        return apiClient;
    }
}
