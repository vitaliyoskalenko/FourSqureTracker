package com.voskalenko.foursquretracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.voskalenko.foursquretracker.Constants;
import com.voskalenko.foursquretracker.R;

public class VerifyDialog extends DialogFragment {

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
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*CookieSyncManager.createInstance(getActivity());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();*/

		String url = getArguments().getString(URL);
		WebView webView = new WebView(getActivity());
        webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);
		
		webView.setWebViewClient(new WebViewClientEx());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
			.setTitle(R.string.authentication)
			.setView(webView);
		return builder.create();
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
	}
	
	public interface VerifyDialogCallback {
		public void onSuccess(String verifyCode);
		public void onFail(String error);
	}
}
