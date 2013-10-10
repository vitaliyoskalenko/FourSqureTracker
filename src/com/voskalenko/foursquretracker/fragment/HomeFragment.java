/*
 * @(#)HomeFragment.java  1.0 2013/09/21
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.googlecode.androidannotations.annotations.*;
import com.voskalenko.foursquretracker.AccountManager;
import com.voskalenko.foursquretracker.FourSqureTrackerHelper;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.service.ScheduleReceiver_;

@EFragment(R.layout.fragment_home)
@OptionsMenu(R.menu.activity_home)
public class HomeFragment extends BaseFragment {

    @Bean
    FourSqureTrackerHelper trackerHelper;
    @Bean
    AccountManager accountManager;
    @ViewById(R.id.btn_switch_on)
    ToggleButton btnSwitchOn;

    static final boolean FLAG_STOP = true;
    static final boolean FLAG_START = false;

    private AccountManager getAccountManager() {
        return accountManager;
    }

    @AfterViews
    void initViews() {
        btnSwitchOn.setChecked(getAccountManager().getIsDetectSvcRunning());
    }

    @Override
    public void onResume() {
        super.onResume();
        switchButtonTitle();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getAccountManager().isSessionActual()) {
            getApiClient().verify(getActivity());
        }
    }

    @Click(R.id.btn_switch_on)
    void onClick() {
        enableDetectService();
        switchButtonTitle();
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.xml.shake);
        btnSwitchOn.setAnimation(shake);
    }


    private void enableDetectService() {
        getAccountManager().setIsDetectSvcRunning(btnSwitchOn.isChecked());

        Intent intent = new Intent(getActivity(), ScheduleReceiver_.class);
        getActivity().sendBroadcast(intent);

        Toast.makeText(getActivity(), btnSwitchOn.isChecked() ?
                R.string.service_activated : R.string.service_deactivated, Toast.LENGTH_SHORT).show();
    }

    private void switchButtonTitle() {
        btnSwitchOn.setText(btnSwitchOn.isChecked() ? R.string.service_is_turn_on : R.string.service_is_turn_off);
    }
}
