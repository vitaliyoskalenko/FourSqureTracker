package com.voskalenko.foursquretracker.dialog;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.voskalenko.foursquretracker.Constants;
import com.voskalenko.foursquretracker.R;

@EFragment(R.layout.dialog_verify)
public class VerifyDialog extends DialogFragment {

    @ViewById(R.id.web_view)
    private WebView webView;
    @ViewById(R.id.verify_progress)
    private ProgressBar verifyProgress;

    @AfterViews
    void initViews() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClientEx());
    }

    private static final String URL = "url";

    private VerifyDialogCallback callback;

    public static VerifyDialog newInstance(String url, VerifyDialogCallback callback) {

        VerifyDialog dlg = new VerifyDialog(callback);
        Bundle args = new Bundle();
        args.putString(URL, url);
        dlg.setArguments(args);
        return dlg;
    }

    public VerifyDialog(VerifyDialogCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String url = getArguments().getString(URL);
        webView.loadUrl(url);
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
            callback.onFail(description);
            dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            webView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.setVisibility(View.GONE);
        }
    }

    public interface VerifyDialogCallback {
        public void onSuccess(String verifyCode);

        public void onFail(String error);
    }
}
