package com.voskalenko.foursquretracker.ui;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.voskalenko.foursquretracker.R;


@EActivity
@OptionsMenu(R.menu.activity_home)
public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    @AfterInject
    void init() {
        HomeFragment homeFragment;
        homeFragment = HomeFragment_.builder().build();
        replaceFragment(R.id.activity_base_frame, homeFragment);
    }

    @OptionsItem(R.id.menu_settings)
    void menuSettings() {
        FourSqureTrackerPreference.start(this);
    }
}
