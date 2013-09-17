//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.voskalenko.foursquretracker.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ToggleButton;
import com.voskalenko.foursquretracker.FourSqureTrackerApp_;
import com.voskalenko.foursquretracker.R.id;
import com.voskalenko.foursquretracker.R.layout;

public final class HomeActivity_
    extends HomeActivity
{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_home);
    }

    private void init_(Bundle savedInstanceState) {
        trackerApp = FourSqureTrackerApp_.getInstance_(this);
        init();
    }

    private void afterSetContentView_() {
        btnSwitchOn = ((ToggleButton) findViewById(id.btn_switch_on));
        {
            View view = findViewById(id.btn_switch_on);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        HomeActivity_.this.btn_switch_on();
                    }

                }
                );
            }
        }
        ((FourSqureTrackerApp_) trackerApp).afterSetContentView_();
        initViews();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterSetContentView_();
    }

    public static HomeActivity_.IntentBuilder_ intent(Context context) {
        return new HomeActivity_.IntentBuilder_(context);
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, HomeActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public HomeActivity_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

        public void startForResult(int requestCode) {
            if (context_ instanceof Activity) {
                ((Activity) context_).startActivityForResult(intent_, requestCode);
            } else {
                context_.startActivity(intent_);
            }
        }

    }

}
