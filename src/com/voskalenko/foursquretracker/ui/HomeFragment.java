/*
 * @(#)HomeFragment.java  1.0 2013/09/21
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.ui;

import android.content.Intent;
import android.os.Bundle;
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
        switchButtonDrawable();
        if (getAccountManager().getIsDetectSvcRunning()) {
            startOrStopSchedule(FLAG_STOP);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getAccountManager().isSessionActual())
            getApiClient().verify(getActivity());
    }

    @Click(R.id.btn_switch_on)
    void onClick() {
        switchButtonDrawable();
        Toast.makeText(getActivity(), btnSwitchOn.isChecked() ?
                R.string.service_activated : R.string.service_deactivated, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getAccountManager().setIsDetectSvcRunning(btnSwitchOn.isChecked());
        if (btnSwitchOn.isChecked()) {
            startOrStopSchedule(FLAG_START);
        }
    }

    private void startOrStopSchedule(boolean scheduleFlag) {
        Intent intent = new Intent(getActivity(), ScheduleReceiver_.class);
        intent.putExtra(ScheduleReceiver_.STOP_SCHEDULE, scheduleFlag);
        getActivity().sendBroadcast(intent);
    }
    private void switchButtonDrawable() {
        btnSwitchOn.setBackgroundResource(btnSwitchOn.isChecked() ? R.drawable.checkinon : R.drawable.checkinoff);
    }
}
