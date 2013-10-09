/*
 * @(#)HomeActivity.java  1.0 2013/09/21
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.activity;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.fragment.HistoryFragment;
import com.voskalenko.foursquretracker.fragment.HistoryFragment_;
import com.voskalenko.foursquretracker.fragment.HomeFragment;
import com.voskalenko.foursquretracker.fragment.HomeFragment_;


@EActivity
public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private HomeFragment homeFragment;

    @AfterInject
    void init() {
        showHomeFragment();
    }

    @OptionsItem(R.id.menu_settings)
    void menuSettings() {
        FourSqureTrackerPreference.start(this);
    }

    @OptionsItem(android.R.id.home)
    void menuHome() {
        showHomeFragment();
    }

    @OptionsItem(R.id.menu_history)
    void menuHistory() {
        HistoryFragment historyFragment = HistoryFragment_.builder().build();
        replaceFragment(R.id.activity_base_frame, historyFragment);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showHomeFragment() {
        if (homeFragment == null) {
            homeFragment = HomeFragment_.builder().build();
        }
        replaceFragment(R.id.activity_base_frame, homeFragment);
        getActionBar().setDisplayHomeAsUpEnabled(false);
    }
}
