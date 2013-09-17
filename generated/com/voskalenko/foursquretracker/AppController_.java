//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.voskalenko.foursquretracker;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.view.View;
import com.voskalenko.foursquretracker.task.ApiClient_;

public final class AppController_
    extends AppController
{

    private Context context_;
    private static AppController_ instance_;

    private AppController_(Context context) {
        context_ = context;
        init_();
    }

    public void afterSetContentView_() {
        if (!(context_ instanceof Activity)) {
            return ;
        }
        ((ApiClient_) apiClient).afterSetContentView_();
    }

    /**
     * You should check that context is an activity before calling this method
     * 
     */
    public View findViewById(int id) {
        Activity activity_ = ((Activity) context_);
        return activity_.findViewById(id);
    }

    @SuppressWarnings("all")
    private void init_() {
        if (context_ instanceof Activity) {
            Activity activity = ((Activity) context_);
        }
        activityMng = ((ActivityManager) context_.getSystemService(Context.ACTIVITY_SERVICE));
        ctx = context_;
        apiClient = ApiClient_.getInstance_(context_);
        init();
    }

    public static AppController_ getInstance_(Context context) {
        if (instance_ == null) {
            instance_ = new AppController_(context.getApplicationContext());
        }
        return instance_;
    }

    public void rebind(Context context) {
    }

}