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

    @AfterViews
    void initViews() {
        btnSwitchOn.setChecked(accountManager.getIsDetectSvcRunning());
        if (accountManager.getIsDetectSvcRunning()) {
            startOrStopSchedule(FLAG_STOP);
        }
    }

    @AfterInject
    void init() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!accountManager.isSessionActual())
            getApiClient().verify(getActivity());
    }

    @Click(R.id.btn_switch_on)
    void onClick() {
        Toast.makeText(getActivity(), btnSwitchOn.isChecked() ?
                R.string.service_activated : R.string.service_deactivated, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accountManager.setIsDetectSvcRunning(btnSwitchOn.isChecked());
        if (btnSwitchOn.isChecked()) {
            startOrStopSchedule(FLAG_START);
        }
    }

    private void startOrStopSchedule(boolean scheduleFlag) {
        Intent intent = new Intent(getActivity(), ScheduleReceiver_.class);
        intent.putExtra(ScheduleReceiver_.STOP_SCHEDULE, scheduleFlag);
        getActivity().sendBroadcast(intent);
    }
}
