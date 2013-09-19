package com.voskalenko.foursquretracker.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ToggleButton;
import com.googlecode.androidannotations.annotations.*;
import com.voskalenko.foursquretracker.FourSqureTrackerApp;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.service.ScheduleReceiver_;


@EActivity(R.layout.activity_home)
public class HomeActivity extends Activity {

    @Bean
    FourSqureTrackerApp trackerApp;

    private Context ctx;
    private static final String TAG = HomeActivity.class.getSimpleName();

    @AfterInject
    void init() {
        ctx = this;
        Intent intent = new Intent(ctx, ScheduleReceiver_.class);
        intent.putExtra(ScheduleReceiver_.STOP_SCHEDULE, true);
        sendBroadcast(intent);
        if(!trackerApp.sessionIsActual())
            trackerApp.verify();
    }

    @ViewById(R.id.btn_switch_on)
    ToggleButton btnSwitchOn;

    @AfterViews
    void initViews() {
       btnSwitchOn.setChecked(trackerApp.isDetectSvcRunning());
    }

    @Click
    void btn_switch_on() {

        Intent intent = new Intent(ctx, ScheduleReceiver_.class);
        if (btnSwitchOn.getTextOn().equals("ON"))
            intent.putExtra(ScheduleReceiver_.STOP_SCHEDULE, false);
        else intent.putExtra(ScheduleReceiver_.STOP_SCHEDULE, true);

        sendBroadcast(intent);
    }
}
