/*
 * @(#)VerifyDialog.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.dialog;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.ViewById;
import com.voskalenko.foursquretracker.Constants;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.callback.VerifyDialogCallback;

/**
 * Dialog shows FourSqure website for to authenticate and getting token
 *
 * @author Vitaly Oskalenko
 * @version 1.0 11 Sep 2013
 */

@EFragment(R.layout.dialog_verify)
public class VerifyDialog extends DialogFragment {

    @ViewById(R.id.web_view)
    WebView webView;
    @ViewById(R.id.verify_progress)
    ProgressBar verifyProgress;
    @FragmentArg("verifyUrl")
    String verifyUrl;
    @FragmentArg("callback")
    VerifyDialogCallback callback;

    @AfterViews
    void initViews() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClientEx());
        webView.loadUrl(verifyUrl);
    }

    private class WebViewClientEx extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(Constants.CALLBACK_URL.toLowerCase())) {
                String urls[] = url.split("=");
                callback.onSuccess(urls[1]);
                VerifyDialog.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            callback.onFail(description, null);
            dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            verifyProgress.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            verifyProgress.setVisibility(View.GONE);
        }
    }
}
