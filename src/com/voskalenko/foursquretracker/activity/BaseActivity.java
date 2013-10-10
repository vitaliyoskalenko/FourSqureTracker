/*
 * @(#)BaseActivity.java  1.0 2013/09/21
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.voskalenko.foursquretracker.R;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public void replaceFragment(int conteinerId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out);
        fragmentTransaction.replace(conteinerId, fragment);
        fragmentTransaction.commit();
    }
}
